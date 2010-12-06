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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREParser {
	private String programfilepath;
	private FileReader programfilereader;
	private MiniREToken[] tokencollection;
	private MiniREScanner scanner;

	public MiniREParser() {
		scanner = new MiniREScanner(programfilereader);
	}

	/**
	 * Sets the program file reader that is going to be used to read the program
	 * by our "getAllTokens" function.
	 * @param filepath
	 */
	public void fileToParse(String filepath) {
		File prgmfile = new File(filepath);
		if(prgmfile.exists()) {
			programfilepath = filepath;
			try {
				programfilereader = new FileReader(prgmfile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			//Then the inputed file does not exist, raise an error accordingly.
		}
	}

	/**
	 * Creates the tokens that represent the file and checks each against the
	 * grammar.
	 * @return list of tokens
	 * @throws IOException if scanner throws exception
	 */
	public List<MiniREToken> getAllTokens() throws IOException {
		List<MiniREToken> tokens = scanner.scan();
		return tokens;
	}
}
