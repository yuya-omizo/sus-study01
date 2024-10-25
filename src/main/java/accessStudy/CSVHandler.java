package accessStudy;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class CSVHandler {
	private static final Logger logger = LogManager.getLogger(CSVHandler.class);

	//csvのpathを受け取ってリストにして返す
	public List<String[]> readCSV(String path) throws CsvException {
		List<String[]> csvData = new ArrayList<>();
		File file = new File(path);
		try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
			String[] line;
			while((line = csvReader.readNext()) !=null) {
				csvData.add(line); //1行ずつリストに格納
			}
			return csvData;
		} catch (IOException e) {
			logger.error("File Error: " + e);
		}
		return null;
	}
}