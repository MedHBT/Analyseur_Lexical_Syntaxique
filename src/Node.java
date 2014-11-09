import java.util.Vector;


public class Node {

	private String id;
	private Vector<Node> child;
	private Action action;
	
	public Node(String id){
		this.id = id;
		this.child = new Vector<Node>();
		this.action = new Action();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public Vector<Node> getChild() {
		return child;
	}

	public void setChild(Vector<Node> child) {
		this.child = child;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}
	
	public void addChild(Node node){
		boolean exist = false;
		for(int i = 0;i<child.size();i++){
			if(child.elementAt(i).getId().equals(node.getId()))
				exist = true;
		}
		if(!exist){
			child.add(node);
		}
	}
	
	public String toString(){
		String output = "[";
		for(int i =0;i<child.size();i++){
			output += child.elementAt(i).getId() + " ";
		}
		output += "]";
		return output;
	}
}
