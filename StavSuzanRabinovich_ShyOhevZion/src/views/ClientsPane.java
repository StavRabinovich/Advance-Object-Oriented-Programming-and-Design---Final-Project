package views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.SaleNotifier;

public class ClientsPane extends View { /* Will Present all Clients that Receives Updates */

	/* Fields */
	private Scene scene;
	private BorderPane bPane;
	private TextArea clientsNames;
	private Button btnExit;

	/* Constructors */
	public ClientsPane(Stage stage) {
		super(stage);
		try {
			buildScene();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Override Methods */
	@Override
	protected void buildScene() {
		clientsNames = new TextArea("no clients to update!"); // Always empty clients list at first
		clientsNames.setEditable(false);
		bPane = new BorderPane(clientsNames);

		// Button
		btnExit = new Button("Exit");
		btnExit.setPrefSize(SizeRules.BUTTON.width(), SizeRules.BUTTON.height());
		btnExit.setDisable(true);
		btnExit.setOnAction(e -> this.close());
		VBox vbxButton = new VBox(20, btnExit);
		vbxButton.setAlignment(Pos.CENTER);
		vbxButton.setPadding(new Insets(20));
		bPane.setBottom(vbxButton);

		// Thread
		insertClientsToPane(); // Pane's thread

		// Show
		scene = new Scene(bPane, 500, 300);
		stage.setTitle("Clients");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.showAndWait();
	}

	/* Other Methods */
	private void insertClientsToPane() { // Pane's thread
		Thread t = new Thread(() -> {
			try {
				SaleNotifier notifier = SaleNotifier.instance(); // Singleton of sale notifier
				notifier.sendMSGToAll(); // Notifies all observers (Clients that wants updates)
				if (notifier.hasClientsToNotify()) { // Hasn't finished
					clientsNames.setText("Clients who got the update:\n");
					for (String reply : notifier.getReplies()) {
						Thread.sleep(2000); // Wait 2 seconds
						Platform.runLater(() -> { // Append client's reply (name) to the textArea
							clientsNames.appendText("-\t" + reply + "\n");
						});
					}
				}
				btnExit.setDisable(false);
			} catch (InterruptedException e1) {
			}
		});
		t.start(); // Start thread
	}

}
