package model.command;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import views.SizeRules;
import views.View;

public abstract class CommandButton extends Button implements Command {

	/* Fields */
	View connectedView;

	/* Constructors */
	public CommandButton(View view) {
		connectedView = view;
		this.setOnAction(connectedView.buttonPressed);
		this.setPrefSize(SizeRules.BUTTON.width(), SizeRules.BUTTON.height());

		this.setOnMouseEntered(entered -> this.getScene().setCursor(Cursor.HAND));
		this.setOnMouseExited(entered -> this.getScene().setCursor(Cursor.DEFAULT));
	}

	/* Override Methods */
	@Override
	public abstract void execute();
}
