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

import de.bcxp.challenge.util.FileReaderHelper;
import de.bcxp.challenge.util.FileTypeChecker;
import de.bcxp.challengeExceptions.InvalidFileFormatException;

/**
 * This class is responsible for mapping a csv file to an object list.
 * @author catherine heyart
 *
 * @param <T> Class of the object that the csv file will be mapped to. 
 * The class needs to follow JavaBeans conventions and the fields that should be mapped need to be marked with an OpenCSV annotation.
 */
public class CsvToObjectMapper <T> implements FileToObjectMapper <T>{
	
	/**
	 * Class of Type T.
	 */
	private Class<T> clazz;
	
	/**
	 * Separator that is used in the CSV file which will be parsed. Default is set to ','
	 */
	private char separator;
	
	/**
	 * HashSet containing all the fields of T that will be mapped.  
	 */
	private HashSet<String> beanFields;
	
	/**
	 * Name of this class.
	 */
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
	 * Maps Csv File to object list. Only maps a file if the file extension is .csv and if the header is valid. 
	 * The header is considered valid if at least one of its columns corresponds to an annotated field of T.
	 * Rows that are not valid get skipped and thus not mapped. A row is not valid if its number of columns doesn't
	 * correspond with the number of columns in the header, or if a value does not match the value type of the corresponding field in T. 
	 * If none of the rows are valid, an empty list will be returned. 
	 */
	@Override
	public List<T> mapFileToObjectList(Path filePath) throws FileNotFoundException, InvalidFileFormatException {
		
		if(filePath == null) {
			throw new IllegalArgumentException("The file path cannot be null.");
		}
		
		if(!FileTypeChecker.isCsvFile(filePath)) {
			throw new InvalidFileFormatException("File " + filePath.toString() + " is invalid. The file must be a .csv file.");
		}
		
		if(!headerIsValid(filePath)) {
			throw new InvalidFileFormatException("The header is not valid. \n"
					+ "The header needs to have at least one column which matches an annotated field of the bean to be mapped. \n"
					+ "It also needs to contain the same separator as the one defined in " + THIS_CLASS_NAME);
		}
		
		List<T> list = null; 
		try {
			CsvToBean<T> beans = new CsvToBeanBuilder<T>(new FileReader(filePath.toString()))
					 .withType(clazz)
		             .withSeparator(separator)
		            // .withVerifier(BeanVerifier)  -> could be used to filter out beans that have invalid values
		             .withIgnoreEmptyLine(true)
		             .withThrowExceptions(false)
					 .build();
			
			list = beans.parse();
			
			if(!beans.getCapturedExceptions().isEmpty()) {
				logCapturedExceptions(beans, filePath); 
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	///////////HELPER METHODS//////////////////////////////////////////////////
	
	/**
	 * This method checks if the first line in a given file is a valid csv header. 	 
	 * The header is considered valid if 1) at least one of its columns corresponds to an annotated field of T,
	 * and 2) the separator used in the header is the same separator that is defined in this class. 
	 * @param filePath path to the file which contains the header
	 * @return true, if the header is considered valid (matches the above mentioned criteria)
	 * @throws FileNotFoundException if the file could not be found
	 */
	private boolean headerIsValid(Path filePath) throws FileNotFoundException{
		String header = FileReaderHelper.readFirstLineOfFile(filePath);
		
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
	
	
	
	/**
	 * This method detects all the annotated fields of T and returns in a HashSet in form of strings.
	 * @return Hashset containing all the bean fields that will be mapped.
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
	 * Helper method which detects all the annotated fields of T and returns them as a single String. 
	 * The fields are separated by the separator defined in this class.
	 * @return String containint all the bean fields that will be mapped
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
	    	
	    	//map bean object to Stringwriter -> will contain:
	    	//1) column/field names in first line
	    	//2) default values in second line
	        StatefulBeanToCsv<T> sbc = new StatefulBeanToCsvBuilder<T>(writer)
	          .withQuotechar('\"')
	          .withSeparator(separator)
	          .build();
			 sbc.write(defaultBean);
			
			 //retrieve first line (line containing the column names)
		    Scanner scanner = new Scanner(writer.toString());
		    return scanner.nextLine();

	    } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
		}
	    return "";    
	}
	
	/**
	 * Helper method which logs the exceptions caught while the csv file got mapped to beans.
	 * @param beans Beans containing the caputred expressions
	 * @param filePath path of the file which got mapped and which the exceptions refer to
	 */
	//TO DO: instead of logging the exceptions to the console, it is better to log them into a file 
	private void logCapturedExceptions(CsvToBean<T> beans, Path filePath) {
		System.err.println("Captured Exceptions from " + THIS_CLASS_NAME + " while parsing " + filePath.toString() + ":" );
		System.err.println("The listed row(s) will not be mapped to objects.");
		beans.getCapturedExceptions().forEach(e -> {
			System.err.println(e.getLineNumber() + ":" + e);
		});
	}
	
	

}
