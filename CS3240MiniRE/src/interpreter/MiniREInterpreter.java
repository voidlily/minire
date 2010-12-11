/**
 * The interpreter will take in the tokens from the parser and will execute
 * them according to our grammar. So a token that indicates it is a variable
 * will be interpreted as a variable when executed by this interpreter.
 */

package interpreter;

import grammar.Lexical;
import grammar.Terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import minire.CharacterHelper;
import parser.MiniREToken;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREInterpreter {
	private HashMap<String, Object> sym;

	public void interpretTokens(List<MiniREToken> tokencollection) {
		sym = new HashMap<String, Object>();
		boolean ended = false;
		boolean started = false;
		MiniREToken tok;
		if(tokencollection == null || tokencollection.size() == 0) {
			System.out.println("Token collection passed to interpretTokens() was not usable because:");
			if(tokencollection == null) {
				System.out.println("The passed in token collection was null.");
			}
			else if(tokencollection.size() == 0) {
				System.out.println("The size of the token collection was 0.");
			}
			System.exit(0);
		}
		List<MiniREToken> buff = new ArrayList<MiniREToken>();
		for(int i = 0; i < tokencollection.size(); i++) {
			tok = tokencollection.get(i);
			if(ended) {
				//Then we have additional tokens when we shouldn't have.
				System.out.println("The program continues after it supposedly ends.");
				System.out.println("See line " + tok.getLinenum() + " in the program.");
				System.exit(0);
			}

			buff.add(tok);
			if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.begin) {
				if(started) {
					//Then we shouldn't have hit the begin terminal again.
					System.out.println("Hit the begin terminal on line " + tok.getLinenum() + " when the program has already begun.");
					System.exit(0);
				}
				started = true;
				buff.clear();
			}
			else if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.end) {
				if(!started || ended) {
					//Then we shouldn't hit the end terminal.
					System.out.println("Hit the end terminal on line " + tok.getLinenum() + " when it was unexpected.");
					System.exit(0);
				}
				ended = true;
				buff.clear();
			}
			else if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.ifterm) {
				//Get the if-else block.
				buff = getIfElseBlock(i, tokencollection);
				i += buff.size() - 1;
				executeBlock(buff);
				buff.clear();
			}
			else if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.whileterm) {
				//Get the while block.
				buff = getWhileBlock(i, tokencollection);
				i += buff.size() - 1;
				executeBlock(buff);
				buff.clear();
			}
			else {
				//Then just get the line to run. This should be an ID, 'print', or 'replace'
				//according to the grammar.
				buff = getLineBlock(i, tokencollection);
				i += buff.size() - 1;
				executeLine(buff);
				buff.clear();
			}

			if(!started) {
				//The first token in the program should start us properly.
				System.out.println("The first terminal in the program was not 'begin'.");
				System.exit(0);
			}
		}
	}

	/**
	 * Takes in a "buffer" of tokens that are supposed to end with a ';', effectively
	 * being a single line of code to execute.
	 * @param tokens is the buffer that should be executed.
	 */
	@SuppressWarnings("unchecked")
	private void executeLine(List<MiniREToken> tokens) {
		MiniREToken tok;
		int tempint;
		if(tokens.size() > 0) {
			tok = tokens.get(0);

			// Assignment
			if(tok.getTokentype() == MiniREToken.Type.LEXICAL && tok.getLex() == Lexical.ID) {
				//The only grammar rule that should match is STATEMENT1, else
				//it didn't match the grammar properly in the parser.
				tok = tokens.get(1); // should be :=
				if(tok.getTokentype() != MiniREToken.Type.TERMINAL || tok.getTerm() != Terminal.assignment) {
					System.out.println("The variable " + tokens.get(0).getTokenstr()
							+ " is not followed by an assignment to some exp at line "
							+ tok.getLinenum() + ".");
					System.exit(0);
				}
				//Want to make sure the last token is a ';'
				tok = tokens.get(tokens.size() - 1); // ;
				if(tok.getTokentype() != MiniREToken.Type.TERMINAL || tok.getTerm() != Terminal.semicolon) {
					System.out.println("The variable assignment at line "
							+ tokens.get(0).getLinenum() + " does not end with a ';'");
					System.exit(0);
				}
				if(tokens.size() == 4) {
					//Then it is either 'ID := ID;' or 'ID := INTNUM;'
					tok = tokens.get(2); // value (right hand side)

					// value is ID
					if(tok.getTokentype() == MiniREToken.Type.LEXICAL && tok.getLex() == Lexical.ID) {
						//Need to confirm that this other ID already exists in the stack.
						if(!sym.containsKey(tok.getTokenstr())) {
							System.out.println("The variable " + tok.getTokenstr()
									+ " does not exist yet at line " + tok.getLinenum());
							System.exit(0);
						}
						// store a pointer to var 1's value in var 2's entry
						sym.put(tokens.get(0).getTokenstr(), sym.get(tok.getTokenstr()));
					}

					// value is int
					else if(tok.getTokentype() == MiniREToken.Type.LEXICAL && tok.getLex() == Lexical.INTNUM) {
						//Just have to confirm that the token is an INTNUM.
						if(!Lexical.checkStrAgainstLexicalType(tok.getTokenstr(), Lexical.INTNUM)) {
							System.out.println("Was under the impression that the variable "
									+ tokens.get(0).getTokenstr()
									+ " was supposed to receive an integer number in the assignment at line "
									+ tokens.get(0).getLinenum());
							System.exit(0);
						}
						tempint = CharacterHelper.convertStringToInt(tok.getTokenstr());
						// use new Integer to make sure it's stored as object
						sym.put(tokens.get(0).getTokenstr(), new Integer(tempint));
					}
					else {
						//Then we didn't get ID or INTNUM and must error.
						System.out.println("Was expecting the variable to be set "
								+ "to another variable or an integer, did not "
								+ "find either at line " + tok.getLinenum());
						System.exit(0);
					}
				}
				else {
					//Then it is 'ID := ( <exp> );' 'ID := # <exp>;' '<exp> <bin-op> <exp>' or 'find REGEX in <file>'.
				}
			}
		}

		for(int i = 0; i < tokens.size(); i++) {
			tok = tokens.get(i);
			if(tok.getTokentype() == MiniREToken.Type.LEXICAL && tok.getLex() == Lexical.ID) {

			}




			if(tok.getTokentype() == MiniREToken.Type.TERMINAL) {

			}
		}
	}

	/**
	 * Overloads the executeBlock() function to start it at position 0.
	 * @param tokens
	 */
	private void executeBlock(List<MiniREToken> tokens) {
		executeBlock(tokens, 0);
	}

	/**
	 * Will execute a block of tokens (while or if-else block). This block will
	 * call executeLine() or executeBlock() as appropriate.
	 * @param tokens
	 * @param pos
	 */
	private void executeBlock(List<MiniREToken> tokens, int pos) {

	}

	/**
	 * We want to get the if-else block. We know that the first token will be 'if'.
	 * @param pos
	 * @param tokencollection
	 * @return
	 */
	private List<MiniREToken> getIfElseBlock(int pos, List<MiniREToken> tokencollection) {
		List<MiniREToken> buff = new ArrayList<MiniREToken>();
		List<MiniREToken> tempbuff = new ArrayList<MiniREToken>();
		MiniREToken tok;
		for(int i = pos; i < tokencollection.size(); i++) {
			tok = tokencollection.get(i);
			buff.add(tok);
			if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.whileterm) {
				//Then we need to recursively call getWhileBlock().
				tempbuff = getWhileBlock(i, tokencollection);
				i += tempbuff.size() - 1;
				//Remove the while token that was just added.
				buff.remove(tok);
				//Add the tempbuff to the present buff.
				buff.addAll(tempbuff);
			}
			//Want to make sure that we are not recursively hitting the same token over and over.
			else if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.ifterm && buff.size() > 1) {
				//Then we need to recursively call getIfElseBlock().
				tempbuff = getIfElseBlock(i, tokencollection);
				i += tempbuff.size() - 1;
				//Remove the if token that was just added.
				buff.remove(tok);
				//Add the tempbuff to the present buff.
				buff.addAll(tempbuff);
			}
			else if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.closecurly) {
				//Then we have reached the end of the while block.
				break;
			}
		}

		return buff;
	}

	/**
	 * We want to get the while block. We know that the first token will be 'while'.
	 * @param pos
	 * @param tokencollection
	 * @return
	 */
	private List<MiniREToken> getWhileBlock(int pos, List<MiniREToken> tokencollection) {
		List<MiniREToken> buff = new ArrayList<MiniREToken>();
		List<MiniREToken> tempbuff = new ArrayList<MiniREToken>();
		MiniREToken tok;
		for(int i = pos; i < tokencollection.size(); i++) {
			tok = tokencollection.get(i);
			buff.add(tok);
			//Want to make sure that we are not recursively hitting the same token over and over.
			if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.whileterm && buff.size() > 1) {
				//Then we need to recursively call getWhileBlock().
				tempbuff = getWhileBlock(i, tokencollection);
				i += tempbuff.size() - 1;
				//Remove the while token that was just added.
				buff.remove(tok);
				//Add the tempbuff to the present buff.
				buff.addAll(tempbuff);
			}
			else if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.ifterm) {
				//Then we need to recursively call getIfElseBlock().
				tempbuff = getIfElseBlock(i, tokencollection);
				i += tempbuff.size() - 1;
				//Remove the if token that was just added.
				buff.remove(tok);
				//Add the tempbuff to the present buff.
				buff.addAll(tempbuff);
			}
			else if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.closecurly) {
				//Then we have reached the end of the while block.
				break;
			}
		}
		return buff;
	}

	/**
	 * We want to get the line by itself to run it. The first token could be an ID,
	 * 'replace', or 'print'.
	 * @param pos
	 * @param tokencollection
	 * @return
	 */
	private List<MiniREToken> getLineBlock(int pos, List<MiniREToken> tokencollection) {
		List<MiniREToken> buff = new ArrayList<MiniREToken>();
		MiniREToken tok;
		for(int i = pos; i < tokencollection.size(); i++) {
			tok = tokencollection.get(i);
			buff.add(tok);
			if(tok.getTokentype() == MiniREToken.Type.TERMINAL && tok.getTerm() == Terminal.semicolon) {
				//Then the end of the line has been reached.
				break;
			}
		}
		return buff;
	}
}
