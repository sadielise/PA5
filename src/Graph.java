import java.util.ArrayList;
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
	 * I have added a boolean value to the class to help get you start with manipulating the constructor and the 
	 * methods to differentiate between directed/undirected.
	 */
	private boolean directed;

	/**
	 * For each vertex, we need to keep track of the edges, so for each edge, 
	 * we need to store the second vertex and the edge weight. This can be done 
	 * as a <key, value> pair, with the second vertex as the key, and the weight 
	 * as the value. The TreeMap data structure is used to store a list of these
	 * (key, value) pairs for each vertex, accessible as adjList.get(v)
	 */
	private Vector<TreeMap<Integer, Integer>> adjList;

	/**
	 * keeps track of the order of the filenames in the graph
	 */
	private ArrayList<String> filenames;

	/**
	 * Constructor for weighted graph </br>
	 * Precondition: The number of vertices n should be greater than 0 </br>
	 * Postcondition: Intializes the graph with n vertices
	 * @param n
	 */
	public Graph(boolean directed)
	{
		numVertices = 0;
		numEdges = 0;
		adjList = new Vector<TreeMap<Integer, Integer>>();
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
	public int getEdgeWeight(Integer v, Integer w)
	{
		return adjList.get(v).get(w);
	}


	/**
	 * adds a vertex to the graph
	 * @param the name of the file vertex
	 */
	public void addVertex(String filename){
		int index = filenames.indexOf(filename);
		
		//if the index is -1 then the file has not been added yet
		if(index == -1){
			numVertices++;
			adjList.add(new TreeMap<Integer, Integer>());
			filenames.add(filename);
		}
	}


	/**
	 * Adds an edge from c to w with weight wgt to the graph </br>
	 * Precondition The vertices contained within the edge e exist in the graph
	 * @param v
	 * @param w
	 * @param wgt
	 */
	public void addEdge(String file1, String file2, int wgt) 
	{
		int v = filenames.indexOf(file1);
		int w = filenames.indexOf(file2);
		// Add the edge to both v's and w's adjacency list
		adjList.get(v).put(w, wgt);
		//EDIT goes both ways only if not directed
		if(!directed) adjList.get(w).put(v, wgt);
		numEdges++;
	}


	public void addEdge(Integer v, Integer w, int wgt) 
	{
		// Add the edge to both v's and w's adjacency list
		adjList.get(v).put(w, wgt);
		//EDIT goes both ways only if not directed
		if(!directed) adjList.get(w).put(v, wgt);
		numEdges++;
	}


	/**
	 * Adds an edge to the graph
	 * @param e
	 */
	public void addEdge(Edge e)
	{
		//Extract the vertices and weight from the edge e
		Integer v = e.getV();
		Integer w = e.getW();
		int weight = e.getWeight();
		addEdge(v, w, weight);
	}

	/**
	 * Removes an edge from the graph </br>
	 * Precondition: The vertices contained in the edge e exist in the graph
	 * @param e
	 */
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
	 * @param v
	 * @param w
	 * @return The edge with the weight
	 */
	public Edge FindEdge(String file1, String file2)
	{
		int v = filenames.indexOf(file1);
		int w = filenames.indexOf(file2);
		int weight = adjList.get(v).get(w);
		return new Edge(v, w, weight);
	}

	/**
	 * package access </br>
	 * Returns the adjacency list for given vertex
	 * @param v
	 * @return The associated adjacency list
	 */
	TreeMap<Integer, Integer> getAdjList(Integer v)
	{
		return adjList.get(v);
	}

	public String toString(){
		String temp = "";
		for(int i = 0; i < numVertices; i++){
			TreeMap<Integer, Integer> adjListSearch = getAdjList(i);
			temp = temp + filenames.get(i);
			for(int j = 0; j < numVertices; j++){
				Integer weight = adjListSearch.get(j);
				if(weight!=null){
					temp = temp + "=> " + filenames.get(j) + "[" + weight + "]";
				}
			}
			temp = temp + "\n";
		}


		return temp;
	}
	public static void main(String args[]){
		Graph graph = new Graph(false);
		graph.addVertex("vertex0");
		graph.addVertex("vertex1");
		graph.addVertex("vertex2");
		graph.addVertex("vertex3");
		graph.addVertex("vertex4");
		graph.addVertex("vertex5");
		graph.addVertex("vertex5");


		graph.addEdge("vertex0", "vertex1", 3);
		graph.addEdge("vertex2", "vertex3", 2);
		graph.addEdge("vertex2", "vertex0", 9);
		graph.addEdge("vertex3", "vertex0", 5);
		graph.addEdge("vertex1", "vertex4", 1);
		graph.addEdge("vertex1", "vertex5", 6);
		graph.addEdge("vertex4", "vertex5", 4);
		System.out.println("undirected: \n" + graph.toString());

		//		Graph graph2 = new Graph(true);
		//		graph2.addEdge(0, 1, 3);
		//		graph2.addEdge(0,2,9);
		//		graph2.addEdge(0,3,5);
		//		graph2.addEdge(2,3,2);
		//		graph2.addEdge(1,4,1);
		//		graph2.addEdge(1,5,6);
		//		graph2.addEdge(4, 5, 4);
		//		System.out.println("directed: \n" + graph2.toString());
		//

	}

}