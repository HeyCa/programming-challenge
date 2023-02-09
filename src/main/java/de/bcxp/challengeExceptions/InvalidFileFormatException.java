package de.bcxp.challengeExceptions;

/**
 * This class defines an exception which should be thrown when a file does not meet format requirements.
 * @author catherine heyart
 *
 */
public class InvalidFileFormatException extends Exception{
	
	/**
	 * Exception which should be thrown when a file does not meet format requirements.
	 * @param errorMessage Message which explains the error that occured
	 */
	public InvalidFileFormatException(String errorMessage) {
	    super(errorMessage);
	}

}
