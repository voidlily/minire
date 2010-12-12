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
import grammar.Lexical;
import grammar.Terminal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import minire.CharacterHelper;

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
	
	Iterator<MiniREToken> itr;
	
	MiniREToken token;
	
	private boolean match(String ExpectedName) {
		MiniREToken ExpectedToken = new MiniREToken(ExpectedName);
		if (token.getTokenstr().equals(ExpectedToken.getTokenstr()) && token.getTokentype().equals(ExpectedToken.getTokentype())){
			token = itr.next();
			return true;
		} else {
			System.err.println("token does not match expected value on line "+token.getLinenum());
			return false;
		}
	}
	
	private void begin() {
		//begin <statement-list> end
		while(!token.getTokenstr().equals("end")){
			statement();
		}
	}
	
	private void statement() {
		//<statement> <statement-list>
		token = itr.next();
		if (token.getTokenstr().equals("replace")) {
				replace();
		} else if (token.getTokenstr().equals("print")) {
				print();
		} else if (token.getTokenstr().equals("ID")) {
				id();
		}
	}
	
	private void replace() {
		//replace REGEX with ASCIISTR in <file-names> ;
		token = itr.next();
		if(token.getLex()==Lexical.REGEX) {
			if(match("with")){
				if(token.getLex()==Lexical.ASCIISTR) {
					if(match("in")){
						filenames();
						match(";");
					}
				}
			}
			
		} else {
			System.err.println("invalid replace call on line "+token.getLinenum());
		}
	}
	
	private void print() {
		//print ( <exp-list> ) ;
		match("(");
		exp_list();
		match(")");
	}
	
	private void exp_list() {
		while(!token.getTokenstr().equals(")")){
			exp();
		}
	}
	
	private void filenames() {
		String left = file();
		match("->");
		left+=file();
	}
	
	private String file() {
		token = itr.next();
		if(token.getLex()==Lexical.ASCIISTR) {
			
		}
	}
	
	private void find() {
		//find REGEX in <file>
		token = itr.next();
		if(token.getLex()==Lexical.REGEX) {
			if(match("in")){
				file();
				match(";");
			}
		} else {
			System.err.println("invalid find call on line "+token.getLinenum());
		}
	}
	
	private void id() {
		switch(itr.next()){
			case Terminal.assignment:
				break;
			else:
				break;
		}
	}
	
	private void exp() {
		token left = token;
		token = itr.next();
		switch(token.getTerm()){
			case mult:
				token = itr.next();
				
				break;
			case div:
				break;
			case plus:
				break;
			case minus:
				break;
			case semicolon:
				return left;
				break;
			else:
				break;
		}
	}
	
	
}
