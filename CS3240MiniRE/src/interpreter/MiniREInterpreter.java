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
		boolean ended = false;
		boolean started = false;
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
			if(ended) {
				//Then we have additional tokens when we shouldn't have.
				System.out.println("The program continues after it supposedly ends.");
				System.out.println("See line " + tok.getLinenum() + " in the program.");
				System.exit(0);
			}
			
			buff.add(tok);
			if(tok.getTokentype() == MiniREToken.Type.TERMINAL) {
				//Is it the the semicolon terminal.
				if(tok.getTerm() == Terminal.semicolon) {
					executeTokens(buff);
					buff.clear();
				}
				else if(tok.getTerm() == Terminal.begin) {
					if(buff.size() > 1) {
						//For some reason we are getting a begin terminal when it isn't the
						//start of the program.
						System.out.println("The 'begin' terminal is not expected on line " + tok.getLinenum());
						System.exit(0);
					}
					//Let us know that the program has been started.
					started = true;
					buff.clear();
				}
				else if(tok.getTerm() == Terminal.end) {
					if(buff.size() > 1) {
						//Then there is more than just "end" in the buffer.
						//We won't get here if the previous token is a ';'.
						//Therefore, the previous "line" of the program is 
						//invalid and we can go ahead and exit out of it.
						System.out.println("The 'end' terminal was hit without the previous line ending in a ';'");
						System.out.println("The 'end' terminal is found on line " + tok.getLinenum());
						System.exit(0);
					}
					//Set the boolean that indicates that we should be done.
					ended = true;
					buff.clear();
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
			
			if(!started) {
				//The first token in the program should start us properly.
				System.out.println("The first terminal in the program was not 'begin'.");
				System.exit(0);
			}
		}
	}
	
	public void executeTokens(List<MiniREToken> tokens) {
		
	}
}
