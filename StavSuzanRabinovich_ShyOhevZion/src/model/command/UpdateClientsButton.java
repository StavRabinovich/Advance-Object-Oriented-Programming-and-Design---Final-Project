package model.command;

import javafx.stage.Stage;
import views.ClientsPane;
import views.MainView;
import views.View;

public class UpdateClientsButton extends CommandButton { /* CommandButton of Show Clients */

	/* Constructors */
	public UpdateClientsButton(View view) {
		super(view);
		this.setText("Update Clients");
	}

	/* Override Methods */
	@Override
	public void execute() {
		MainView mainV = (MainView) connectedView;
		mainV.clearAllFields();
		new ClientsPane(new Stage()); // Presents Clients's list
	}

}
