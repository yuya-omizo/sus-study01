package accessStudy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

// DB接続専用のクラス
public class DBConnection {
	private static final ResourceBundle rb = ResourceBundle.getBundle("DBinfo");
	private static final String dbPath = rb.getString("db.path");
	private static final String url = "jdbc:ucanaccess://" + dbPath + ";" + "jackcessOpener=accessStudy.Dec;"; //パスワード付accdbはjackcessOpenerに自作クラスを指定
	private static final String user = ""; //accdbの場合、ユーザー名は空文字
	private static final String dbPassword = rb.getString("db.password");
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, dbPassword);
    }
}
