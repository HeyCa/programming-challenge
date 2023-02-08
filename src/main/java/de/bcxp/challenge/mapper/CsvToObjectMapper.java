package de.bcxp.challenge.mapper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;

import de.bcxp.challengeExceptions.InvalidFileFormatException;

public class CsvToObjectMapper <T> implements FileToObjectMapper <T>{
	
	/**
	 * Class of Type T
	 */
	private Class<T> clazz;
	
	/**
	 * separator that is used in the CSV file. Default is set to ','
	 */
	private char separator;
	
	public CsvToObjectMapper(Class<T> clazz){
		separator = ',';
		this.clazz = clazz;
	}
	
	public CsvToObjectMapper(Class<T> clazz, char separator) {
		this.separator = separator;
		this.clazz = clazz;
	}
	
	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	
	/**
	 * Maps Csv File to object list. Only maps a file if the header is valid. The header is considered valid if at least one of its columns corresponds to a field of T.
	 * Rows that are not valid get skipped and thus not mapped. A row is not valid if its number of columns don't correspond with the number of columns in the header, or if a value does not match the value type of the corresponding field in T. 
	 * If none of the rows are valid, an empty list will be returned. 
	 */
	@Override
	public List<T> mapFileToObjectList(Path filePath) throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		
		if(filePath == null) {
			throw new IllegalArgumentException("The file path cannot be null.");
		}
		
		//TO DO: Check if file is right format -> InvlaidFileFormatException
		
		//TO DO: Check if file is empty -> InvalidFileFormatException
		
		//TO DO: Check if header is valid -> InvalidFileFormatException
		
		
		List<T> list = null; 
		try {
			CsvToBean<T> beans = new CsvToBeanBuilder<T>(new FileReader(filePath.toString()))
					 .withType(clazz)
		             .withSeparator(separator)
		            // .withVerifier(this)
		             .withIgnoreEmptyLine(true)
		             .withThrowExceptions(false)
					 .build();
			
			list = beans.parse();
			
			if(!beans.getCapturedExceptions().isEmpty()) {
				logCapturedExceptions(beans, filePath); 
			}
			
		} catch (IllegalStateException e) {
			//TO DO: add to logger 
			e.printStackTrace();
		}
		return list;
	}
	
	//TO DO: Log to logger
	private void logCapturedExceptions(CsvToBean<T> beans, Path filePath) {
		System.out.println("Captured Exceptions for " + filePath.toString() + ":" );
		beans.getCapturedExceptions().forEach(e -> {
			System.err.println(e.getLineNumber() + ":" + e);
		 	//TO DO: add to logger
		});
	}

}
