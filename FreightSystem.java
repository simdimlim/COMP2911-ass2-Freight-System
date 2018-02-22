import java.io.*;
import java.util.*;

public class FreightSystem<E> {
	private AdjacencyListGraph<E> map;
	private List<Job<E>> jobs;
	private List<State<E>> states;
	private List<State<E>> closed;
	private int minPathCost;

	public FreightSystem() {
		map = new AdjacencyListGraph<E>();
		jobs = new LinkedList<Job<E>>();
		states = new ArrayList<State<E>>();
		closed = new ArrayList<State<E>>();
		minPathCost = 0;
	}
	
	public static void main(String[] args) {
		FreightSystem<String> fs = new FreightSystem<String>();
		Scanner sc = null;
		Scanner ls = null;
		try {
			sc = new Scanner(new FileReader(args[0]));
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				ls = new Scanner(line);
				while (ls.hasNext()) {
					String keyword = ls.next();
					if (keyword.equals("Unloading")) {
						int unloadingCost = ls.nextInt();
						String town = ls.next();
						//add node to map
						fs.map.addNode(town, unloadingCost);
					} else if (keyword.equals("Cost")) {
						int cost = ls.nextInt();
						String start = ls.next();
						String end = ls.next();
						Node<String> startNode = fs.map.getNode(start);
						Node<String> endNode = fs.map.getNode(end);
						//add edge to map in both directions
						fs.map.addEdge(startNode, endNode, cost);
						fs.map.addEdge(endNode, startNode, cost);
					} else if (keyword.equals("Job")) {
						String start = ls.next();
						String end = ls.next();
						Node<String> jobStart = fs.map.getNode(start);
						Node<String> jobEnd = fs.map.getNode(end);
						//add job to list of jobs that need to be done
						fs.addJob(jobStart, jobEnd);
						//add weight of job edge to minimum path cost which will be used when calculating the heuristic
						int edgeWeight = fs.map.ifEdgeExists(jobStart, jobEnd);
						if (edgeWeight > 0) {
							fs.minPathCost += edgeWeight;
						}
					} else if (keyword.equals("#")) {
						break;
					}
				}
			}
			//every journey will start at Sydney
			Node<String> start = fs.map.getNode("Sydney");
			State<String> finalPath = fs.aStarSearch(start);
			if (finalPath != null) {
				fs.printPath(finalPath);
			} else {
				System.out.println("No solution");
			}
		} catch (FileNotFoundException e) {}
		finally {
			if (sc != null)
				sc.close();
			if (ls != null)
				ls.close();
		}
	}

	/**
	 * Performs A* search on the map starting from the given node.
	 * 
	 * @param from The starting node
	 * @return A state containing the optimal path, null otherwise.
	 */
	public State<E> aStarSearch(Node<E> from) {
		Comparator<State<E>> comparator = new StateComparator<E>();
		//openSet will store states we still have to explore
		PriorityQueue<State<E>> openSet = new PriorityQueue<State<E>>(10, comparator);
		
		//initialise the first start from the start node
		State<E> initialState = new State<E>(from, 0, minPathCost, null, jobs);
		//add it into the openSet
		openSet.add(initialState);
		states.add(initialState);
		
		int nodesExpanded = 0;
		
		//initialise our heuristic function
		HeuristicContext<E> ctx = new HeuristicContext<E>();
//		ctx.setHeuristicStrategy(new ZeroHeuristicStrategy<E>());
		ctx.setHeuristicStrategy(new JobPriorityStrategy<E>());

		while (!openSet.isEmpty()) {
			nodesExpanded++;
			//remove the state with the smallest fCost
			State<E> curr = openSet.poll();
			Node<E> currNode = curr.getCurrNode();
			//add this state to the closed set
			closed.add(curr);
			
			// check if we've reached our goal state
			if (curr.getPrevState() != null) {
				//see if within this state's last trip whether it completed a job
				Job<E> j = new Job<E>(curr.getPrevState().getCurrNode(), currNode);
				
				//if yes, remove the job from the state's required jobs
				if (curr.containsJob(j)) {
					curr.setGCost(curr.getGCost()+j.getEnd().getCost());
					curr.removeJob(j);
				}
				
				//if there are no more jobs in the state's list, we have reached our goal state
				if (curr.isJobsEmpty()) {
					System.out.println(nodesExpanded + " nodes expanded");
					System.out.println("cost = " + curr.getGCost());
					return curr;
				}
			}
			
			//get the neighbours of the current state/node we have just removed from the openSet
			List<Node<E>> neighbours = currNode.getConnected();
			
			for (Node<E> n : neighbours) {
				ArrayList<Job<E>> currJobs = new ArrayList<Job<E>>(curr.getJobs());
				int tentative_gCost = curr.getGCost() + currNode.getNeighbourCost(n);
				//create a state from the current node to the neighbouring node
				State<E> s = new State<E>(n, tentative_gCost, curr.getHValue(), curr, currJobs);
				
				//calculate this state's heuristic value
				int h = ctx.calculateHeuristic(new Job<E>(currNode, n), s);
				
				//if this state completes a job or reaches a node that is the start of a job,
				//add it to the openSet if it isn't already there
				if (curr.containsJobStart(n) || curr.containsJob(new Job<E>(currNode, n))) {
					s.setFCost(tentative_gCost+h);
					s.setHCost(h);
					openSet.add(s);
					states.add(s);
					continue;
				}
				
				if (closed.contains(s)) {
					State<E> closedState = getState(s);
					//if this state is in the closedSet but gCost is cheaper than the state's current gCost,
					//remove this state from the closedSet and add it to the openSet with the cheaper gCost
					if (tentative_gCost < closedState.getGCost()) {
						State<E> newState = new State<E>(n, tentative_gCost, h, curr, currJobs);
						newState.setFCost(tentative_gCost+h);
						closed.remove(closedState);
						states.remove(closedState);
						openSet.add(newState);
						states.add(newState);
					}
				} else if (openSet.contains(s)) {
					//if this state is in the openSet but gCost is cheaper than the state's current gCost,
					//replace it with the state with the cheaper gCost
					State<E> openState = getState(s);
					if (tentative_gCost < openState.getGCost()) {
						State<E> newState = new State<E>(n, tentative_gCost, h, curr, currJobs);
						newState.setFCost(tentative_gCost+h);
						openSet.remove(openState);
						states.remove(openState);
						openSet.add(newState);
						states.add(newState);
					}
				} else if (!openSet.contains(s)) {
					//if this state is not yet on the openSet, add it
					s.setFCost(tentative_gCost+h);
					s.setHCost(h);
					openSet.add(s);
					states.add(s);
				}
			}
			
		}
		System.out.println(nodesExpanded + " nodes expanded");
		return null;
	}
	
	/**
	 * Adds a job to the FreightSystem's list of required jobs.
	 * 
	 * @param start The start location/node
	 * @param end The end location/node
	 */
	public void addJob(Node<E> start, Node<E> end) {
		jobs.add(new Job<E>(start, end));
	}
	
	/**
	 * Checks through all the current states made so far.
	 * 
	 * @param state The state to be checked 
	 * @return The state if it exists, null otherwise.
	 */
	public State<E> getState (State<E> state) {
		for (State<E> s : states) {
			if (s.equals(state)) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Prints the path taken to complete all the jobs.
	 * 
	 * @param s The final state with the path that completed all jobs
	 */
	public void printPath(State<E> s) {
		State<E> curr = s;
		LinkedList<State<E>> path = new LinkedList<State<E>>();
		while (curr.getPrevState() != null) {
			//store all nodes from the path into a list
			path.addFirst(curr);
			curr = curr.getPrevState();
		}
		
		List<Job<E>> jobsCopy = new LinkedList<Job<E>>(jobs);
		
		for (int i = 0; i < path.size(); i++) {
			State<E> currStatePath = path.get(i);
			Job<E> j = new Job<E>(currStatePath.getPrevNode(), currStatePath.getCurrNode());
			if (jobsCopy.contains(j)) {
				jobsCopy.remove(j);
				System.out.println("Job " + j.toString());
			} else {
				System.out.println("Empty " + j.toString());
			}
		}
	}
}
