public class Edge {
	/**
	 * The vertices of the edge
	 */
	private Integer v, w;
	/**
	 * The weight of the edge
	 */
	private int weight;	
	
	/**
	 * Constructor. Creates an edge from v to w with weight edgeWeight
	 * @param first node
	 * @param second node
	 * @param weight of the edge
	 */
	public Edge(Integer first, Integer second, int edgeWeight)
	{
		v = first;
		w = second;
		weight = edgeWeight;
	}
	
	/**
	 * @return the edge weight
	 */
	public int getWeight() 
	{
		return weight;
	}
	
	/**
	 * @return returns the first vertex of the edge
	 */
	public Integer getV()
	{
		return v;
	}
	
	/**
	 * @return returns the second vertex of the edge
	 */
	public Integer getW()
	{
		return w;
	}
}