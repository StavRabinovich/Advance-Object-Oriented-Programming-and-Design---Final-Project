package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Store implements Iterable<Map.Entry<String, Product>> {

	/* Constants */
	public static final File STORE_FILE = new File("resources/products.txt");

	public enum SortOrder { // The numeric value will be saved in the file, the string will be presented to
							// the user and the values decides the sort order of the map
		DESCENDING(-1, "Z to A"), NOT_SORTED(0, "Not Sorted"), ASCENDING(1, "A to Z");
		
		/* Fields */
		private int order;
		private String text;
		
		/* Constructors */
		SortOrder(int sortOrder, String sortText) {
			order = sortOrder;
			text = sortText;
		}

		/* SortOrder Functions */
		public int order() {
			return order;
		}

		public String text() {
			return text;
		}

		public static SortOrder byNumericValue(int sortOrder) {
			for (SortOrder s : SortOrder.values())
				if (s.order() == sortOrder)
					return s;
			return null;
		}
	}

	/* Fields */
	private Map<String, Product> productsMap;
	private boolean initialized = false;
	private SortOrder sortOrder;

	/* Constructors */
	public Store() throws IOException {
		if (!STORE_FILE.exists() || iterator() == null)
			throw new IOException();
		initMap();
		initNotifier();
		initialized = true;
	}

	/* Get, Set, Add & Remove */
	public Map<String, Product> getProductsMap() {
		return productsMap;
	}

	public boolean removeProduct(String pId) {
		Iterator<Entry<String, Product>> iter = iterator();
		if (!iter.hasNext())
			return false;

		String id = ((Entry<String, Product>) iter.next()).getKey();
		while (!id.equals(pId) && iter.hasNext())
			id = ((Entry<String, Product>) iter.next()).getKey();

		if (id.equals(pId)) {
			iter.remove();
			readMapFromFile();
			return true;
		}
		return false;
	}

	public boolean removeAllProducts() {
		Iterator<Entry<String, Product>> iter = iterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}
		readMapFromFile();
		return true;
	}

	public boolean addProduct(String pID, Product p) { // Adds product to store
		if (pID == null || pID.isBlank()) // Will not add product without ID
			return false;
		productsMap.put(pID, p); // Add product to map
		return writeMapToFile(); // Updates map
	}

	/* Override Methods */
	@Override
	public Iterator<Entry<String, Product>> iterator() {
		try {
			return new FileIterator(STORE_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* Other Methods */
	public Snapshot createSnapshot() { // Saves snapshot
		return new Snapshot(this, productsMap);
	}

	private void restore(Map<String, Product> savedstate) { // Restores from snapshot
		productsMap.clear();
		productsMap.putAll(savedstate);
		writeMapToFile();
	}

	public Product searchProduct(String pID) { // Search product in productMap by productID
		return productsMap.get(pID);
	}

	public int getStoreProfit() { // Returns sum of all products's profits in store
		int count = 0;
		for (Entry<String, Product> entry : productsMap.entrySet()) {
			count += entry.getValue().calculateProfit();
		}
		return count;
	}

	private void initMap() { // Initiates the productsMap
		switch (sortOrder) {
		case NOT_SORTED:
			productsMap = new LinkedHashMap<>(); // a LinkedHashSet lets us iterate through the elements in the order in
													// which they were inserted
			break;
		case ASCENDING:
			productsMap = new TreeMap<>(); // TreeMap uses natural string comparator
			break;
		case DESCENDING:
			productsMap = new TreeMap<>(new Comparator<>() { // reverse order of the natural string comparator
				@Override
				public int compare(String s1, String s2) {
					return s2.compareTo(s1);
				}
			});
			break;
		}

		readMapFromFile();
	}

	private void initNotifier() { // Initiates Notifier
		SaleNotifier notifier = SaleNotifier.instance();
		for (Entry<String, Product> entry : this) {
			notifier.addClientToSaleList(entry.getValue().getClient()); // will add only if the client wants updates
		}
	}

	private boolean readMapFromFile() { // Reads Map from file
		try {
			productsMap.clear(); // Clear previous product map (might not be empty)
			for (Entry<String, Product> entry : this) {
				productsMap.put(entry.getKey(), entry.getValue());
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean writeMapToFile() { // Writes productMap to file
		try (RandomAccessFile raf = new RandomAccessFile(STORE_FILE, "rw")) {
			raf.setLength(0); // From the start
			raf.writeInt(sortOrder.order());
			for (Entry<String, Product> entry : productsMap.entrySet()) {
				raf.writeUTF(entry.getKey());
				entry.getValue().writeToStream(raf);
			}
			raf.close();
			return true; // Writing finished successfully
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/* FileIterator internal class */
	private class FileIterator implements Iterator<Map.Entry<String, Product>> {

		/* Fields */
		private RandomAccessFile raf;
		private boolean canRemove = false;
		private long previousPosition = -1;

		/* Constructors */
		public FileIterator(File file) throws FileNotFoundException, IOException {
			raf = new RandomAccessFile(file, "rw");
			if (initialized)
				raf.seek(4);
			else
				sortOrder = SortOrder.byNumericValue(raf.readInt());
		}

		/* Override Methods */
		@Override
		public boolean hasNext() {
			try {
				return raf.length() != raf.getFilePointer();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public Map.Entry<String, Product> next() {
			try {
				previousPosition = raf.getFilePointer();
				canRemove = true;
				String pID = raf.readUTF();
				Product p = new Product(raf);
				return new AbstractMap.SimpleEntry<String, Product>(pID, p);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void remove() {
			if (!canRemove)
				throw new IllegalStateException();
			try {
				long readPosition = raf.getFilePointer(), writePosition = previousPosition;
				long diff = readPosition - writePosition;
				byte temp;
				while (readPosition < raf.length()) {
					raf.seek(readPosition);
					temp = raf.readByte();
					raf.seek(writePosition);
					raf.write(temp);
					writePosition++;
					readPosition++;
				}
				raf.setLength(raf.length() - diff);
				raf.seek(previousPosition);
				canRemove = false;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/* Memento */
	public class Snapshot {

		/* Fields */
		private Store s;
		private Map<String, Product> Savedstate;

		/* Constructors */
		public Snapshot(Store store, Map<String, Product> state) {
			s = store;
			Savedstate = new LinkedHashMap<String, Product>(state);
		}

		/* Other Methods */
		public void restore() { // Restores to last saved status
			s.restore(Savedstate);
		}
	}
}
