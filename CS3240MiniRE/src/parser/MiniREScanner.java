/**
 * The scanner will take in a line and return the individual tokens from that
 * line of code that is sent to it.
 */
package parser;

import grammar.Lexical;
import grammar.Terminal;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import minire.CharacterHelper;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREScanner {
	private Reader reader;
	/** All the tokens */
	private List<MiniREToken> tokens;
	/** Current line number */
	private int line = 1;
	/** Are we finished reading? */
	private boolean hasMoreTokens = true;
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
							// check for arrow token (> preceded by a -)
							if (tokens.size() != 0) {
								MiniREToken lasttok = tokens.get(tokens.size() - 1);
								MiniREToken arrowtok = new MiniREToken("->", lasttok.getLinenum(), true);
								if (lasttok.getTerm() == Terminal.minus) {
									tokens.set(tokens.size() - 1, arrowtok);
									continue;
								}
							}
						} else if (tok.getTokentype() == MiniREToken.Type.LEXICAL
								&& tok.getLex() == Lexical.INTNUM) {
							// check for 2 - -2 case
							if (tokens.size() != 0) {
								MiniREToken lasttok = tokens.get(tokens.size() - 1);
								if (lasttok.getTerm() == Terminal.minus
										&& !lasttok.isSpaceAfterToken()) {
									tokens.set(tokens.size() - 1,
											new MiniREToken("-" + tok.getTokenstr(),
													lasttok.getLinenum(), true));
									continue;
								}
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
		/** Space between tokens (for 2 - -2 case) */
		boolean spaceBetweenTokens = false;

		boolean inRegex = false;
		char regexDelimiter = '\0';
		if (currTokStr.equals("'") || currTokStr.equals("\"")) {
			inRegex = true;
			regexDelimiter = currTokStr.charAt(0);
		}
		boolean alphanum;
		char next = nextChar();
		if (currTokStr != null && currTokStr.length() > 0) {
			alphanum = CharacterHelper.isLetterOrDigit(currTokStr.charAt(0));
		} else {
			alphanum = CharacterHelper.isLetterOrDigit(next);
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
				next = nextChar();
				if (next == (char) -1) {
					break;
				}
			}
			// once more to actually append the closing tag
			if (next != (char) -1 ) {
				currTokStr += next;
			}
			next = nextChar();
		} else {
			boolean done = Terminal.determineTokenType(currTokStr) != null;
			while (!done && CharacterHelper.isLetterOrDigit(next) == alphanum) {
				if (next != (char) -1) {
					currTokStr += next;
				}
				if (Terminal.determineTokenType(currTokStr) != null) {
					done = true;
				}
//				if (Character.isDefined(next)) {
//					System.out.print(next);
//				}
				next = nextChar();
				if (next == (char) -1) {
					break;
				}
				if (done) {
					break;
				}
			}
		}


		while (next == ' ' || next == '\n' || next == '\t') {
			if (next == ' ') {
				spaceBetweenTokens = true;
			}
			next = nextChar();
		}
		MiniREToken tok = new MiniREToken(currTokStr, line, spaceBetweenTokens);
//		if (Character.isDefined(next)) {
//			System.out.print(next);
//		}


		currTokStr = "" + next;

		return tok;
	}

	private char nextChar() throws IOException {
		char next = (char) reader.read();
		if (next == (char) -1) {
			this.hasMoreTokens = false;
		}
		if (next == '\n') {
			line++;
		}
		return next;
	}
}
