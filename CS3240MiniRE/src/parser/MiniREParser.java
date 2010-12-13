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

import grammar.Lexical;
import grammar.Terminal;
import interpreter.MiniREInterpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import minire.CharacterHelper;
import regex.Match;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREParser {
	private String programfilepath;
	private FileReader programfilereader;
	private MiniREToken[] tokencollection;
	private List<MiniREToken> toks;
	private MiniREScanner scanner;

	public MiniREParser() throws IOException {
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

	ListIterator<MiniREToken> itr;

	MiniREToken token;

	private boolean match(String ExpectedName) {
		MiniREToken ExpectedToken = new MiniREToken(ExpectedName);
		if (token.getTokenstr().equals(ExpectedToken.getTokenstr()) && token.getTokentype().equals(ExpectedToken.getTokentype())){
			token = itr.next();
			//System.out.println(ExpectedName);
			return true;
		} else {
			//System.out.println(token);
			//System.err.println("token does not match expected value "+ExpectedName+" on line "+token.getLinenum());
			return false;
		}
	}

	private boolean match(Terminal term) {
		if (token.getTokentype()==MiniREToken.Type.TERMINAL && token.getTerm() == term) {
			token = itr.next();
			return true;
		} else {
			//System.err.println("token does not match Terminal "+term+" on line "+token.getLinenum());
			return false;
		}
	}

	private boolean match(Lexical lex) {
		if (token.getTokentype()==MiniREToken.Type.LEXICAL && token.getLex() == lex) {
			token = itr.next();
			return true;
		} else {
			//System.err.println("token does not match Lexical "+lex+" on line "+token.getLinenum());
			return false;
		}
	}

	/** matches current token without iterating forward */
	private boolean cmatch(String ExpectedName) {
		MiniREToken ExpectedToken = new MiniREToken(ExpectedName);
		if (token.getTokenstr().equals(ExpectedToken.getTokenstr()) && token.getTokentype().equals(ExpectedToken.getTokentype())){
			return true;
		} else {
			//System.err.println("token does not match expected value "+ExpectedName+" on line "+token.getLinenum());
			return false;
		}
	}

	/** matches current token without iterating forward */
	private boolean cmatch(Terminal term) {
		if (token.getTokentype()==MiniREToken.Type.TERMINAL && token.getTerm() == term) {
			return true;
		} else {
			//System.err.println("token does not match Terminal "+term+" on line "+token.getLinenum());
			return false;
		}
	}

	/** matches current token without iterating forward */
	private boolean cmatch(Lexical lex) {
		if (token.getTokentype()==MiniREToken.Type.LEXICAL && token.getLex() == lex) {
			return true;
		} else {
			//System.err.println("token does not match Lexical "+lex+" on line "+token.getLinenum());
			return false;
		}
	}

	/** matches next token without iterating forward */
	private boolean nmatch(String ExpectedName) {
		MiniREToken ExpectedToken = new MiniREToken(ExpectedName);
		if (itr.next().getTokenstr().equals(ExpectedToken.getTokenstr()) && itr.next().getTokentype().equals(ExpectedToken.getTokentype())){
			return true;
		} else {
			//System.err.println("token does not match expected value "+ExpectedName+" on line "+itr.next().getLinenum());
			return false;
		}
	}

	/** matches next token without iterating forward */
	private boolean nmatch(Terminal term) {
		if (itr.next().getTokentype()==MiniREToken.Type.TERMINAL && itr.next().getTerm() == term) {
			return true;
		} else {
			//System.err.println("token does not match Terminal "+term+" on line "+itr.next().getLinenum());
			return false;
		}
	}

	/** matches next token without iterating forward */
	private boolean nmatch(Lexical lex) {
		if (itr.next().getTokentype()==MiniREToken.Type.LEXICAL && itr.next().getLex() == lex) {
			return true;
		} else {
			//System.err.println("token does not match Lexical "+lex+" on line "+itr.next().getLinenum());
			return false;
		}
	}

	private void begin() throws IOException {
		//begin <statement-list> end
		match("begin");
		token = itr.next();
		while(!token.getTokenstr().equals("end")){
			//System.out.println("lol");
			statement();
			token = itr.next();
		}
		if (token==null) {
			System.err.println("EOF: no matching end terminal");
		}
	}

	private void statement() throws IOException {
		//<statement> <statement-list>
		//System.out.println(token);
		if (match("replace")) {
				replace();
		} else if (match("print")) {
				print();
		} else if (match(Lexical.ID)) {
				id();
		} else if (match("if")) {
				ifblock();
		}
	}

	private void ifblock() throws IOException {
		match("if");
		token = itr.next();
		match("(");
		token = itr.next();
		boolean cond = bool_op(); // TODO incomplete
		token = itr.next();
		match(")");
		token = itr.next();
		match("{");
		while (!nmatch("}")) {
			token = itr.next();
			if (cond) {
				statement();
			}
		}
		token = itr.next();
		if (match("else")) {
			token = itr.next();
			match("{");
			while (!nmatch("}")) {
				token = itr.next();
				if (!cond) {
					statement();
				}
			}
			token = itr.next();
		}
	}

	private void whileblock() throws IOException {
		ListIterator<MiniREToken> orig = itr;
		match("while");
		token = itr.next();
		nmatch("("); // nmatch to get the correct cond_index

		int cond_index = itr.nextIndex();
		ListIterator<MiniREToken> condItr = toks.listIterator(cond_index);

		token = itr.next();
		boolean cond = bool_op();
		match(")");

		token = itr.next();
		nmatch("{"); // nmatch to get the correct block_index
		int block_index = itr.nextIndex();
		token = itr.next();

		while (true) {
			int nested = 1;
			// Repeat the following block for two cases:
			// 1. The condition is true - we will recursively execute all nested blocks,
			// so it is sufficient to match RCURLY
			// 2. The condition is false - we need to track nested blocks to make sure match
			// the correct RCURLY, as none of the nested blocks will be executed
			while (!nmatch("}") || nested > 0) {
				token = itr.next();
				if (cond) {
					statement();
				} else {
					// if the condition is false, keep count of any nested blocks,
					// as we're skipping over but not executing them
					if (nmatch("{")) {
						nested++;
					} else if (nmatch("}")) {
						nested--;
					}
				}
			}
			// check the condition
			orig = itr;
			itr = condItr;
			cond = bool_op(); // TODO incomplete
			// reset the condition iterator
			condItr = toks.listIterator(cond_index);
			if (cond) {
				// if condition holds, reset the iterator to the start of the block
				itr = toks.listIterator(block_index);
			} else {
				// The condition is false, so after resetting the iterator,
				// break out of the infinite loop
				itr = orig;
				break;
			}
		}
	}

	private boolean bool_op() throws IOException {
		Object aa = exp();
		int a = CharacterHelper.convertStringToInt(aa.toString());
		String op = null;
		if (nmatch("==") || nmatch("!=") || nmatch("<") || nmatch(">") || nmatch("<=") || nmatch(">=")) {
			op = token.getTerm().name();
		}
		Object bb = exp();
		int b = CharacterHelper.convertStringToInt(bb.toString());
		if (op != null) {
			return interp.conditionOp(a, op, b);
		}
		return false;
	}

	private void replace() throws IOException {
		System.out.println("check replace");
		//replace REGEX with ASCIISTR in <file-names> ;
		String regex = null;
		String repl = null;
		String[] filenames;
		match("replace");
		if(match(Lexical.REGEX)) {
			regex = token.getTokenstr();
		}
		match("with");
		if(match(Lexical.ASCIISTR)) {
			repl = token.getTokenstr();
		}
		token = itr.next();
		match("in");
		token = itr.next();
		filenames = filenames();
		match(";");
		token = itr.next();
		System.out.println("calls replace");
		System.out.println(token);
		//interp.replace(regex, repl, filenames[0], filenames[1]);
	}

	private void print() throws IOException {
		//print ( <exp-list> ) ;
		match("print");
		match("(");
		exp_list();
		match(")");
	}

	private void exp_list() throws IOException {
		while(!cmatch(")")){
			exp();
		}
	}

	private String[] filenames() {
		String[] temp = new String[2];
		temp[0] = file();
		match("->");
		temp[1] = file();
		return temp;
	}

	private String file() {
		String temp = null;
		if(cmatch(Lexical.ASCIISTR)) {
			temp = token.getTokenstr();
		}
		token = itr.next();
		return temp;
	}

	private void find() throws IOException {
		//find REGEX in <file>
		String regex = null;
		String file = null;
		match("find");
		if(token.getLex()==Lexical.REGEX) {
			regex = token.getTokenstr();
			match("in");
			file = file();
			match(";");
			interp.find(regex, file);
		} else {
			System.err.println("line "+token.getLinenum()+": invalid find call");
		}
	}

	private Object id() throws IOException {
		String id_name = token.getTokenstr();
		Object val = null;
		if (nmatch(":=")) {
			token = itr.next();
			match(":=");
			val = exp();
			match(";");
			interp.assign(id_name, val);
		}  else {
			val = interp.getSym().get(token.getTokenstr());
		}
		return val;
	}

	private boolean binop (int initial, String op) throws IOException {
		Object b = 0;
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
		if (b instanceof Integer) {
			return interp.conditionOp(initial, op,(Integer) b);
		} else {
			System.err.println("line "+token.getLinenum()+": mismatched types in binary operator");
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	private void union_inters (Object initial, String op) throws IOException {
		Object b = 0;
		if (nmatch(Lexical.ID)) {
			String c = itr.next().getTokenstr();
			if(interp.getSym().get(c) instanceof List) {
				b = itr.next().getTokenstr();
			} else {
				System.err.println("line "+token.getLinenum()+" unable to perform list binary operator on a non-list");
			}
		} else {
			b = exp();
		}

		if (b instanceof List) {
			if(op.equals("union")) {
				interp.union((List<Match>) initial,(List<Match>) b);
			} else if(op.equals("inters")) {
				interp.intersect((List<Match>) initial,(List<Match>) b);
			}
		} else {
			System.err.println("line "+token.getLinenum()+": mismatched types in binary operator");
		}
	}


	private Object exp() throws IOException {
		Object val= null;
		if (match(Lexical.ID)){
			val = id();
		} else if (match(Lexical.INTNUM)){
			val = CharacterHelper.convertStringToInt(token.getTokenstr());
		} else if (match("(")) {
			exp();
			match(")");
		} else if (match("find")) {
			find();
		} else if (match("#")) {
			hash();
		} else if (match(";")) {
			return null;
		}

		if (nmatch("+")||nmatch("-")||nmatch("*")||nmatch("/")) {
			token = itr.next();
			if(val instanceof Integer) {
				binop((Integer) val, token.getTokenstr());
			}
		} else if (nmatch("union")||nmatch("inters")) {
			token = itr.next();
			if(val instanceof List) {
				union_inters(val, token.getTokenstr());
			}
		}

		return val;
	}

	@SuppressWarnings("unchecked")
	private Integer hash() throws IOException {
		match("#");
		Object temp = exp();
		if (temp instanceof List) {
			return interp.countOp((List<Object>) temp);
		} else {
			return null;
		}
	}

	public void run(List<MiniREToken> toks) throws IOException {
		token = toks.get(0);
		this.toks = toks;
		itr = toks.listIterator();
		begin();
	}


}
