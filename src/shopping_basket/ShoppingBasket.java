package shopping_basket;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ShoppingBasket {

	ArrayList<OrderItem> orderItems = new ArrayList<OrderItem>();

	private int numberOfProducts;
	private int numberOfItems;
	private float basketTotal;

	public ShoppingBasket() {
		updateFields();
	}

	public void updateFields() {
		this.numberOfProducts = orderItems.size();
		int numberOfItems = 0;
		float basketTotal = 0;
		for (OrderItem orderItem : orderItems) {
			numberOfItems += orderItem.getQuantity();
			basketTotal += orderItem.getTotalOrder();
		}
		this.numberOfItems = numberOfItems;
		this.basketTotal = basketTotal;
	}

	public int getNumberOfProducts() {
		return numberOfProducts;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public float getBasketTotal() {
		return basketTotal;
	}

	public void addProduct(String productName, float latestPrice) {
		addProduct(productName, latestPrice, 1);
	}

	public void addProduct(String productName, float latestPrice, int quantity) {
		if (productIsInBasket(productName)) {
			getOrderItemByProductName(productName).addItems(latestPrice, quantity);
		} else {
			orderItems.add(new OrderItem(productName, latestPrice, quantity));
		}
		updateFields();
	}

	public void removeProduct(String productName, int quantity) {
		OrderItem orderItem = getOrderItemByProductName(productName);
		if (quantity >= orderItem.getQuantity()) {
			removeProduct(productName);
			return;
		} else {
			orderItem.removeItems(quantity);
		}
		updateFields();
	}

	public void removeProduct(String productName) {
		removeProduct(getOrderItemByProductName(productName));
	}

	public void removeProduct(OrderItem orderItem) {
		orderItems.remove(orderItem);
		updateFields();
	}

	public void clearBasket() {
		orderItems.clear();
		updateFields();
	}

	public float currentPrice(String productName) throws NullPointerException {
		return getOrderItemByProductName(productName).getLatestPrice();
	}

	public boolean productIsInBasket(String productName) {
		return getOrderItemByProductName(productName) != null;
	}

	public boolean saveBasket(String fileName) throws FileNotFoundException {
		PrintWriter printWriter = new PrintWriter(String.format("%s.txt", fileName));
		printWriter.println("BRACKNELL : 013 4474 1012");
		printWriter.println();
		printWriter.println("ecx.io Supermarkets Ltd");
		printWriter.println("2 Arlington Square Bracknell RG12 1WA");
		printWriter.println("www.ecx.io/supermarket");
		printWriter.println("Vat Number : 660 4548 36");
		printWriter.println();
		for (OrderItem orderItem : orderItems) {
			printWriter.println(format(orderItem));
		}
		printWriter.println();
		printWriter.println(format(String.format("%d BALANCE DUE", getNumberOfItems()), 0, 0, getBasketTotal()));
		printWriter.close();
		return true;
	}

	public OrderItem getOrderItemByProductName(String productName) {
		for (OrderItem orderItem : orderItems) {
			if (orderItem.getProductName().equals(productName)) {
				return orderItem;
			}
		}
		return null;
	}

	public String format(OrderItem orderItem) {
		return format(orderItem.getProductName(), orderItem.getQuantity(), orderItem.getLatestPrice(),
				orderItem.getTotalOrder());
	}

	public String format(String name, int quantity, float latestPrice, float total) {
		return String.format("%-20s %10s %10s %10s", name, quantity == 0 ? "" : quantity,
				latestPrice == 0 ? "" : String.format("£%.2f", latestPrice), String.format("£%.2f", total));
	}

}
