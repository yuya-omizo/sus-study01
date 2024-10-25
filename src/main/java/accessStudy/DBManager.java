package accessStudy;

import java.util.ResourceBundle;

public class DBManager {
	private String dbUrl;
    private String username;
    private String password;
    
    public DBManager() {
    	 loadProperties();
    }

	private void loadProperties() {
		ResourceBundle rb = ResourceBundle.getBundle("info");
		dbUrl = rb.getString("db.url");
        username = rb.getString("db.username");
        password = rb.getString("db.password");
	}
	
	public void printInfo() {
		System.out.println(dbUrl);
		System.out.println(username);
		System.out.println(password);
	}
	
	public void insertSalesData() {
		
	}
	
	public void insertItemData() {
		
	}
}
