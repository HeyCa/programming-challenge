package de.bcxp.challenge.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.bcxp.challenge.mapper.CsvToObjectMapper;
import de.bcxp.challenge.model.DailyWeather;
import de.bcxp.challenge.repository.Repository;

class DailyWeatherServiceTest {
		
	private Repository<DailyWeather> mockRepo;
	private DailyWeatherService weatherService;
	private List<DailyWeather> data;
	
	@SuppressWarnings("unchecked")
	@BeforeEach
	void setUp() {
		mockRepo = Mockito.mock(Repository.class);
		CsvToObjectMapper<DailyWeather> mockMapper = Mockito.mock(CsvToObjectMapper.class);
		weatherService = new DailyWeatherService(mockRepo, mockMapper);
		
		data = new ArrayList<>();
		data.add(new DailyWeather(1, 59, 88));
		data.add(new DailyWeather(2, 63, 79));
		data.add(new DailyWeather(3, 55, 77));
		data.add(new DailyWeather(4, 59, 77));
		data.add(new DailyWeather(5, 66, 90));
		
		
	}
	
	@Test
	//full repository with valied tempDiff
	void normalCase() {
		Mockito.when(mockRepo.getData()).thenReturn(data);
		int result = weatherService.getDayWithSmallestTempSpread();
		assertEquals(2, result);
	}
	
	@Test
	void emptyRepository() {
		Mockito.when(mockRepo.getData()).thenReturn(new ArrayList<DailyWeather>());
		int result = weatherService.getDayWithSmallestTempSpread();
		assertEquals(-1, result);
	}
	
	@Test
	void nonValiedEntryPresent() {
		data.add(new DailyWeather(6, 59, -1));
		Mockito.when(mockRepo.getData()).thenReturn(data);
		int result = weatherService.getDayWithSmallestTempSpread();
		assertEquals(2, result);
	}
	
	@Test
	void onlyNonValiedEntries() {
		List <DailyWeather> nonValiedData = new ArrayList<>();
		nonValiedData.add(new DailyWeather(1, -1, 88));
		nonValiedData.add(new DailyWeather(2, -1, -1));
		nonValiedData.add(new DailyWeather(3, 55, -1));
		nonValiedData.add(new DailyWeather(4, -1, 77));
		nonValiedData.add(new DailyWeather(5, 66, -1));
		
		Mockito.when(mockRepo.getData()).thenReturn(nonValiedData);
		int result = weatherService.getDayWithSmallestTempSpread();
		assertEquals(-1, result);
	}
	
	@Test
	void doubleValues() {
		data.add(new DailyWeather(6, 63, 79));
		Mockito.when(mockRepo.getData()).thenReturn(data);
		int result = weatherService.getDayWithSmallestTempSpread();
		assertEquals(2, result);
	}

}
