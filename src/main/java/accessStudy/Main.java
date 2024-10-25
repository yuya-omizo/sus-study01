package accessStudy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.exceptions.CsvException;

public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args)  {
		CSVHandler csvData = new CSVHandler();
		List<String[]> li = new ArrayList<>();
		
		try {
			li = csvData.readCSV("/Applications/Eclipse_2023-12.app/Contents/workspace/accessStudy/data/入荷リスト_20240801.csv");			
			li.forEach(arr -> System.out.println(Arrays.toString(arr)));
		} catch (CsvException e) {
			logger.error("CSV Error: " + e);
		}
		
		//とりあえず表示
		DBManager info = new DBManager();
		info.printInfo();
	}
}