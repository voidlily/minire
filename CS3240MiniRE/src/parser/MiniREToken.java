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
	/** If Terminal, the terminal type, else null. */
	private Terminal term;
	/** If Lexical, the lexical type, else null. */
	private Lexical lex;
	/** Space between tokens (for 2 - -2 case) */
	private boolean spaceAfterToken;

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
	public MiniREToken(final String str, final int linenum) {
		this(str, linenum, true);
	}

	/**
	 * @param str string representation of the token
	 * @param spaceAfterToken TODO
	 */
	public MiniREToken(final String str, final int linenum, boolean spaceAfterToken) {
		this.term = null;
		this.lex = null;
		this.tokenstr = str;
		this.tokentype = determineType();
		this.linenum = linenum;
		this.spaceAfterToken = spaceAfterToken;
	}

	public MiniREToken(String str) {
		this(str, 0, true);
	}

	public Type determineType() {
		term = Terminal.determineTokenType(tokenstr);
		lex = Lexical.determineTokenType(tokenstr);
		if (term != null) {
			return Type.TERMINAL;
		} else if (lex != null) {
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

	/**
	 * @return the term
	 */
	public Terminal getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(Terminal term) {
		this.term = term;
	}

	/**
	 * @return the lex
	 */
	public Lexical getLex() {
		return lex;
	}

	/**
	 * @param lex the lex to set
	 */
	public void setLex(Lexical lex) {
		this.lex = lex;
	}

	/**
	 * @return the spaceAfterToken
	 */
	public boolean isSpaceAfterToken() {
		return spaceAfterToken;
	}

	/**
	 * @param spaceAfterToken the spaceAfterToken to set
	 */
	public void setSpaceAfterToken(boolean spaceAfterToken) {
		this.spaceAfterToken = spaceAfterToken;
	}
}
