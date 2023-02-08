package de.bcxp.challenge.service;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.bcxp.challenge.mapper.CsvToObjectMapper;
import de.bcxp.challenge.repository.Repository;
import de.bcxp.challengeExceptions.InvalidFileFormatException;

/**
 * Class contains a repository and handles data. Contains methods to retrieve data meeting certain criteria. 
 * @author catherine heyart
 *
 */
public abstract class DataHandlerService <T>{
			
	/**
	 * Repository containing the data used by this class
	 */
	protected Repository<T> repo;
	
	protected CsvToObjectMapper<T> csvMapper;
	
	public DataHandlerService(Repository<T> repository, CsvToObjectMapper<T> csvMapper) {
		if(repository == null) {
			throw new IllegalArgumentException("Repository cannot be null.");
		}
		if(csvMapper == null) {
			throw new IllegalArgumentException("CSV Mapper cannot be null.");
		}
		this.repo = repository;
		this.csvMapper = csvMapper;
	}
	
	/**
	 * Adds data to the repository from a Csv file. Rows that are not formatted correctly or have the wrong value type get skipped. 
	 * @param filePath path of .csv file which contains the data
	 * @throws InvalidFileFormatException 
	 * @throws FileNotFoundException 
	 */
	public void addDataFromCsvFile(Path filePath, char separator) throws FileNotFoundException, InvalidFileFormatException {
		csvMapper.setSeparator(separator);
		repo.addData(csvMapper.mapFileToObjectList(filePath));
	}
	
	/**
	 * Prints the data which is currenlty present in the repository
	 */
	public void printData() {
		if(repo.isEmpty()) {
			System.out.println("The repository is empty.");
		} else {
			System.out.println("Data:");
			repo.getData().stream().forEach(d -> System.out.println(d));
		}
	}
	
	/**
	 * Retrieves the object that has the lowest value according to the comparator. The Entries matching the conditions defined by the filter will be filtered out. 
	 * The filter can for example be used to filter out default values.
	 * @param comparator defines the values which will be compared (cannot be null)
	 * @param filter defines to condition by which entries will be filtered out (can be null)
	 * @return the object with the lowest value. Null if no such object exists (if the repository is empty or if all objects have been filtered out)
	 */
	protected T getObjectByLowestValue(Comparator<T> comparator, Predicate<T> filter) {
		if(comparator == null) {
			throw new IllegalArgumentException("Comparator cannot be null.");
		}
		
		if(repo.isEmpty()) {
			return null;
		}
				
		List<T> sortedList = sortAndFilterData(comparator, filter);
		
		//System.out.println("Sorted List: ");
		//sortedList.stream().forEach(d -> System.out.println(d));
		
		
		return sortedList.isEmpty() ? null : sortedList.get(0);
	}
	
	/**
	 * Retrieves the object that has the highest value according to the comparator. The Entries matching the conditions defined by the filter will be filtered out. 
	 * The filter can for example be used to filter out default values.
	 * @param comparator defines the values which will be compared (cannot be null)
	 * @param filter defines to condition by which entries will be filtered out (can be null)
	 * @return the object with the highest value. Null if no such object exists (if the repository is empty or if all objects have been filtered out)
	 */
	protected T getObjectByHighestValue(Comparator<T> comparator, Predicate<T> filter) {
		if(comparator == null) {
			throw new IllegalArgumentException("Comparator cannot be null.");
		}
		
		if(repo.isEmpty()) {
			return null;
		}
				
		List<T> sortedList = sortAndFilterData(comparator, filter);
		int size = sortedList.size();
		
		System.out.println("Sorted List: ");
		sortedList.stream().forEach(d -> System.out.println(d));
		
		
		return sortedList.isEmpty() ? null : sortedList.get(size-1);
	}
	
	private List<T> sortAndFilterData(Comparator<T> comparator, Predicate<T> filter){
		if(filter == null) {
			filter = (x) -> (true);
		}
		
		return repo.getData().stream()
				.sorted(comparator)
				.filter(filter)
				.collect(Collectors.toList());
	}

}
