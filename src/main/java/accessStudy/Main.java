package accessStudy;

import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws SQLException  {
		//String tableName = "売り上げ";
		//DBManager.getDatabaseColumnTypes(tableName);
		//DBManager.printDB();
		
		DBManager.insertSalesReport("/Applications/Eclipse_2023-12.app/Contents/workspace/accessStudy/data/売上リスト 2024-08-01.csv");
		DBManager.insertStockArrivalReport("/Applications/Eclipse_2023-12.app/Contents/workspace/accessStudy/data/入荷リスト_20240801.csv");
		DBManager.updateStocks();
	}
}