/**
 *
 */
package grammar;

import minire.CharacterHelper;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public enum Lexical {
	ID ("ID", "'[a-zA-Z][a-zA-Z0-9_]{0,9}'"),
	INTNUM ("INTNUM", "'(-)?[1-9][0-9]*|0'"),
	//Not sure if this regex will even catch ALL regex. We will likely have to
	//manually implement the regex and regex checking anyways, making this more
	//of a guide in that case. Doesn't really matter though, it is just for reference.
	REGEX ("REGEX", "'\'([a-zA-Z0-9_]|\\ |\\\\|\\*|\\+|\\?|\\||\\[|\\]\\(|\\)|\\.|\\'|\\\"|\\[.\\])*\''"),
	ASCIISTR ("ASCIISTR", "'\".\"'"),
	EMPTYSTR ("EMPTYSTR", "");

	@SuppressWarnings("unused")
	private final String str;
	@SuppressWarnings("unused")
	private final String regularexp;
	Lexical(final String str, final String regex) {
		this.str = str;
		this.regularexp = regex;
	}

	/**
	 * Determines which Lexical the passed in string belongs to and returns that.
	 * @param tok
	 * @return null if the passed in string matches none of the Lexicals, a Lexical
	 * if it does match one of the Lexicals.
	 */
	public static Lexical determineTokenType(final String tok) {
		for(Lexical l : Lexical.values()) {
			if(checkStrAgainstLexicalType(tok, l)) {
				return l;
			}
		}
		return null;
	}

	/**
	 * Allows us to check to see if a string matches its Lexical class.
	 * @param str
	 * @param lex
	 * @return return true if the string matches the passed in lexical class,
	 * else returns false.
	 */
	public static boolean checkStrAgainstLexicalType(final String str, final Lexical lex) {
		boolean result = false;
		int i;
		switch(lex) {
		case ID:
			//Length must be 1-10 characters long.
			if(str.length() > 0 && str.length() <= 10) {
				if(CharacterHelper.isLetter(str.charAt(0))) {
					result = true;
					for(i = 1; i < str.length(); i++) {
						if(CharacterHelper.isLetterOrDigit(str.charAt(i)) || str.charAt(i) == '_') {
							result = true;
						}
						else {
							result = false;
							break;
						}
					}
				}
				else {
					result = false;
				}
			}
			break;
		case INTNUM:
			if(str.length() > 0) {
				if(str.charAt(0) == '-') {
					//Then is a negative number.
					for(i = 1; i < str.length(); i++) {
						// leading zeros are invalid
						if (str.length() > 2 && str.charAt(1) == '0') {
							// kind of annoying because adds an extra n checks
							result = false;
							break;
						}
						if(CharacterHelper.isDigit(str.charAt(i))) {
							result = true;
						}
						else {
							result = false;
							break;
						}
					}
				}
				else{
					//Then not a negative number.
					for(i = 0; i < str.length(); i++) {
						if (str.charAt(0) == '0') {
							// kind of annoying because adds an extra n checks
							result = false;
							break;
						}
						if(CharacterHelper.isDigit(str.charAt(i))) {
							result = true;
						}
						else {
							result = false;
							break;
						}
					}
				}
			}
			break;
		case REGEX:
			result = checkValidRegex(str);
			break;
		case ASCIISTR:
			//Make sure that the string is at least ""
			if(str.length() >= 2) {
				if(str.charAt(0) == '"') {
					for(i = 1; i < str.length() - 1; i++) {
						if(str.charAt(i) == '\\') {
							//Want to make sure we aren't escaping the end quote by testing:
							//j != str.length() - 2
							if(i == str.length() - 2) {
								//Then we are trying to escape the final quote, or it
								//doesn't end with a quote.
								result = false;
								break;
							}
							//Check the next character to make sure we are escaping
							//something that actually needs escaping.
							if(str.charAt(i + 1) == '\\') {
								i++;
							}
							else if(str.charAt(i + 1) == '"') {
								i++;
							}
							else {
								//Then the '\' or '"' is not escaped.
								result = false;
								break;
							}
						}
						else if(str.charAt(i) == '"') {
							//Then it wasn't preceded by a \, so isn't escaped.
							result = false;
							break;
						}
						//Anything else is allowed within an ASCII string.
					}
					if(str.charAt(str.length() - 1) == '"') {
						result = true;
					}
				}
			}
			break;
		case EMPTYSTR:
			break;
		}

		return result;
	}

	private static boolean regexEscapeInBrackets(final char c) {
		/*
		 * '\', '[', ']', '^', '-'
		 */
		boolean result = false;
		switch(c) {
		case '\\':
		case '[':
		case ']':
		case '^':
		case '-':
			result = true;
			break;
		}
		return result;
	}

	private static boolean regexEscapeOutsideBrackets(final char c) {
		/*
		 * ' ', '\', '*', '+', '?', '|', '[', ']', '(', ')', '.', ''', '"'
		 */
		boolean result = false;
		switch(c) {
		case ' ':
		case '\\':
		case '*':
		case '+':
		case '?':
		case '|':
		case '[':
		case ']':
		case '(':
		case ')':
		case '.':
		case '\'':
		case '"':
			result = true;
			break;
		}
		return result;
	}

	public static boolean checkValidRegex(final String str) {
		boolean result = false;
		int i = 0;
		char c;
		if(str.length() < 2) {
			//A valid regex has at least the two single quotes.
			return result;
		}
		else {
			//This will catch any regex with at least the single quotes.
			if(str.charAt(i) != '\'') {
				//Must start with a single quote.
				return result;
			}
			i++;
			//Want to check the last character of the string separately.
			for(; i < str.length() - 1; i++) {
				c = str.charAt(i);
				//Check to make sure it is an ASCII printable character first.
				if((int) c < 32 || (int) c > 126) {
					return result;
				}

				if(c == '\\') {
					//Then we are supposed to be escaping something.
					i++;
					c = str.charAt(i);
					if(!regexEscapeOutsideBrackets(c)) {
						//Then we are escaping something that didn't need to be escaped.
						return result;
					}
					if(i >= str.length() - 1) {
						//Then we are escaping the final single quote, which is
						//unacceptable.
						return result;
					}
				}
				else if(c == '.') {
					//Then any ASCII printable character is allowed.
				}
				else if(c == '[') {
					//Handle special parsing for the character class [].
					//Look at the next character.
					i++;
					if(str.charAt(i) == '^') {
						//Then we want to go to the next character.
						i++;
					}
					for(int j = i; j < str.length() - 2; j++) {
						c = str.charAt(j);
						if(c == '\\') {
							j++;
							c = str.charAt(j);
							if(!regexEscapeInBrackets(c)) {
								return result;
							}
						}
						else if(c == '-') {
							if(checkASCIIPrintable(str.charAt(j - 1)) && checkASCIIPrintable(str.charAt(j + 1))) {
								//Checking that before and after the dash is ASCII printable.
								if(regexEscapeInBrackets(str.charAt(j - 1))) {
									//Then make sure it was escaped.
									if(str.charAt(j - 2) != '\\') {
										return result;
									}
								}
								if(regexEscapeInBrackets(str.charAt(j + 1))) {
									//This shouldn't be part of a character class,
									//unless it is the escape character escaping
									//something else.
									if(str.charAt(j + 1) == '\\') {
										if(!regexEscapeInBrackets(str.charAt(j + 2))) {
											return result;
										}
									}
								}
							}
							else {
								return result;
							}
						}
						else if(c == ']') {
							//Want to go back a character so that we can reliably increment
							//by one character after the for loop.
							i = j;
							break;
						}
						else if(regexEscapeInBrackets(c)) {
							return result;
						}
						i = j;
					}
					if(c != ']') {
						i++;
						c = str.charAt(i);
						if(c != ']') {
							//If we still haven't found the ']', then return false.
							return result;
						}
					}
				}
				else if(c == '|') {
					//Make sure that the next character is not the '|' again.
					i++;
					c = str.charAt(i);
					if(c == '|') {
						return result;
					}
				}
				else if(c == '(') {
					if(!matchParens(str.substring(i))) {
						return result;
					}
				}
				else if(c == ')') {
					continue;
				}
				else if(c == '+' || c == '*' || c == '?') {
					continue;
				}
				else if(regexEscapeOutsideBrackets(c)) {
					return result;
				}
			}
			if(str.charAt(i) != '\'') {
				return result;
			}
			else {
				result = true;
			}
		}
		return result;
	}

	private static boolean checkASCIIPrintable(char c) {
		return ((int) c >= 32 && (int) c <= 126);
	}

	private static boolean matchParens(String str) {
		boolean result = false;
		int numparens = 0;
		char c;
		for(int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			if(c == '(') {
				numparens++;
			}
			else if(c == ')') {
				numparens--;
				if(numparens == 0) {
					result = true;
					break;
				}
				else if(numparens < 0) {
					break;
				}
			}
		}
		if(numparens == 0) {
			result = true;
		}
		else {
			result = false;
		}
		return result;
	}
}
