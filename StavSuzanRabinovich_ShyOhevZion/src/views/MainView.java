package views;

import java.io.IOException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Store;
import model.Store.Snapshot;
import model.command.AddProductButton;
import model.command.ProductProfitButton;
import model.command.RemoveAllProductsButton;
import model.command.RemoveProductButton;
import model.command.ShowAllProductsButton;
import model.command.UpdateClientsButton;
import model.command.ShowProductButton;
import model.command.TotalProfitButton;
import model.command.UndoButton;

public class MainView extends View {

	/* Constants */
	public enum Feature { // Is used to access the corresponding fields
		/* Constants */
		PRODUCT_ID("Product ID:"), PRODUCT_NAME("Product name:"), PRODUCT_COST("Product cost price:"),
		PRODUCT_SELL("Product sell price:"), CLIENT_NUMBER("Client number:"), CLIENT_NAME("Client name:");

		/* Fields */
		private Label descriptionLabel;
		private Label errorLabel;

		/* Constructors */
		private Feature(String desc) {
			descriptionLabel = new Label(String.format("%-25s", desc));
			errorLabel = new Label();
			hideError();
		}

		/* Other Methods */
		public Label descriptionLabel() {
			return descriptionLabel;
		}

		public Label errorLabel() {
			return errorLabel;
		}

		private void showError(String errTxt) {
			errorLabel.setText(errTxt);
			errorLabel.setVisible(true);
		}

		private void hideError() {
			errorLabel.setVisible(false);
		}
	}

	/* Fields */
	private Scene scene;
	private BorderPane bpMain;
	private VBox productVBox;
	private Button btnAdd, btnUndo, btnRemove, btnRemoveAll, btnProduct, btnAllProducts, btnProfit, btnClients,
			btnSumProfit;
	private GridPane topPane, buttonsPane;
	private GridPane[] featureGrid;
	private TextField[] featureTextFields;
	private CheckBox getUpdate;
	private Label[] errLabels; // error's placement will be defined by label's order.
	private Store store;
	private Snapshot snap;

