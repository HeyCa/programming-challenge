package de.bcxp.challenge.mapper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import de.bcxp.challenge.util.FileTypeChecker;
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
	
	/**
	 * hashSet containing all the fields of T that will be mapped  
	 */
	private HashSet<String> beanFields;
	
	private final String THIS_CLASS_NAME =  this.getClass().getName();
	
	
	public CsvToObjectMapper(Class<T> clazz){
		separator = ',';
		this.clazz = clazz;
		beanFields = retrieveBeanFields();
	}
	
	public CsvToObjectMapper(Class<T> clazz, char separator) {
		this.separator = separator;
		this.clazz = clazz;
		beanFields = retrieveBeanFields();
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
	public List<T> mapFileToObjectList(Path filePath) throws FileNotFoundException, InvalidFileFormatException {
		
		if(filePath == null) {
			throw new IllegalArgumentException("The file path cannot be null.");
		}
		
		if(!FileTypeChecker.isCsvFile(filePath)) {
			throw new InvalidFileFormatException("The file must be a .csv file");
		}
		
		if(!headerIsValid(filePath)) {
			throw new InvalidFileFormatException("The header is not valid. \n"
					+ "The header needs to have at least one column which matches a field of the bean to be mapped. \n"
					+ "It also needs to contain the same separator as the one defined in the class " + THIS_CLASS_NAME);
		}
		
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
	
	
	
	///////////HELPER METHODS//////////////////////////////////////////////////
	
	private boolean headerIsValid(Path filePath) throws FileNotFoundException{
		String header = readFirstLineOfFile(filePath);
		
		if(header == null || header.isBlank()) {
			return false;
		}
		
		String[] columns = header.split("" + separator);
		
		for(String c : columns){
        	if(beanFields.contains(c.toUpperCase())) {
        		return true;
        	}
        }
		return false;
	}
	
	//TO DO: outsource to util class (?)
	private String readFirstLineOfFile(Path filePath) throws FileNotFoundException {
		String result = null;
	    try (BufferedReader reader = Files.newBufferedReader(filePath)) {
	            result = reader.readLine();
	    } catch (IOException e) {
	    	if(e instanceof FileNotFoundException || e instanceof NoSuchFileException) {
	    		throw new FileNotFoundException("The file could not be found");
	    	} 
			e.printStackTrace();
		}
	    return result;	
	}
	
	/**
	 * 
	 * @return Hashset containing all the bean fields that will be mapped 
	 */
	private HashSet<String> retrieveBeanFields(){
		String fieldString = getFieldsAsString();
		HashSet<String> hashSet = new HashSet<>();
		
		String[] fields = fieldString.split("" + separator);
		for(String s : fields){
        	s = s.replace("\"", "");
        	hashSet.add(s);
        }
		return hashSet;	
	}
	
	
	/**
	 * Returns a string of all annotated fields. The fields are separated by the separator defined in this class.
	 * @return
	 */
	//there is likely to be a better (less elaborate) way to get this information, but I didn't manage to find it.
	private String getFieldsAsString()  {
		
	    T defaultBean = null;
	    
		try {
			//initialize bean with default values (by invoking parameterless constructor)
			defaultBean = clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

	    
	    try (Writer writer  = new StringWriter()) {
	    	
	    	//map bean object to csv string
	        StatefulBeanToCsv<T> sbc = new StatefulBeanToCsvBuilder<T>(writer)
	          .withQuotechar('\"')
	          .withSeparator(separator)
	          .build();

			 sbc.write(defaultBean);
			
		    Scanner scanner = new Scanner(writer.toString());
		    return scanner.nextLine();

	    } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return "";    
	}
	
	//TO DO: Log to logger
	private void logCapturedExceptions(CsvToBean<T> beans, Path filePath) {
		System.err.println("Captured Exceptions from class" + THIS_CLASS_NAME + " while parsing " + filePath.toString() + ":" );
		System.err.println("The listed row(s) will not be mapped to objects.");
		beans.getCapturedExceptions().forEach(e -> {
			System.err.println(e.getLineNumber() + ":" + e);
		 	//TO DO: add to logger
		});
	}
	
	

}
