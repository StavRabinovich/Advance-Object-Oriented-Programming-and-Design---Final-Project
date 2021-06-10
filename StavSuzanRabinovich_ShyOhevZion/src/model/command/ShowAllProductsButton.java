package model.command;

import views.Alerts;
import views.MainView;
import views.View;

public class ShowAllProductsButton extends CommandButton {

	/* Constructors */
	public ShowAllProductsButton(View view) {
		super(view);
		this.setText("Present all products"); // Button's Text
	}
	
	/* Override Methods */
	@Override
	public void execute() {
		MainView mainV = (MainView) connectedView;
		mainV.clearAllFields();
		Alerts.allProductsAlert(mainV.getStore().getProductsMap()); // Presents all products.
	}
}
