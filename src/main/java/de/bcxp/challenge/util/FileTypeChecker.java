package de.bcxp.challenge.util;

import java.io.File;
import java.nio.file.Path;

public class FileTypeChecker {

	public static boolean isCsvFile(Path filePath) {
		if(filePath == null) {
			throw new IllegalArgumentException("filePath cannot be null.");
		}
		return getFileExtension(filePath.toString()).equals("csv");
	}
	
	private static String getFileExtension(String filePath) {
	    String fileName = new File(filePath).getName();
	    int dotIndex = fileName.lastIndexOf('.');
	    return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
	}
}
