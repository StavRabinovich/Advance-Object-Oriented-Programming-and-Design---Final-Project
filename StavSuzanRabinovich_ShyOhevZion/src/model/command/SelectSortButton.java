package model.command;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javafx.stage.Stage;
import model.Store;
import views.MainView;
import views.SortSelectionView;

public class SelectSortButton extends CommandButton {

	/* Fields */
	Store.SortOrder selectedSortOrder;

	/* Constructors */
	public SelectSortButton(SortSelectionView view, Store.SortOrder sortOrder) {
		super(view);
		selectedSortOrder = sortOrder;
		this.setText(sortOrder.text()); // Button's Text
	}

	/* Override Methods */
	@Override
	public void execute() {
		try (RandomAccessFile raf = new RandomAccessFile(Store.STORE_FILE, "rw")) { 
			raf.writeInt(selectedSortOrder.order()); // Writes selected sort order to file
			raf.close();
			connectedView.close();
			new MainView(new Stage()); // Opens mainView
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
