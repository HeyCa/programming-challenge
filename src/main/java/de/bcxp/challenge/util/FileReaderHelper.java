package de.bcxp.challenge.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * Helper class contains methods which can be used when reading files
 * @author catherine heyart
 *
 */
public class FileReaderHelper {
	
	/**
	 * Reads the first line in a file and returns it
	 * @param filePath path to the file of which the first line will be read
	 * @return first line of the file as a String
	 * @throws FileNotFoundException if the file could not be found
	 */
	public static String readFirstLineOfFile(Path filePath) throws FileNotFoundException {
		String result = null;
	    try (BufferedReader reader = Files.newBufferedReader(filePath)) {
	            result = reader.readLine();
	    } catch (IOException e) {
	    	if(e instanceof FileNotFoundException || e instanceof NoSuchFileException) {
	    		throw new FileNotFoundException("The file could not be found: " + filePath.toString());
	    	} 
			e.printStackTrace();
		}
	    return result;	
	}
}
