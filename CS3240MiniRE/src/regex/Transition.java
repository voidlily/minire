/**
 * 
 */
package regex;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public class Transition {
	/*
	 * Class Transition
	 * * String exactStrMatch
	 * * int[2] range
	 * * State nextState
	 */
	private String exactMatch;
	private int[] range;
	private State nextState;
	private boolean notThisRange;
	private State subDFA;
	
	public Transition(String exactstr) {
		this.exactMatch = exactstr;
		this.range = null;
		this.nextState = new State();
		this.notThisRange = false;
		this.subDFA = null;
	}
	
	public Transition(int r1, int r2) {
		this.exactMatch = null;
		this.setRange(r1, r2);
		this.nextState = new State();
		this.notThisRange = false;
		this.subDFA = null;
	}
	
	public Transition(int r1, int r2, boolean useRange) {
		this.exactMatch = null;
		this.setRange(r1, r2);
		this.nextState = new State();
		this.notThisRange = useRange;
		this.subDFA = null;
	}
	
	public Transition(State sub, String op) {
		this.exactMatch = op;
		this.range = null;
		this.nextState = new State();
		this.notThisRange = false;
		this.subDFA = sub;
	}
	
	public Transition(State sub) {
		this.exactMatch = null;
		this.range = null;
		this.nextState = new State();
		this.notThisRange = false;
		this.subDFA = sub;
	}

	/**
	 * @return the exactMatch
	 */
	public String getExactMatch() {
		return exactMatch;
	}

	/**
	 * @param exactMatch the exactMatch to set
	 */
	public void setExactMatch(String exactMatch) {
		this.exactMatch = exactMatch;
	}

	/**
	 * @return the range
	 */
	public int[] getRange() {
		return range;
	}

	/**
	 * @param range the range to set
	 */
	public void setRange(int r1, int r2) {
		range = new int[2];
		range[0] = r1;
		range[1] = r2;
	}

	/**
	 * @return the nextState
	 */
	public State getNextState() {
		return nextState;
	}

	/**
	 * @param nextState the nextState to set
	 */
	public void setNextState(State nextState) {
		this.nextState = nextState;
	}

	/**
	 * @return the notThisRange
	 */
	public boolean isNotThisRange() {
		return notThisRange;
	}

	/**
	 * @param notThisRange the notThisRange to set
	 */
	public void setNotThisRange(boolean notThisRange) {
		this.notThisRange = notThisRange;
	}

	/**
	 * @return the subDFA
	 */
	public State getSubDFA() {
		return subDFA;
	}

	/**
	 * @param subDFA the subDFA to set
	 */
	public void setSubDFA(State subDFA) {
		this.subDFA = subDFA;
	}
}
