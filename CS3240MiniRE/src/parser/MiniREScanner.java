/**
 * The scanner will take in a line and return the individual tokens from that
 * line of code that is sent to it.
 */
package parser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

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
	/** Is the current token alphanumeric or not? (for terminals) */
	private boolean currTokenAlphaNum;
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
		char next = (char) reader.read();
		// TODO extract to method these 3 code blocks, and the other while next == ' '
		if (next == -1) {
			this.hasMoreTokens = false;
			return null;
		}
		if (next == '\n') {
			line++;
		}
		while (next == ' ') {
			next = (char) reader.read();
		}
		currTokStr += next;

		// TODO: if currTokStr = (start of regex character) {
		// scanRegex();
		// } else {
		// scanNonRegex();
		// }
		// the section below will be extracted to scanNonRegex() method

		// ***** Is this allowed? *****
		currTokenAlphaNum = Character.isLetterOrDigit(next);

		// Continue adding more characters to the candidate token
		// until we hit a non-matching (with respect to letter or digit) character
		while (Character.isLetterOrDigit(next) == currTokenAlphaNum) {
			currTokStr += next;
			next = (char) reader.read();
			while (next == ' ') {
				next = (char) reader.read();
			}
		}

		MiniREToken tok = new MiniREToken(currTokStr, line);

		currTokStr = "" + next;

		return tok;
	}
}
