package de.bcxp.challenge.service;

import java.util.List;

import de.bcxp.challenge.model.DailyWeather;
import de.bcxp.challenge.repository.Repository;

public class DailyWeatherService extends DataHandlerService <DailyWeather>{

	public DailyWeatherService(Repository<DailyWeather> repository) {
		super(repository);
	}
	
	
	/**
	 * Returns day number of the day with the smallest temperature spread. 
	 * @return day number of the day with the smallest temperature spread
	 */
	public int getDayWithSmallestTemperatureSpread() {
		List<DailyWeather> list = repository.getData();
		if(list.isEmpty()) {
			return 0;
		}
		return -1;
	}

}
