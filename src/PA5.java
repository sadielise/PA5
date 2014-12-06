// PA5
// Authors: Sadie Henry, Gabriella Fontani
// Date: 12/1/2014
// Class: CS200

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PA5 {
	
	public static String outputFile;

	public static void main(String[] args){

		try {
			Scanner scan = new Scanner(new File(args[0]));
			
			// scan in the name of the output file for the graph
			outputFile = scan.nextLine();
			//scan in the size of the hash table
			int tempInt = scan.nextInt();
			//initialize WebPages with the int
			WebPages pages = new WebPages(tempInt);
			//clear scanner
			scan.nextLine();
			
			//read in the files to be added
			String tempString = scan.nextLine();
			//while there are more files to be read in
			while(!tempString.equals("*EOFs*")){
				//add the page
				pages.addPage(tempString);
				//get the name of the next page from the scanner
				tempString = scan.nextLine();
			}

			//remove the stop words
			tempString = scan.nextLine();
			//while there are more stop words to prune
			while(!tempString.equals("*STOPs*")){
				//prune the stop word
				pages.pruneStopWords(tempString);
				//get the next word to be pruned
				tempString = scan.nextLine();
			}
			
			//alphabetize graph
			pages.alphabetize();
			
			// arraylist for queries
			ArrayList<String> queries = new ArrayList<String>();
			// get queries
			while(scan.hasNext()){
				String line = scan.nextLine();
				line = line.toLowerCase();
				queries.add(line);
			}
			
 			// call best pages and print out query info
			for(int i = 0; i < queries.size(); i++){
				String query = queries.get(i);
				System.out.println(pages.bestPages(query));				
			}
			
			// print the graph in output file
			pages.writeDotFile(outputFile);
			
		
			scan.close();
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		
	}
}