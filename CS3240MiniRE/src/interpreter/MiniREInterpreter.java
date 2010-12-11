/**
 * The interpreter will take in the tokens from the parser and will execute 
 * them according to our grammar. So a token that indicates it is a variable
 * will be interpreted as a variable when executed by this interpreter.
 */

package interpreter;

import parser.MiniREToken;
import grammar.Terminal;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREInterpreter {

	public void interpretTokens(List<MiniREToken> tokencollection) {
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
		for(MiniREToken tok : tokencollection) {
			if(tok.getTokentype() == MiniREToken.Type.TERMINAL) {
				//Is it the the semicolon terminal.
				if(tok.getTerm() == Terminal.semicolon) {
					
				}
			}
			else if(tok.getTokentype() == MiniREToken.Type.LEXICAL) {
				
			}
			else {
				//Then it is INVALID.
				System.out.println("The parser found that the token: "+ tok.getTokenstr());
				System.out.println("At line " + tok.getLinenum() + " is invalid.");
				System.exit(0);
			}
		}
	}
	
	public void executeTokens(MiniREToken tok) {
		
	}
}
