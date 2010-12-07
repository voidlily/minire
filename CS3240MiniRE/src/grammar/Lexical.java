/**
 * 
 */
package grammar;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public enum Lexical {
	ID ("ID", "'[a-zA-Z][a-zA-Z0-9_]{0,9}'"),
	INTNUM ("INTNUM", "'(-)?[1-9][0-9]*|0'"),
	//Not sure if this regex will even catch ALL regex. We will likely have to
	//manually implement the regex and regex checking anyways, making this more
	//of a guide in that case.
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
		switch(lex) {
		case ID:
			//Length must be 1-10 characters long.
			if(str.length() > 0 && str.length() <= 10) {
				if(Character.isLetter(str.charAt(0))) {
					result = true;
					for(int i = 1; i < str.length(); i++) {
						if(Character.isLetterOrDigit(str.charAt(i)) || str.charAt(i) == '_') {
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
			try {
				Integer.parseInt(str);
				result = true;
			} catch (NumberFormatException e) {
				result = false;
			}
			break;
		case REGEX:
			break;
		case ASCIISTR:
			break;
		case EMPTYSTR:
			break;
		default:
			break;
		}
		
		return result;
	}
}
