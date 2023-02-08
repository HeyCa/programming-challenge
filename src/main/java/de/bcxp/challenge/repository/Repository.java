package de.bcxp.challenge.repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository of a certain data type. Contains methods to manage data input into the repository.
 * @author catherine heyart
 *
 * @param <T> object type saved in the repository
 */
public class Repository <T>{
	
	/**
	 * List containing all the data of the repository
	 */
	private List <T> data;
	
	public Repository() {
		data = new ArrayList<>();
	}
	
	/**
	 * Adds new data to the repository. The data gets appended to the already existing data
	 * @param newData
	 */
	public void addData(List<T> newData) {
		if(newData == null) {
			throw new IllegalArgumentException("The data can not be null.");
		}
		data.addAll(newData);
	}
	
	public List<T> getData() {
		return data;
	}
	/**
	 * Checks if the repository is empty.
	 * @return true, if the repository is empty
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

}