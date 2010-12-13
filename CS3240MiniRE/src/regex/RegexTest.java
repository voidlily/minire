/**
 * 
 */
package regex;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public class RegexTest {
	Regex testreg;
	Regex testbrackets;
	Regex testnotbrackets;
	Regex testunion;
	Regex teststar;
	Regex testplus;
	Regex testquestion;
	Regex testemptyunion;
	Regex testparens;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testreg = new Regex("'ab'");
		testbrackets = new Regex("'[a-zA-Z]'");
		testnotbrackets = new Regex("'[^a-zA-Z]'");
		testunion = new Regex("'ab|bc'");
		testemptyunion = new Regex("'|a'");
		teststar = new Regex("'a*'");
		testplus = new Regex("'a+'");
		testquestion = new Regex("'a?'");
		testparens = new Regex("'(ab)(d|c)'");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		testreg = null;
		testbrackets = null;
		testnotbrackets = null;
		testunion = null;
		testemptyunion = null;
		teststar = null;
		testplus = null;
		testquestion = null;
		testparens = null;
	}
	
	@Test
	public void testStartState() {
		State tempstate = testreg.getStartState();
		assertTrue(tempstate.getTransitions().size() == 1);
		assertTrue(tempstate.getTransitions().get(0).getNextState().isAcceptState());
		tempstate = tempstate.getTransitions().get(0).getSubDFA();
		assertTrue(tempstate.getTransitions().size() == 1);
		assertTrue(tempstate.getTransitions().get(0).getExactMatch().equals("a"));
		tempstate = tempstate.getTransitions().get(0).getNextState();
		assertTrue(tempstate.getTransitions().get(0).getExactMatch().equals("b"));
		assertTrue(tempstate.getTransitions().get(0).getNextState().isAcceptState());
		List<String> lines = new ArrayList<String>();
		lines.add("ab");
		lines.add("blah blah ab blah");
		List<Match> templist = testreg.match(lines);
		System.out.println(templist);
		assertTrue(templist.size() == 2);
	}
	
	@Test
	public void testUnion() {
		State tempstate = testunion.getStartState();
		assertTrue(tempstate.getTransitions().size() == 1);
		assertTrue(tempstate.getTransitions().get(0).getNextState().isAcceptState());
		tempstate = tempstate.getTransitions().get(0).getSubDFA();
		assertFalse(tempstate.isAcceptState());
		assertTrue(tempstate.getTransitions().size() == 2);
		assertTrue(tempstate.getTransitions().get(0).getSubDFA().getTransitions().size() == 1);
		assertTrue(tempstate.getTransitions().get(0).getSubDFA().getTransitions().get(0).getExactMatch().equals("a"));
		assertTrue(tempstate.getTransitions().get(0).getSubDFA().getTransitions().get(0).getNextState().getTransitions().get(0).getExactMatch().equals("b"));
		assertTrue(tempstate.getTransitions().get(0).getSubDFA().getTransitions().get(0).getNextState().getTransitions().get(0).getNextState().isAcceptState());
		assertTrue(tempstate.getTransitions().get(1).getSubDFA().getTransitions().size() == 1);
		assertTrue(tempstate.getTransitions().get(1).getSubDFA().getTransitions().get(0).getExactMatch().equals("b"));
		assertTrue(tempstate.getTransitions().get(1).getSubDFA().getTransitions().get(0).getNextState().getTransitions().get(0).getExactMatch().equals("c"));
		assertTrue(tempstate.getTransitions().get(1).getSubDFA().getTransitions().get(0).getNextState().getTransitions().get(0).getNextState().isAcceptState());
	}
	
	@Test
	public void testEmptyUnion() {
		State tempstate = testemptyunion.getStartState();
		assertTrue(tempstate.getTransitions().size() == 1);
		assertTrue(tempstate.getTransitions().get(0).getNextState().isAcceptState());
		tempstate = tempstate.getTransitions().get(0).getSubDFA();
		assertFalse(tempstate.isAcceptState());
//		System.out.println(tempstate.getTransitions().get(0).getNextState().isAcceptState());
		assertTrue(tempstate.getTransitions().size() == 1);
//		assertTrue(tempstate.getTransitions().get(0).getExactMatch())
		assertTrue(tempstate.getTransitions().get(0).getSubDFA().getTransitions().size() == 1);
		assertTrue(tempstate.getTransitions().get(0).getSubDFA().getTransitions().get(0).getExactMatch().equals(""));
		assertTrue(tempstate.getTransitions().get(0).getSubDFA().getTransitions().get(0).getNextState().isAcceptState());
//		assertTrue(tempstate.getTransitions().get(1).getSubDFA().getTransitions().size() == 1);
//		assertTrue(tempstate.getTransitions().get(1).getSubDFA().getTransitions().get(0).getExactMatch().equals("a"));
//		assertTrue(tempstate.getTransitions().get(1).getSubDFA().getTransitions().get(0).getNextState().isAcceptState());
	}
	
	@Test
	public void testParens() {
		State tempstate = testparens.getStartState();
		assertFalse(tempstate.isAcceptState());
		assertTrue(tempstate.getTransitions().size() == 1);
		assertFalse(tempstate.getTransitions().get(0).getNextState().isAcceptState());
		assertTrue(tempstate.getTransitions().get(0).getExactMatch().equals("ab"));
		tempstate = tempstate.getTransitions().get(0).getNextState();
		assertTrue(tempstate.getTransitions().size() == 2);
		assertTrue(tempstate.getTransitions().get(0).getNextState().isAcceptState());
		assertTrue(tempstate.getTransitions().get(0).getExactMatch().equals("d"));
		assertTrue(tempstate.getTransitions().get(0).getNextState().getTransitions().size() == 0);
		assertTrue(tempstate.getTransitions().get(1).getNextState().isAcceptState());
		assertTrue(tempstate.getTransitions().get(1).getExactMatch().equals("c"));
		assertTrue(tempstate.getTransitions().get(1).getNextState().getTransitions().size() == 0);
	}
	
	public void teststar() {
		State tempstate = teststar.getStartState();
	}
	
	@Test
	public void testRegexBrackets() {
		State tempstate = testbrackets.getStartState();
		
	}
}
