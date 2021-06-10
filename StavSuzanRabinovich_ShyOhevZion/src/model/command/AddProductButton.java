package model.command;

import model.Client;
import model.Product;
import model.SaleNotifier;
import model.Store;
import views.Alerts;
import views.MainView;
import views.MainView.Feature;
import views.View;

public class AddProductButton extends CommandButton {

	public AddProductButton(View view) {
		super(view);
		this.setText("Add / Update");
	}

	@Override
	public void execute() {

		boolean validProduct = true, validClient = true;

		MainView mainV = (MainView) connectedView;
		String pID = mainV.getFeatureInput(Feature.PRODUCT_ID);
		String pName = mainV.getFeatureInput(Feature.PRODUCT_NAME);
		String pCost = mainV.getFeatureInput(Feature.PRODUCT_COST);
		String pSell = mainV.getFeatureInput(Feature.PRODUCT_SELL);
		String cName = mainV.getFeatureInput(Feature.CLIENT_NAME);
		String cNumber = mainV.getFeatureInput(Feature.CLIENT_NUMBER);

		if (pID.isBlank()) {
			mainV.clearErrors();
			mainV.ShowFeatureError(Feature.PRODUCT_ID, "Product's ID can't be empty");
		} else {
			Product p = new Product("", 0, 0);
			mainV.clearFeatureError(Feature.PRODUCT_ID);
			// name check
			if (pName.isBlank()) {
				p.setName("NO PRODUCT NAME");
				mainV.clearFeatureError(Feature.PRODUCT_NAME);
			} else if (pName.matches("^[a-z][a-z ]*$")) { // letters only
				p.setName(pName);
				mainV.clearFeatureError(Feature.PRODUCT_NAME);
			} else {
				mainV.ShowFeatureError(Feature.PRODUCT_NAME, "Only alphabet characters are allowed");
				validProduct = false;
			}

			// cost check
			if (pCost.isBlank())
				mainV.clearFeatureError(Feature.PRODUCT_COST);

			else if (pCost.matches("^-?[0-9]+$")) // numbers only
			{
				if (Integer.parseInt(pCost) >= 0) {
					p.setCost(Integer.parseInt(pCost));
					mainV.clearFeatureError(Feature.PRODUCT_COST);
				} else {
					mainV.ShowFeatureError(Feature.PRODUCT_COST, "Product's cost can't be negative");
					validProduct = false;
				}
			} else {
				mainV.ShowFeatureError(Feature.PRODUCT_COST, "Product's Cost must be a number");
				validProduct = false;
			}

			// sell check
			if (pSell.isBlank())
				mainV.clearFeatureError(Feature.PRODUCT_SELL);

			else if (pSell.matches("^-?[0-9]+$")) // numbers only
			{
				if (Integer.parseInt(pSell) >= 0) {
					p.setSell(Integer.parseInt(pSell));
					if (p.calculateProfit() > 0)
						mainV.clearFeatureError(Feature.PRODUCT_SELL);
					else {
						mainV.ShowFeatureError(Feature.PRODUCT_COST, "Product's profit can't be negative");
						mainV.ShowFeatureError(Feature.PRODUCT_SELL, "Product's profit can't be negative");
						validProduct = false;
					}
				} else {
					mainV.ShowFeatureError(Feature.PRODUCT_SELL, "Product's sell price can't be negative");
					validProduct = false;
				}
			} else {
				mainV.ShowFeatureError(Feature.PRODUCT_SELL, "Product's sell price must be a number");
				validProduct = false;
			}

			p.setClient(new Client());

			if (cNumber.isBlank())
				mainV.clearFeatureError(Feature.CLIENT_NUMBER);
			else {
				if (cNumber.matches("^05[0-9]{8}$")) {
					p.getClient().setNumber(cNumber);				// Number is valid..
					p.getClient().setUpdate(mainV.getUpdateValue());// ..so we can update the client (or not)
					mainV.clearFeatureError(Feature.CLIENT_NUMBER);
				} else {
					if (cNumber.matches("^[0-9]+$")) // numbers only
						mainV.ShowFeatureError(Feature.CLIENT_NUMBER, "Client's Number must have valid format");
					else
						mainV.ShowFeatureError(Feature.CLIENT_NUMBER, "Only numeric characters");
					validClient = false;
				}
			}

			if (cName.isBlank())
				mainV.clearFeatureError(Feature.CLIENT_NAME);

			else {
				if (cName.matches("^[a-z][a-z ]*$")) { // i.e. john doe
					mainV.clearFeatureError(Feature.CLIENT_NAME);
					if (p.getClient() != null) {
						p.getClient().setName(cName);
					}
				} else {
					mainV.ShowFeatureError(Feature.CLIENT_NAME, "Only alphabet characters");
					validClient = false;
				}
			}

			if (validProduct && validClient) {
				mainV.clearErrors();
				mainV.clearAllFields();
				Store store = ((MainView) mainV).getStore();

				if (store.searchProduct(pID) != null) {
					mainV.disableUndo();
					Alerts.showSuccess("Product was replaced!");
				} else {
					mainV.createStoreSnapshot();
					mainV.enableUndo();
					Alerts.showSuccess("Product was added to store!");
				}
				store.addProduct(pID, p);
				SaleNotifier.instance().addClientToSaleList(p.getClient());
			}
		}
	}
}
