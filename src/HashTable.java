
public class HashTable implements TermIndex{
	//size keeps track of how many terms are in the hash table
	private int size;

	//termTable is the hash table where terms are stored
	private Term[] termTable;

	//hash size is the size of the array 
	private int hashSize;

	/**
	 * constructor creates termTable to be the correct size
	 * set the size to 0 because no terms have been added
	 * set hashSize to hashSize
	 * @param hashSize the size that the hash table should be
	 */
	public HashTable(int hashSize){
		termTable = new Term[hashSize];
		size = 0;
		this.hashSize = hashSize;
	}


	/**
	 * adds a term to the hash table
	 * expands size of table and rehashes when 80% full
	 * @param filename the file that the word to be added comes from
	 * @param newWord the word to be added to the hashTable
	 */
	public void add(String filename, String newWord){
		//check if the hash table is 80% full
		if(((double)size/(double)hashSize) >=.8){
			reHash();
		}//end if

		//add the new term to the termTable
		//compute the hash code
		int hashCode = hashCode(newWord);	
		//variable to keep track of probing
		int probe = hashCode;

		//instance variables to assist in adding
		boolean added = false;
		int counter = 0;

		//see if the word is already in the hashTable
		int position = this.find(newWord);

		//if the word has already been added
		if(position>=0){
			termTable[position].incFrequency(filename);
		}

		//if the word has not already been added then add the word
		else{
			//while the word has not been added
			while(!added){			
				//if the space is open
				if(termTable[probe]==null || termTable[probe].getName().equals("RESERVED")){
					Term tempTerm = new Term(newWord);
					tempTerm.incFrequency(filename);
					termTable[probe] = tempTerm;
					added = true;
				}

				//if the location is full and the word has not been added already, do quadratic probing
				else{
					counter++;
					probe = (hashCode + (int)Math.pow(counter, 2))%hashSize;
				}//end else
			}//end while

			//increment size
			this.size++;
		}
	}

	/**
	 * increases the size of the hash table and 
	 * re-adds all of the terms according to their
	 * new hash code for the new table size
	 */
	private void reHash(){
		//if the hash size is getting full make a new table
		hashSize = (2*hashSize) +1;
		Term[] newTable = new Term[hashSize];
		int hashCode = 0;
		int probe = 0;

		//re-add all of the terms
		for(int i = 0; i < termTable.length; i++){
			//if the position in the original array is full then re add the value to the new array
			if(termTable[i]!=null && !termTable[i].getName().equals("RESERVED")){
				//compute the hash code
				hashCode = hashCode(termTable[i].getName());
				probe = hashCode;
				//add the word
				boolean added = false;
				int counter = 0;
				//while the word has not been added
				while(!added){
					//if the space is open
					if(newTable[probe] == null || newTable[probe].getName().equals("RESERVED")){
						newTable[probe] = termTable[i];
						added = true;
					}
					else{
						counter++;
						probe = (hashCode + (int)Math.pow(counter, 2))%hashSize;
					}//end else
				}//end while
			}//end if
		}//end for
		termTable = newTable;
	}

	
	/**
	 * getter method for the size
	 * @return how may terms are in the hash table
	 */
	public int size(){
		return this.size;
	}

	/**
	 * getter method for hashSize
	 * @return the size of the array
	 */
	public int hashSize(){
		return this.hashSize();
	}

	/**
	 * delete the word from the hash table
	 * if the word is not in the hash table throw an exception
	 * @param word the word to be deleted from the hashTable
	 */
	public void delete(String word){
		int look = find(word);
		//the word was found
		if(look >= 0){
			Term reservedTerm = new Term("RESERVED");
			termTable[look] = reservedTerm;
			this.size--;
		}
		//otherwise the word was not found and cannot be deleted
		else{
			throw new HashTableException("Deletion unsuccessful.  Word not found.");
		}
	}

	/**
	 * returns the term if found and null if not found(non-Javadoc)
	 * @see TermIndex#get(java.lang.String, java.lang.Boolean)
	 * @param word the name of the term to retrieve
	 * @param printP ??????
	 */
	public Term get(String word, Boolean printP){
		int position = this.find(word);
		if(position>=0){
			return termTable[position];
		}
		else{
			return null;
		}

	}

	/**
	 * @param word the name of the Term to be found
	 * @return -1 if the word was not found or the position where the word is located
	 */
	protected int find(String word){
		int positionsChecked = 0;
		//compute the hash code
		int hashCode = hashCode(word);
		int probe = hashCode;

		while(positionsChecked<hashSize){
			//if the term is found return the location in the array 
			if(termTable[probe] ==null){
				//if the position is null then the term is not in the table because
				//to add the term the same probing process would have been used to add it
				return -1;
			}
			else if(termTable[probe].getName().equals(word)){
				return probe;

			}
			//otherwise do quadratic probing
			positionsChecked++;
			probe = (hashCode + (int)Math.pow(positionsChecked, 2))%hashSize;
		}

		//if the code gets to here then the word  was not found
		return -1;
	}

	/**
	 * @param position
	 * @return what is in the hashTable at position
	 */
	public Term get(int position){
		return termTable[position];
	}

	
	/**
	 * calculates the hash code and index for a specified word
	 * @param word
	 * @return the index where the Term should be inserted
	 */
	public int hashCode(String word){
		int intCode = Math.abs(word.toLowerCase().hashCode());
		intCode = intCode%hashSize;
		return intCode;
	}

	/**
	 * @param other
	 * @return true if other is in the hash table, false if it is not
	 */
	public boolean contains(Object other) 
	{
		if(other instanceof Term)
		{
			Term otherTerm = (Term) other;
			if(this.find(otherTerm.getName()) >=0)
				return true;
		}
		return false;
	}

	/**
	 * Testing the methods
	 * @param args
	 */
//	public static void main(String[] args){
//		HashTable testing = new HashTable(5);
//		//test add
//		testing.add("docs", "newWord");
//		testing.add("doc", "newWord");
//		testing.add("document", "word");
//		testing.add("doc", "words");
//		testing.add("doc", "Gabriella");
//		testing.add("d", "g");
//
//		//test get
//		Term test = testing.get("jkdlsa", false);
//		if(test!=null) System.out.println("Testing get: " + test.getName());
//
//		
//		//print terms to test iterator
//		String outputString = "";
//		HashTableIterator itr = new HashTableIterator(testing);
//		while(itr.hasNext())
//		{
//			outputString += ((itr.next()).getName() + "\n");
//		}	
//		System.out.println(outputString);
//		
//		//test delete
//		testing.delete("Gabriella");
//		testing.delete("word");
//		HashTableIterator itr2 = new HashTableIterator(testing);
//		outputString = "";
//		while(itr2.hasNext())
//		{
//			outputString += ((itr2.next()).getName() + "\n");
//		}
//		System.out.println(outputString);
//		testing.add("doc", "Gabriella");
//		
//		HashTableIterator itr3 = new HashTableIterator(testing);
//		outputString = "";
//		while(itr3.hasNext())
//		{
//			outputString += ((itr3.next()).getName() + "\n");
//		}
//		System.out.println(outputString);
//		
//		Term tempTerm = new Term("gas");
//		boolean testcontain = testing.contains(tempTerm);
//		if(testcontain == true){
//			System.out.println("true");
//		}
//
//	}

}
