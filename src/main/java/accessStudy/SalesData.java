package accessStudy;

//売り上げリストのカスタムクラス
public class SalesData{
	private String id; //UUID
	private int productId;
	private int quantity;
	private String date;
	
	public SalesData(String id, int productId, int quantity, String date) {
		this.id = id; //UUID
		this.productId = productId;
		this.quantity = quantity;
		this.date = date;
	}
	
	public String getName() { return this.id; }
	public int getProductId() { return this.productId; }
	public int getQuantity() { return this.quantity; }
	public String getDate() { return this.date; }
}