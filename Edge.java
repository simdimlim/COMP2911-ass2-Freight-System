
public class Edge<E> {
	private Node<E> to;
	private int weight;
	
	public Edge(Node<E> to, int weight) {
		this.to = to;
		this.weight = weight;
	}
	
	public Node<E> getNode() {
		return to;
	}
	
	public int getWeight() {
		return weight;
	}
}
