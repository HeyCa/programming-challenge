package de.bcxp.challenge.mapping;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.bcxp.challenge.mapper.CsvToObjectMapper;
import de.bcxp.challenge.mapper.FileToObjectMapper;
import de.bcxp.challengeExceptions.InvalidFileFormatException;


public class CsvToObjectMapperTest {
	
	private List<MockBean> correctBeanList;
	private FileToObjectMapper<MockBean> mapper;
	private static final String FILE_PATH = "src/test/resources/de/bcxp/challenge/";
	
	@BeforeEach
	void setUp() throws Exception {
		mapper = new CsvToObjectMapper<>();  
		
		correctBeanList = new ArrayList<MockBean>();
		correctBeanList.add(new MockBean("January", 1, 59.1f));
		correctBeanList.add(new MockBean("February", 2, 63.5f));
		correctBeanList.add(new MockBean("January", 3, 55.0f));
		correctBeanList.add(new MockBean("January", 4, 59f));
		
	}

	
	@Test
	void normalCase() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException{
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_normalCase.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	void emptyFile() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		assertThrows(InvalidFileFormatException.class, () -> {
			mapper.mapFileToObjectList(Path.of(FILE_PATH + "empty.csv"));
	    });
	}
	
	@Test
	void wrongFilePath() {
		assertThrows(FileNotFoundException.class, () -> {
			mapper.mapFileToObjectList(Path.of("/wrong/weather_normalCase.csv"));
	    });
	}
	
	@Test
	void filePathIsNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			mapper.mapFileToObjectList(null);
	    });
	}
	
	@Test
	void nonCsvFile() {
		assertThrows(InvalidFileFormatException.class, () -> {
			mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather.json"));
	    });
	}
	
	@Test
	//header does not contain columns that correspond to the fields in MockBean
	void invalidHeader() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		assertThrows(InvalidFileFormatException.class, () -> {
			mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_noColumnHeader.csv"));
	    });
	}
	
	
	@Test
	//header contains wrong separator (e.g. ';' instead of ','). The separator used in the header should correspond with the separator specified in the csvMapper
	void headerWithWrongSeparator() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		assertThrows(InvalidFileFormatException.class, () -> {
			mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_noColumnHeader.csv"));
	    });
	}
	
	
	@Test
	//CSV Column missing : value of missing column gets instantiated with default value
	void csvColumnMissing() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		correctBeanList.forEach(b -> b.setTemp(-1.0f));
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_columnMissing.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	//columns present in csv file which are not present in bean get ignored
	void csvColumnTooMuch() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_columnTooMuch.csv"));
		assertEquals(result, correctBeanList);
	}
	

	@Test
	//Entry with wrong type gets skipped (CsvDataTypeMismatchException gets logged)
	void entryWithWrongType() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		correctBeanList.remove(1);
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_entryWithWrongType.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	//entry where number of data fields does not match number of headers gets skipped (CsvRequiredFieldEmptyException gets logged)
	void entryNumberOfDataFieldsDoesNotMatchNumberOfHeaders() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		correctBeanList.remove(1);
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_wrongNumberOfDataFields.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	//entry with wrong separators gets skipped (CsvRequiredFieldEmptyException or CsvDataTypeMismatchException gets logged)
	void entryContainsWrongSeparators() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		correctBeanList.remove(1);
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_entryWithWrongSeparator.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	//empty row gets skipped
	void csvFileContainsEmptyRow() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		correctBeanList.remove(1);
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_emptyRow.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	//skip all Entries -> return empty List (for each entry CsvDataTypeMismatchException gets logged)
	void allEntriesWithWrongType() throws FileNotFoundException, IllegalArgumentException, InvalidFileFormatException {
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_allEntriesWithWrongType.csv"));
		assertEquals(result, new ArrayList<MockBean>());
	}
	
	
	
	
	


	
	

	
	
	
	
	
	

}