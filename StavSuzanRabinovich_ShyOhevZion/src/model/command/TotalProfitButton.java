package model.command;

import views.Alerts;
import views.MainView;
import views.View;

public class TotalProfitButton extends CommandButton {

	/* Constructors */
	public TotalProfitButton(View view) {
		super(view);
		this.setText("Show Store Profit"); // Button's Text
	}

	/* Override Methods */
	@Override
	public void execute() {
		MainView mainV = (MainView) connectedView; // Connect to our main view
		if (mainV.getStore().getProductsMap().isEmpty()) // No products
			Alerts.showError("The store has no products!");
		else
			Alerts.showInformation("Store's total profit: " + mainV.getStore().getStoreProfit());
		mainV.clearAllFields();
	}
}
