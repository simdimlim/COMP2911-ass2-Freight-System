import java.util.*;

public class StateComparator<E> implements Comparator<State<E>> {

	@Override
	public int compare(State<E> s1, State<E> s2) {
		if (s1.getFCost() < s2.getFCost()) {
			return -1;
		}
		if (s1.getFCost() > s2.getFCost()) {
			return 1;
		}
		return 0;
	}

}
