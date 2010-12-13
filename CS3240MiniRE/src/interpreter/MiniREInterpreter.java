/**
 * The interpreter will take in the tokens from the parser and will execute
 * them according to our grammar. So a token that indicates it is a variable
 * will be interpreted as a variable when executed by this interpreter.
 */

package interpreter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import minire.CharacterHelper;
import regex.Match;
import regex.Regex;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREInterpreter {
	protected HashMap<String, Object> sym = new HashMap<String, Object>();

	public void assignVar(final String id, final String value) {
		assert(sym.containsKey(value));
		sym.put(id, sym.get(value));
	}

	public void assign(final String id, final Object value) {
		sym.put(id, value);
	}

	public void replace(final String regex, final String repstr, final String inputfile,
			final String outputfile) throws IOException {
		File input = new File(inputfile);
		File output = new File(outputfile);
		BufferedReader read = new BufferedReader(new FileReader(input));
		List<String> lines = new ArrayList<String>();

		String line = read.readLine();
		while (line != null) {
			lines.add(line);
			line = read.readLine();
		}

		read.close();

		Regex r = new Regex(regex);
		List<String> newlines = r.replace(lines, repstr);

		BufferedWriter write = new BufferedWriter(new FileWriter(output));
		for (String s : newlines) {
			write.write(s);
		}

		write.close();

	}

	public List<Match> find(final String regex, final String filename) throws IOException {
		File input = new File(filename);
		BufferedReader read = new BufferedReader(new FileReader(input));
		List<String> lines = new ArrayList<String>();

		String line = read.readLine();
		while (line != null) {
			lines.add(line);
			line = read.readLine();
		}

		read.close();

		Regex r = new Regex(regex);
		List<Match> matches = r.match(lines);

		return matches;
	}

	public Object binaryOp(String a, String op, String b) {
		int inta = CharacterHelper.convertStringToInt(a);
		int intb = CharacterHelper.convertStringToInt(b);
		return binaryOp(inta, op, intb);
	}

	public Object binaryOp(String a, String op, int b) {
		int inta = CharacterHelper.convertStringToInt(a);
		return binaryOp(inta, op, b);
	}

	public Object binaryOp(int a, String op, String b) {
		int intb = CharacterHelper.convertStringToInt(b);
		return binaryOp(a, op, intb);
	}

	public Object binaryOp(int a, String op, int b) {
		int retnum = 0;
		//Should be of length 1.
		switch(op.charAt(0)) {
		case '+':
			retnum = a + b;
			break;
		case '-':
			retnum = a - b;
			break;
		case '*':
			retnum = a * b;
			break;
		case '/':
			retnum = a / b;
			break;
		}
		return retnum;
	}

	public void printOp(int val) {
		System.out.println(val);
	}

	public void printOp(String val) {
		System.out.println(val);
	}

	public void printOp(List<Object> val) {
		System.out.print("{");
		for(int i = 0; i < val.size(); i++) {
			System.out.print(val.get(i).toString());
			if(i < val.size() - 1) {
				System.out.print(",");
			}
		}
		System.out.print("}\n");
	}

	/**
	 * This is the function that is called for the '#' operator. It takes in a
	 * List object and returns the length/size of the list.
	 * @param val
	 * @return
	 */
	public int countOp(List<Object> val) {
		return val.size();
	}

	public boolean conditionOp(int a, String op, int b) {
		boolean ret = false;
		switch(op.charAt(0)) {
		case '=':
			if(op.length() > 1 && op.charAt(1) == '=') {
				ret = (a == b);
			}
			break;
		case '<':
			if(op.length() > 1 && op.charAt(1) == '=') {
				ret = (a <= b);
			}
			else {
				ret = (a < b);
			}
			break;
		case '>':
			if(op.length() > 1 && op.charAt(1) == '=') {
				ret = (a >= b);
			}
			else {
				ret = (a > b);
			}
			break;
		case '!':
			if(op.length() > 1 && op.charAt(1) == '=') {
				ret = (a != b);
			}
			break;
		}
		return ret;
	}

	public List<Match> union(final List<Match> a, final List<Match> b) {
		List<Match> union = new ArrayList<Match>(a);
		union.addAll(b);
		return union;
	}

	public List<Match> intersect(final List<Match> a, final List<Match> b) {
		List<Match> intersect = new ArrayList<Match>(a);
		intersect.retainAll(b);
		return intersect;
	}

	/**
	 * @return the sym
	 */
	public HashMap<String, Object> getSym() {
		return sym;
	}
}
