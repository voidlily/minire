package minire;

public class CharacterMatcher {

	public static boolean isLetterOrDigit(char c) {
		/* For reference - ASCII breakdown:
		 * [A-Z] = 65 - 90
		 * [a-z] = 97 - 122
		 * [0-9] = 48 - 57
		 */
		return (isDigit(c) || isLetter(c));
	}

	public static boolean isLetter(char c) {
		int asciival = c;
		if((asciival >= 65 && asciival <= 90) || (asciival >= 97 && asciival <= 122)) {
			return true;
		}
		return false;
	}

	public static boolean isDigit(char c) {
		int asciival = c;
		if(asciival >= 48 && asciival <= 57) {
			return true;
		}
		return false;
	}
}
