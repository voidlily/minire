/**
 * The scanner will take in a line and return the individual tokens from that
 * line of code that is sent to it.
 */
package parser;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREScanner {
	private String linetoparse;
	private int lineposition;
	
	public MiniREScanner() {
		
	}
	
	/**
	 * Get the next token from the line. Includes ' ', ';', '\n'.
	 * @return MiniREToken
	 */
	public MiniREToken getNextToken() {
		MiniREToken tok = new MiniREToken();
		String tempstr = "";
		
		while(this.linetoparse.length() > this.lineposition) {
			if(this.linetoparse.charAt(this.lineposition) == ' ') {
				//Want to make sure that we go past the space.
				this.lineposition++;
				break;
			}
			tempstr += this.linetoparse.charAt(this.lineposition);
			this.lineposition++;
		}
		tok.tokenstr = tempstr;
		return tok;
	}
	
	/**
	 * Are there more characters on the present line.
	 * @return boolean
	 */
	public boolean hasMoreTokens() {
		if(this.linetoparse.length() > this.lineposition) {
			return true;
		}
		return false;
	}
	
	/**
	 * Parse the passed in line.
	 * @param line
	 */
	public void parseThisLine(String line) {
		//TODO: parse by statement instead of by line? (multi-line strings and statements)
		// semicolon delimited, effectively implement string.split
		// store in list of strings, each string is one statement
		// also needs to handle begin/end
		this.lineposition = 0;
		if(line.length() > 0) {
			//TODO: Ensure that trim does not remove too much white space.
			// ignore whitespace - (scanner will not send tokens made entirely of whitespace)
			// however, spaces in strings and regexes should not be ignored
			// needs whitespace/EOF to detect ends of tokens
			// and the separation between begin/end
			// (see official clarifications forum post)
			this.linetoparse = line.trim();
		}
		else {
			this.linetoparse = "";
		}
	}
}
