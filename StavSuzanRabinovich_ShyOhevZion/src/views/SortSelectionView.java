package views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Store;
import model.command.SelectSortButton;

public class SortSelectionView extends View {

	/* Fields */
	private Button btnAscending, btnDescending, btnNotSorted;

	/* Constructors */
	public SortSelectionView(Stage theStage) {
		super(theStage);
		buildScene();
	}

	/* Override Methods */
	@Override
	protected void buildScene() {
		// Buttons
		btnAscending = new SelectSortButton(this, Store.SortOrder.ASCENDING);
		btnDescending = new SelectSortButton(this, Store.SortOrder.DESCENDING);
		btnNotSorted = new SelectSortButton(this, Store.SortOrder.NOT_SORTED);
		VBox vbox = new VBox(20);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(new Label("Please select preferred sort order:"), btnAscending, btnDescending,
				btnNotSorted);
		// Show
		Scene scene = new Scene(vbox, 260, 200);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.show();

		disableAllFocus();
		centerStageInScreen();
	}
}
