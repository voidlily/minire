/**
 *
 */
package grammar;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public enum Terminal {
	begin ("begin"),
	end ("end"),
	replace ("replace"),
	with ("with"),
	in ("in"),
	arrow ("->"),
	openparen ("("),
	closeparen (")"),
	semicolon (";"),
	assignment (":="),
	comma (","),
	pound ("#"),
	find ("find"),
	plus ("+"),
	minus ("-"),
	mult ("*"),
	div ("/"),
	union ("union"),
	inters ("inters"),
	//Bonus part.
	ifterm ("if"),
	opencurly ("{"),
	closecurly ("}"),
	elseterm ("else"),
	whileterm ("while"),
	equalbool ("=="),
	notequalbool ("!="),
	lessbool ("<"),
	greaterbool (">"),
	lessequalbool ("<="),
	greaterequalbool (">=");

	private final String str;
	Terminal(final String str) {
		this.str = str;
	}

	public static Terminal determineTokenType(final String tok) {
		for(Terminal t : Terminal.values()) {
			if(tok.equals(t.str)) {
				return t;
			}
		}
		return null;
	}

	public String toString() {
		return str;
	}
}
