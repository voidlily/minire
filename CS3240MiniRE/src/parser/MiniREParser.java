/**
 * The parser will take in the program that will be run and then parse
 * it according to the grammar rules that are in place. Included functionality
 * of the parser:
 * * Take in the program
 * * Go through the program line by line and find all tokens (scanner)
 * * Check each token and set of tokens against a defined grammar
 * * Determine if an error exists in the passed in program
 * * Hand it back to the caller in a format the interpreter can run
 * 
 */

package parser;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREParser {
	private String programfile;
	private MiniREToken[] tokencollection;
	
	public void fileToParse(String filepath) {
		
	}
	
	public MiniREToken[] getAllTokens() {
		return null;
	}
}