	/* Constructors */
	public MainView(Stage stage) {
		super(stage);
		try {
			store = new Store();
			buildScene();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Get, Set, Add & Remove */
	public String getFeatureInput(Feature f) {
		return featureTextFields[f.ordinal()].getText().toLowerCase().strip();
	}

	public boolean getUpdateValue() {
		return getUpdate.isSelected();
	}

	public Store getStore() {
		return store;
	}

	public Snapshot getStoreSnapshot() {
		return snap;
	}

	public void createStoreSnapshot() {
		snap = store.createSnapshot();
	}

	/* Override Methods */
	@Override
	protected void buildScene() { // Builds the scene
		productFeaturesInsertion(); // Inserts All features
		buttonsInsertion(); // Inserts Buttons

		// Creates Scene's BorderPane
		bpMain = new BorderPane(productVBox);
		bpMain.setBottom(buttonsPane);

		// BorderPane's Top
		topPane = new GridPane();
		gridProperties(topPane, 1);
		Reflection r = new Reflection();
		r.setFraction(0.5);
		Text title = new Text("Our Store");
		title.setFont(Font.font("Source Sans Pro", FontWeight.BOLD, 40));
		title.setFill(Color.TEAL);
		title.setEffect(r);
		topPane.add(title, 0, 0);
		bpMain.setTop(topPane);

		// Scene and stage setting
		scene = new Scene(bpMain, SizeRules.MAIN_WINDOW.width(), SizeRules.MAIN_WINDOW.height());
		stage.setScene(scene);
		disableAllFocus();
		stage.setTitle("Our Store");
		stage.getIcons().add(new Image("file:resources/cart-icon.png"));
		stage.setResizable(false);
		stage.show();
	}

	/* Other Methods */
	// Undo Methods
	public void enableUndo() { // Enables undo
		btnUndo.setDisable(false);
	}

	public void disableUndo() { // Disables undo
		btnUndo.setDisable(true);
	}

	// Error(s) Methods
	public void ShowFeatureError(Feature f, String errTxt) { // Presents feature's error
		f.showError(errTxt);
		featureTextFields[f.ordinal()].setEffect(new DropShadow(5, 3, 3, Color.web("#d01b1b")));
	}

	public void clearFeatureError(Feature f) { // Delete feature's error
		f.hideError();
		if (featureTextFields[f.ordinal()].getEffect() != null) {
			featureTextFields[f.ordinal()].setEffect(null);
		}
	}

	public void clearErrors() { // Delete all features's errors
		for (Feature f : Feature.values()) {
			clearFeatureError(f);
		}
	}

	// Other
	public void clearAllFields() { // Clear all fields
		for (TextField tf : featureTextFields)
			tf.clear();
		getUpdate.setSelected(false);
	}

	// Pane & Fields definitions
	private void gridProperties(GridPane gp, int i) {// All Gridpane's properties
		gp.setPadding(new Insets(i * i * 5));
		ColumnConstraints c = new ColumnConstraints();
		c.setPercentWidth(50);
		gp.getColumnConstraints().add(c);
		gp.setHgap(i * 10);
		gp.setVgap(i * 5);
		gp.setAlignment(Pos.CENTER);
	}

	private void insertButtonsToGrid(Button btn, int inOrder) {// Defines button and inserts to button's grid
		btn.setPrefSize(SizeRules.BUTTON.width(), SizeRules.BUTTON.height());
		buttonsPane.add(btn, inOrder / 6, inOrder % 6);
	}

	private void buildButtonsPaneFields() { // Buttons pane features
		// GridPane
		buttonsPane = new GridPane();
		gridProperties(buttonsPane, 2);

		// Buttons
		btnAdd = new AddProductButton(this);
		btnUndo = new UndoButton(this);
		btnRemove = new RemoveProductButton(this);
		btnRemoveAll = new RemoveAllProductsButton(this);
		btnProfit = new ProductProfitButton(this);
		btnSumProfit = new TotalProfitButton(this);
		btnAllProducts = new ShowAllProductsButton(this);
		btnClients = new UpdateClientsButton(this);
		btnProduct = new ShowProductButton(this);
	}

	// Pane's Insertions
	private void productFeaturesInsertion() { // Insert to product's features pane
		// GridPane VBox
		productVBox = new VBox(5);
		productVBox.setPadding(new Insets(10));

		getUpdate = new CheckBox("Send me updates!"); // Checkbox

		// Labels
		int len = Feature.values().length;
		featureGrid = new GridPane[len];
		featureTextFields = new TextField[len];
		errLabels = new Label[len];

		// FeatureGrids
		for (int i = 0; i < len; i++) {
			Feature currFeature = Feature.values()[i];
			featureGrid[i] = new GridPane(); // Each feature has his own GridPane
			errLabels[i] = currFeature.errorLabel(); // Features's error label
			featureTextFields[i] = new TextField();
			featureTextFields[i].setPrefSize(SizeRules.TEXT_FIELD.width(), SizeRules.TEXT_FIELD.height());
			featureGrid[i].addRow(0, currFeature.descriptionLabel(), currFeature.errorLabel);
			featureGrid[i].add(featureTextFields[i], 0, 1, 2, 1);
			productVBox.getChildren().add(featureGrid[i]);
		}
		featureGrid[5].add(getUpdate, 0, 2, 2, 1); // With client's grid
	}

	private void buttonsInsertion() {// Insert to buttons pane
		buildButtonsPaneFields();
		// Insert
		insertButtonsToGrid(btnAdd, 0);
		insertButtonsToGrid(btnUndo, 1);
		insertButtonsToGrid(btnRemove, 2);
		insertButtonsToGrid(btnRemoveAll, 3);
		insertButtonsToGrid(btnProfit, 4);
		insertButtonsToGrid(btnSumProfit, 5);
		insertButtonsToGrid(btnAllProducts, 6);
		insertButtonsToGrid(btnClients, 7);
		insertButtonsToGrid(btnProduct, 8);
	}

}
