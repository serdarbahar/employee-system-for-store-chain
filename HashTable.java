import java.util.ArrayList;

public class HashTable<E extends Entry> {

	public ArrayList<E> table = new ArrayList<E>();
	
	private int size = 0;
	private static double LOAD_FACTOR_THRESHOLD = 0.8;
	private static int INITIAL_CAPACITY = 997;
	
	
	HashTable() {
		for (int i = 0; i<INITIAL_CAPACITY; i++) {
			table.add(null);
		}
	}
	
	private int hash(E e) {
		
		if (e == null) {
			return -1;
		}
		int hashVal = e.hashCode() % table.size();
		if (hashVal<0) {
			hashVal += table.size();
		}
		return hashVal;
	}
	
	
	
	public void insert(E e) {
		
		if (++size > LOAD_FACTOR_THRESHOLD * table.size()) {
			rehash();
		}
		

		int hashVal = hash(e);
		int increment = 1;
		
		//linear probing
		while  ( table.get(hashVal) != null &&
				!table.get(hashVal).isDeleted )  {
			
				hashVal = (hashVal+increment) % table.size();
				
		}
		
		
		table.set(hashVal,e);
		e.isDeleted = false;
	}
	

	
	
	public E remove(String s) {

		E e = find(s);
		int pos = findPos(e);
		
		if (pos != -1) {
			table.get(pos).isDeleted = true;
			return e;
		}
		
		else {
			return null;
		}
		
	}
		

	public int findPos(E e) {
		
		int hashVal = hash(e);
		int initial = hashVal;
		if (hashVal == -1) {
			return -1;
		}
		
		int increment = 1;
		while ( !table.get(hashVal).equals(e)) {
			
			hashVal = (hashVal+increment) % table.size();
			
			if (hashVal == initial) 
				return -1;
		}
		
		if (table.get(hashVal).isDeleted) {
			return -1;
		}
		
		return hashVal;
		
	}
	
	
	
	
	
	private void rehash() {

		//create new array
		ArrayList<E> newTable = new ArrayList<>();
		for (int i = 0; i< (nextPrime(2 * table.size())); i++) {
			newTable.add(null);
		}
		
		ArrayList<E> oldTable = this.table;
		this.table = newTable;
		
		
		//copy contents over
		for (int i = 0; i < oldTable.size() ; i++) {
			if (oldTable.get(i) != null && !oldTable.get(i).isDeleted) {
				insert(oldTable.get(i));
			}
		}
				
	}

	
	
	
	
	
	
	
	public E find(String s) {
		
		int hashVal = s.hashCode() % table.size();
		if (hashVal<0) {
			hashVal += table.size();
		}
		int initial = hashVal;
		int increment = 1;
		
		if (table.get(hashVal) == null) 
			return null;
 		
		while (!table.get(hashVal).name.equals(s) ) {
			hashVal = (hashVal + increment) % table.size();
			if (initial == hashVal) 
				return null;
			if (table.get(hashVal) == null) 
				return null;
				

		}
		
		if (!table.get(hashVal).isDeleted) {
			return table.get(hashVal);
		}
		else 
			return null;

		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private int nextPrime(int i) {

		for (; !isPrime(i); i++) {}
			
		return i;
		
	}



	private boolean isPrime(int i) {
		
		for (int j = 2 ; j<i ; j++)
			if (i%j == 0)
				return false;

		return true;
	}
	
	
	
	
	
	
	
	
	
	
}
