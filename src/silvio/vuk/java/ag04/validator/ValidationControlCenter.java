package silvio.vuk.java.ag04.validator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.InvalidPropertiesFormatException;

/**
 * Receives path to file that will be checked and validated.
 * 
 * @author silvio
 *
 */
public final class ValidationControlCenter {

	private static String VALIDATOR_DELIMITER = ";";
	private static String FIRST_PARAMETER = "type=";
	private static String SECOND_PARAMETER = "value=";
	private static String ALLOWED_EXTENSION = "txt";
	private static String FS;
	private static String OS = System.getProperty("os.name").toLowerCase();
	
	private String fileForValidationPath;

	static{
		if (isWindows()) {
			FS = "\\";
		} else if (isMac()) {
			FS = "/";
		} else if (isUnix()) {
			FS = "/";
		} else if (isSolaris()) {
			FS = "/";
		} else {
			// OS not supported
		}
	}
	
	/**
	 * 
	 * @param path path to the file that will be validated
	 */
	public ValidationControlCenter(String path)
	{
		this.fileForValidationPath = path;
	}

	/**
	 * 
	 * @throws FileNotFoundException file does not exist
	 * @throws IOException 
	 * @throws InvalidPropertiesFormatException error with the format of the file
	 */
	public void validateFile() throws FileNotFoundException, IOException, InvalidPropertiesFormatException 
	{
		String extension = fileForValidationPath.substring(fileForValidationPath.lastIndexOf(".") + 1);

		if(!extension.equals(ALLOWED_EXTENSION))
			throw new FileSystemException("Invalid file extension\n"
					+ "Only " +ALLOWED_EXTENSION + " extension is allowed!");

		BufferedReader br = new BufferedReader(new FileReader(fileForValidationPath));
		String line;

		int rowCounter = 1; // used for tracking the current number of line 

		clearFile();
		
		while ((line = br.readLine()) != null) 
		{
			String[] splitedLine = line.split(VALIDATOR_DELIMITER);

			if(!checkLineForm(splitedLine[0], 0)){
				br.close();
				throw new InvalidPropertiesFormatException("File format contains errors at line: " +rowCounter +"\nLine: " +splitedLine[0]);
			}
			if(!checkLineForm(splitedLine[1], 1)){
				br.close();
				throw new InvalidPropertiesFormatException("File format contains errors at line: " +rowCounter +"\nLine: " +splitedLine[1]);
			}

			boolean result = validateLine(splitedLine[0], splitedLine[1], rowCounter);
			writeToFile(line, result);
			rowCounter++;
		}

		br.close();
	}

	/**
	 * Cleans file from recent text.
	 * 
	 * @throws IOException if the named file exists but is a directory rather than a regular file, does not 
	 exist but cannot be created, or cannot be opened for any other reason
	 */
	private void clearFile() throws IOException
	{
		File file = new File(fileForValidationPath);
		String fileFullPath = file.getAbsolutePath();
		String filePath = fileFullPath.substring(0, fileFullPath.lastIndexOf(FS));
		filePath += FS + "ValidationResults.txt";
		
		// Create file
        FileWriter fstream = new FileWriter(filePath, false);
        fstream.write("");
        
        //Close the output stream
        fstream.close();
	}
	
	/**
	 * Inserts result into ValidationResults.txt file in same location as the inputed file.
	 * 
	 * @param line line that was read before, will use it as a base string
	 * @param validatorResult result if the validation is valid or invalid
	 * @throws IOException if the named file exists but is a directory rather than a regular file, does not 
	 exist but cannot be created, or cannot be opened for any other reason
	 */
	private void writeToFile(String line, boolean validatorResult) throws IOException
	{
		File file = new File(fileForValidationPath);
		String fileFullPath = file.getAbsolutePath();
		String filePath = fileFullPath.substring(0, fileFullPath.lastIndexOf(FS));
		filePath += FS +"ValidationResults.txt";
		
		// Create file
        FileWriter fstream = new FileWriter(filePath, true);
        BufferedWriter out = new BufferedWriter(fstream);
        
        String finnalLine = line + "validation_result=" +(validatorResult?"valid":"invalid") +";" +System.getProperty("line.separator");
        
        out.write(finnalLine);
        
        //Close the output stream
        out.close();
	}
	
	/**
	 * Sends data to {@link ValidatorFactory} to be validated.
	 * 
	 * @param paramType the type/key 
	 * @param paramValue  the value of that type/key
	 * @param rowCounter the number of the current row from the file we read
	 * @return true if line is valid or false otherwise 
	 * @throws InvalidPropertiesFormatException error with the form of the read text
	 */
	private boolean validateLine(String paramType, String paramValue, int rowCounter) throws InvalidPropertiesFormatException
	{
		String type = paramType.substring(FIRST_PARAMETER.length());
		String value = paramValue.substring(SECOND_PARAMETER.length());

		Validator validator = ValidatorFactory.getValidator(type);
		if(validator == null)
		{
			throw new InvalidPropertiesFormatException("File format contains errors at line: " +rowCounter +"\nNo such validator type: " + paramType);
		}
		else
		{
			boolean result = validator.validate(value);
			if(result)
				return true;
			else
				return false;
		}
	}

	/**
	 * Checks if the form of line is correct.
	 * 
	 * @param line current line that is being checked
	 * @param parameterNumber indicator that tells which part of line is being checked.
	 * @return true if everything is fine with the form, false if it has errors.
	 */
	private boolean checkLineForm(String line, int parameterNumber)
	{
		switch(parameterNumber)
		{
		case 0:
			if(line.indexOf(FIRST_PARAMETER) == 0)
				return true;
			else
				return false;
		case 1:
			if(line.indexOf(SECOND_PARAMETER) == 0)
				return true;
			else 
				return false;
		default:
			return false;
		}
	}
	
	public static boolean isWindows() {
		 
		return (OS.indexOf("win") >= 0);
 
	}
 
	public static boolean isMac() {
 
		return (OS.indexOf("mac") >= 0);
 
	}
 
	public static boolean isUnix() {
 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
 
	}
 
	public static boolean isSolaris() {
 
		return (OS.indexOf("sunos") >= 0);
 
	}
}
