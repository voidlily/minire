/**
 *
 */
package grammar;

import minire.CharacterMatcher;

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

	private final String str;
	@SuppressWarnings("unused")
	private final String regularexp;
	Lexical(String str, String regex) {
		this.str = str;
		this.regularexp = regex;
	}

	/**
	 * Determines which Lexical the passed in string belongs to and returns that.
	 * @param tok
	 * @return null if the passed in string matches none of the Lexicals, a Lexical
	 * if it does match one of the Lexicals.
	 */
	public static Lexical determineTokenType(String tok) {
		for(Lexical l : Lexical.values()) {
			if(tok.equals(l.str)) {
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
	public static boolean checkStrAgainstLexicalType(String str, Lexical lex) {
		boolean result = false;
		int i;
		switch(lex) {
		case ID:
			//Length must be 1-10 characters long.
			if(str.length() > 0 && str.length() <= 10) {
				if(CharacterMatcher.isLetter(str.charAt(0))) {
					result = true;
					for(i = 1; i < str.length(); i++) {
						if(CharacterMatcher.isLetterOrDigit(str.charAt(i)) || str.charAt(i) == '_') {
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
						if(CharacterMatcher.isDigit(str.charAt(i))) {
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
						if(CharacterMatcher.isDigit(str.charAt(i))) {
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
			result = true;
			if(str.length() >= 2) {
				if(str.charAt(0) == '\'') {
					for(i = 1; i < str.length() - 1; i++) {
						if(CharacterMatcher.isLetterOrDigit(str.charAt(i))) {
							//These are perfectly safe as is.
						}
						else if(str.charAt(i) == '\\') {
							//Then check the next character to see what we are escaping.
							if(regexEscapeOutsideBrackets(str.charAt(i + 1))) {
								//Then we are escaping something we are supposed to.
								i++;
							}
							else {
								//We don't need to escape this character.
								result = false;
								break;
							}
						}
						else if(str.charAt(i) == '[') {
							/* Now we are inside [], must ensure that:
							 * * There are no nested []
							 * * We escape the necessary characters inside the []
							 */
							for(int j = i + 1; j < str.length() - 1; j++) {
								if(str.charAt(j) == ']') {
									//Then we are closing the []s.
									break;
								}
								else if(str.charAt(j) == '\\') {
									//Then is the next character something that should be escaped?
									if(regexEscapeInBrackets(str.charAt(j + 1))) {
										//We should be escaping this character.
										j++;
									}
									else {
										result = false;
										break;
									}
								}
								else if(regexEscapeInBrackets(str.charAt(j))) {
									//Then should have been escaped.
									result = false;
									break;
								}
							}
						}
						else if(regexEscapeOutsideBrackets(str.charAt(i))) {
							//Then we hit something that likely should be escaped and isn't.
							result = false;
							break;
						}
					}
					if(result && str.charAt(str.length() - 1) == '\'') {
						result = true;
					}
					else {
						result = false;
					}
				}
				else {
					result = false;
				}
			}
			else {
				result = false;
			}
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

	private static boolean regexEscapeInBrackets(char c) {
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

	private static boolean regexEscapeOutsideBrackets(char c) {
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
}
