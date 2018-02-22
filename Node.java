import java.util.*;

public class Node<E> {
	private E name;
	private int cost;
	private List<Edge<E>> edges;
	
	public Node(E name, int cost) {
		this.name = name;
		this.cost = cost;
		edges = new LinkedList<Edge<E>>();
	}
	
	public E getName() {
		return name;
	}
	
	public int getCost() {
		return cost;
	}
	
	public LinkedList<Node<E>> getConnected() {
		LinkedList<Node<E>> connected = new LinkedList<Node<E>>();
		for (Edge<E> e : edges) {
			connected.add(e.getNode());
		}
		return connected;
	}
	
	public void addEdge(Node<E> n, int weight) {
		edges.add(new Edge<E>(n, weight));
	}
	
	public int getNeighbourCost(Node<E> to) {
		for (Edge<E> e : edges) {
			if (e.getNode().equals(to)) {
				return e.getWeight();
			}
		}
		return -1;
	}
	
	public int ifEdgeExists(Node<E> n) {
		for (Edge<E> e : edges) {
			if (e.getNode().equals(n)) {
				return e.getWeight();
			}
		}
		return -1;
	}
	
	@Override
	public boolean equals (Object o) {
		if (this.getClass().equals(o.getClass())) {
			@SuppressWarnings("unchecked")
			Node<E> n = (Node<E>) o;
			return this.name.equals(n.name);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return (String) this.name;
	}
}
