package model.command;

import model.Product;
import model.Store;
import views.Alerts;
import views.MainView;
import views.View;
import views.MainView.Feature;

public class ShowProductButton extends CommandButton {
	
	/* Fields */
	private Store s;
	
	/* Constructors */
	public ShowProductButton(View view) {
		super(view);
		s = ((MainView) view).getStore();
		this.setText("Present product by ID"); // Button's Text
	}

	/* Override Methods */
	@Override
	public void execute() {
		MainView mainV = (MainView) connectedView;
		String pNumber = mainV.getFeatureInput(Feature.PRODUCT_ID);
		if (pNumber.isBlank()) {
			mainV.ShowFeatureError(Feature.PRODUCT_ID, "Product's ID can't be empty");
		} else {
			mainV.clearErrors();
			mainV.clearAllFields();
			Product p = s.getProductsMap().get(pNumber); // get product
			if (p == null)
				Alerts.showError("Product not found!");
			else
				Alerts.productAlert(p, pNumber);
		}
		mainV.clearAllFields();
	}
}
