// PA5
// Authors: Sadie Henry, Gabriella Fontani
// Date: 12/1/2014
// Class: CS200
// This code was taken from the CS200 Recitation 14 and adjusted for our use

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Vector;


public class Graph {
	/**
	 * number of vertices in the graph
	 */
	private int numVertices;

	/**
	 * number of edges in the graph
	 */
	private int numEdges;

	/**
	 * Boolean value to differentiate b/w directed and undirected graphs
	 */
	private boolean directed;

	/**
	 * Vector to keep track of the graph 
	 */
	private Vector<ArrayList<String>> adjList;

	/**
	 * keeps track of the order of the filenames in the graph
	 */
	private ArrayList<String> filenames;

	/**
	 * Constructor for graph </br>
	 * Precondition: The number of vertices n should be greater than 0 </br>
	 * Postcondition: Initializes the graph with n vertices
	 * @param n
	 */
	public Graph(boolean directed)
	{
		numVertices = 0;
		numEdges = 0;
		adjList = new Vector<ArrayList<String>>();
		this.directed = directed;
		filenames = new ArrayList<String>();
	}

	/**
	 * Determines the number of vertices in the graph </br>
	 * Precondition: None. </br>
	 * @return number of vertices in the graph
	 */
	public int getNumVertices() 
	{
		return numVertices;
	}

	/**Determines the number of edges in the graph </br>
	 * Precondition: None.
	 * @return the number of edges in the graph
	 */
	public int getNumEdges()
	{
		return numEdges;
	}

	/**
	 * Determines the weight of the edge between the vertices v and w </br>
	 * Precondition: Edge must exist within the graph
	 * @param v
	 * @param w
	 * @return The weight of the edge.
	 */
	//dont need edgeweight
	//	public int getEdgeWeight(Integer v, Integer w)
	//	{
	//		return adjList.get(v).get(w);
	//	}


	/**
	 * adds a vertex to the graph
	 * @param the name of the file vertex
	 */
	public void addVertex(String filename){
		int index = filenames.indexOf(filename);

		//if the index is -1 then the file has not been added yet
		if(index == -1){
			numVertices++;
			adjList.add(new ArrayList<String>());
			filenames.add(filename);
		}
	}


	/**
	 * Adds an edge from c to w and vice versa if undirected</br>
	 * Precondition The vertices contained within the edge e exist in the graph
	 */
	public void addEdge(String file1, String file2) 
	{
		int v = filenames.indexOf(file1);
		int w = filenames.indexOf(file2);
		// Add the edge to both v's and w's adjacency list
		adjList.get(v).add(file2);
		//EDIT goes both ways only if not directed
		if(!directed) adjList.get(w).add(file1);
		numEdges++;
	}



	//don't need this method for PA5
	//	public void addEdge(Integer v, Integer w, int wgt) 
	//	{
	//		// Add the edge to both v's and w's adjacency list
	//		adjList.get(v).put(w, wgt);
	//		//EDIT goes both ways only if not directed
	//		if(!directed) adjList.get(w).put(v, wgt);
	//		numEdges++;
	//	}

	//don't need this method for PA5
	//	public void addEdge(Edge e)
	//	{
	//		//Extract the vertices and weight from the edge e
	//		Integer v = e.getV();
	//		Integer w = e.getW();
	//		int weight = e.getWeight();
	//		addEdge(v, w, weight);
	//	}

	/**
	 * Removes an edge from the graph </br>
	 * Precondition: The vertices contained in the edge e exist in the graph
	 * @param e
	 */
	//NOT TESTED WITH NEW FORMAT!!! DO NOT USE UNTIL EDITED
	public void removeEdge(Edge e)
	{
		// Extract the vertices from the edge e
		Integer v = e.getV();
		Integer w = e.getW();

		// Remove the edge from v's and w's adjacency list
		adjList.get(v).remove(w);
		//EDIT only not directed graph has this
		if(!directed) adjList.get(w).remove(v);
		
		numEdges--;
	}

