
public interface Graph<E> {
	/**
	 * Adds a node to the graph.
	 * 
	 * @param n The node to be added
	 * @param cost The node's weight
	 */
	public void addNode (E n, int cost);
	
	/**
	 * Adds an edge to the graph.
	 * 
	 * @param from The node on the first end of the edge
	 * @param to The node on the last end of the edge
	 * @param cost The edge's weight
	 */
	public void addEdge (Node<E> from, Node<E> to, int cost);
	
	/**
	 * Retrieve a node from the graph.
	 * 
	 * @param n The node's identity
	 * @return The node
	 */
	public Node<E> getNode (E n);
	
	/**
	 * Checks if an edge exists within the graph.
	 * 
	 * @param from The first end of the edge
	 * @param to The second end of the edge
	 * @return The weight of the edge if it exists, -1 otherwise.
	 */
	public int ifEdgeExists(Node<E> from, Node<E> to);
}
