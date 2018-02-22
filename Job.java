
public class Job<E> {
	private Node<E> start;
	private Node<E> end;
	
	/**
	 * @param start The start location of a job
	 * @param end The end location of a job
	 */
	public Job(Node<E> start, Node<E> end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public boolean equals (Object o) {
		if (this.getClass().equals(o.getClass())) {
			@SuppressWarnings("unchecked")
			Job<E> j = (Job<E>) o;
			return this.start.equals(j.start) && this.end.equals(j.end);
		}
		return false;
	}
	
	public Node<E> getStart() {
		return start;
	}
	
	public Node<E> getEnd() {
		return end;
	}
	
	@Override
	public String toString() {
		return start.getName() + " to " + end.getName();
	}
}
