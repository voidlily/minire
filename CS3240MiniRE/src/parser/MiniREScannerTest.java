package parser;

import static org.junit.Assert.*;

import grammar.Lexical;
import grammar.Terminal;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MiniREScannerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testScan() throws IOException {
		String file = "begin\n\n" +

		" replace '[a-z]+b' with \"<lowercase,b>\" in \"input1.txt\" -> \"output1.txt\";\n" +
		" replace '[^1-9]+c? | \\\'' with \"<digit,c or \'>\" in \"input2.txt\" -> \"output2.txt\";\n" +
		" replace '([0-9][0-9])/' with \"<nondigit,nondigit,/>\" in \"input2.txt\" -> \"output2-v2.txt\";\n\n" +

		" print (find '[gp]r[ae]y' in \"input1.txt\");\n" +
		" print( find 'red|green|blue|black|white|gray' in \"input1.txt\" );\n\n" +

		"end";

		Reader r = new StringReader(file);
		MiniREScanner s = new MiniREScanner(r);

		List<MiniREToken> tokens = s.scan();
		for (MiniREToken tok : tokens) {
			System.out.println(tok.getTokenstr());
		}
	}

	@Test
	public void testNegative() throws IOException {
//		Reader r = new StringReader("print (2 - -2)");
		Reader r = new StringReader("2 -- 2");
//		Reader r = new StringReader("2 - -2");
//		Reader r = new StringReader("2 + -2");
		MiniREScanner s = new MiniREScanner(r);

		List<MiniREToken> tokens = s.scan();
		for (MiniREToken tok : tokens) {
			System.out.println(tok.getTokenstr());
		}

	}
	@Test
	public void testNegative2() throws IOException {
//		Reader r = new StringReader("print (2 - -2)");
//		Reader r = new StringReader("2 -- 2");
		Reader r = new StringReader("2 - -2");
//		Reader r = new StringReader("2 + -2");
		MiniREScanner s = new MiniREScanner(r);

		List<MiniREToken> tokens = s.scan();
		for (MiniREToken tok : tokens) {
			System.out.println(tok.getTokenstr());
		}

	}

	@Test
	public void testLeadingZero() throws IOException {
		Reader r = new StringReader("-045");

		MiniREScanner s = new MiniREScanner(r);

		List<MiniREToken> tokens = s.scan();
		assertFalse("Expected token to not be INTNUM, got " + tokens.get(0).getLex(),
				tokens.get(0).getLex() == Lexical.INTNUM);
	}

	@Test
	public void testSoleNegative() throws IOException {
		Reader r = new StringReader("-");

		MiniREScanner s = new MiniREScanner(r);

		List<MiniREToken> tokens = s.scan();
		assertFalse("Expected token to not be INTNUM, got " + tokens.get(0).getLex(),
				tokens.get(0).getLex() == Lexical.INTNUM);
		assertEquals(tokens.get(0).getTerm(), Terminal.minus);
	}
}
