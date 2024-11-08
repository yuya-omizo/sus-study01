package accessStudy;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.exceptions.CsvException;

public class DBManager {
	private static final Logger logger = LogManager.getLogger(Main.class);
	
	public static void printDB() { 	
		try (
			Connection conn = DBConnection.getConnection();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM 売り上げ")){
			while (rs.next()) {
				System.out.println(rs.getInt("合計売上数"));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public static void getDatabaseColumnTypes(String tableName) {
	    try (Connection conn = DBConnection.getConnection()) {
	        DatabaseMetaData metaData = conn.getMetaData();
	        try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
	            while (columns.next()) {
	                String columnName = columns.getString("COLUMN_NAME");
	                String columnType = columns.getString("TYPE_NAME");
	                System.out.println("Table: " + tableName + ", Column: " + columnName + ", Type: " + columnType);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void insertSalesReport(String csvPath) throws SQLException {
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
		 
		List<SalesData> salesData = new ArrayList<>();
		for(String[] row: csvList) {
			int productId = searchProductId(row[0]); // 商品名でクエリして商品ID取得
			int quantity = Integer.parseInt(row[1]); // 販売個数
			salesData.add(new SalesData(productId, quantity, date));
		}
	    
	    String sql = "INSERT INTO 売り上げ (ID, 商品ID, 販売個数, 日時) VALUES (?, ?, ?, ?)";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        // トランザクション開始
	    	conn.setAutoCommit(false);
	    	
	    	// 現在の最大ID取得
	            int maxId = 0;
	            try (Statement stmt = conn.createStatement();
	                 ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM 売り上げ")) {
	                if (rs.next()) {
	                    maxId = rs.getInt(1);
	                }
	            }
	            
	        // バッチ処理
	        try {
	            for (SalesData data : salesData) {
	            	pstmt.setInt(1, ++maxId);
	                pstmt.setInt(2, data.getProductId());
	                pstmt.setInt(3, data.getQuantity());
	                pstmt.setString(4, data.getDate());
	                pstmt.addBatch();
	            }
	            
	            // コミット
	            int[] results = pstmt.executeBatch();
	            conn.commit();
	            
	            // 結果のログ
	            int total = 0;
	            for (int result : results) {
	                if (result >= 0) total += result;
	            }
	            logger.info("{}件の売上データを登録しました", total);
	            
	        } catch (SQLException e) {
	            // ロールバック
	            conn.rollback();
	            logger.error("データ登録中にエラーが発生しました", e);
	            throw new RuntimeException("売上データの登録に失敗しました", e);
	        }
	        
	    } catch (SQLException e) {
	        logger.error("データベース接続エラー", e);
	        throw new RuntimeException("データベース操作に失敗しました", e);
	    }
	    
	    logger.info("売上データ処理完了しました");
	}

	public static void insertStockArrivalReport(String csvPath) throws SQLException {
		    logger.info("入荷データを読み込みます");
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
		     
		    List<StockArrivalData> stockArrivalData = new ArrayList<>();
		    for(String[] row: csvList) {
		        int productId = searchProductId(row[0]); // 商品名でクエリして商品ID取得
		        int quantity = Integer.parseInt(row[1]); // 入荷数
		        stockArrivalData.add(new StockArrivalData(productId, quantity, date));
		    }

		    String sql = "INSERT INTO 入荷 (ID, 商品ID, 入荷数, 日時) VALUES (?, ?, ?, ?)";
		    
		    try (Connection conn = DBConnection.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {
		    	// トランザクション開始
		    	conn.setAutoCommit(false);
		        
		        // 現在の最大ID取得
		        int maxId = 0;
		        try (Statement stmt = conn.createStatement();
		             ResultSet rs = stmt.executeQuery("SELECT MAX(ID) FROM 入荷")) {
		            if (rs.next()) {
		                maxId = rs.getInt(1);
		            }
		        }
		        
		        // バッチ処理
		        try {
		            for (StockArrivalData data : stockArrivalData) {
		                pstmt.setInt(1, ++maxId);
		                pstmt.setInt(2, data.getProductId());
		                pstmt.setInt(3, data.getQuantity());
		                pstmt.setString(4, data.getDate());
		                pstmt.addBatch();
		            }
		            
		            // コミット
		            int[] results = pstmt.executeBatch();
		            conn.commit();
		            
		            // 結果ログ
		            int total = 0;
		            for (int result : results) {
		                if (result >= 0) total += result;
		            }
		            logger.info("{}件の入荷データを登録しました", total);
		            
		        } catch (SQLException e) {
		            // ロールバック
		            conn.rollback();
		            logger.error("データ登録中にエラーが発生しました", e);
		            throw new RuntimeException("入荷データの登録に失敗しました", e);
		        }
		    } catch (SQLException e) {
		        logger.error("データベース接続エラー", e);
		        throw new RuntimeException("データベース操作に失敗しました", e);
		    }
		    
		    logger.info("入荷データ処理完了しました");
	}
	
	public static int searchProductId(String param) {
		String sql = "SELECT 商品ID FROM 商品マスタ WHERE 商品名 = ?";
		String columnName = "商品ID";
		String productId = excuteQuery(sql, columnName, param);
		if (productId == null) {
	        logger.error("商品 '{}' が見つからないため、処理を中断します", param);
	        throw new RuntimeException("商品が見つかりません: " + param); // Runtimeをスローして後続の処理を中断する
	    }
	    return Integer.parseInt(productId);
	}
	
	public static String excuteQuery(String sql, String columnName, String param) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, param);
			
			try (ResultSet rs = pstmt.executeQuery()) { //検索結果が重複している場合は、複数行返すので、whileを使う。今は一意なのでif
				if (rs.next()) {
					String str = rs.getString(columnName);
					return str;
				} else {
					throw new SQLException(param);
				}
			}
			
		}catch (SQLException e) {
			logger.error("クエリエラー", e);
		}
		return null;
	}
	
	public static void updateStocks() {
		String sqltStockArrival = "SELECT 商品ID, SUM(入荷数) AS 合計入荷数 FROM 入荷 GROUP BY 商品ID";
	    String sqlSales = "SELECT 商品ID, SUM(CAST(販売個数 AS INTEGER)) AS 合計売上数 FROM 売り上げ GROUP BY 商品ID"; //販売個数の型だけVARCHARなので、キャストしないといけない
	    String sqlUpdateStock = "UPDATE 商品マスタ SET 在庫数 = ? WHERE 商品ID = ?";
	    try (Connection conn = DBConnection.getConnection();
	            Statement stmt = conn.createStatement();
	            PreparedStatement pstmtUpdateStock = conn.prepareStatement(sqlUpdateStock)) {
	    	
	    	// 入荷データの集計(商品IDごと)
	        Map<Integer, Integer> stockArrivalMap = new HashMap<>();
	        try (ResultSet rsArrival = stmt.executeQuery(sqltStockArrival)) {
	            while (rsArrival.next()) {
	                int productId = rsArrival.getInt("商品ID");
	                int totalArrival = rsArrival.getInt("合計入荷数");
	                stockArrivalMap.put(productId, totalArrival);
	            }
	            System.out.println("-----入荷データ集計------");
	            stockArrivalMap.forEach((key, value) -> System.out.println("商品ID: " + key + ", 合計数: " + value));
	        }
	        
	        
	        // 売り上げデータの集計(商品IDごと)
	        Map<Integer, Integer> salesMap = new HashMap<>();
	        try (ResultSet rsSales = stmt.executeQuery(sqlSales)) { //ここでエラー。SQLが違う？
	            while (rsSales.next()) {
	                int productId = rsSales.getInt("商品ID");
	                int totalSales = rsSales.getInt("合計売上数");
	                salesMap.put(productId, totalSales);
	            }
	            System.out.println("-----売り上げデータ集計------");
	            salesMap.forEach((key, value) -> System.out.println("商品ID: " + key + ", 合計数: " + value));
	        }
	        
	        // 在庫数のアップデート処理を書く
	    	
	        
	    }catch (SQLException e) {
	    	 logger.error("データベース接続エラー", e);
	         throw new RuntimeException("在庫データの更新に失敗しました", e);
	    }   
	}
	
}