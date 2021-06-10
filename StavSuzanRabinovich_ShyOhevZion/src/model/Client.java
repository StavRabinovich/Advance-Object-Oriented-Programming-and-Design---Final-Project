package model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import observer.Receiver;
import observer.Sender;

public class Client implements Sender, Receiver {

	/* Fields */
	private String name;
	private String number;
	private boolean receiveUpdates;

	/* Constructors */
	public Client(String name, String number, boolean update) {
		setName(name);
		setNumber(number);
		setUpdate(update);
	}

	public Client() {
		this(null, null, false);
	}

	public Client(DataInput in) throws IOException {
		this(in.readUTF(), in.readUTF(), in.readBoolean());
	}

	/* Get, Set, Add & Remove */
	public String getName() {
		return name;
	}

	public void setName(String cName) { // Default definition
		name = (cName == null) ? "no name" : cName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String cNumber) { // Default definition
		number = (cNumber == null) ? "" : cNumber;
	}

	public boolean wantsUpdate() {
		return receiveUpdates;
	}

	public void setUpdate(boolean cUpdate) {
		receiveUpdates = cUpdate;
	}

	/* Override Methods */
	@Override
	public void receiveMSG(Sender sender) {
		if (wantsUpdate() && sender instanceof SaleNotifier)
			sendMSG((SaleNotifier) sender);
	}

	@Override
	public void sendMSG(Receiver receiver) {
		receiver.receiveMSG(this);
	}

	@Override
	public String toString() {
		return String.format("Client's name: %s \tClient's number: %s\t WILL %sRECEIVE UPDATES", name, number,
				(!(receiveUpdates) || number.isBlank()) ? "NOT " : "");
	}

	/* Other Methods */
	public boolean writeToStream(DataOutput out) { // Writes client to data output
		try {
			out.writeUTF(name);
			out.writeUTF(number);
			out.writeBoolean(receiveUpdates);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
