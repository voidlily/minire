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

import grammar.GrammarRule;
import grammar.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	
	public List<List<MiniREToken>> getTokensByLines(List<MiniREToken> tokens) throws IOException {
		List<List<MiniREToken>> ret = new ArrayList<List<MiniREToken>>();
		List<MiniREToken> current_line = new ArrayList<MiniREToken>();
		int linenum = tokens.get(0).getLinenum();
		for (MiniREToken t: tokens) {
			if(Terminal.determineTokenType(t.getTokenstr())==Terminal.semicolon) {
				t.setLinenum(t.getLinenum()-1);
			}
			
			if(t.getLinenum() > linenum) {
				ret.add(current_line);
				current_line = new ArrayList<MiniREToken>();
				current_line.add(t);
				linenum = t.getLinenum();
			} else {
				current_line.add(t);
			}
		}
		ret.add(current_line); //adds final line
		return ret;
	}
	
	public MiniREToken.Type[] getRuleByLine(List<MiniREToken> line) {
		MiniREToken.Type[] ret = null;
		List<MiniREToken.Type> line_types = new ArrayList<MiniREToken.Type>();
		for (MiniREToken t: line) {
			line_types.add(t.getTokentype());
		}
		for (GrammarRule g: GrammarRule.values()) {
			MiniREToken.Type[] t_rule = g.getRuletypes();
			if(line_types.size() == t_rule.length) {
				for (int i = 0; i < line_types.size(); i++) {
					boolean c_valid = true;
					if (line_types.get(i)!=t_rule[i]) {
						c_valid = false;
					}
					
					if(c_valid == false) {
						i++;
					} else {
						if (i==line_types.size()-1){
							return t_rule;
						}
					}
				}
			}
		}
		return ret;
	}
	
}
