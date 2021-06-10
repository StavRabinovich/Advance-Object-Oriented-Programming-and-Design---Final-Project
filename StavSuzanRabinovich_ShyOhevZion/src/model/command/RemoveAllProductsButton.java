package model.command;

import model.Store;
import views.Alerts;
import views.MainView;
import views.View;

public class RemoveAllProductsButton extends CommandButton {

	/* Constructors */
	public RemoveAllProductsButton(View view) {
		super(view);
		this.setText("Remove All Products"); // Button's Text
	}

	/* Override Methods */
	@Override
	public void execute() {
		MainView mainV = (MainView) connectedView;
		Store store = mainV.getStore();
		mainV.clearErrors();
		mainV.disableUndo();
		store.removeAllProducts(); // Removes all products
		Alerts.showSuccess("All Products removed!");
	}
}