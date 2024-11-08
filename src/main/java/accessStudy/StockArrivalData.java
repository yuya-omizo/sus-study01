package accessStudy;

//入荷リストのカスタムクラス
public class StockArrivalData {
	private int productId;
	private int quantity;
	private String date;
	
	public StockArrivalData(int productId, int quantity, String date) {
		this.productId = productId;
		this.quantity = quantity;
		this.date = date;
	}
	
	public int getProductId() { return this.productId; }
	public int getQuantity() { return this.quantity; }
	public String getDate() { return this.date; }
	
}
