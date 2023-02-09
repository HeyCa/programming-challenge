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
 * This class has access to a repository of type T and provides methods to input and retrieve data. 
 * @author catherine heyart
 *
 */
public abstract class DataHandlerService <T>{
			
	/**
	 * Repository containing the data used by this class
	 */
	protected Repository<T> repo;
	
	/**
	 * Mapper which maps csv files to objects of type T
	 */
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
	 * @param separator Separator used in the csv file
	 * @throws InvalidFileFormatException if the file does not have a .csv extension or if the header is invalid. 
	 * The header is considered invalid if 1) none of its columns correspond to a field of the Object T, or 
	 * 2) the separator in the header does not correspond to the separator provided as a parameter in this method.
	 * @throws FileNotFoundException if the file could not be found
	 */
	public void addDataFromCsvFile(Path filePath, char separator) throws FileNotFoundException, InvalidFileFormatException {
		csvMapper.setSeparator(separator);
		repo.addData(csvMapper.mapFileToObjectList(filePath));
	}
	
	/**
	 * Prints the data which is currently present in the repository
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
	 * Retrieves the object that has the lowest value according to the comparator. The entries NOT matching the conditions defined by the filter will be filtered out. 
	 * (The filter can for example be used to filter out default values.)
	 * @param comparator defines the values which will be compared (cannot be null)
	 * @param filter defines the condition by which entries will be filtered (can be null)
	 * @return the object with the lowest value. Null if no such object exists (if the repository is empty or if all objects have been filtered out)
	 */
	protected T getObjectByLowestValue(Comparator<T> comparator, Predicate<T> filter) {
		if(repo.isEmpty()) {
			return null;
		}
				
		List<T> sortedList = sortAndFilterData(comparator, filter);
		
		return sortedList.isEmpty() ? null : sortedList.get(0);
	}
	
	/**
	 * Retrieves the object that has the highest value according to the comparator. The Entries NOT matching the conditions defined by the filter will be filtered out. 
	 * The filter can for example be used to filter out default values.
	 * @param comparator defines the values which will be compared (cannot be null)
	 * @param filter defines to condition by which entries will be filtered (can be null)
	 * @return the object with the highest value. Null if no such object exists (if the repository is empty or if all objects have been filtered out)
	 */
	protected T getObjectByHighestValue(Comparator<T> comparator, Predicate<T> filter) {
		if(repo.isEmpty()) {
			return null;
		}
				
		List<T> sortedList = sortAndFilterData(comparator, filter);
		int size = sortedList.size();
		
		return sortedList.isEmpty() ? null : sortedList.get(size-1);
	}
	
	/**
	 * Helper method which sorts and filters data and returns the results as a list
	 * @param comparator defines the values by which the list will be sorted by (cannot be null)
	 * @param filter defines the condition by which the data will be filtered by (all entries NOT meeting the condition will be filtered out)
	 * @return list of sorted and filtered data
	 */
	private List<T> sortAndFilterData(Comparator<T> comparator, Predicate<T> filter){
		if(comparator == null) {
			throw new IllegalArgumentException("Comparator cannot be null.");
		}
		
		if(filter == null) {
			filter = (x) -> (true);  //condition is always met -> no entries will be filtered out
		}
		
		return repo.getData().stream()
				.sorted(comparator)
				.filter(filter)
				.collect(Collectors.toList());
	}

}
