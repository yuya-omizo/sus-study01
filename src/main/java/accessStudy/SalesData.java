package accessStudy;

//売り上げリストのカスタムクラス
public class SalesData{
	//private int id; 
	private int productId;
	private int quantity;
	private String date;
	
	public SalesData(int productId, int quantity, String date) {
		//this.id = id; 
		this.productId = productId;
		this.quantity = quantity;
		this.date = date;
	}
	
	//public int getId() { return this.id; }
	public int getProductId() { return this.productId; }
	public int getQuantity() { return this.quantity; }
	public String getDate() { return this.date; }
}