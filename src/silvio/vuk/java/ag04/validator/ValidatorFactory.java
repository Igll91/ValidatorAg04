package silvio.vuk.java.ag04.validator;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * Validator Factory, checks if the given type exists.
 * 
 * Checks if the given validatorType exists and returns its class path or null.
 * @author silvio
 *
 */
public class ValidatorFactory {

	private static org.apache.log4j.Logger log = Logger.getLogger(ValidatorFactory.class);
	
	private static final String VALIDATIOR_CONFIGURATION = "validation";
    private static Hashtable<String, String> myValidatorMappings = new Hashtable<String, String>();
 
    static 
    {
        try {
            loadMyValidatorsMappings();
        } 
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    /**
     * Returns object, decided by parameter which is checked in validation.properties.
     * 
     * @param validatorType name of the key used to receive the value of the given key, which represents the class used to create object.
     * @return object of type {@link Validator} received from validation.properties, or null 
     * if no type was found with that key 
     */
    public static Validator getValidator(String validatorType)
    {
    	String className = myValidatorMappings.get(validatorType);
    	 
        Validator validator = null;
 
        try 
        {
            if( className != null) 
            {
                Class cls = Class.forName(className);
                validator = (Validator)cls.newInstance();
            }
        } 
        catch (Exception e) {
            log.error(e.getMessage());
        }
 
        return validator;
    }
    
    /**
     * Loads data from VALIDATOR_CONFIGURATION into hashtable  myValidatorMappings.
     */
    private static void loadMyValidatorsMappings() 
    {
        ResourceBundle rb = ResourceBundle.getBundle(VALIDATIOR_CONFIGURATION, Locale.getDefault());
        for (Enumeration<String> e = rb.getKeys(); e.hasMoreElements();) 
        {
            String key = (String) e.nextElement();
            myValidatorMappings.put(key, rb.getString(key));
        }
    }
}
