import java.util.*;

public class AdjacencyListGraph<E> implements Graph<E> {
	
	private List<Node<E>> nodes;
	
	public AdjacencyListGraph() {
		nodes = new LinkedList<Node<E>>();
	}
	
	@Override
	public void addNode(E n, int cost) {
		nodes.add(new Node<E>(n, cost));
	}

	@Override
	public void addEdge(Node<E> from, Node<E> to, int cost) {
		from.addEdge(to, cost);
	}

	@Override
	public Node<E> getNode(E n) {
		for (Node<E> nd : nodes) {
			if (nd.getName().equals(n)) {
				return nd;
			}
		}
		return null;
	}

	@Override
	public int ifEdgeExists(Node<E> from, Node<E> to) {
		for (Node<E> n : nodes) {
			if (n.equals(from)) {
				return n.ifEdgeExists(to);
			} else if (n.equals(to)) {
				return n.ifEdgeExists(from);
			}
		}
		return -1;
	}

}
