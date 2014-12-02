// PA5
// Authors: Sadie Henry, Gabriella Fontani
// Date: 12/1/2014
// Class: CS200

import java.util.Iterator;

public class HashTableIterator implements Iterator<Term>{

	private HashTable hTable;
	private int position;
	private int elementsFound;

	public HashTableIterator(HashTable hTable){
		this.hTable = hTable;
		position = 0;
		elementsFound = 0;
	}

	public boolean hasNext() {
		return elementsFound < hTable.size();
	}

	public Term next() {

		if(hasNext()){
			elementsFound++;
			boolean wordFound = false;
			while(!wordFound){
				if(hTable.get(position)==null || hTable.get(position).getName().equals("RESERVED")){
					//if the position in the hash table is null or reserved then there is no term there
					//increment position and loop until a term is found
					position++;
				}
				else{
					//there is a term in the position so increment position and return the value
					Term tempTerm = hTable.get(position);
					position++;
					return tempTerm;
				}
			}
			//if gets to this point then something went wrong
			throw new HashTableException("The Iterator has problems in next or hasNext");
		}
		else{
			throw new HashTableException("No terms available");
		}
	}

	public void remove() {


	}



}
