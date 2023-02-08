package de.bcxp.challenge.service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import de.bcxp.challenge.model.DailyWeather;
import de.bcxp.challenge.repository.Repository;

public class DailyWeatherService extends DataHandlerService <DailyWeather>{
	
	private DailyWeather defaultObject;
	
	public DailyWeatherService(Repository<DailyWeather> repository) {
		super(repository);
		defaultObject = new DailyWeather();
	}
	
	
	/**
	 * Returns day number of the day with the smallest temperature spread. 
	 * @return day number of the day with the smallest temperature spread. -1 if no such day exists (because repository is empty or because no valid temperature data is present)
	 */
	public int getDayWithSmallestTemperatureSpread() {
		//Compares the temperature spread of two objects
		Comparator<DailyWeather> comparator = (w1, w2) -> w1.getTempDiffInF() - w2.getTempDiffInF();
		//Filters out all instances where the temperature spread value has the default value
		Predicate<DailyWeather> filter = w -> (w.getTempDiffInF() != defaultObject.getTempDiffInF());
		
		DailyWeather result = getObjectByLowestValue(comparator, filter); 
	
		return result == null ? -1 : result.getDayOfTheMonth();
	}

}
