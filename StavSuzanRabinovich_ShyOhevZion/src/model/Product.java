package model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Product { /* Product's Class */

	/* Fields */
	private String name; // Product's name
	private int cost; // Product's cost price
	private int sell; // Product's sell price
	private Client client; // Product's client

	/* Constructors */
	public Product(String name, int cost, int sell) {
		setName(name);
		setCost(cost);
		setSell(sell);
	}

	public Product(String name, int cost, int sell, Client client) {
		this(name, cost, sell);
		setClient(client);
	}

	public Product(DataInput in) throws IOException { // Creates from data input
		this(in.readUTF(), in.readInt(), in.readInt());
		if (in.readBoolean()) // product has an associated client
			client = new Client(in);
	}

	/* Get, Set, Add & Remove */
	public String getName() {
		return name;
	}

	public void setName(String pName) {
		name = (pName == null) ? "NO PRODUCT NAME" : pName;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int pCost) {
		cost = pCost;
	}

	public int getSell() {
		return sell;
	}

	public void setSell(int pSell) {
		sell = pSell;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client pClient) {
		client = pClient;
	}

	/* Override Methods */
	@Override
	public String toString() {
		return String.format("Name: %s \tCost: %d \tSell: %d\n\t\t%s", name, cost, sell,
				(client == null) ? "No Client" : client);
	}

	/* Other Methods */
	public int calculateProfit() { // Calculates profit
		return sell - cost;
	}

	public boolean writeToStream(DataOutput out) { // Writes to stream
		try {
			out.writeUTF(name);
			out.writeInt(cost);
			out.writeInt(sell);
			if (client == null) {
				out.writeBoolean(false);
				return true;
			} else {
				out.writeBoolean(true);
				return client.writeToStream(out);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}