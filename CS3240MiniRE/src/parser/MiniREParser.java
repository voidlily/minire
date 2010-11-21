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

import java.io.*;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREParser {
	private String programfilepath;
	private FileReader programfilereader;
	private MiniREToken[] tokencollection;
	private MiniREScanner scanner;
	
	public MiniREParser() {
		scanner = new MiniREScanner();
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
	 * @return
	 */
	public MiniREToken[] getAllTokens() {
		try {
			String line = "";
			char nextchar;
			if(programfilereader.ready()) {
				//Read till the end of the file.
				nextchar = (char) programfilereader.read();
				while(nextchar != -1) {
					while(nextchar != '\n') {
						line += nextchar;
						nextchar = (char) programfilereader.read();
					}
					scanner.parseThisLine(line);
					while(scanner.hasMoreTokens()) {
						tokencollection[tokencollection.length] = scanner.getNextToken();
					}
				}
			}
			return tokencollection;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
