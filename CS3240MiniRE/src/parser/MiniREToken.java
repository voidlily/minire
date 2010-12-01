/**
 * Each token will represent the "words" in a programming language. Each
 * token will be interpreted a certain way by the overall grammar, but
 * the token itself will not be self-aware of this handling.
 */
package parser;

import grammar.*;


/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREToken {
	public String tokenstr;
	public Type tokentype;
	//Line number of the token within the program that the token resides in.
	public int linenum;
	
	public MiniREToken(String str) {
		this.tokenstr = str;
		this.tokentype = determineType();
	}
	
	public Type determineType() {
		if(Terminal.determineTokenType(tokenstr) != null) {
			return Type.TERMINAL;
		}
		else if(Lexical.determineTokenType(tokenstr) != null) {
			return Type.LEXICAL;
		}
		else {
			return Type.INVALID;
		}
	}
}
