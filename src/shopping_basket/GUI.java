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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.eteks.sweethome3d.swing.NullableSpinner;
import com.eteks.sweethome3d.swing.NullableSpinner.NullableSpinnerNumberModel;

public class GUI {

	ShoppingBasket shoppingBasket = new ShoppingBasket();

	boolean controlPressed;

	final static Font STANDARD_FONT = new Font("Lucida Grande", Font.PLAIN, 13);
	final static Font MONOSPACED_FONT = new Font("Andale Mono", Font.PLAIN, 13);

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
		DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		list.setBounds(12, 84, 432, 272);
		list.setFont(MONOSPACED_FONT);
		frame.getContentPane().add(list);

		JLabel labelProductName = new JLabel("Product Name");
		labelProductName.setBounds(12, 8, 88, 16);
		labelProductName.setFont(STANDARD_FONT);
		frame.getContentPane().add(labelProductName);

		JLabel labelLatestPrice = new JLabel("Latest Price");
		labelLatestPrice.setBounds(228, 8, 72, 16);
		labelLatestPrice.setFont(STANDARD_FONT);
		frame.getContentPane().add(labelLatestPrice);

		JLabel labelQuantity = new JLabel("Quantity");
		labelQuantity.setBounds(342, 8, 54, 16);
		labelQuantity.setFont(STANDARD_FONT);
		frame.getContentPane().add(labelQuantity);

		JLabel labelBasket = new JLabel("Basket");
		labelBasket.setBounds(12, 64, 41, 16);
		labelBasket.setFont(STANDARD_FONT);
		frame.getContentPane().add(labelBasket);

		JLabel labelNoItems = new JLabel("No. Items");
		labelNoItems.setBounds(12, 368, 61, 16);
		labelNoItems.setFont(STANDARD_FONT);
		frame.getContentPane().add(labelNoItems);

		JLabel labelTotal = new JLabel("Total");
		labelTotal.setBounds(307, 368, 32, 16);
		labelTotal.setFont(STANDARD_FONT);
		frame.getContentPane().add(labelTotal);

		JTextField fieldProductName = new JTextField();
		fieldProductName.setBounds(9, 28, 210, 24);
		fieldProductName.setFont(STANDARD_FONT);
		frame.getContentPane().add(fieldProductName);

		JTextField fieldLatestPrice = new JTextField();
		fieldLatestPrice.setBounds(225, 28, 108, 24);
		fieldLatestPrice.setFont(STANDARD_FONT);
		frame.getContentPane().add(fieldLatestPrice);

		NullableSpinnerNumberModel fieldQuantityNumberModel = new NullableSpinnerNumberModel(0, 0, 0, 1);
		fieldQuantityNumberModel.setMaximum(null);
		NullableSpinner fieldQuantity = new NullableSpinner(fieldQuantityNumberModel);
		fieldQuantity.setBounds(339, 28, 106, 24);
		fieldQuantity.setFont(STANDARD_FONT);
		frame.getContentPane().add(fieldQuantity);

		fieldNoItems = new JTextField();
		fieldNoItems.setBounds(77, 364, 104, 24);
		fieldNoItems.setEditable(false);
		fieldNoItems.setFont(STANDARD_FONT);
		frame.getContentPane().add(fieldNoItems);

		fieldTotal = new JTextField();
		fieldTotal.setBounds(343, 364, 104, 24);
		fieldTotal.setEditable(false);
		fieldTotal.setFont(STANDARD_FONT);
		frame.getContentPane().add(fieldTotal);

