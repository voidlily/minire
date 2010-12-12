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

import interpreter.MiniREInterpreter;
import parser.MiniREToken.Type;

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
	
	private MiniREInterpreter interp = new MiniREInterpreter();

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
			System.err.println("token does not match expected value "+ExpectedName+" on line "+token.getLinenum());
			return false;
		}
	}
	
	private boolean match(Terminal term) {
		if (token.getTokentype()==MiniREToken.Type.TERMINAL && token.getTerm() == term) {
			token = itr.next();
			return true;
		} else {
			System.err.println("token does not match Terminal "+term+" on line "+token.getLinenum());
			return false;
		}
	}
	
	private boolean match(Lexical lex) {
		if (token.getTokentype()==MiniREToken.Type.LEXICAL && token.getLex() == lex) {
			token = itr.next();
			return true;
		} else {
			System.err.println("token does not match Lexical "+lex+" on line "+token.getLinenum());
			return false;
		}
	}
	
	/** matches current token without iterating forward */
	private boolean cmatch(String ExpectedName) {
		MiniREToken ExpectedToken = new MiniREToken(ExpectedName);
		if (token.getTokenstr().equals(ExpectedToken.getTokenstr()) && token.getTokentype().equals(ExpectedToken.getTokentype())){
			return true;
		} else {
			System.err.println("token does not match expected value "+ExpectedName+" on line "+token.getLinenum());
			return false;
		}
	}
	
	/** matches current token without iterating forward */
	private boolean cmatch(Terminal term) {
		if (token.getTokentype()==MiniREToken.Type.TERMINAL && token.getTerm() == term) {
			return true;
		} else {
			System.err.println("token does not match Terminal "+term+" on line "+token.getLinenum());
			return false;
		}
	}
	
	/** matches current token without iterating forward */
	private boolean cmatch(Lexical lex) {
		if (token.getTokentype()==MiniREToken.Type.LEXICAL && token.getLex() == lex) {
			return true;
		} else {
			System.err.println("token does not match Lexical "+lex+" on line "+token.getLinenum());
			return false;
		}
	}
	
	/** matches next token without iterating forward */
	private boolean nmatch(String ExpectedName) {
		MiniREToken ExpectedToken = new MiniREToken(ExpectedName);
		if (itr.next().getTokenstr().equals(ExpectedToken.getTokenstr()) && itr.next().getTokentype().equals(ExpectedToken.getTokentype())){
			return true;
		} else {
			System.err.println("token does not match expected value "+ExpectedName+" on line "+itr.next().getLinenum());
			return false;
		}
	}
	
	/** matches next token without iterating forward */
	private boolean nmatch(Terminal term) {
		if (itr.next().getTokentype()==MiniREToken.Type.TERMINAL && itr.next().getTerm() == term) {
			return true;
		} else {
			System.err.println("token does not match Terminal "+term+" on line "+itr.next().getLinenum());
			return false;
		}
	}
	
	/** matches next token without iterating forward */
	private boolean nmatch(Lexical lex) {
		if (itr.next().getTokentype()==MiniREToken.Type.LEXICAL && itr.next().getLex() == lex) {
			return true;
		} else {
			System.err.println("token does not match Lexical "+lex+" on line "+itr.next().getLinenum());
			return false;
		}
	}
	
	private void begin() {
		//begin <statement-list> end
		match("begin");
		while(!token.getTokenstr().equals("end")){
			statement();
		}
		if (token==null) {       
			System.err.println("no matching end terminal");
                }
	}
	
	private void statement() {
		//<statement> <statement-list>
		if (cmatch("replace")) {
				replace();
		} else if (cmatch("print")) {
				print();
		} else if (cmatch(Lexical.ID)) {
				id();
		}
	}
	
	private void replace() {
		//replace REGEX with ASCIISTR in <file-names> ;
			match("replace");
			match(Lexical.REGEX);
			match("with");
			match(Lexical.ASCIISTR);
			match("in");
			filenames();
			match(";");
	}
	
	private void print() {
		//print ( <exp-list> ) ;
		match("print");
		match("(");
		exp_list();
		match(")");
	}
	
	private void exp_list() {
		while(!cmatch(")")){
			exp();
		}
	}
	
	private void filenames() {
		String left = file();
		match("->");
		left+=" "+file();
	}
	
	private String file() {
		cmatch(Lexical.ASCIISTR);
		return token.getTokenstr();
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
	
	private Object id() {
		String id_name = token.getTokenstr();
		Object val = null;
		if (nmatch(":=")) {
			token = itr.next();
			match(":=");
			val = exp();
			interp.assign(id_name, val);
		}  else {
			val = interp.getSym().get(token.getTokenstr());
		}
		return val;
	}
	
	private boolean binop (int initial, String op) {
		int b = 0;
		if (nmatch(Lexical.INTNUM)) {
			b = CharacterHelper.convertStringToInt(itr.next().getTokenstr());
		} else if (nmatch(Lexical.ID)) {
			String c = itr.next().getTokenstr();
			if(interp.getSym().get(c) instanceof Integer) {
				b = CharacterHelper.convertStringToInt(itr.next().getTokenstr());
			} else {
				System.err.println("line "+token.getLinenum()+" unable to perform mathematical binary operator on a non-Integer");
			}
		} else {
			b = exp();
		}
		return interp.conditionOp(initial, op, b);
	}
	
	private Integer exp() {
		Object val= null;
		if (match(Lexical.ID)){
			val = id();
		} else if (match(Lexical.INTNUM)){
			val = CharacterHelper.convertStringToInt(token.getTokenstr());
		} else if (match("(")) {
			exp();
			match(")");
		}
		
		if (nmatch("+")||nmatch("-")||nmatch("*")||nmatch("/")) {
			if(val instanceof Integer) {
				binop((Integer) val, token.getTokenstr());
			}
		}
		
		if(val instanceof Integer) {
			return (Integer) val;
		} else {
			return null;
		}
	}
	
	
}
