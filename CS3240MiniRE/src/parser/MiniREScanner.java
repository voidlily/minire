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
	
	public boolean hasMoreTokens() {
		if(this.linetoparse.length() > this.lineposition) {
			return true;
		}
		return false;
	}
	
	public void parseThisLine(String line) {
		this.lineposition = 0;
		if(line.length() > 0) {
			this.linetoparse = line.trim();
		}
		else {
			this.linetoparse = "";
		}
	}
}
