/**
 * The scanner will take in a line and return the individual tokens from that
 * line of code that is sent to it.
 */
package parser;

import grammar.Terminal;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import minire.CharacterMatcher;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREScanner {
	private Reader reader;
	/** All the tokens */
	private List<MiniREToken> tokens;
	/** Current line number */
	private int line = 0;
	/** Are we finished reading? */
	private boolean hasMoreTokens = true;
	/** Escape the next character? */
	private boolean escape = false;
	/** Are we currently reading a regex? */
	private boolean inRegex = false;
	/** The current token string. */
	private String currTokStr = "";

	public MiniREScanner(final Reader reader) {
		this.reader = reader;
	}

	/**
	 * Scan using the reader, returning a list of tokens.
	 *
	 * A given MiniREScanner instance will only perform the scanning operation once
	 * and cache the results.
	 * If scan() is called again, the cached results are immediately returned.
	 * @return
	 */
	public List<MiniREToken> scan() throws IOException {
		if (tokens != null && !tokens.isEmpty()) {
			return tokens;
		} else {
			tokens = new ArrayList<MiniREToken>();
			if (reader.ready()) {
				while (this.hasMoreTokens) {
					MiniREToken tok = this.nextToken();
					// tok is null when we reach EOF
					if (tok != null) {
						if (tok.getTerm() == Terminal.greaterbool) {
							MiniREToken lasttok = tokens.get(tokens.size() - 1);
							MiniREToken arrowtok = new MiniREToken("->", lasttok.getLinenum());
							if (lasttok.getTerm() == Terminal.minus) {
								tokens.set(tokens.size() - 1, arrowtok);
								continue;
							}
						}
						tokens.add(tok);
					}
				}
			}
		}
		return tokens;
	}


	/**
	 * Get the next token from the line. Includes ' ', ';', '\n'.
	 * @return MiniREToken
	 */
	private MiniREToken nextToken() throws IOException {
		boolean inRegex = false;
		char regexDelimiter = '\0';
		if (currTokStr.equals("'") || currTokStr.equals("\"")) {
			inRegex = true;
			regexDelimiter = currTokStr.charAt(0);
		}
		char next = (char) reader.read();
		// TODO extract to method these 3 code blocks, and the other while next == ' '
		if (next == (char) -1) {
			this.hasMoreTokens = false;
			return null;
		}
		if (next == '\n') {
			line++;
		}
		while (!inRegex && (next == ' ' || next == '\n' || next == '\t')) {
			next = (char) reader.read();
			if (next == (char) -1) {
				this.hasMoreTokens = false;
			}
		}

		// Continue adding more characters to the candidate token
		// until we hit a non-matching (with respect to letter or digit) character
		if (inRegex) {
			boolean escape = false;
			while (escape || next != regexDelimiter) {
				escape = false;
				if (next == '\\') {
					escape = true;
				}
				currTokStr += next;
				next = (char) reader.read();
				if (next == (char) -1) {
					this.hasMoreTokens = false;
					break;
				}
			}
			// once more to actually append the closing tag
			currTokStr += next;
			next = (char) reader.read();
			if (next == (char) -1) {
				this.hasMoreTokens = false;
			}
		} else {
			boolean done = Terminal.determineTokenType(currTokStr) != null;
			boolean alphanum = CharacterMatcher.isLetterOrDigit(next);
			while (!done && CharacterMatcher.isLetterOrDigit(next) == alphanum) {
				currTokStr += next;
				if (Terminal.determineTokenType(currTokStr) != null) {
					done = true;
				}
				while (next == ' ' || next == '\n' || next == '\t') {
					next = (char) reader.read();
				}
				if (next == (char) -1) {
					this.hasMoreTokens = false;
					break;
				}
//				if (Character.isDefined(next)) {
//					System.out.print(next);
//				}
				next = (char) reader.read();
				if (next == (char) -1) {
					hasMoreTokens = false;
					break;
				}
				if (done) {
					break;
				}
			}
		}

		while (next == ' ' || next == '\n' || next == '\t') {
			next = (char) reader.read();
		}
//		if (Character.isDefined(next)) {
//			System.out.print(next);
//		}
		MiniREToken tok = new MiniREToken(currTokStr, line);

		currTokStr = "" + next;

		return tok;
	}
}
