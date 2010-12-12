package grammar;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LexicalTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCheckValidRegex() {
		assertTrue(Lexical.checkValidRegex("''"));
		assertTrue(Lexical.checkValidRegex("'abc'"));
		assertTrue(Lexical.checkValidRegex("'[^d-g]+'"));
		assertTrue(Lexical.checkValidRegex("'[d-g]*'"));
		assertTrue(Lexical.checkValidRegex("'a*b+c?'"));
		assertTrue(Lexical.checkValidRegex("'[a-bc-dA-Z]'"));
		assertTrue(Lexical.checkValidRegex("'(a|b)'"));
		assertTrue(Lexical.checkValidRegex("'(|b)'"));
		assertFalse(Lexical.checkValidRegex("'||'"));
		assertFalse(Lexical.checkValidRegex("'((((((((()))))'"));
		assertTrue(Lexical.checkValidRegex("'()()()(())(()())'"));
		assertTrue(Lexical.checkValidRegex("'[^d-g]'"));
		assertTrue(Lexical.checkValidRegex("'()*'"));
		assertTrue(Lexical.checkValidRegex("'[]*'"));
		assertTrue(Lexical.checkValidRegex("'(abc|123)?(|b)ab[a-c1-3]*[^d-g]+'"));
		assertTrue(Lexical.checkValidRegex("'.'"));
		assertTrue(Lexical.checkValidRegex("'.abc[a-c]'"));
		assertFalse(Lexical.checkValidRegex("'[[]]'"));
		assertTrue(Lexical.checkValidRegex("'\\\\'"));
		assertTrue(Lexical.checkValidRegex("'[\\^]'"));
	}
}
