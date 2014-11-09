
public class Action {

	private String action;
	
	public Action(){
		this.action = "";
	}
	
	public void setAction(String nodeID){
		switch(nodeID){
		case "S":
			action = "{return pile.pop() ;}";
			break;
		case "A" :
			action = ""/*"{ACTION SEMANTIQUE DE A}"*/;
			break;
		case "B" :
			action = ""/*"{ACTION SEMANTIQUE DE B}"*/;
			break;
		case "C" :
			action = "{pile.push(new automate(pile.pop(),pile.pop(),+));}";
			break;
		case "D":
			action = ""/*"{ACTION SEMANTIQUE DE D}"*/;
			break;
		case "E":
			action = "{pile.push(new automate(pile.pop(),pile.pop(),.));}";
			break;
		case "F" :
			action = "{pile.push(new automate(lexem.getValue()));}";
			break;
		case "G" :
			action = "{pile.push(new automate(pile.pop(),*));}";
			break;
		}
	}

	public String getAction() {
		return action;
	}
	
	
}
