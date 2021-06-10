package model.command;

import views.Alerts;
import views.MainView;
import views.View;

public class UndoButton extends CommandButton {

	/* Constructors */
	public UndoButton(View view) {
		super(view);
		this.setText("Undo Add");
		this.setDisable(true); // nothing to undo at first
	}

	/* Override Methods */
	@Override
	public void execute() {
		MainView mainV = (MainView) connectedView;
		mainV.getStoreSnapshot().restore();
		mainV.disableUndo(); // Can't undo more than once!
		Alerts.showSuccess("Undid last Product add!");
	}
}
