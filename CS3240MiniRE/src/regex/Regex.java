package regex;

import grammar.Lexical;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public class Regex {
	private String regexstr;
	private boolean compiled = false;
	private State startState;

	public Regex(final String str) {
		this.regexstr = str;
		if(Lexical.checkValidRegex(this.regexstr)) {
			//Then it is valid so we can actually compile it to the DFA representation.
			//First must remove the single quotes around the regex.
			this.regexstr = this.regexstr.substring(1);
			this.regexstr = this.regexstr.substring(0, this.regexstr.length() - 1);
			if(this.regexstr.length() > 0) {
				startState = compile(this.regexstr);
				if(startState != null) {
					compiled = true;
				}
			}
			else {
				startState = new State();
				startState.setAcceptState(true);
				compiled = true;
			}
		}
	}
	
	public State getStartState() {
		return startState;
	}

	public List<Match> match(final List<String> lines) {
		List<Match> matchlist = new ArrayList<Match>();
		if(compiled) {
			for(int i = 0; i < lines.size(); i++) {
				matchlist.addAll(checkLineAgainstPattern(lines.get(i), i));
			}
		}
		return matchlist;
	}
	
	private List<Match> checkLineAgainstPattern(String s, int linenum) {
		List<Match> matchlist = new ArrayList<Match>();
		int pos = 0;
		for(int i = 0; i < s.length(); i++) {
			pos = recurseThroughRegex(s.substring(i), startState);
			if(pos > 0) {
				//TODO figure out a way to actually get the file object for the second param to the Match constructor.
				matchlist.add(new Match(s.substring(i, pos), "", linenum, i));
			}
		}
		return matchlist;
	}
	
	private int recurseThroughRegex(String s, State dfa) {
		//TODO this is not returning properly right now, have to work on the return
		//logic so that we can return determine what the string is that was matched.
		int found = 0;
		int[] range;
		char c;
		for(Transition t : dfa.getTransitions()) {
			if(t.getSubDFA() != null) {
				found += recurseThroughRegex(s, t.getSubDFA());
				if(found > 0) {
					break;
				}
			}
			else if(t.getRange() != null) {
				c = s.charAt(0);
				range = t.getRange();
				if(t.isNotThisRange()) {
					//Then we test everything but this range.
					if((int) c < range[0] && (int) c > range[1]) {
						found++;
					}
				}
				else {
					//Then we test this range.
					if((int) c >= range[0] && (int) c <= range[1]) {
						found++;
					}
				}
			}
			else if(t.getExactMatch() != null) {
				c = s.charAt(0);
				if(t.getExactMatch().equals("" + c)) {
					found++;
				}
			}
			if(found > 0 && t.getNextState().isAcceptState()) {
				found++;
			}
			else {
				if(s.length() > 1) {
					found += recurseThroughRegex(s.substring(1), t.getNextState());
				}
				else {
					found = 0;
				}
			}
		}
		return found;
	}

	public List<String> replace(final List<String> lines, final String rep) {
		// TODO stub
		return null;
	}
	
	private List<String> tokenizeRegexString(String str) {
		List<String> regparts = new ArrayList<String>();
		String tempstr = "";
		char c;
		int j;
		for(int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			switch(c) {
			case '\\':
				tempstr += c;
				i++;
				c = str.charAt(i);
				tempstr += c;
				break;
			case '(':
			case '[':
				if(tempstr.length() > 0) {
					regparts.add(tempstr);
					tempstr = "";
				}
				j = posMatchingParen(str.substring(i));
				//Added one to make sure we were actually including the last character
				//of the regex part.
				regparts.add(str.substring(i, i + j + 1));
				i += j;
				break;
			case '*':
			case '+':
			case '?':
				if(tempstr.length() > 0) {
					regparts.add(tempstr);
					tempstr = "";
				}
				//Puts the previous token inside parentheses to make it easier 
				//to handle in the compiler.
				if(regparts.size() > 0) {
					tempstr = regparts.remove(regparts.size() - 1);
				}
				else {
					tempstr = "";
				}
				tempstr = '(' + tempstr + c + ')';
				regparts.add(tempstr);
				break;
			case '|':
			case '.':
				if(tempstr.length() > 0) {
					regparts.add(tempstr);
					tempstr = "";
				}
				regparts.add("" + c);
				break;
			default:
				tempstr += c;
				break;
			}
		}
		if(tempstr.length() > 0) {
			regparts.add(tempstr);
		}
		return regparts;
	}
	
	private State compile(String s) {
		State compStart = new State();
		List<State> dfastack = new ArrayList<State>();
		State prevDFA = null, tempDFA = null;
		Transition trans;
		boolean unionDFAs = false;
		char c;
		for(String str : tokenizeRegexString(s)) {
			if(str.length() > 0) {
				c = str.charAt(0);
				//It should be, but just in case.
				switch(c) {
				case '(':
					//Then we have a parentheses and we should tokenize the contained
					//string. Must be sure to strip the outside parens off first.
					if(!str.equals('(' + s + ')')) {
						//Only want to recursively go deeper if the token is not
						//exactly the same as it was before.
						dfastack.add(compile(str.substring(1, str.length() - 1)));
					}
					else {
						//Since the only time that additional parens are added is by
						//the repeaters, we should call the repeaters.
						if(s.endsWith("*") || s.endsWith("+") || s.endsWith("?")) {
							//Create the dfa for the string without the repeater.
							dfastack.add(compile(s.substring(0, s.length() - 1)));
							//TODO properly handle the ability for the prevDFA to be
							//a string of chracters and we only want the last character
							//to actually repeat... Obviously if inside parens, then don't
							//need to worry about this (ex: '(ab)*' != 'ab*').
							prevDFA = dfastack.remove(dfastack.size() - 1);
							dfastack.add(createDFAForRepeaters(prevDFA, "" + c));
							prevDFA = null;
						}
					}
					//Must check to see if a union must occur.
					if(unionDFAs) {
						//TODO handle the special case of empty string.
						//Pop off the last two from the stack and put them in 
						//a single DFA.
						if(dfastack.size() >= 2) {
							prevDFA = dfastack.remove(dfastack.size() - 1);
							tempDFA = dfastack.remove(dfastack.size() - 1);
							dfastack.add(createDFAUnion(tempDFA, prevDFA));
							prevDFA = null;
							tempDFA = null;
							unionDFAs = false;
						}
						else {
							
						}
					}
					break;
				case '[':
					//Then we have a bracket that just needs the internals tokenized
					//to the appropriate ranges.
					dfastack.add(createDFAForBrackets(str));
					//Must check to see if a union must occur.
					if(unionDFAs) {
						//Pop off the last two from the stack and put them in 
						//a single DFA.
						if(dfastack.size() >= 2) {
							prevDFA = dfastack.remove(dfastack.size() - 1);
							tempDFA = dfastack.remove(dfastack.size() - 1);
							dfastack.add(createDFAUnion(tempDFA, prevDFA));
							prevDFA = null;
							tempDFA = null;
							unionDFAs = false;
						}
						else {
							
						}
					}
					break;
				case '*':
				case '+':
				case '?':
					//Then the Transition needs to store a copy of the DFA that
					//will be run 0 - infinity, 1 - infinity, or 0 - 1 times.
					prevDFA = dfastack.remove(dfastack.size() - 1);
					dfastack.add(createDFAForRepeaters(prevDFA, "" + c));
					prevDFA = null;
					//Will always be followed by a close parentheses, so don't 
					//need to check for a union needing to be done.
					break;
				case '|':
					unionDFAs = true;
					break;
				default:
					dfastack.add(createDFAForSimpleString(str));
					//Must check to see if a union must occur.
					if(unionDFAs) {
						//Pop off the last two from the stack and put them in 
						//a single DFA.
						if(dfastack.size() >= 2) {
							prevDFA = dfastack.remove(dfastack.size() - 1);
							tempDFA = dfastack.remove(dfastack.size() - 1);
							dfastack.add(createDFAUnion(tempDFA, prevDFA));
							prevDFA = null;
							tempDFA = null;
							unionDFAs = false;
						}
						else {
							
						}
					}
					break;
				}
			}
		}
		//Must check to see if a union must occur, don't want to leave without
		//taking care of it first.
		if(unionDFAs) {
			//Pop off the last two from the stack and put them in 
			//a single DFA.
			if(dfastack.size() >= 2) {
				prevDFA = dfastack.remove(dfastack.size() - 1);
				tempDFA = dfastack.remove(dfastack.size() - 1);
				dfastack.add(createDFAUnion(tempDFA, prevDFA));
				prevDFA = null;
				tempDFA = null;
				unionDFAs = false;
			}
			else {
				
			}
		}
		//Now need to move all the DFAs into the start State.
		prevDFA = compStart;
		for(State dfa : dfastack) {
			//Our new ending.
			tempDFA = new State();
			trans = new Transition(dfa);
			trans.setNextState(tempDFA);
			prevDFA.addTransition(trans);
			//Now prevDFA is our new ending.
			prevDFA = tempDFA;
		}
		//Set the ending to be our accept state.
		prevDFA.setAcceptState(true);
		return compStart;
	}

	/**
	 * Pass in the string that you wish to find the matching parens for and
	 * the parentheses type that you are looking to find. So if we wish to find
	 * the position of the matching end parens for '(', then '(' is passed in to
	 * parenType and ')' is what we are searching for.
	 * @param str
	 * @param parenType
	 * @return the position of the end parentheses type, or -1 if not found,
	 * Lexical.checkValidRegex() should ensure we don't hit these though.
	 */
	private int posMatchingParen(String str) {
		int pos = 1;
		int numparens = 0;
		char c, startparen, endparen;
		switch(str.charAt(0)) {
		case '(':
			startparen = '(';
			endparen = ')';
			break;
		case '[':
			startparen = '[';
			endparen = ']';
			break;
		default:
			return -1;
		}
		numparens++;
		for(; pos < str.length(); pos++) {
			c = str.charAt(pos);
			if(c == '\\') {
				//Want to skip the next character because it is escaped.
				pos++;
			}
			else if(c == startparen) {
				numparens++;
			}
			else if(c == endparen) {
				numparens--;
				if(numparens == 0) {
					return pos;
				}
				else if(numparens < 0) {
					return -1;
				}
			}
		}
		//If we get here, then the matching parentheses position was not found.
		return -1;
	}
	
	private State createDFAForSimpleString(String s) {
		State start = new State();
		State tempState;
		State lastState = start;
		Transition trans;
		for(int i = 0; i < s.length(); i++) {
			trans = new Transition(s.substring(i, i + 1));
			tempState = new State();
			if(i == s.length() - 1) {
				tempState.setAcceptState(true);
			}
			trans.setNextState(tempState);
			lastState.addTransition(trans);
			lastState = tempState;
		}
		return start;
	}
	
	private State createDFAForBrackets(String s) {
		char c;
		int i = 0, r1 = -1, r2 = -1;
		boolean notrange = false;
		State start = new State();
		State end = new State();
		end.setAcceptState(true);
		Transition trans;
		//Remove the '[' and ']' from the ends of the string.
		s = s.substring(1, s.length() - 1);
		if(s.charAt(0) == '^') {
			i++;
			notrange = true;
		}
		//Must break it up into the different ranges.
		for(; i < s.length(); i++) {
			c = s.charAt(i);
			if(c == '-') {
				if(r1 < 0) {
					//Houstan, we have a problem.
				}
				else {
					i++;
					c = s.charAt(i);
					if(c == '\\') {
						//Then we are escaping a character and need to go one further.
						i++;
						c = s.charAt(i);
					}
					r2 = (int) c;
					trans = new Transition(r1, r2, notrange);
					trans.setNextState(end);
					start.addTransition(trans);
					r1 = -1;
					r2 = -1;
				}
			}
			else if(c == '\\') {
				//Then we are escaping something.
				i++;
				c = s.charAt(i);
				if(r1 < 0) {
					r1 = (int) c;
				}
				else {
					r2 = r1;
					trans = new Transition(r1, r2, notrange);
					trans.setNextState(end);
					start.addTransition(trans);
					r1 = (int) c;
					r2 = -1;
				}
			}
			else {
				if(r1 < 0) {
					r1 = (int) c;
				}
				else {
					r2 = r1;
					trans = new Transition(r1, r2, notrange);
					trans.setNextState(end);
					start.addTransition(trans);
					r1 = (int) c;
					r2 = -1;
				}
			}
		}
		if(r1 > 0) {
			r2 = r1;
			trans = new Transition(r1, r2, notrange);
			trans.setNextState(end);
			start.addTransition(trans);
		}
		return start;
	}
	
	private State createDFAForRepeaters(State state, String s) {
		State start = new State();
		State end = new State();
		end.setAcceptState(true);
		Transition trans = new Transition(state, s);
		trans.setNextState(end);
		start.addTransition(trans);
		return start;
	}
	
	private State createDFAUnion(State dfa1, State dfa2) {
		State start = new State();
		State end = new State();
		end.setAcceptState(true);
		Transition trans = new Transition(dfa1);
		trans.setNextState(end);
		start.addTransition(trans);
		trans = new Transition(dfa2);
		trans.setNextState(end);
		start.addTransition(trans);
		return start;
	}
}
