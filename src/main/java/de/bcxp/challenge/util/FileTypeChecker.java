package de.bcxp.challenge.util;

import java.io.File;
import java.nio.file.Path;

/**
 * This class provides methods to check file types.
 * @author catherine heyart
 *
 */
public class FileTypeChecker {

	/**
	 * Checks if a given file has a "csv" extension.
	 * @param filePath path to the file which will be checked
	 * @return true, if the file has a "csv" extension
	 */
	public static boolean isCsvFile(Path filePath) {
		if(filePath == null) {
			throw new IllegalArgumentException("filePath cannot be null.");
		}
		return getFileExtension(filePath.toString()).equals("csv");
	}
	
	/**
	 * Helper class which retrieves the extension of a given file.
	 * @param filePath path of the file of which the extension is retrieved
	 * @return extension of the file. Empty string if no extension was determined 
	 */
	private static String getFileExtension(String filePath) {
	    String fileName = new File(filePath).getName();
	    int dotIndex = fileName.lastIndexOf('.');
	    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}
}
