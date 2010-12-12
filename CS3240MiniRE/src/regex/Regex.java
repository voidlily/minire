package regex;

import java.util.List;

public class Regex {
	private String str;
	private boolean compiled = false;

	public Regex(final String str) {
		this.str = str;
	}

	public List<Match> match(final List<String> lines) {
		// TODO stub
		return null;
	}

	public void replace(final List<String> lines, final String rep) {
		// TODO stub
	}
}
