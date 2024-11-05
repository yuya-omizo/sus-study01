package accessStudy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.exceptions.CsvException;

public class DBManager {  
	private static final Logger logger = LogManager.getLogger(Main.class);
	
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
				System.out.println(rs.getString("商品名"));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public static void insertDailySales(String csvPath) {
		logger.info("売上データを読み込みます");
		Calendar cl = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(cl.getTime());
		
		CSVHandler csvData = new CSVHandler();
		List<String[]> csvList = new ArrayList<>();
		
		try {
			csvList = csvData.readCSV(csvPath);
			//csvList.forEach(arr -> System.out.println(Arrays.toString(arr)));
		} catch (CsvException e) {
			e.printStackTrace();
		}
		 
		//商品マスタから商品IDを読み込んでカラムに追加
		List<SalesData> salesData = new ArrayList<>();
		
		for(String[] row: csvList) {
			String id = UUID.randomUUID().toString();
			int productId = searchProductId(row[0]);
			int quantity = Integer.parseInt(row[1]);
			//int price = serchProductPrice(productId); 値段は使わない
			
			salesData.add(new SalesData(id, productId, quantity, date));
		}
		
		//ここにデータベース格納の処理を書く
		
		//Loggerでインフォ表示
		logger.info("売上データをDBに格納しました");
	}
	
	public static int searchProductId(String name) {
		String sql = "SELECT 商品ID FROM 商品マスタ WHERE 商品名 = '?'";
		sql = sql.replace("?", name); //多分よくない
		String columnName = "商品ID";
		return Integer.parseInt(searchDB(sql,columnName));
	}
	
	public static int serchProductPrice(int productId) {
		String sql = "SELECT 値段 FROM 商品マスタ WHERE 商品ID = '?'";
		sql = sql.replace("?", String.valueOf(productId)); //多分よくない
		String columnName = "値段";
		return Integer.parseInt(searchDB(sql,columnName));
	}
	
	public static String searchDB(String sql, String columnName) {
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
			ResultSet rs = st.executeQuery(sql)){
			while (rs.next()) {
				String str = rs.getString(columnName);
				return str;
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return null;
	}	

}