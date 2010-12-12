package minire;

public class CharacterHelper {

	public static boolean isLetterOrDigit(final char c) {
		/* For reference - ASCII breakdown:
		 * [A-Z] = 65 - 90
		 * [a-z] = 97 - 122
		 * [0-9] = 48 - 57
		 */
		return (isDigit(c) || isLetter(c));
	}

	public static boolean isLetter(final char c) {
		int asciival = c;
		if((asciival >= 65 && asciival <= 90) || (asciival >= 97 && asciival <= 122)) {
			return true;
		}
		return false;
	}

	public static boolean isDigit(final char c) {
		int asciival = c;
		if(asciival >= 48 && asciival <= 57) {
			return true;
		}
		return false;
	}

	public static int convertStringToInt(final String str) {
		boolean negative = false;
		int i = 0;
		int ret = 0;
		if(str.length() > 0) {
			if(str.charAt(0) == '-') {
				negative = true;
				i = 1;
			}
			for(; i < str.length(); i++) {
				ret = ret * 10 + (str.charAt(i) - '0');
			}
			if(negative) {
				return ret * -1;
			}
			else {
				return ret;
			}
		}
		return 0;
	}
}
