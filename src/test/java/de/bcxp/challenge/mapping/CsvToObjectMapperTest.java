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

/**
 * Test class for CsvToObjectMapper
 * @author catherine heyart
 *
 */
public class CsvToObjectMapperTest {
	
	private List<MockBean> correctBeanList;
	private FileToObjectMapper<MockBean> mapper;
	private static final String FILE_PATH = "src/test/resources/de/bcxp/challenge/";
	
	@BeforeEach
	void setUp() throws Exception {
		mapper = new CsvToObjectMapper<>(MockBean.class);  
		
		correctBeanList = new ArrayList<MockBean>();
		correctBeanList.add(new MockBean("January", 1, 59.1f));
		correctBeanList.add(new MockBean("February", 2, 63.5f));
		correctBeanList.add(new MockBean("January", 3, 55.0f));
		correctBeanList.add(new MockBean("January", 4, 59f));	
	}

	
	@Test
	void normalCase() throws FileNotFoundException, InvalidFileFormatException{
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_normalCase.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	void emptyFile() throws FileNotFoundException, InvalidFileFormatException {
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
	void invalidHeader() throws FileNotFoundException, InvalidFileFormatException {
		assertThrows(InvalidFileFormatException.class, () -> {
			mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_noColumnHeader.csv"));
	    });
	}
	
	
	@Test
	//header contains wrong separator (The separator used in the header does not correspond with the separator specified in the csvMapper)
	void headerWithWrongSeparator() throws FileNotFoundException, InvalidFileFormatException {
		assertThrows(InvalidFileFormatException.class, () -> {
			mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_noColumnHeader.csv"));
	    });
	}
	
	
	@Test
	//CSV column missing : value of missing column gets instantiated with default value
	void csvColumnMissing() throws FileNotFoundException, InvalidFileFormatException {
		correctBeanList.forEach(b -> b.setTemp(-1.0f));
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_columnMissing.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	//columns which are present in csv file and not present in bean get ignored
	void csvColumnTooMuch() throws FileNotFoundException, InvalidFileFormatException {
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_columnTooMuch.csv"));
		assertEquals(result, correctBeanList);
	}
	

	@Test
	//Entry with wrong type gets skipped (CsvDataTypeMismatchException gets logged)
	void entryWithWrongType() throws FileNotFoundException, InvalidFileFormatException {
		correctBeanList.remove(1);
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_entryWithWrongType.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	//entry where number of data fields does not match number of headers gets skipped (CsvRequiredFieldEmptyException gets logged)
	void entryNumberOfDataFieldsDoesNotMatchNumberOfHeaders() throws FileNotFoundException, InvalidFileFormatException {
		correctBeanList.remove(1);
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_wrongNumberOfDataFields.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	//entry with wrong separators gets skipped (CsvRequiredFieldEmptyException gets logged)
	void entryContainsWrongSeparators() throws FileNotFoundException, InvalidFileFormatException {
		correctBeanList.remove(0);
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_entryWithWrongSeparator.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	//empty row gets skipped
	void csvFileContainsEmptyRow() throws FileNotFoundException, InvalidFileFormatException {
		correctBeanList.remove(1);
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_emptyRow.csv"));
		assertEquals(result, correctBeanList);
	}
	
	@Test
	//skip all Entries (because all entries contain wrong type)-> return empty List (for each entry CsvDataTypeMismatchException gets logged)
	void allEntriesWithWrongType() throws FileNotFoundException, InvalidFileFormatException {
		List<MockBean> result = mapper.mapFileToObjectList(Path.of(FILE_PATH + "weather_allEntriesWithWrongType.csv"));
		assertEquals(result, new ArrayList<MockBean>());
	}
	
	
	
	
	


	
	

	
	
	
	
	
	

}
