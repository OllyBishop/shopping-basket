package shopping_basket;

public class OrderItem {

	private String productName;
	private float latestPrice;
	private int quantity;

	public OrderItem(String productName, float latestPrice) {
		this(productName, latestPrice, 1);
	}

	public OrderItem(String productName, float latestPrice, int quantity) {
		this.productName = productName;
		this.latestPrice = latestPrice;
		this.quantity = quantity;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public float getLatestPrice() {
		return this.latestPrice;
	}

	public void setLatestPrice(float latestPrice) {
		this.latestPrice = latestPrice;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getTotalOrder() {
		return getLatestPrice() * getQuantity();
	}

	// Test only
	public int addItem() {
		return addItems(1);
	}
	//

	public int addItems(float latestPrice, int quantity) {
		setLatestPrice(latestPrice);
		return addItems(quantity);
	}

	public int addItems(int quantity) {
		setQuantity(getQuantity() + quantity);
		return getQuantity();
	}

	// Test only
	public int removeItem() {
		return removeItems(1);
	}
	//

	public int removeItems(int quantity) {
		setQuantity(getQuantity() - quantity);
		return getQuantity();
	}

}
