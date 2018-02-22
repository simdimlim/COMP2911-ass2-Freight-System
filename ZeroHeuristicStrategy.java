
public class ZeroHeuristicStrategy<E> implements HeuristicStrategy<E> {

	@Override
	public int findHeuristic(Job<E> j, State<E> s) {
		return 0;
	}

}