		JButton buttonAdd = new JButton("Add");
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
				String productName = fieldProductName.getText().trim();
				float latestPrice;
				try {
					latestPrice = Float.parseFloat(fieldLatestPrice.getText());
				} catch (NumberFormatException nFE) {
					JOptionPane.showMessageDialog(frame, String.format("Latest Price error: %s", nFE.getMessage()));
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
		buttonAdd.setFont(STANDARD_FONT);
		frame.getContentPane().add(buttonAdd);

		JButton buttonRemove = new JButton("Remove");
		buttonRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
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
		buttonRemove.setFont(STANDARD_FONT);
		frame.getContentPane().add(buttonRemove);

		JButton buttonEdit = new JButton("Edit");
		buttonEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
				List<String> selectedValues = list.getSelectedValuesList();
				if (selectedValues.isEmpty()) {
					JOptionPane.showMessageDialog(frame, "Please select a product from the Basket.");
				} else if (selectedValues.size() == 1) {
					OrderItem orderItem = shoppingBasket.getOrderItemByProductName(selectedValues.get(0).split(" ")[0]);
					JLabel labelEditLatestPrice = new JLabel("Latest Price");
					labelEditLatestPrice.setFont(STANDARD_FONT);
					JTextField fieldEditLatestPrice = new JTextField(String.format("%.2f", orderItem.getLatestPrice()));
					fieldEditLatestPrice.setFont(STANDARD_FONT);
					JLabel labelEditQuantity = new JLabel("Quantity");
					labelEditQuantity.setFont(STANDARD_FONT);
					NullableSpinnerNumberModel fieldEditQuantityNumberModel = new NullableSpinnerNumberModel(0, 0, 0,
							1);
					fieldEditQuantityNumberModel.setValue(orderItem.getQuantity());
					fieldEditQuantityNumberModel.setMaximum(null);
					NullableSpinner fieldEditQuantity = new NullableSpinner(fieldEditQuantityNumberModel);
					fieldEditQuantity.setFont(STANDARD_FONT);
					if (JOptionPane.showOptionDialog(frame,
							new JComponent[] { labelEditLatestPrice, fieldEditLatestPrice, labelEditQuantity,
									fieldEditQuantity },
							orderItem.getProductName(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
							null, new String[] { "Save", "Cancel" }, "default") == JOptionPane.OK_OPTION) {
						try {
							orderItem.setLatestPrice(Float.parseFloat(fieldEditLatestPrice.getText()));
						} catch (NumberFormatException nFE) {
							JOptionPane.showMessageDialog(frame,
									String.format("Latest Price error: %s", nFE.getMessage()));
							actionPerformed(aE);
							return;
						}
						orderItem.setQuantity((int) fieldEditQuantity.getValue());
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
		buttonEdit.setFont(STANDARD_FONT);
		frame.getContentPane().add(buttonEdit);

		JButton buttonClearBasket = new JButton("Clear Basket");
		buttonClearBasket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				shoppingBasket.clearBasket();
				updateGUI();
			}
		});
		buttonClearBasket.setBounds(450, 164, 104, 24);
		buttonClearBasket.setFont(STANDARD_FONT);
		frame.getContentPane().add(buttonClearBasket);

		JButton buttonSave = new JButton("Save");
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
				JLabel labelSpecify = new JLabel("Please specify a file name:");
				labelSpecify.setFont(STANDARD_FONT);
				JTextField fieldFileName = new JTextField();
				fieldFileName.setFont(STANDARD_FONT);
				JLabel labelExtension = new JLabel(".txt");
				labelExtension.setFont(STANDARD_FONT);
				if (JOptionPane.showOptionDialog(frame,
						new JComponent[] { labelSpecify, fieldFileName, labelExtension }, "Save",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
						new String[] { "Save", "Cancel" }, "default") == JOptionPane.OK_OPTION) {
					try {
						shoppingBasket.saveBasket(fieldFileName.getText());
						JOptionPane.showMessageDialog(frame, "Receipt successfully saved.");
					} catch (FileNotFoundException fNFE) {
						JOptionPane.showMessageDialog(frame,
								String.format("Receipt writing failed. %s", fNFE.getMessage()), "Save Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		buttonSave.setBounds(450, 204, 104, 24);
		buttonSave.setFont(STANDARD_FONT);
		frame.getContentPane().add(buttonSave);

		JButton buttonExit = new JButton("Exit");
		buttonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent aE) {
				System.exit(0);
			}
		});
		buttonExit.setBounds(450, 364, 104, 24);
		buttonExit.setFont(STANDARD_FONT);
		frame.getContentPane().add(buttonExit);

		JButton[] buttons = { buttonAdd, buttonRemove, buttonEdit, buttonClearBasket, buttonSave, buttonExit };

		Image image = null;
		int imageSize = 40;
		try {
			image = ImageIO.read(new File("img/Information.svg.png"));
		} catch (IOException iOE) {
			iOE.printStackTrace();
		}
		JLabel label = new JLabel(null,
				new ImageIcon(image.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH)), JLabel.CENTER);
		label.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent mE) {
				List<JLabel> messageAL = new ArrayList<JLabel>();
				JLabel labelKeys = new JLabel("Shortcut keys:");
				labelKeys.setFont(STANDARD_FONT);
				messageAL.add(labelKeys);
				for (JButton button : buttons) {
					String buttonText = button.getText();
					JLabel label = new JLabel(String.format("Ctrl-%-5s %s",
							buttonText.equals("Exit") ? "Esc" : buttonText.substring(0, 1), buttonText));
					label.setFont(MONOSPACED_FONT);
					messageAL.add(label);
				}
				JOptionPane.showMessageDialog(frame, messageAL.toArray(new JLabel[messageAL.size()]), "Info",
						JOptionPane.INFORMATION_MESSAGE);
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
