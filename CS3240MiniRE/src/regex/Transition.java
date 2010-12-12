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
	
	public Transition(String exactstr) {
		this.exactMatch = exactstr;
		this.range = null;
		this.nextState = new State();
	}
	
	public Transition(int r1, int r2) {
		this.exactMatch = null;
		this.setRange(r1, r2);
		this.nextState = new State();
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
}
