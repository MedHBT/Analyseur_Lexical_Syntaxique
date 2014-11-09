import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class State {
	
	private int id;
	private boolean start;
	private boolean end;
	private Hashtable<String, Vector<State>> transitions;
	
	public State(int id){
		this.id =id ;
		this.start =false;
		this.end =false;
		this.transitions = new Hashtable<String, Vector<State>>();
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setTransition(State state,String label){
		if(!transitions.containsKey(label)){
			Vector<State> tempVector = new Vector<State>();
			tempVector.add(state);
			transitions.put(label, tempVector);
		}
		else{
			this.transitions.get(label).add(state);
		}
	}
	
	public Hashtable<String, Vector<State>> getEndState(){
		
		Hashtable<String, Vector<State>> tempTable = new Hashtable<String, Vector<State>>();
		Enumeration<Vector<State>> e1;
		Enumeration<String> e2;
		for(e1 = transitions.elements() , e2 = transitions.keys(); e1.hasMoreElements() ;){
			Vector<State> tempVector2= (Vector<State>) e1.nextElement();
			String key = (String) e2.nextElement();
			Vector<State> tempVectorState = new Vector<State>();
			for(int i = 0;i<tempVector2.size();i++){
				State tempState = tempVector2.elementAt(i);
				if(tempState.isEnd()){
					tempVectorState.add(tempState);
				}
			}
			tempTable.put(key,tempVectorState);
		}
		return tempTable;
	}

	public int getId() {
		return id;
	}

	public Hashtable<String, Vector<State>> getTransitions() {
		return transitions;
	}

	public boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}
	
	//public String toString(){return String.valueOf(this.id);}
	
	public String toString(){
		String output = "";
		Enumeration<Vector<State>> e1;
		Enumeration<String> e2;
		for(e1 = transitions.elements() , e2 = transitions.keys(); e1.hasMoreElements() ;){
			Vector<State> tempVector1 = (Vector<State>) e1.nextElement();
			String key = (String) e2.nextElement();
			for(int i = 0;i<tempVector1.size();i++){
				output += "("+String.valueOf(id)+","+key+","+String.valueOf(tempVector1.elementAt(i).getId())+")";
				if(i<(tempVector1.size()-1) ){output += ",";}
			}
			if(e2.hasMoreElements()){output += ",";}
		}
		return output;
	}
	
}
