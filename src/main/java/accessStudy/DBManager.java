package accessStudy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private static String productMaster;
    
	//データベース接続用クラス
	public static void getConnProductDB() {
		//ResourceBundle rb = ResourceBundle.getBundle("info");
	   	//productMaster = rb.getString("db.productMaster");
	   	
		try {
			Connection conn = DriverManager.getConnection("jdbc:ucanaccess:///Applications/Eclipse_2023-12.app/Contents/workspace/accessStudy/data/OldVerDB.mdb");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM 商品マスタ");

			while (rs.next()) {
				System.out.println(rs.getString("商品ID"));
			}
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
		
	
	public void printInfo() {
		System.out.println(productMaster);
	}
	
	public void insertSalesData() {
		
	}
	
	public void insertItemData() {
		
	}

}
