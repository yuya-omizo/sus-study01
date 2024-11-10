package accessStudy;

import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws SQLException  {
		//DBManager.printDB();
		//DBManager.getDatabaseColumnTypes("売り上げ");
		
		//売り上げデータ処理
		DBManager.insertSalesReport("/Applications/Eclipse_2023-12.app/Contents/workspace/accessStudy/data/売上リスト 2024-08-01.csv");
		
		//入荷データ処理
		DBManager.insertStockArrivalReport("/Applications/Eclipse_2023-12.app/Contents/workspace/accessStudy/data/入荷リスト_20240801.csv");
		
		//在庫数更新
		DBManager.updateStocks();
	}
}