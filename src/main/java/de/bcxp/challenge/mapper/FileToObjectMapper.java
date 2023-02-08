package de.bcxp.challenge.mapper;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

/**
 * 
 * Defines a class that maps a file to an object or object list. 
 * @author catherine heyart
 *
 * @param <T> Object(s) that a file will be mapped to. Class T might need to follow specific conventions, so that the mapping can be successful. 
 */
public interface FileToObjectMapper <T>{
	
	/**
	 * Parses a file and maps it to an object list. 
	 * @param filePath path to the file that will be mapped
	 * @return List of mapped objects
	 * @throws FileNotFoundException if file could not be found
	 * @throws  if illegal argument was passed (e.g. null)
	 */
	
	public List<T> mapFileToObjectList(Path filePath) throws FileNotFoundException, IllegalArgumentException;


}
