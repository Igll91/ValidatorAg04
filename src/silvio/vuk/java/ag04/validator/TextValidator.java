package silvio.vuk.java.ag04.validator;

/**
 * Validator that checks if passed string contains allowed characters.
 * 
 * @author silvio vuk
 *
 */
public class TextValidator implements Validator {

	/**
	 * Checks if String contains allowed characters {@link StringChecker}
	 * 
	 * @param textToValidate String that will be checked.
	 * @return true if string is valid, else false
	 */
	@Override
	public boolean validate(String textToValidate) {
		return StringChecker.isAsciiPrintable(textToValidate);
	}
}
