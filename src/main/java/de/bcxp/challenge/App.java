package de.bcxp.challenge;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.List;

import de.bcxp.challenge.mapper.CsvToObjectMapper;
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

        //WEATHER
    	Repository<DailyWeather> repo = new Repository<>();
    	CsvToObjectMapper<DailyWeather> mapper = new CsvToObjectMapper<>(DailyWeather.class);
    	DailyWeatherService weatherService = new DailyWeatherService(repo, mapper);
    	try {
			weatherService.addDataFromCsvFile(WEATHER_FILE_PATH, ',');
		} catch (FileNotFoundException | InvalidFileFormatException e) {
			e.printStackTrace();
		}
        int dayWithSmallestTempSpread = weatherService.getDayWithSmallestTempSpread();     // Your day analysis function call …
        System.out.printf("Day with smallest temperature spread: %d%n", dayWithSmallestTempSpread);

        
        
        
        
        String countryWithHighestPopulationDensity = "Some country"; // Your population density analysis function call …
        System.out.printf("Country with highest population density: %s%n", countryWithHighestPopulationDensity);
    }
}
