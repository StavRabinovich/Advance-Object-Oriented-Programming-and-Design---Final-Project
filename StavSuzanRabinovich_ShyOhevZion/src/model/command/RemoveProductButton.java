package model.command;

import model.Product;
import model.SaleNotifier;
import model.Store;
import views.Alerts;
import views.MainView;
import views.View;
import views.MainView.Feature;

public class RemoveProductButton extends CommandButton {

	/* Constructors */
	public RemoveProductButton(View view) {
		super(view);
		this.setText("Remove Product By ID"); // Button's Text
	}

	/* Override Methods */
	@Override
	public void execute() {
		MainView mainV = (MainView) connectedView;
		Store store = ((MainView) mainV).getStore();
		String pID = mainV.getFeatureInput(Feature.PRODUCT_ID);
		if (pID.isBlank()) { // No product ID
			mainV.clearErrors();
			mainV.ShowFeatureError(Feature.PRODUCT_ID, "Product's ID Can't be empty");
		} else {
			mainV.clearFeatureError(Feature.PRODUCT_ID);
			Product p = store.searchProduct(pID);
			if (p != null) { // Product exists
				store.removeProduct(pID);
				SaleNotifier.instance().removeClient(p.getClient());
				mainV.disableUndo();
				Alerts.showSuccess("Product removed!");
			} else
				Alerts.showError("Product not found!");
		}
	}
}