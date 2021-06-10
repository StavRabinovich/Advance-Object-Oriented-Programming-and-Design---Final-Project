package views;

import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import model.Product;

public class Alerts {

	/* Basic Creation */
	private static void showAlert(AlertType alertType, String title, String header, String message) {
		Alert alert = new Alert(alertType);

		alert.setTitle(title);
		alert.setHeaderText(header);

		if (message != null && !message.isBlank()) {
			TextArea textArea = new TextArea(message);
			textArea.setEditable(false);
			alert.getDialogPane().setExpandableContent(new ScrollPane(textArea));
			alert.getDialogPane().setExpanded(true);
			alert.setResizable(false);
		}

		alert.showAndWait();
	}

	public static void showInformation(String message) {
		showAlert(AlertType.INFORMATION, "Info", message, "");
	}

	public static void showInformation(String header, String message) {
		showAlert(AlertType.INFORMATION, "Info", header, message);
	}

	public static void showSuccess(String message) {
		showAlert(AlertType.INFORMATION, "Success", message, "");
	}

	public static void showError(String message) {
		showAlert(AlertType.ERROR, "Error", message, "");
	}

	/* Specific Alerts - By Buttons order */
	public static void productAlert(Product p, String pID) { // Show product Alert
		showAlert(AlertType.INFORMATION, "Product Info", "Product Details", p.toString());
	}
	
	public static void allProductsAlert(Map<String, Product> pMap) { // All Products Alert
		if (pMap.isEmpty())
			showError("There are no products to present!");
		else {
			String productInfo = "";
			for (Entry<String, Product> entry : pMap.entrySet())
				productInfo += entry.getKey() + ": " + entry.getValue().toString() + "\n";
			showAlert(AlertType.INFORMATION, "All Products Information", "All Products Information:", productInfo);
		}
	}
}
