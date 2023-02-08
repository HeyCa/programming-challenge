package de.bcxp.challenge.service;

import java.util.Comparator;
import java.util.function.Predicate;

import de.bcxp.challenge.mapper.CsvToObjectMapper;
import de.bcxp.challenge.model.Country;
import de.bcxp.challenge.repository.Repository;

/**
 * This service class provides methods to retrieve Country data.
 * @author catherine heyart
 *
 */
public class CountryService extends DataHandlerService<Country>{
	
	private Country defaultObject;
	
	public CountryService(Repository<Country> repository, CsvToObjectMapper<Country> csvMapper) {
		super(repository, csvMapper);
		defaultObject = new Country();
	}
	
	
	/**
	 * Returns country name of country with the highest population density (from all the Country instances currently saved in the repository)
	 * @return name of the country with the highest population density. Return empty string if no such country exists (because repository is empty or because no valid population density data is present)
	 */
	public String getCountryNameWithHighestPopulationDensity() {
		//Compares the population density spread of two objects
		Comparator<Country> comparator = (c1, c2) -> c1.getPopulationDensity() - c2.getPopulationDensity();
		//Filters out all instances where the population density is the default value
		Predicate<Country> filter = c -> (c.getPopulationDensity() != defaultObject.getPopulationDensity());
		
		Country result = getObjectByHighestValue(comparator, filter); 
	
		return result == null ? "" : result.getName();
	}

}
