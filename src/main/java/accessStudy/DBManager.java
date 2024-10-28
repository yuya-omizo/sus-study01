package accessStudy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DBManager {    
	public static void connectToDB() {
		ResourceBundle rb = ResourceBundle.getBundle("DBinfo");
		
		//パスワード付accdbはjackcessOpenerに自作クラスを指定
		String dbPath = rb.getString("db.path");
		String url = "jdbc:ucanaccess://" + dbPath + ";" + "jackcessOpener=accessStudy.Dec;";
		
		//accdbの場合、ユーザー名は空文字
        String user = "";
        String dbPassword = rb.getString("db.password");
	   	
		try (
			Connection conn = DriverManager.getConnection(url, user, dbPassword);
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM 商品マスタ")){
			while (rs.next()) {
				System.out.println(rs.getString("商品ID"));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void insertSalesData() {
		
	}
	
	public void insertItemData() {
		
	}

}
