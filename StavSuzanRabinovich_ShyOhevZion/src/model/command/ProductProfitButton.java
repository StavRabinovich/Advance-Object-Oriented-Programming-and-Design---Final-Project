package model.command;

import model.Product;
import views.Alerts;
import views.MainView;
import views.MainView.Feature;
import views.View;

public class ProductProfitButton extends CommandButton {

	/* Constructors */
	public ProductProfitButton(View view) {
		super(view);
		this.setText("Show Product Profit"); // Button's Text
	}

	/* Override Methods */
	@Override
	public void execute() {
		MainView mainV = (MainView) connectedView;
		String pID = mainV.getFeatureInput(Feature.PRODUCT_ID);
		Product p = mainV.getStore().searchProduct(pID); // Search for product
		if (p == null)
			Alerts.showError("Product Not Found!");
		else
			Alerts.showInformation(String.format("Profit of Product ID '%s': %d", pID, p.calculateProfit()));
		mainV.clearAllFields(); // Clear all fields
	}
}
