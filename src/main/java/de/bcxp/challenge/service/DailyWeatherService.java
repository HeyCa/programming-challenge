package de.bcxp.challenge.service;

import java.util.Comparator;
import java.util.function.Predicate;

import de.bcxp.challenge.mapper.CsvToObjectMapper;
import de.bcxp.challenge.model.DailyWeather;
import de.bcxp.challenge.repository.Repository;

/**
 * This service class provides methods to retrieve DailyWeather data.
 * @author catherine heyart
 *
 */
public class DailyWeatherService extends DataHandlerService <DailyWeather>{
	
	/**
	 * DailyWeather object which holds default values.
	 */
	private DailyWeather defaultObject;
	
	public DailyWeatherService(Repository<DailyWeather> repository, CsvToObjectMapper<DailyWeather> csvMapper) {
		super(repository, csvMapper);
		defaultObject = new DailyWeather();
	}
	
	
	/**
	 * Returns day number of the day with the smallest temperature spread (among all the data currenlty saved in the repository).
	 * @return day number of the day with the smallest temperature spread. -1 if no such day exists (because repository is empty or because no valid temperature data is present)
	 */
	public int getDayWithSmallestTempSpread() {
		//Compares the temperature spread of two objects
		Comparator<DailyWeather> comparator = (w1, w2) -> w1.getTempDiffInF() - w2.getTempDiffInF();
		//Filters out all instances where the temperature spread value has the default value
		Predicate<DailyWeather> filter = w -> (w.getTempDiffInF() != defaultObject.getTempDiffInF());
		
		DailyWeather result = getObjectByLowestValue(comparator, filter); 
	
		return result == null ? -1 : result.getDayOfTheMonth();
	}

}
