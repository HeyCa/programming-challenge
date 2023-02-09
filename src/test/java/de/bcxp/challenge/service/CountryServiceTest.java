package de.bcxp.challenge.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.bcxp.challenge.mapper.CsvToObjectMapper;
import de.bcxp.challenge.model.Country;
import de.bcxp.challenge.repository.Repository;

/**
 * Test class for CountryService
 * @author catherine heyart
 *
 */
class CountryServiceTest {

	private Repository<Country> mockRepo;
	private CountryService countryService;
	private List<Country> data;
	
	@SuppressWarnings("unchecked")
	@BeforeEach
	void setUp() {
		mockRepo = Mockito.mock(Repository.class);
		CsvToObjectMapper<Country> mockMapper = Mockito.mock(CsvToObjectMapper.class);
		countryService = new CountryService(mockRepo, mockMapper);
		
		data = new ArrayList<>();
		data.add(new Country("Luxembourg", 633347, 2586));
		data.add(new Country("Netherlands", 17614840, 41543));
		data.add(new Country("Malta", 516100, 316));
		data.add(new Country("Estonia", 1330068, 45227));
		data.add(new Country("Germany", 83120520, 357386));	
	}
	
	@Test
	//full repository where all entries have valid population density
	void normalCase() {
		Mockito.when(mockRepo.getData()).thenReturn(data);
		String result = countryService.getCountryNameWithHighestPopulationDensity();
		assertEquals("Malta", result);
	}
	
	@Test
	void emptyRepository() {
		Mockito.when(mockRepo.getData()).thenReturn(new ArrayList<Country>());
		String result = countryService.getCountryNameWithHighestPopulationDensity();
		assertEquals("", result);
	}
	
	@Test
	//one entry on repository contains invalid population density (value -1) which gets filtered out
	void nonValidEntryPresent() {
		data.add(new Country("Hungary", 59, -1));
		Mockito.when(mockRepo.getData()).thenReturn(data);
		String result = countryService.getCountryNameWithHighestPopulationDensity();
		assertEquals("Malta", result);
	}
	
	@Test
	//all entries contain invalid population densities -> empty array is returned because all entries get filtered out
	void onlyNonValidEntries() {
		List <Country> nonValidData = new ArrayList<>();
		nonValidData.add(new Country("Luxembourg", -1, 2586));
		nonValidData.add(new Country("Netherlands", 17614840, 0));
		nonValidData.add(new Country("Malta", -1, 316));
		nonValidData.add(new Country("Estonia", -1, -1));
		nonValidData.add(new Country("Germany", 83120520, -1));
		
		Mockito.when(mockRepo.getData()).thenReturn(nonValidData);
		String result = countryService.getCountryNameWithHighestPopulationDensity();
		assertEquals("", result);
	}
	
	@Test
	void highestValueIsPresentTwice() {
		data.add(new Country("Double Malta", 516100, 316));
		Mockito.when(mockRepo.getData()).thenReturn(data);
		String result = countryService.getCountryNameWithHighestPopulationDensity();
		assertTrue(result.equals("Malta") || result.equals("Double Malta"));
	}

}