	/**
	 * Finds the edge connecting v and w. </br>
	 * Precondition: The edge exists
	 */
	public Edge FindEdge(String file1, String file2)
	{
		int v = filenames.indexOf(file1);
		int w = filenames.indexOf(file2);
		//no weights for PA5
		int weight = 0;
		return new Edge(v, w, weight);
	}

	
	/**
	 * this method alphabetizes the files in the adjList
	 */
	public void alphaList(){
		Vector<ArrayList<String>> tempGraph = new Vector<ArrayList<String>>();
		ArrayList<String> tempFileList = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		for(int k = 0; k<filenames.size(); k++){
			tempFileList.add(filenames.get(k));
		}
		Collections.sort(tempFileList);
		int index = 0;
		for(int i = 0; i < tempFileList.size(); i++){
			tempGraph.add(new ArrayList<String>());
			index = filenames.indexOf(tempFileList.get(i));
			temp = adjList.get(index);
			for(int j = 0; j<temp.size(); j++){
				tempGraph.get(i).add(temp.get(j));
			}
		}
		
		filenames = tempFileList;
		adjList = tempGraph;
	}
	
	/**
	 * package access </br>
	 * Returns the adjacency list for given vertex
	 */
	ArrayList<String> getAdjList(Integer v)
	{
		return adjList.get(v);
	}


	public String toString(){
		String temp = "";
		for(int i = 0; i < numVertices; i++){
			ArrayList<String> adjListSearch = getAdjList(i);
			temp = temp + filenames.get(i);
			for(int j = 0; j < adjListSearch.size(); j++){
				int weight =0;
				temp = temp + "=> " + adjListSearch.get(j) + "[" + weight + "]";
			}
			temp = temp + "\n";
		}
		return temp;
	}

	// gets the list of files and where they point to
	// helper method for WebPages.toDotFile
	public String toFile(){
		String temp = "";
		for(int i = 0; i < numVertices; i++){
			ArrayList<String> adjListSearch = getAdjList(i);
			for(int j = 0; j < adjListSearch.size(); j++){
				temp = temp + "\"" + filenames.get(i) + "\"";
				temp = temp + " -> \"" + adjListSearch.get(j) + "\";";
				temp = temp + "\n";
			}
		}
		return temp;
	}

	// gets the number of files that point to a specific file/vertex
	public int numInDegree(String filename){
		int count = 0;
		for(int i = 0; i<adjList.size(); i++){
			if(adjList.get(i).contains(filename)){
				count++;
			}
		}
		return count;
	}


//	public static void main(String args[]){
//		Graph graph = new Graph(false);
//		graph.addVertex("vertex2");
//		graph.addVertex("vertex1");
//		graph.addVertex("vertex0");
//		graph.addVertex("vertex3");
//		graph.addVertex("vertex4");
//		graph.addVertex("vertex5");
//		graph.addVertex("vertex5");
//
//
//		graph.addEdge("vertex0", "vertex1");
//		graph.addEdge("vertex2", "vertex3");
//		graph.addEdge("vertex3", "vertex0");
//		graph.addEdge("vertex2", "vertex0");
//		graph.addEdge("vertex1", "vertex4");
//		graph.addEdge("vertex1", "vertex5");
//		graph.addEdge("vertex4", "vertex5");
//		//System.out.println("undirected: \n" + graph.toString());
//
//		Graph graph2 = new Graph(true);
//		graph2.addVertex("vertex2");
//		graph2.addVertex("vertex1");
//		graph2.addVertex("vertex0");
//		graph2.addVertex("vertex3");
//		graph2.addVertex("vertex4");
//		graph2.addVertex("vertex5");
//		graph2.addVertex("vertex5");
//		graph2.addEdge("vertex0", "vertex1");
//		graph2.addEdge("vertex0","vertex3");
//		graph2.addEdge("vertex0","vertex2");
//		graph2.addEdge("vertex2","vertex3");
//		graph2.addEdge("vertex1","vertex4");
//		graph2.addEdge("vertex1","vertex5");
//		graph2.addEdge("vertex4", "vertex5");
//		graph2.alphaList();
//		System.out.println("directed in order: \n" + graph2.toString());
//		System.out.println("indegree of vertex0: " + graph2.numInDegree("vertex0"));
//		System.out.println("indegree of vertex1: " + graph2.numInDegree("vertex1"));
//		System.out.println("indegree of vertex2: " + graph2.numInDegree("vertex2"));
//		System.out.println("indegree of vertex3: " + graph2.numInDegree("vertex3"));
//		System.out.println("indegree of vertex4: " + graph2.numInDegree("vertex4"));
//		System.out.println("indegree of vertex5: " + graph2.numInDegree("vertex5"));
//
//
//
//
//
//
//
//
//	}

}