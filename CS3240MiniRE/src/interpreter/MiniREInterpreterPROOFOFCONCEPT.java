/**
 * The interpreter will take in the tokens from the parser and will execute
 * them according to our grammar. So a token that indicates it is a variable
 * will be interpreted as a variable when executed by this interpreter.
 *
 * THIS CLASS IS A PROOF OF CONCEPT TO SHOW THE RESULT OF A SUCCESSFUL PARSE.
 */

package interpreter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import regex.Match;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREInterpreterPROOFOFCONCEPT extends MiniREInterpreter {

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

		List<String> newlines = new ArrayList<String>();
		for (String s : lines) {
			String ns = s.replaceAll(regex, repstr);
			newlines.add(ns);
		}

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

		List<Match> matches = new ArrayList<Match>();

		for (int i = 0; i < lines.size(); i++) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(line);

			int j = 0;
			while (m.find(j)) {
				Match match = new Match(m.group(), filename, j, m.start());
				matches.add(match);
				j = m.end();
			}
		}

		return matches;
	}
}
