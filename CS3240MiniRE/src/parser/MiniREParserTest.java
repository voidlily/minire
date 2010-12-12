package parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MiniREParserTest {

	List<MiniREToken> tokens;
	String file; 
	
	@Before
	public void setUp() throws Exception {
		file = "begin\n\n" +

		" replace '[a-z]+b' with \"<lowercase,b>\" in \"input1.txt\" -> \"output1.txt\";\n" +
		" replace '[^1-9]+c? | \\\'' with \"<digit,c or \'>\" in \"input2.txt\" -> \"output2.txt\";\n" +
		" replace '([0-9][0-9])/' with \"<nondigit,nondigit,/>\" in \"input2.txt\" -> \"output2-v2.txt\";\n\n" +

		" print (find '[gp]r[ae]y' in \"input1.txt\");\n" +
		" print( find 'red|green|blue|black|white|gray' in \"input1.txt\" );\n\n" +

		"end";

		Reader r = new StringReader(file);
		MiniREScanner s = new MiniREScanner(r);

		tokens = s.scan();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTokensByLines() throws IOException {
		MiniREParser p = new MiniREParser();
		List<List<MiniREToken>> a = p.getTokensByLines(tokens);
		
		System.out.println(a);
		
		/*for (List<MiniREToken> h: a) {
			for (MiniREToken j: h) {
				System.out.print(j);
			}
			System.out.println("");
		}*/
	}

}
