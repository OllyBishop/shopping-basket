package shopping_basket;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.eteks.sweethome3d.swing.NullableSpinner;
import com.eteks.sweethome3d.swing.NullableSpinner.NullableSpinnerNumberModel;

public class GUI {
	
	// Testing 1

	ShoppingBasket shoppingBasket = new ShoppingBasket();

	boolean controlPressed;

	Font monospacedFont = new Font("Andale Mono", Font.PLAIN, 13);

	JFrame frame;

	DefaultListModel<String> model;

	JTextField fieldNoItems;
	JTextField fieldTotal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
		updateGUI();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 560, 420);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Shopping Basket");
		frame.getContentPane().setLayout(null);

		model = new DefaultListModel<String>();
		JList<String> list = new JList<String>(model);
		list.setBounds(12, 84, 432, 272);
		list.setFont(monospacedFont);
		frame.getContentPane().add(list);

		JLabel labelProductName = new JLabel("Product Name");
		labelProductName.setBounds(12, 8, 88, 16);
		frame.getContentPane().add(labelProductName);

		JLabel labelLatestPrice = new JLabel("Latest Price");
		labelLatestPrice.setBounds(232, 8, 72, 16);
		frame.getContentPane().add(labelLatestPrice);

		JLabel labelQuantity = new JLabel("Quantity");
		labelQuantity.setBounds(340, 8, 54, 16);
		frame.getContentPane().add(labelQuantity);

		JLabel labelBasket = new JLabel("Basket");
		labelBasket.setBounds(12, 64, 41, 16);
		frame.getContentPane().add(labelBasket);

		JLabel labelNoItems = new JLabel("No. Items");
		labelNoItems.setBounds(12, 368, 61, 16);
		frame.getContentPane().add(labelNoItems);

		JLabel labelTotal = new JLabel("Total");
		labelTotal.setBounds(304, 368, 32, 16);
		frame.getContentPane().add(labelTotal);

		JTextField fieldProductName = new JTextField();
		fieldProductName.setBounds(12, 28, 216, 24);
		frame.getContentPane().add(fieldProductName);

		JTextField fieldLatestPrice = new JTextField();
		fieldLatestPrice.setBounds(232, 28, 104, 24);
		frame.getContentPane().add(fieldLatestPrice);

		NullableSpinnerNumberModel fieldQuantityNumberModel = new NullableSpinnerNumberModel(0, 0, 0, 1);
		fieldQuantityNumberModel.setMaximum(null);
		NullableSpinner fieldQuantity = new NullableSpinner(fieldQuantityNumberModel);
		fieldQuantity.setBounds(340, 28, 104, 24);
		frame.getContentPane().add(fieldQuantity);

		fieldNoItems = new JTextField();
		fieldNoItems.setBounds(77, 364, 104, 24);
		fieldNoItems.setEditable(false);
		frame.getContentPane().add(fieldNoItems);

		fieldTotal = new JTextField();
		fieldTotal.setBounds(340, 364, 104, 24);
		fieldTotal.setEditable(false);
		frame.getContentPane().add(fieldTotal);

		JButton buttonAdd = new JButton("Add");
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
				String productName = fieldProductName.getText().trim();
				float latestPrice;
				try {
					latestPrice = Float.parseFloat(fieldLatestPrice.getText().trim());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame, String.format("Latest Price error: %s", e.getMessage()));
					return;
				}
				int quantity = (int) fieldQuantity.getValue();
				boolean validProductName = !productName.equals("");
				boolean validLatestPrice = 0 < latestPrice;
				boolean validQuantity = 0 < quantity;
				if (validProductName && validLatestPrice && validQuantity) {
					if (quantity == 1) {
						shoppingBasket.addProduct(productName, latestPrice);
					} else {
						shoppingBasket.addProduct(productName, latestPrice, quantity);
					}
					updateGUI();
				} else {
					JOptionPane.showMessageDialog(frame,
							!validProductName ? "Please enter a Product Name."
									: !validLatestPrice ? "Please enter a Latest Price greater than zero."
											: !validQuantity ? "Please enter a Quantity greater than zero." : null);
				}
			}
		});
		buttonAdd.setBounds(450, 28, 104, 24);
		frame.getContentPane().add(buttonAdd);

		JButton buttonRemove = new JButton("Remove");
		buttonRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> selectedValues = list.getSelectedValuesList();
				String productName = fieldProductName.getText().trim();
				if (!selectedValues.isEmpty()) {
					for (String selectedValue : selectedValues) {
						shoppingBasket.removeProduct(selectedValue.split(" ")[0]);
					}
					updateGUI();
				} else if (!productName.equals("")) {
					if (shoppingBasket.productIsInBasket(productName)) {
						shoppingBasket.removeProduct(productName, (int) fieldQuantity.getValue());
						updateGUI();
					} else {
						JOptionPane.showMessageDialog(frame,
								String.format("No product with name \"%s\" exists in the Basket.", productName));
					}
				} else {
					JOptionPane.showMessageDialog(frame,
							"Please enter a Product Name or select a product/products from the Basket.");
				}
			}
		});
		buttonRemove.setBounds(450, 84, 104, 24);
		frame.getContentPane().add(buttonRemove);

		JButton buttonEdit = new JButton("Edit");
		buttonEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
				List<String> selectedValues = list.getSelectedValuesList();
				if (selectedValues.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "Please select a product from the Basket.");
				} else if (selectedValues.size() == 1) {
					OrderItem orderItem = shoppingBasket.getOrderItemByProductName(selectedValues.get(0).split(" ")[0]);
					JTextField fieldLatestPrice = new JTextField(String.format("%.2f", orderItem.getLatestPrice()));
					NullableSpinnerNumberModel fieldQuantityNumberModel = new NullableSpinnerNumberModel(0, 0, 0, 1);
					fieldQuantityNumberModel.setMaximum(null);
					NullableSpinner fieldQuantity = new NullableSpinner(fieldQuantityNumberModel);
					fieldQuantity.setValue(orderItem.getQuantity());
					if (JOptionPane.showOptionDialog(frame,
							new Object[] { "Latest Price", fieldLatestPrice, "Quantity", fieldQuantity },
							orderItem.getProductName(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
							null, new String[] { "Save", "Cancel" }, "default") == JOptionPane.OK_OPTION) {
						try {
							orderItem.setLatestPrice(Float.parseFloat(fieldLatestPrice.getText().trim()));
						} catch (Exception e) {
							JOptionPane.showMessageDialog(frame,
									String.format("Latest Price error: %s", e.getMessage()));
							actionPerformed(aE);
							return;
						}
						orderItem.setQuantity((int) fieldQuantity.getValue());
						if (orderItem.getQuantity() == 0) {
							shoppingBasket.removeProduct(orderItem);
						}
						shoppingBasket.updateFields();
						updateGUI();
					}
				} else {
					JOptionPane.showMessageDialog(frame, "You may only edit one product at a time.");
				}

			}
		});
		buttonEdit.setBounds(450, 124, 104, 24);
		frame.getContentPane().add(buttonEdit);

		JButton buttonClearBasket = new JButton("Clear Basket");
		buttonClearBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shoppingBasket.clearBasket();
				updateGUI();
			}
		});
		buttonClearBasket.setBounds(450, 164, 104, 24);
		frame.getContentPane().add(buttonClearBasket);

		JButton buttonSave = new JButton("Save");
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
				JTextField fieldFileName = new JTextField();
				if (JOptionPane.showOptionDialog(frame,
						new Object[] { "Please specify a file name:", fieldFileName, ".txt" }, "Save",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
						new String[] { "Save", "Cancel" }, "default") == JOptionPane.OK_OPTION) {
					try {
						shoppingBasket.saveBasket(fieldFileName.getText());
						JOptionPane.showMessageDialog(frame, "Receipt successfully saved.");
					} catch (Exception e) {
						JOptionPane.showMessageDialog(frame, String.format("Receipt writing failed.%n%s: %s",
								e.getClass().getName(), e.getMessage()), "Save Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		buttonSave.setBounds(450, 204, 104, 24);
		frame.getContentPane().add(buttonSave);

		JButton buttonExit = new JButton("Exit");
		buttonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
				System.exit(0);
			}
		});
		buttonExit.setBounds(450, 364, 104, 24);
		frame.getContentPane().add(buttonExit);

		Image image = null;
		int imageSize = 40;
		try {
			image = ImageIO.read(new File("img/Information.svg.png"));
		} catch (IOException iOE) {
			iOE.printStackTrace();
		}
		JLabel label = new JLabel("", new ImageIcon(image.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH)),
				JLabel.CENTER);
		label.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mE) {
				JLabel[] message = { new JLabel("Shortcut keys:"), new JLabel("Ctrl-A     Add"),
						new JLabel("Ctrl-R     Remove"), new JLabel("Ctrl-E     Edit"),
						new JLabel("Ctrl-C     Clear Basket"), new JLabel("Ctrl-S     Save"),
						new JLabel("Ctrl-Esc   Exit") };
				for (JLabel label : message) {
					if (label.getText().startsWith("Ctrl-")) {
						label.setFont(monospacedFont);
					}
				}
				JOptionPane.showMessageDialog(frame, message, "Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(label, BorderLayout.CENTER);
		int buttonSaveLowerY = buttonSave.getY() + buttonSave.getHeight();
		int buttonExitUpperY = buttonExit.getY();
		panel.setBounds(buttonSave.getX() + (buttonSave.getWidth() / 2) - (imageSize / 2),
				buttonSaveLowerY + ((buttonExitUpperY - buttonSaveLowerY) / 2) - (imageSize / 2), imageSize, imageSize);
		frame.getContentPane().add(panel);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent kE) {
				if (kE.getKeyCode() == KeyEvent.VK_CONTROL) {
					controlPressed = kE.getID() == KeyEvent.KEY_PRESSED;
				}
				if (controlPressed && frame.isActive()) {
					if (kE.getID() == KeyEvent.KEY_PRESSED) {
						switch (kE.getKeyCode()) {
						case KeyEvent.VK_A:
							try {
								fieldQuantity.commitEdit();
								fieldQuantity.requestFocusInWindow();
							} catch (ParseException pE) {
								JOptionPane.showMessageDialog(frame, "Invalid quantity.");
								break;
							}
							buttonAdd.doClick();
							break;
						case KeyEvent.VK_R:
							try {
								fieldQuantity.commitEdit();
								fieldQuantity.requestFocusInWindow();
							} catch (ParseException pE) {
								JOptionPane.showMessageDialog(frame, "Invalid quantity.");
								break;
							}
							buttonRemove.doClick();
							break;
						case KeyEvent.VK_E:
							buttonEdit.doClick();
							break;
						case KeyEvent.VK_C:
							buttonClearBasket.doClick();
							break;
						case KeyEvent.VK_S:
							buttonSave.doClick();
							break;
						case KeyEvent.VK_ESCAPE:
							buttonExit.doClick();
							break;
						}
					}
				}
				return controlPressed;
			}
		});

	}

	public void updateGUI() {
		model.removeAllElements();
		for (OrderItem orderItem : shoppingBasket.orderItems) {
			model.addElement(shoppingBasket.format(orderItem));
		}
		fieldNoItems.setText(Integer.toString(shoppingBasket.getNumberOfItems()));
		fieldTotal.setText(String.format("£%.2f", shoppingBasket.getBasketTotal()));
	}

}
