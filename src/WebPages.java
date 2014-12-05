// PA5
// Authors: Sadie Henry, Gabriella Fontani
// Date: 12/1/2014
// Class: CS200

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class WebPages 
{
	// global variable for hash table of Terms
	private HashTable termIndex;

	// global variable for the number of pages read in
	private int pageCount;

	// variables for highest sim value
	String highestSimString;
	double highestSimVal = 0.0;

	// global variable for the names of pages read in
	private ArrayList<String> fileNames = new ArrayList<String>();

	// global variable for a graph
	Graph graph = new Graph(true);

	//initializes a new index, a binary search tree of Term
	public WebPages(int hashSize)
	{
		termIndex = new HashTable(hashSize);
	}

	//reads in the page in filename, divides it into words as before 
	//and adds those words and their counts to the termIndex. 
	public void addPage(String filename)
	{
		try
		{
			//increment the pageCount
			pageCount++;

			//add the file name to arraylist
			fileNames.add(filename);

			//read line-by-line through the file to get words
			Scanner readFile = new Scanner(new File(filename));
			String line = "";
			while(readFile.hasNextLine())
			{
				//read in a file
				line = line + readFile.nextLine();
			}
				//remove HTML tags from the line
				line = stripHTML(line, filename);

				//delimit by everything but letters, numbers, ', and <>
				Scanner readLine = new Scanner(line).useDelimiter("[^\\w'<>]+");

				while(readLine.hasNext())
				{
					//set the line to lowercase
					String word = readLine.next().toLowerCase();

					//add the word to the TermIndex
					addToTermIndex(word, filename);					

				}

				readLine.close();
		
			readFile.close();
		}
		catch(IOException e)
		{
			System.out.println("Error: Unable to read file");
		}

	}
	
	// helper method to alphabetize a list 
	public void alphabetize(){
		graph.alphaList();
	}


	//prints on a separate line each word followed by two spaces followed by its 
	//frequency in the order in which it is stored in the ArrayList (as in PA1).
	public void printTerms()
	{
		System.out.println(toListString());	
	}

	//returns required output string for arraylist
	public String toListString()
	{
		String outputString = "WORDS\n";
		//check for an empty tree
		if(termIndex.size() == 0)
			return "Error: Empty List";
		else
		{
			HashTableIterator itr = new HashTableIterator(termIndex);
			while(itr.hasNext())
			{
				outputString += ((itr.next()).getName() + "\n");
			}		
		}

		return outputString; 
	}

	//method to strip HTML tags out of a string
	//also adds files to the graph when necessary
	public String stripHTML(String a, String fileName)
	{
		graph.addVertex(fileName);
		String pattern1 = "(<a\\p{Space}*href=\"http://)";
		String pattern2 = "(.*)(\">.*)";
		//split the file into an array containing the different files
		String[] array = a.split(pattern1);
		//make the files just the filenames
		for(int i = 0; i < array.length; i++){
			array[i] = array[i].replaceAll(pattern2, "$1");
		}
		
		for(int j = 1; j<array.length; j++){
			graph.addVertex(array[j]);
			graph.addEdge(fileName, array[j]);
		}


		return a.replaceAll("<.*?>", " ");
	}

	//adds words to the term tree
	public void addToTermIndex(String word, String document)
	{
		termIndex.add(document, word);
	}

	//TFIDF method
	private double TFIDF(String document, String word)
	{
		//get the term for that word
		Term term = termIndex.get(word, false);


		float TF = (float) term.getTermFrequency(document);
		//System.out.println("Word: " + word + " Document: " + document + " TF: " + TF);
		float D = pageCount;
		//System.out.println("Word: " + word + " Document: " + document + " D: " + D);
		float DF = term.getDocFrequency();
		//System.out.println("Word: " + word + " Document: " + document + " DF: " + DF);

		return TF * Math.log(D / DF);
	}

	// Wi,q method
	private double WIQ(String word){

		// get the term
		Term temp = termIndex.get(word, false);

		// get the number of docs that contain word
		int dfi = temp.getDocFrequency();

		double pc = pageCount;
		double df = (double)dfi;

		return .5*(1 + Math.log(pc/df));
	}

	private String listToString(ArrayList<String> array){
		String retVal = "[";
		for(int i = 0; i < array.size(); i++){
			retVal += array.get(i) + " ";
		}
		retVal += "]";
		return retVal;
	}

	private void setZero(ArrayList<Double> array){

		for(int i = 0; i < pageCount; i++){
			array.add(0.0);
		}
	}

	/**
	 * remove word from the hash table
	 * @param word the word that needs to be removed from the hash table
	 */
	public void pruneStopWords(String word) {
		termIndex.delete(word);

	}

	//whichPages method
	public String[] whichPages(String word){

		//make a new term to compare to the term index
		Term newTerm = new Term(word);

		//search through term index
		if(termIndex.contains(newTerm)){

			//get the listOfFileNames for that term
			ArrayList<String> arrayList = termIndex.get(word, false).getListOfFileNames();

			//copy array list to string array
			String[] stringArray = new String[arrayList.size()];
			for(int i = 0; i < arrayList.size(); i++){
				stringArray[i] = arrayList.get(i);
			}

			//return array
			return stringArray;

		}

		else{
			return null;
		}

	}

	// returns the number of edges incoming to a file
	public int inDegree(String filename){
		return graph.numInDegree(filename);
	}

	// creates a file that specifies the graph
	public void writeDotFile(String outputFile){

		try{

			// create a print writer
			PrintWriter writer = new PrintWriter(outputFile);

			// create the first line of the program
			writer.println("digraph graph5 {");

			// get the files and where they point to
			String files = graph.toFile();
			writer.print(files);

			// print the final bracket
			writer.println("}");		
		}	
		catch(Exception e){
			System.err.println("Print writer error: " + e);
			System.exit(0);
		}
	}

	// compute cosine similarity
	public String bestPages(String query){

		// array list for individual terms in a query
		ArrayList<String> queryList = new ArrayList<String>();

		// scanner to ready query
		Scanner scan = new Scanner(query);

		// read each term of the query into the ArrayList
		while(scan.hasNext()){
			String s = scan.next();
			queryList.add(s);
		}

		scan.close();

		// sort query list
		Collections.sort(queryList);

		/* array that supports mapping b/w positions in the component
		   arrays and which documents are being referenced */
		ArrayList<String> docs = fileNames;
		Collections.sort(docs);

		// array that keeps the numerators
		ArrayList<Double> common = new ArrayList<Double>();
		// set all positions to 0.0
		setZero(common);

		// array that keeps the first summation in the denominators
		ArrayList<Double> docSpecific = new ArrayList<Double>();
		// set all positions to 0.0
		setZero(docSpecific);

		// variable for second summation in the denominator (scalar)
		Double queryWeights = 0.0;

		// variable for WIQ calculation
		double wiq = 0.0;

		// variable for TFIDF calculation
		double wid = 0.0;

		// variables for highest sim value
		String highestSimString = "";
		double highestSimVal = 0.0;

		// create iterator
		HashTableIterator iterator = new HashTableIterator(termIndex);		

		// for each term i
		while(iterator.hasNext()){

			// get temp term
			Term temp = iterator.next();

			// if the term is in the query
			if(queryList.contains(temp.getName())){

				// compute WIQ, square it, and add it to queryWeights
				wiq = WIQ(temp.getName());
				queryWeights += (wiq * wiq);

			}

			// get list of files that contain term 
			ArrayList<String> docFileNames = temp.getListOfFileNames();

			// for each document d that contains term i
			for(int k = 0; k < temp.getDocFrequency(); k++){

				// compute and square tfidf
				wid = TFIDF(docFileNames.get(k), temp.getName());
				double wid2 = wid * wid;

				// find index of file in docs array
				int index = docs.indexOf(docFileNames.get(k));
				// add to value in docSpecific in the position for doc d
				double currentVal = docSpecific.get(index);
				docSpecific.set(index, wid2+currentVal);

				// if term is in both query and doc d
				if(queryList.contains(temp.getName())){

					// multiply wi,d and wi,q
					double commonVal = wid * wiq;

					// add to value in common array in position for doc k
					double currentVal1 = common.get(index);
					common.set(index, currentVal1+commonVal);
				}

			}

		}

		// for each document d
		for(int m = 0; m < docs.size(); m++){

			double sim = common.get(m)/((Math.sqrt(docSpecific.get(m)))*(Math.sqrt(queryWeights)));

			// keep track of doc w/ highest sim value
			if(sim >= highestSimVal){
				highestSimVal = sim;
				highestSimString = docs.get(m);
			}
		}

		// multiply the heighest sim val by the indegree of 
		highestSimVal = highestSimVal * inDegree(highestSimString);


		DecimalFormat fmt = new DecimalFormat("0.00");

		if(queryWeights == 0){
			return " not found in files";
		}

		return listToString(queryList) + " in " + highestSimString + ": " + fmt.format(highestSimVal);

	}

	//testing!!
//	public static void main(String args[]){
//		String test = "test2.txt";
//		WebPages page = new WebPages(5);
//		page.addPage(test);
//		System.out.println(page.graph.toString());
//		page.printTerms();
//
//	}

}
