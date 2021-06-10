package core;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Store;
import views.MainView;
import views.SortSelectionView;

public class Launcher extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		if ((Store.STORE_FILE.exists()) && (Store.STORE_FILE.length() > 0)) // If file exists and not empty
			new MainView(primaryStage);
		else
			new SortSelectionView(primaryStage);
	}
}
