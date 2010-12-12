/**
 * 
 */
package regex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public class State {
	/* Class State
	 * * boolean acceptState
	 * * List<Transition> transitions
	 */
	private boolean acceptState;
	private List<Transition> transitions;
	
	public State() {
		this.transitions = new ArrayList<Transition>();
		this.acceptState = false;
	}

	/**
	 * @return the acceptState
	 */
	public boolean isAcceptState() {
		return acceptState;
	}

	/**
	 * @param acceptState the acceptState to set
	 */
	public void setAcceptState(boolean acceptState) {
		this.acceptState = acceptState;
	}

	/**
	 * @return the transitions
	 */
	public List<Transition> getTransitions() {
		return transitions;
	}
	
	/**
	 * Adds the transition t to the transitions list.
	 * @param t
	 */
	public void addTransition(Transition t) {
		transitions.add(t);
	}
}
