/**
 * Each token will represent the "words" in a programming language. Each
 * token will be interpreted a certain way by the overall grammar, but
 * the token itself will not be self-aware of this handling.
 */
package parser;

import grammar.Lexical;
import grammar.Terminal;


/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREToken {
	/** String representation of the token. */
	private String tokenstr;
	/** Enum type of the token. */
	private Type tokentype;
	/** Line number of the token within the program that the token resides in. */
	private int linenum;

	/** MiniREToken type enum. */
	public static enum Type {
		LEXICAL,
		TERMINAL,
		INVALID,
		RULE;
	}

	/**
	 * @param str string representation of the token
	 */
	public MiniREToken(final String str) {
		this.tokenstr = str;
		this.tokentype = determineType();
	}

	public Type determineType() {
		if (Terminal.determineTokenType(tokenstr) != null) {
			return Type.TERMINAL;
		} else if (Lexical.determineTokenType(tokenstr) != null) {
			return Type.LEXICAL;
		} else {
			return Type.INVALID;
		}
	}

	/**
	 * @return the tokenstr
	 */
	public String getTokenstr() {
		return tokenstr;
	}

	/**
	 * @param tokenstr the tokenstr to set
	 */
	public void setTokenstr(final String tokenstr) {
		this.tokenstr = tokenstr;
	}

	/**
	 * @return the tokentype
	 */
	public Type getTokentype() {
		return tokentype;
	}

	/**
	 * @param tokentype the tokentype to set
	 */
	public void setTokentype(final Type tokentype) {
		this.tokentype = tokentype;
	}

	/**
	 * @return the linenum
	 */
	public int getLinenum() {
		return linenum;
	}

	/**
	 * @param linenum the linenum to set
	 */
	public void setLinenum(int linenum) {
		this.linenum = linenum;
	}
}
