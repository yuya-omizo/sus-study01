package accessStudy;

import java.sql.SQLException;

public class Main {
	public static void main(String[] args)  {
		//DBManager.connectToDB();
		
		//CSV渡してリスト変換→売り上げテーブルに格納
		try {
			DBManager.insertDailySales("/Applications/Eclipse_2023-12.app/Contents/workspace/accessStudy/data/売上リスト 2024-08-01.csv");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}