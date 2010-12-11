/**
 * The interpreter will take in the tokens from the parser and will execute 
 * them according to our grammar. So a token that indicates it is a variable
 * will be interpreted as a variable when executed by this interpreter.
 */

package interpreter;

import parser.MiniREToken;
import java.util.List;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREInterpreter {
	private List<MiniREToken> minireprgm;
	
	public void interpretTokens(List<MiniREToken> tokencollection) {
		
	}
	
	public int tokensGrammarLocation(String tokenstr) {
		return 0;
	}
	
	public void executeToken(MiniREToken tok) {
		
	}
}
