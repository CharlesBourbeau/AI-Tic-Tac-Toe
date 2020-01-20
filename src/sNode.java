import java.util.ArrayList;
import java.util.List;

public class sNode {
	State pState;
	sNode next;
	List<sNode> child_nodes = new ArrayList<>();
	
	public sNode(State aState) {
		pState = aState;
	}

}
