package accessStudy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args)  {
		//DBManager.connectToDB();
		//CSVをjavaリスト変換→売り上げテーブルに格納
		DBManager.insertDailySales("/Applications/Eclipse_2023-12.app/Contents/workspace/accessStudy/data/売上リスト 2024-08-01.csv");
		
	}
}