package model;

import java.util.ArrayList;
import java.util.List;

import observer.Receiver;
import observer.Sender;

public class SaleNotifier implements Sender, Receiver {

	/* Fields */
	private static final SaleNotifier INSTANCE = new SaleNotifier();
	private ArrayList<Receiver> clients;
	private ArrayList<String> replies;

	public List<String> getReplies() {
		return List.copyOf(replies);
	}

	/* Override Methods */
	@Override
	public void sendMSG(Receiver receiver) {
		receiver.receiveMSG(this);
	}

	public boolean hasClientsToNotify() {
		return clients.size() > 0;
	}

	@Override
	public void receiveMSG(Sender sender) {
		if (sender instanceof Client) {
			Client c = (Client) sender;
			if (c.wantsUpdate())
				replies.add(c.getName());
		}
	}

	/* Other Methods */
	public boolean addClientToSaleList(Client c) {
		if (c != null && c.wantsUpdate())
			return clients.add(c); // returns true
		return false;
	}

	private SaleNotifier() {
		clients = new ArrayList<>();
		replies = new ArrayList<>();
	}

	public static SaleNotifier instance() {
		if (INSTANCE == null)
			return new SaleNotifier();
		else
			return INSTANCE;
	}

	public void sendMSGToAll() {
		replies.clear();
		for (Receiver receiver : clients) {
			sendMSG(receiver);
		}
	}

	public void removeClient(Client client) {
		clients.remove(client);
	}

	public void removeAllClients() {
		clients.clear();
		replies.clear();
	}
}
