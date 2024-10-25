package accessStudy;

import java.sql.Date;
import java.util.Map;

public class SalesSystemData {
	public record ProductData(String productId, String productName, int stock) {
	}

	public record SalesData(String productId, int quantity, Date saleDate) {
	}

	public record InventoryData(String productId, int quantity, Date receivedDate) {
	}

	public record SummaryData(Map<String, Integer> salesByProduct, Date startDate, Date endDate) {
	}
}
