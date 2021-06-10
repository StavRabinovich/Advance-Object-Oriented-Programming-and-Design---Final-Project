package views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.command.Command;

/* An abstract class containing integral components for derivative views */
public abstract class View {

	/* Fields */
	protected Stage stage;
	public final EventHandler<ActionEvent> buttonPressed = e -> ((Command) e.getSource()).execute();

	/* Constructors */
	public View() {
		setStage(new Stage());
	}

	public View(Stage stage) {
		setStage(stage);
	}

	/* Get, Set, Add & Remove */
	public Stage getStage() {
		return stage;
	}

	private void setStage(Stage stage) {
		this.stage = stage;
	}

	/* Other Methods */
	protected abstract void buildScene();

	public final void close() {
		stage.close();
	}

	protected final void centerStageInScreen() {
		if (stage.isShowing()) {
			stage.setX((Screen.getPrimary().getVisualBounds().getWidth() - stage.getWidth()) / 2);
			stage.setY((Screen.getPrimary().getVisualBounds().getHeight() - stage.getHeight()) / 2);
		}
	}

	public void disableAllFocus() {
		if (stage.getScene() != null)
			disableFocus(stage.getScene().getRoot());
	}

	private void disableFocus(Node node) {
		if (node != null) {
			node.setFocusTraversable(false);
			if (node instanceof Parent)
				for (Node n : ((Parent) node).getChildrenUnmodifiable())
					disableFocus(n);
		}
	}
}