import java.util.*;

public class State<E> {
	private Node<E> currentNode;
	private State<E> prevState;
	private int gCost;
	private int fCost;
	private List<Job<E>> jobs;
	private int hValue;

	public State(Node<E> currentNode, int gCost, int hValue, State<E> prevState, List<Job<E>> jobs) {
		this.currentNode = currentNode;
		this.prevState = prevState;
		this.gCost = gCost;
		this.hValue = hValue;
		fCost = gCost+hValue;
		this.jobs = jobs;
	}
	
	public Node<E> getCurrNode() {
		return currentNode;
	}
	
	public int getGCost() {
		return gCost;
	}
	
	public State<E> getPrevState() {
		return prevState;
	}
	
	@Override
	public boolean equals (Object o) {
		if (this.getClass().equals(o.getClass())) {
			@SuppressWarnings("unchecked")
			State<E> s = (State<E>) o;
			//two states are equal if the have the same current node, previous node and gCost
			if (prevState != null && s.prevState != null) {
				return this.currentNode.equals(s.currentNode) && this.prevState.getCurrNode().equals(s.prevState.getCurrNode()) && this.gCost == s.gCost;
			} else {
				return this.currentNode.equals(s.currentNode);
			}
		}
		return false;
	}
	
	public void changeGCost(int cost) {
		gCost = cost;
	}

	public String getPathString(String s) {
		String returnString = currentNode.toString().concat(s);
		if (prevState != null) {
			return prevState.getPathString(" -> " + returnString);
		}
		return returnString;
	}
	
	public List<Job<E>> getJobs() {
		return jobs;
	}
	
	public void removeJob(Job<E> j) {
		jobs.remove(j);
	}
	
	public boolean isJobsEmpty() {
		return jobs.isEmpty();
	}
	
	public void addJob(Job<E> j) {
		jobs.add(j);
	}
	
	public void setGCost(int cost) {
		gCost = cost;
	}
	
	public boolean containsJob(Job<E> j) {
		return jobs.contains(j);
	}
	
	public boolean containsJobStart(Node<E> n) {
		for (Job<E> j : jobs) {
			if (j.getStart().equals(n)) {
				return true;
			}
		}
		return false;
	}
	
	public Node<E> getPrevNode() {
		return prevState.getCurrNode();
	}
	
	public int getFCost() {
		return fCost;
	}
	
	public void setFCost(int n) {
		fCost = n;
	}
	
	public int getHValue() {
		return hValue;
	}
	
	public void setHCost(int h) {
		hValue = h;
	}
}
