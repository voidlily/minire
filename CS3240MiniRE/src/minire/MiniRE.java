/**
 *
 */
package minire;

import parser.MiniREToken;
import java.io.File;
import java.io.IOException;

import parser.*;
import interpreter.*;
import java.util.List;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public class MiniRE {
	private static String programfile;

	public static List<MiniREToken> startParsing() throws IOException {
		MiniREParser parse = new MiniREParser();
		parse.fileToParse(programfile);
		try {
			return parse.getAllTokens();
		} catch (IOException e) {
			return null;
		}
	}

	public static void main(String[] args) throws IOException {
		if(args.length == 1) {
			File tempfile = new File(args[0]);
			if(tempfile.exists()) {
				//Then we can actually run the program.
				programfile = args[0];
				if(programfile.endsWith(".mre")) {
					//Make sure the program is actually of type '.mre'.
					startParsing();
				}
				else {
					System.out.println("The passed in program is not of type '.mre'.");
				}
			}
			else {
				//Then the program does not exist.
				System.out.println("The passed in file " + args[0] + " does not exist.");
			}
		}
		else {
			System.out.println("The wrong number of parameters were passed in.");
			System.out.println("Simply give the name of the minire program you wish to run.");
		}
	}
}
