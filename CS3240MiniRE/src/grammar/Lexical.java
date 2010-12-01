/**
 * 
 */
package grammar;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public enum Lexical {
	ID ("ID", "regex"),
	INTNUM ("INTNUM", "regex"),
	REGEX ("REGEX", "regex"),
	ASCIISTR ("ASCIISTR", "regex"),
	EMPTYSTR ("EMPTYSTR", "regex");
	
	private final String str;
	private final String regularexp;
	Lexical(String str, String regex) {
		this.str = str;
		this.regularexp = regex;
	}
	
	public static Lexical determineTokenType(String tok) {
		for(Lexical l : Lexical.values()) {
			//TODO: Instead of just testing if the token equals one of the types, 
			//validate the token string to one of the types (INTNUM will be a numeric
			//number that can be tested with Interger.parseInt()).
			if(tok.equals(l.str)) {
				return l;
			}
		}
		return null;
	}
}
