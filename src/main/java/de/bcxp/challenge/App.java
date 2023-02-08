package de.bcxp.challenge;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

import de.bcxp.challenge.mapper.CsvToObjectMapper;
import de.bcxp.challenge.model.Country;
import de.bcxp.challenge.model.DailyWeather;
import de.bcxp.challenge.repository.Repository;
import de.bcxp.challenge.service.DailyWeatherService;
import de.bcxp.challengeExceptions.InvalidFileFormatException;

/**
 * The entry class for your solution. This class is only aimed as starting point and not intended as baseline for your software
 * design. Read: create your own classes and packages as appropriate.
 */
public final class App {

    /**
     * This is the main entry method of your program.
     * @param args The CLI arguments passed
     */
    public static void main(String... args) {
    	
    	final Path WEATHER_FILE_PATH = Path.of("src/main/resources/de/bcxp/challenge/weather.csv");
    	final Path COUNTRY_FILE_PATH = Path.of("src/main/resources/de/bcxp/challenge/countries.csv");

    	
        //WEATHER
    	Repository<DailyWeather> weatherRepo = new Repository<>();
    	CsvToObjectMapper<DailyWeather> weatherMapper = new CsvToObjectMapper<>(DailyWeather.class);
    	DailyWeatherService weatherService = new DailyWeatherService(weatherRepo, weatherMapper);
    	try {
			weatherService.addDataFromCsvFile(WEATHER_FILE_PATH, ',');
		} catch (FileNotFoundException | InvalidFileFormatException e) {
			e.printStackTrace();
		}
        int dayWithSmallestTempSpread = weatherService.getDayWithSmallestTempSpread();     // Your day analysis function call …
        System.out.printf("Day with smallest temperature spread: %d%n", dayWithSmallestTempSpread);

        Repository<Country> countryRepo = new Repository();
    	CsvToObjectMapper<Country> countryMapper = new CsvToObjectMapper<>(Country.class, ';');
    	try {
			List<Country> list = countryMapper.mapFileToObjectList(COUNTRY_FILE_PATH);
	    	System.out.println(list);
		} catch (FileNotFoundException | InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        
        String countryWithHighestPopulationDensity = "Some country"; // Your population density analysis function call …
        System.out.printf("Country with highest population density: %s%n", countryWithHighestPopulationDensity);
    }
}
