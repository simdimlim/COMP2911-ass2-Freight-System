
public class JobPriorityStrategy<E> implements HeuristicStrategy<E> {

	@Override
	public int findHeuristic(Job<E> j, State<E> s) {
		int h = s.getHValue();
		int edgeWeight = j.getStart().getNeighbourCost(j.getEnd());
		int f = h-edgeWeight;
		//if next node will complete a job, minus the entire edge cost and return smallest value for heuristic
		if (s.containsJob(j)) {
			return f;
		} else if (s.containsJobStart(j.getEnd())) {
			//if next node is the start of a job, minus half the edge cost and return
			return h-((int)edgeWeight/2);
		}
		//return original h value
		return h;
	}
	
}
