package silvio.vuk.java.ag04.validator;


/**
 * Validator that checks if passed string is number.
 * 
 * @author silvio vuk
 *
 */
public class NumberValidator implements Validator {
	
	/**
	 * Checks if the given string is number or not.
	 * 
	 * @param textToValidate String that will be checked.
	 * @return return true if the passed string is number, else returns false
	 */
	@Override
	public boolean validate(String textToValidate)
	{
		try
		{
			Integer.parseInt(textToValidate);
			return true;
		}
		catch(NumberFormatException ex)
		{
			return false;
		}
	}
}
