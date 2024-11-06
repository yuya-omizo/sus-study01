package accessStudy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

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
			ResultSet rs = st.executeQuery("SELECT * FROM 売り上げ")){
			while (rs.next()) {
				System.out.println(rs.getString("ID"));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public static void insertDailySales(String csvPath) throws SQLException {
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
			//String id = UUID.randomUUID().toString();
			int productId = searchProductId(row[0]);
			int quantity = Integer.parseInt(row[1]);
			
			salesData.add(new SalesData(productId, quantity, date));
		}
		
		// データベース格納処理
	    ResourceBundle rb = ResourceBundle.getBundle("DBinfo");
	    String dbPath = rb.getString("db.path");
	    String url = "jdbc:ucanaccess://" + dbPath + ";jackcessOpener=accessStudy.Dec;";
	    String user = "";
	    String dbPassword = rb.getString("db.password");
	    
	    String sql = "INSERT INTO 売り上げ (ID, 商品ID, 販売個数, 日時) VALUES (?, ?, ?, ?)";
	    
	    try (Connection conn = DriverManager.getConnection(url, user, dbPassword);
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        conn.setAutoCommit(false);
	        try {
	            // 現在の最大ID取得
	            int maxId = 0;
	            try (Statement stmt = conn.createStatement();
	                 ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM 売り上げ")) {
	                if (rs.next()) {
	                    maxId = rs.getInt(1);
	                }
	            }
	            
	        try {
	            // 一括処理
	            for (SalesData data : salesData) {
	            	pstmt.setInt(1, ++maxId);
	                pstmt.setInt(2, data.getProductId());
	                pstmt.setInt(3, data.getQuantity());
	                pstmt.setString(4, data.getDate());
	                pstmt.addBatch();
	            }
	            
	            // 実行
	            int[] results = pstmt.executeBatch();
	            conn.commit();
	            
	            // 結果ログ
	            int total = 0;
	            for (int result : results) {
	                if (result >= 0) total += result;
	            }
	            logger.info("{}件の売上データを登録しました", total);
	            
	        } catch (SQLException e) {
	            // ロールバック処理
	            conn.rollback();
	            logger.error("データ登録中にエラーが発生しました", e);
	            throw new RuntimeException("売上データの登録に失敗しました", e);
	        }
	        
	    } catch (SQLException e) {
	        logger.error("データベース接続エラー", e);
	        throw new RuntimeException("データベース操作に失敗しました", e);
	    }
	    
	    logger.info("売上データをDBに格納しました");
	}
	}
	
	public static int searchProductId(String name) {
		String sql = "SELECT 商品ID FROM 商品マスタ WHERE 商品名 = '?'";
		sql = sql.replace("?", name); //格納処理を参考にして直す
		String columnName = "商品ID";
		return Integer.parseInt(searchDB(sql,columnName));
	}
	
	public static int searchProductPrice(int productId) {
		String sql = "SELECT 値段 FROM 商品マスタ WHERE 商品ID = '?'";
		sql = sql.replace("?", String.valueOf(productId)); //格納処理を参考にして直す
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