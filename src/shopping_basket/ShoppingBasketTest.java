package shopping_basket;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

public class ShoppingBasketTest {

	ShoppingBasket shoppingBasket = new ShoppingBasket();

	@Test
	public void testAddProductUnspecifiedQuantity() {
		// Arrange
		String productName = "Apple";
		float latestPrice = 0.10f;

		// Act
		shoppingBasket.addProduct(productName, latestPrice);

		// Assert
		OrderItem orderItem = shoppingBasket.getOrderItemByProductName(productName);

		assertEquals(productName, orderItem.getProductName());
		assertEquals(latestPrice, orderItem.getLatestPrice(), 0);
		assertEquals(1, orderItem.getQuantity());
		assertEquals(0.10f, orderItem.getTotalOrder(), 0);

		assertEquals(1, shoppingBasket.getNumberOfProducts());
		assertEquals(1, shoppingBasket.getNumberOfItems());
		assertEquals(0.10f, shoppingBasket.getBasketTotal(), 0);
	}

	@Test
	public void testAddProductSpecifiedQuantity() {
		// Arrange
		String productName = "Apple";
		float latestPrice = 0.10f;
		int quantity = 10;

		// Act
		shoppingBasket.addProduct(productName, latestPrice, quantity);

		// Assert
		OrderItem orderItem = shoppingBasket.getOrderItemByProductName(productName);

		assertEquals(productName, orderItem.getProductName());
		assertEquals(latestPrice, orderItem.getLatestPrice(), 0);
		assertEquals(quantity, orderItem.getQuantity());
		assertEquals(1.00f, orderItem.getTotalOrder(), 0);

		assertEquals(1, shoppingBasket.getNumberOfProducts());
		assertEquals(10, shoppingBasket.getNumberOfItems());
		assertEquals(1.00f, shoppingBasket.getBasketTotal(), 0);
	}

	@Test
	public void testAddProductsUnspecifiedQuantities() {
		// Arrange
		String productName1 = "Apple";
		float latestPrice1 = 0.10f;

		String productName2 = "Pineapple";
		float latestPrice2 = 0.45f;

		// Act
		shoppingBasket.addProduct(productName1, latestPrice1);
		shoppingBasket.addProduct(productName2, latestPrice2);

		// Assert
		OrderItem orderItem1 = shoppingBasket.getOrderItemByProductName(productName1);
		OrderItem orderItem2 = shoppingBasket.getOrderItemByProductName(productName2);

		assertEquals(productName1, orderItem1.getProductName());
		assertEquals(latestPrice1, orderItem1.getLatestPrice(), 0);
		assertEquals(1, orderItem1.getQuantity());
		assertEquals(0.10f, orderItem1.getTotalOrder(), 0);

		assertEquals(productName2, orderItem2.getProductName());
		assertEquals(latestPrice2, orderItem2.getLatestPrice(), 0);
		assertEquals(1, orderItem2.getQuantity());
		assertEquals(0.45f, orderItem2.getTotalOrder(), 0);

		assertEquals(2, shoppingBasket.getNumberOfProducts());
		assertEquals(2, shoppingBasket.getNumberOfItems());
		assertEquals(0.55f, shoppingBasket.getBasketTotal(), 0);
	}

	@Test
	public void testAddProductsSpecifiedQuantities() {
		// Arrange
		String productName1 = "Apple";
		float latestPrice1 = 0.10f;
		int quantity1 = 10;

		String productName2 = "Pineapple";
		float latestPrice2 = 0.45f;
		int quantity2 = 2;

		// Act
		shoppingBasket.addProduct(productName1, latestPrice1, quantity1);
		shoppingBasket.addProduct(productName2, latestPrice2, quantity2);

		// Assert
		OrderItem orderItem1 = shoppingBasket.getOrderItemByProductName(productName1);
		OrderItem orderItem2 = shoppingBasket.getOrderItemByProductName(productName2);

		assertEquals(productName1, orderItem1.getProductName());
		assertEquals(latestPrice1, orderItem1.getLatestPrice(), 0);
		assertEquals(10, orderItem1.getQuantity());
		assertEquals(1.00f, orderItem1.getTotalOrder(), 0);

		assertEquals(productName2, orderItem2.getProductName());
		assertEquals(latestPrice2, orderItem2.getLatestPrice(), 0);
		assertEquals(2, orderItem2.getQuantity());
		assertEquals(0.90f, orderItem2.getTotalOrder(), 0);

		assertEquals(2, shoppingBasket.getNumberOfProducts());
		assertEquals(12, shoppingBasket.getNumberOfItems());
		assertEquals(1.90f, shoppingBasket.getBasketTotal(), 0);
	}

	@Test
	public void testAddItem() {
		// Arrange
		String productName = "Apple";
		float latestPrice = 0.10f;
		int quantity = 10;

		// Act
		shoppingBasket.addProduct(productName, latestPrice, quantity);
		shoppingBasket.getOrderItemByProductName(productName).addItem();

		// Assert
		OrderItem orderItem = shoppingBasket.getOrderItemByProductName(productName);

		assertEquals(productName, orderItem.getProductName());
		assertEquals(latestPrice, orderItem.getLatestPrice(), 0);
		assertEquals(quantity + 1, orderItem.getQuantity());
		assertEquals((quantity + 1) * latestPrice, orderItem.getTotalOrder(), 0);

		shoppingBasket.updateFields();

		assertEquals(1, shoppingBasket.getNumberOfProducts());
		assertEquals(quantity + 1, shoppingBasket.getNumberOfItems());
		assertEquals((quantity + 1) * latestPrice, shoppingBasket.getBasketTotal(), 0);
	}

