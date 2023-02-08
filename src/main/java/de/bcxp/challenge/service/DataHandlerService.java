package de.bcxp.challenge.service;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Predicate;

import de.bcxp.challenge.repository.Repository;

/**
 * Class contains a repository and handles data. Contains methods to retrieve data meeting certain criteria. 
 * @author catherine heyart
 *
 */
public abstract class DataHandlerService <T>{
	
	/**
	 * Repository containing the data used by this class
	 */
	protected Repository<T> repository;
	
	public DataHandlerService(Repository<T> repository) {
		this.repository = repository;
	}
	
	/**
	 * Adds data to the repository from a Csv file
	 * @param filePath path of .csv file which contains the data
	 */
	public void addDataFromCsvFile(Path filePath) {
		//TO DO
	}
	
	/**
	 * Retrieves the object that has the lowest value according to the comparator. The Entries matching the conditions defined by the filter will be filtered out. 
	 * The filter can for example be used to filter out default values.
	 * @param comparator defines the values which will be compared
	 * @param filter defines to condition by which entries will be filtered out
	 * @return the object with the lowest value. Null if no such object exists (if the repository is empty or if all objects have been filtered out)
	 */
	protected T getObjectByLowestValue(Comparator<T> comparator, Predicate<T> filter) {
		//TO DO
		return null;
	}
	
	/**
	 * Retrieves the object that has the highest value according to the comparator. The Entries matching the conditions defined by the filter will be filtered out. 
	 * The filter can for example be used to filter out default values.
	 * @param comparator defines the values which will be compared
	 * @param filter defines to condition by which entries will be filtered out
	 * @return the object with the highest value. Null if no such object exists (if the repository is empty or if all objects have been filtered out)
	 */
	protected T getObjectByHighestValue(Comparator<T> comparator, Predicate<T> filter) {
		//TO DO
		return null;
	}

}
