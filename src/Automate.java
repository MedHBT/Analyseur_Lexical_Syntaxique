import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class Automate {
	private int numState;
	private String name;
	private Vector<State> states;
	private Vector<String> alphabet;
	private Vector<Vector<String>> transitions;
	private State initialState;
	private Vector<State> finalStates;
	
	/*public Automate(){
		numState = 1;
		this.name = "M";
		this.states = new Vector<State>();
		this.alphabet = new Vector<String>();
		this.transitions = new Vector<Vector<String>>();
		this.initialState =new State(0);
		this.finalStates = new Vector<State>();
	}*/
	
	public Automate(String terminal){
		numState = 1;
		this.name = "M";	
		this.states = new Vector<State>();
		this.states.add(0,new State(this.getStateIdAvailable(this.states)));
		this.states.get(0).setStart(true);
		this.states.add(1,new State(this.getStateIdAvailable(this.states)));
		this.states.get(1).setEnd(true);
		this.states.get(0).setTransition(this.states.get(1), terminal);
		
		this.alphabet = new Vector<String>();
		this.alphabet.add(terminal);
		
		this.transitions = new Vector<Vector<String>>();
		
		this.initialState = this.states.get(0);
		
		this.finalStates = new Vector<State>();
		this.finalStates.add(this.states.get(1));
	}
	
	public Automate(Automate A){
		numState = 1;
		this.name = "M";
		this.states = A.getStates();
		this.alphabet = A.getAlphabet();		
		this.transitions = A.getTransitions();	
		this.initialState = A.getInitialState();
		this.finalStates = A.getFinalStates();
	}
	
	public Automate(Automate A,String s) throws ExceptionInInitializerError{
		numState = 1;
		this.name = "M";
		if(s.equals("*")){
			this.states = A.getStates();
			this.alphabet = A.getAlphabet();
			
			this.initialState =new State(this.getStateIdAvailable(this.states));
			this.initialState.setStart(true);
			this.initialState.setEnd(true);
			
			int idInitial = A.getInitialState().getId();
			
			State tempS ;
			
			for(int j=0;j<this.states.size();j++){
				if(this.states.get(j).getId()==idInitial){
					tempS = this.states.get(j);
					Enumeration<String> e1=this.states.get(j).getTransitions().keys();
					Enumeration<Vector<State>> e2=this.states.get(j).getTransitions().elements();					
					while(e1.hasMoreElements()){
						String tempKey=e1.nextElement();
						Vector<State> tempVectorState=e2.nextElement();
						for(int i=0;i<tempVectorState.size();i++){
							this.initialState.setTransition(tempVectorState.elementAt(i), tempKey);
						}
					}
					for(int l=0;l<this.states.size();l++){	
						if(this.states.get(l).isEnd()){
							Enumeration<String> e3=tempS.getTransitions().keys();
							Enumeration<Vector<State>> e4=tempS.getTransitions().elements();
							while(e3.hasMoreElements()){
								String tempKey=e3.nextElement();
								Vector<State> tempVectorState=e4.nextElement();
								for(int m=0;m<tempVectorState.size();m++){
									this.states.get(l).setTransition(tempVectorState.get(m), tempKey);
								}
							}
						}
					}
					break;
				}
			}
			
			this.states.add(this.initialState);			
			
			this.transitions = A.getTransitions();
			
			this.finalStates = A.getFinalStates();
			this.finalStates.add(this.initialState);
		}
		else{
			throw new ExceptionInInitializerError();
		}
	}
	
	public Automate(Automate A,Automate B,String Op)throws ExceptionInInitializerError{
		numState = 1;
		this.name = "M";
		State ISA,ISB = null;
		Vector<State> StatesTraite;
		int tempSizeA = A.getStates().size();
		switch(Op){
		case "+":
			this.states = A.getStates();
			this.getStateByID(A.getInitialState().getId()).setStart(false);
			StatesTraite=new Vector<State>();
			for(int i=0;i<B.getStates().size();i++){
				if(!StatesTraite.contains(B.getStates().elementAt(i))){
					B.getStates().elementAt(i).setId(tempSizeA+i);
					//Enumeration<String> e1 = B.getStates().elementAt(i).getTransitions().keys();
					Enumeration<Vector<State>> e2 = B.getStates().elementAt(i).getTransitions().elements();
					while(e2.hasMoreElements()){
						//String tempKey =e1.nextElement();
						Vector<State> tempV =e2.nextElement();
						for(int j=0;j<tempV.size();j++){
							tempV.get(j).setId(tempSizeA+tempV.get(j).getId());
							StatesTraite.add(tempV.get(j));
						}
					}
					StatesTraite.add(B.getStates().elementAt(i));
				}
			}
			for(int i=0;i<B.getStates().size();i++){
				B.getStates().get(i).setStart(false);
				this.states.add(B.getStates().get(i));
				if(B.getStates().get(i).getId()==B.getInitialState().getId()){
					ISB=B.getStates().get(i);
				}
			}
			this.initialState = new State(this.getStateIdAvailable(this.states));
			
			ISA=this.getStateByID(A.getInitialState().getId());
			
			if(ISA != null){
				Enumeration<String> e1=ISA.getTransitions().keys();
				Enumeration<Vector<State>> e2=ISA.getTransitions().elements();
				while(e1.hasMoreElements()){
					String tempKey = e1.nextElement();
					Vector<State> tempV = e2.nextElement();
					for(int j = 0 ;j<tempV.size() ; j++){
						this.initialState.setTransition(tempV.get(j), tempKey);
					}
				}
			}
			if(ISB != null){
				Enumeration<String> e1=ISB.getTransitions().keys();
				Enumeration<Vector<State>> e2=ISB.getTransitions().elements();
				while(e1.hasMoreElements()){
					String tempKey = e1.nextElement();
					Vector<State> tempV = e2.nextElement();
					for(int j = 0 ;j<tempV.size() ; j++){
						this.initialState.setTransition(tempV.get(j), tempKey);
					}
				}
			}
			
			this.alphabet = A.getAlphabet();
			for(int j=0;j<B.getAlphabet().size();j++){
				if(!this.alphabet.contains(B.getAlphabet().get(j))){
					this.alphabet.add(B.getAlphabet().get(j));
				}
			}
			
			this.states.add(this.initialState);
			
			this.finalStates = new Vector<State>();
			for(int i =0;i<this.states.size();i++){
				if(this.states.elementAt(i).isEnd()){
					this.finalStates.add(this.states.elementAt(i));
				}
			}
			break;
		case ".":
			this.states = A.getStates();
			
			StatesTraite=new Vector<State>();
			for(int i=0;i<B.getStates().size();i++){
				if(!StatesTraite.contains(B.getStates().elementAt(i))){
					B.getStates().elementAt(i).setId(tempSizeA+i);
					//Enumeration<String> e1 = B.getStates().elementAt(i).getTransitions().keys();
					Enumeration<Vector<State>> e2 = B.getStates().elementAt(i).getTransitions().elements();
					while(e2.hasMoreElements()){
						//String tempKey =e1.nextElement();
						Vector<State> tempV =e2.nextElement();
						for(int j=0;j<tempV.size();j++){
							tempV.get(j).setId(tempSizeA+tempV.get(j).getId());
							StatesTraite.add(tempV.get(j));
						}
					}
					StatesTraite.add(B.getStates().elementAt(i));
				}
			}
			for(int i=0;i<B.getStates().size();i++){
				B.getStates().get(i).setStart(false);
				this.states.add(B.getStates().get(i));
				if(B.getStates().get(i).getId()==B.getInitialState().getId()){
					ISB=B.getStates().get(i);
				}
			}
			this.initialState = this.getStateByID(A.getInitialState().getId());
			
			for(int i=0;i<tempSizeA;i++){
				if(A.getStates().get(i).isEnd()){
					State s =this.getStateByID(A.getStates().get(i).getId());
					s.setEnd(false);
					Enumeration<String> e1 = ISB.getTransitions().keys();
					Enumeration<Vector<State>> e2 = ISB.getTransitions().elements();
					while(e1.hasMoreElements()){
						String tempKey =e1.nextElement();
						Vector<State> tempV=e2.nextElement();
						for(int j=0;j<tempV.size();j++){
							s.setTransition(tempV.get(j), tempKey);
						}
					}
				}
			}
			
			this.alphabet = A.getAlphabet();
			for(int j=0;j<B.getAlphabet().size();j++){
				if(!this.alphabet.contains(B.getAlphabet().get(j))){
					this.alphabet.add(B.getAlphabet().get(j));
				}
			}
			
			this.finalStates = new Vector<State>();
			for(int i =0;i<this.states.size();i++){
				if(this.states.elementAt(i).isEnd()){
					this.finalStates.add(this.states.elementAt(i));
				}
			}
			break;
		default:
		}
	}
	
	public void drawAutomata(String filename){
		try {
		    PrintWriter out = new PrintWriter(filename);
		    if(!this.initialState.isEnd()){
			    out.println("digraph {\n"+
						" rankdir=LR;\n"+
						" "+initialState.getId()+" [shape=circle,style=filled,color=gray];\n");
			}
		    else{
			    out.println("digraph {\n"+
						" rankdir=LR;\n"+
						" "+initialState.getId()+" [shape=doublecircle,style=filled,color=gray];\n");
		    }
		    
		    Hashtable<String, Vector<State>> initTransition = initialState.getTransitions();
		    Enumeration<Vector<State>> e1;
		    Enumeration<String> e2;
		    for(e1 = initTransition.elements(), e2 = initTransition.keys(); e1.hasMoreElements() ;){
				Vector<State> tempVector= (Vector<State>) e1.nextElement();
				String key = (String) e2.nextElement();
				for(int i = 0;i<tempVector.size();i++){
					out.println(" "+initialState.getId()+"->\""+tempVector.elementAt(i).getId()+"\"[label=\""+key+"\"];\n");
				}
		    }
		    
		    for(int i = 0;i<states.size();i++){
		    	State st = states.elementAt(i);
		    	if(!st.equals(initialState)){
		    		out.print(" \"" + st.getId() + "\" [");
		    		if(st.isEnd())
		    			out.print("shape=doublecircle");
		    		else
		    			out.print("shape=circle");
		    		out.println("];");
		    	}
		    }
		    
		    out.println();
		    
		    for(int i = 0;i<states.size();i++){
		    	State st = states.elementAt(i);
		    	if(!st.equals(initialState)){
		    		Enumeration<Vector<State>> e3 ;
		    		Enumeration<String> e4;
			    	for(e3 = st.getTransitions().elements() , e4 = st.getTransitions().keys(); e3.hasMoreElements() ;){
						Vector<State> tempVector= (Vector<State>) e3.nextElement();
						String key = (String) e4.nextElement();
						for(int j = 0;j<tempVector.size();j++){
							out.println(" \""+st.getId()+"\"->\""+tempVector.elementAt(j).getId()+"\"[label=\""+key+"\"];");
							
						}
			    	}
		    	}
		    }
		    
		    out.println("}");
		    out.close();
		}
		catch (FileNotFoundException e) {
		    throw new Error("Error in "+filename+" while openning");
		} 
		
		try{
			String cmd1[]={"cmd","/c","dot -Tpng automate.dot -o automate.png"};
			String cmd2[]={"cmd","/c","del automate.dot"};
			
			Process process1=new ProcessBuilder(cmd1).start();
			process1.waitFor();
			Process process2=new ProcessBuilder(cmd2).start();
			process2.waitFor();
		}catch(IOException |InterruptedException ex){
			//Logger.getLogger(shell.class.getName()).log(Level.SEVERE,null,ex);
			System.out.println(ex.getMessage());
		}
		
	}
	
	public State getStateByID(int ID){
		for(int i=0;i<this.states.size();i++){
			if(this.states.get(i).getId()==ID){
				return this.states.get(i);
			}
		}
		return null;
	}
	
	public boolean VerifiyIdAvailable(int a){
		for(int i=0;i<this.states.size();i++){
			if(this.states.elementAt(i).getId()==a){
				return false;
			}
		}
		return true;
	}
	
	public int getStateIdAvailable(Vector<State> V){
		int temp = 0;
		Vector<Integer> tempVector=new Vector<Integer>();
		for(int i=0;i<V.size();i++){
			tempVector.add(Integer.valueOf(V.get(i).getId()));
		}
		if(V.isEmpty()){
			return 0;			
		}
		else{
			while(tempVector.contains(Integer.valueOf(temp))){
				temp++;
			}
			return temp;
		}
	}

	public int getNumState() {
		return numState;
	}

	public String getName() {
		return name;
	}

	public Vector<State> getStates() {
		return states;
	}

	public Vector<String> getAlphabet() {
		return alphabet;
	}

	public Vector<Vector<String>> getTransitions() {
		return transitions;
	}

	public State getInitialState() {
		return initialState;
	}

	public Vector<State> getFinalStates() {
		return finalStates;
	}
	
	public String toString(){
		String output = name + "=({";
		for(int i = 0;i<states.size();i++){
			output += states.elementAt(i).getId();
			if(i<states.size()-1){output += ",";}
		}
		output += "},{";
		for(int i = 0;i<alphabet.size();i++){
			output += alphabet.elementAt(i);
			if(i<alphabet.size()-1){output += ",";}
		}
		output += "},{";
		for(int i =0;i<states.size();i++){
			output += states.elementAt(i).toString();
			if(i!=states.size()-1 && !states.elementAt(i).toString().isEmpty()){output += ",";}
		}
		output += "},";
		output += initialState.getId();
		output += ",{";
		for(int i = 0;i<finalStates.size();i++){
			output += finalStates.elementAt(i).getId();
			if(i!=finalStates.size()-1){output += ",";}
		}
		output += "})";
		return output;
		
	}

}