	@Test
	public void testAddItems() {
		// Arrange
		String productName = "Apple";
		float latestPrice = 0.10f;
		int quantity = 10;
		int addQuantity = 5;

		// Act
		shoppingBasket.addProduct(productName, latestPrice, quantity);
		shoppingBasket.getOrderItemByProductName(productName).addItems(addQuantity);

		// Assert
		OrderItem orderItem = shoppingBasket.getOrderItemByProductName(productName);

		assertEquals(productName, orderItem.getProductName());
		assertEquals(latestPrice, orderItem.getLatestPrice(), 0);
		assertEquals(quantity + addQuantity, orderItem.getQuantity());
		assertEquals((quantity + addQuantity) * latestPrice, orderItem.getTotalOrder(), 0);

		shoppingBasket.updateFields();

		assertEquals(1, shoppingBasket.getNumberOfProducts());
		assertEquals(quantity + addQuantity, shoppingBasket.getNumberOfItems());
		assertEquals((quantity + addQuantity) * latestPrice, shoppingBasket.getBasketTotal(), 0);
	}

	@Test
	public void testRemoveProductByProductName() {
		// Arrange
		String productName = "Apple";
		float latestPrice = 0.10f;
		int quantity = 10;

		// Act
		shoppingBasket.addProduct(productName, latestPrice, quantity);
		shoppingBasket.removeProduct(productName);

		// Assert
		OrderItem orderItem = shoppingBasket.getOrderItemByProductName(productName);

		assertEquals(null, orderItem);

		assertEquals(0, shoppingBasket.getNumberOfProducts());
		assertEquals(0, shoppingBasket.getNumberOfItems());
		assertEquals(0.00f, shoppingBasket.getBasketTotal(), 0);
	}

	@Test
	public void testRemoveProductByQuantity() {
		// Arrange
		String productName = "Apple";
		float latestPrice = 0.10f;
		int quantity = 10;

		// Act
		shoppingBasket.addProduct(productName, latestPrice, quantity);
		shoppingBasket.removeProduct(productName, quantity);

		// Assert
		OrderItem orderItem = shoppingBasket.getOrderItemByProductName(productName);

		shoppingBasket.updateFields();

		assertEquals(null, orderItem);

		assertEquals(0, shoppingBasket.getNumberOfProducts());
		assertEquals(0, shoppingBasket.getNumberOfItems());
		assertEquals(0.00f, shoppingBasket.getBasketTotal(), 0);
	}

	@Test
	public void testRemoveItem() {
		// Arrange
		String productName = "Apple";
		float latestPrice = 0.10f;
		int quantity = 10;

		// Act
		shoppingBasket.addProduct(productName, latestPrice, quantity);
		shoppingBasket.getOrderItemByProductName(productName).removeItem();

		// Assert
		OrderItem orderItem = shoppingBasket.getOrderItemByProductName(productName);

		assertEquals(productName, orderItem.getProductName());
		assertEquals(latestPrice, orderItem.getLatestPrice(), 0);
		assertEquals(quantity - 1, orderItem.getQuantity());
		assertEquals((quantity - 1) * latestPrice, orderItem.getTotalOrder(), 0);

		shoppingBasket.updateFields();

		assertEquals(1, shoppingBasket.getNumberOfProducts());
		assertEquals(quantity - 1, shoppingBasket.getNumberOfItems());
		assertEquals((quantity - 1) * latestPrice, shoppingBasket.getBasketTotal(), 0);
	}

	@Test
	public void testRemoveItems() {
		// Arrange
		String productName = "Apple";
		float latestPrice = 0.10f;
		int quantity = 10;
		int removeQuantity = 5;

		// Act
		shoppingBasket.addProduct(productName, latestPrice, quantity);
		shoppingBasket.getOrderItemByProductName(productName).removeItems(removeQuantity);

		// Assert
		OrderItem orderItem = shoppingBasket.getOrderItemByProductName(productName);

		assertEquals(productName, orderItem.getProductName());
		assertEquals(latestPrice, orderItem.getLatestPrice(), 0);
		assertEquals(quantity - removeQuantity, orderItem.getQuantity());
		assertEquals((quantity - removeQuantity) * latestPrice, orderItem.getTotalOrder(), 0);

		shoppingBasket.updateFields();

		assertEquals(1, shoppingBasket.getNumberOfProducts());
		assertEquals(quantity - removeQuantity, shoppingBasket.getNumberOfItems());
		assertEquals((quantity - removeQuantity) * latestPrice, shoppingBasket.getBasketTotal(), 0);
	}

	@Test
	public void saveBasketValidFileName() {
		try {
			shoppingBasket.saveBasket("test");
		} catch (FileNotFoundException e) {
			fail();
		}
	}

	@Test
	public void saveBasketInvalidFileName() {
		try {
			shoppingBasket.saveBasket("/");
			fail();
		} catch (FileNotFoundException e) {
		}
	}

}
