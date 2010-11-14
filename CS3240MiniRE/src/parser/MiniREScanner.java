/**
 * The scanner will take in a line and return the individual tokens from that
 * line of code that is sent to it.
 */
package parser;

import java.util.Scanner;

/**
 * @author Incomprehensible Penguin Arena
 */
public class MiniREScanner {
	private String linetoparse;
	private int lineposition;
	
	public MiniREToken getNextToken() {
		MiniREToken tok = new MiniREToken();
		String tempstr;
		
		if(this.linetoparse.length() > 0) {
			//Gets the first word.
			tempstr = this.linetoparse.split("[ \t]")[0];
			
			return tok;
		}
		else
			return null;
	}
	
	public boolean isMoreTokens() {
		if(this.linetoparse.length() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void parseThisLine(String line) {
		Scanner scan = new Scanner(line);
		while(scan.hasNext()) {
			
		}
		if(line.length() > 0) {
			this.linetoparse = line.trim();
		}
		else {
			this.linetoparse = "";
		}
	}
}
