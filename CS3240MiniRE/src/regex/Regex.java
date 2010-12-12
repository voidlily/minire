package regex;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Incomprehensible Penguin Arena
 *
 */
public class Regex {
	private String str;
	private boolean compiled = false;

	public Regex(final String str) {
		this.str = str;
	}

	public List<Match> match(final List<String> lines) {
		List<Match> matchlist = new ArrayList<Match>();
		
		return matchlist;
	}

	public List<String> replace(final List<String> lines, final String rep) {
		// TODO stub
		//Cache results of match?
		return null;
	}
	
	/*
	 * Class Transition
	 * * String exactStrMatch
	 * * int[2] range
	 * * State nextState
	 * 
	 * Class State
	 * * boolean acceptState
	 * * List<Transition> transitions
	 */
}
