package de.bcxp.challenge.mapper;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

import de.bcxp.challengeExceptions.InvalidFileFormatException;

public class CsvToObjectMapper <T> implements FileToObjectMapper <T>{
	
	
	
	/**
	 * Maps Csv File to object list. Only maps a file if the header is valid. The header is considered valid if at least one of its columns corresponds to a field of T.
	 * Rows that are not valid get skipped and thus not mapped. A row is not valid if its number of columns don't correspond with the number of columns in the header, or if a value does not match the value type of the corresponding field in T. 
	 * If none of the rows are valid, an empty list will be returned. 
	 */
	@Override
	public List<T> mapFileToObjectList(Path filePath) throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		// TODO Auto-generated method stub
		return null;
	}

}
