import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;



class Tree{
	private Node root;
	
	public Tree(){
		this.root = null;
	}
	
	
	
	public Node getRoot() {
		return root;
	}



	public void setRoot(Node root) {
		this.root = root;
	}



	public Tree(String parent,String child){
		Node par = new Node(parent);
		Node chi = new Node(child);
		if(!child.equals("EPS"))
			par.getAction().setAction(parent);
		par.getChild().add(chi);
		root = par;
	}
	
	public Tree(Tree t,String parent){
		Node par = new Node(parent);
		par.getChild().add(t.getRoot());
		root = par;
	}
	
	public Tree(Tree t,String type,String parent){
		if(type.equals("()")){
			Node par = new Node(parent);
			Node opar = new Node("(");
			Node cpar = new Node(")");
			par.getChild().add(cpar);
			par.getChild().add(t.getRoot());
			par.getChild().add(opar);
			root = par;
		}
		else if(type.equals("*")){
			Node par = new Node(parent);
			Node star = new Node("*");
			par.getAction().setAction(parent);
			par.getChild().add(t.getRoot());
			par.getChild().add(star);
			root = par;
		}
	}
	
	public Tree(Tree t1,Tree t2,String parent){
		//System.out.println(t1.getRoot().getAction().getAction());
		Node par = new Node(parent);
		par.getAction().setAction(parent);
		par.getChild().add(t1.getRoot());
		par.getChild().add(t2.getRoot());
		for(int i = 0;i<par.getChild().size();i++){
			for(int j = i+1;j<par.getChild().size();j++){
				if(par.getChild().elementAt(i).getId().equals(par.getChild().elementAt(j).getId())){
					par.getChild().elementAt(i).getChild().addAll(par.getChild().elementAt(j).getChild());
					par.getChild().remove(par.getChild().elementAt(j));
					j--;
				}
			}
		}
		root = par;
	}
	
	public String toString(){
		StringBuilder output = new StringBuilder();
		Node n = root;
		getString(n, output);
		return output.toString();
		
	}
	
	private void getString(Node cur,StringBuilder st){
		if(cur.getChild() != null){
			st.append("{");
			st.append(cur.getId());
			st.append(cur.toString());
			st.append("}");
			Vector<Node> son = cur.getChild();
			for(int i = 0;i<son.size();i++){
				getString(son.elementAt(i),st);
			}
		}
		
	}
	
	public void drawTree(){
		try {
		    PrintWriter out = new PrintWriter("tree.dot");
		    out.println("strict graph {\n");
		    StringBuilder st = new StringBuilder();
		    Node first = root;
		    generateTreeScript(0,0,st,first);
		    out.println(st.toString());	    
		    out.println("}");
		    out.close();
		}
		catch (FileNotFoundException e) {
		    throw new Error("Error in file while openning");
		} 
		
		try { 
			String cmd1[] = {"cmd", "/c","dot -Tpng tree.dot -o tree.png"};
			String cmd2[] = {"cmd", "/c","del tree.dot"};
	        Process process1 = new ProcessBuilder(cmd1).start(); 
	        process1.waitFor();
	        Process process2 = new ProcessBuilder(cmd2).start(); 
	        process2.waitFor();
	    } catch (IOException | InterruptedException ex) {  
	    	//Logger.getLogger(Shell.class.getName()).log(Level.SEVERE, null, ex);  
	    }  
	}
	
	public void drawTreeD(){
		try {
		    PrintWriter out = new PrintWriter("treeD.dot");
		    out.println("strict graph {");
		    StringBuilder st = new StringBuilder();
		    StringBuilder st2 = new StringBuilder();
		    Node first = root;
		    generateTreeScriptD(0,0,st,st2,first);
		    out.println(st.toString());	  
		    out.println(st2.toString());
		    out.println("}");
		    out.close();
		}
		catch (FileNotFoundException e) {
		    throw new Error("Error in file while openning");
		} 
		
		try { 
			String cmd1[] = {"cmd", "/c","dot -Tpng treeD.dot -o treeD.png"};
			String cmd2[] = {"cmd", "/c","del treeD.dot"};
	        Process process1 = new ProcessBuilder(cmd1).start(); 
	        process1.waitFor();
	        Process process2 = new ProcessBuilder(cmd2).start(); 
	        process2.waitFor();
	    } catch (IOException | InterruptedException ex) { 
	    	System.out.println("Error while generating tree.png"+ex.getMessage());
	    	//Logger.getLogger(Shell.class.getName()).log(Level.SEVERE, null, ex);  
	    }  
	}
	
	private void generateTreeScriptD(int node,int numbOfChild,StringBuilder st,StringBuilder st2,Node first){
		int i = first.getChild().size()-1;
		int child = numbOfChild+1;
		boolean action = true;
		while(!first.getChild().isEmpty() && i>-1){
			
			st.append(node + "[label=\"" + first.getId() + "\"];\n");
			st.append(child + "[label=\"" + first.getChild().elementAt(i).getId() + "\"];\n");
			st.append(node + " -- " + child + ";\n");
			if(!first.getAction().getAction().equals("") && action){
				st2.append((child*4-1) + "[label=\"" + first.getAction().getAction() + "\",color=red];\n");
				st2.append(node + " -- " + (child*4-1) + " [style=dotted,color=red];\n");
				action = false;
			}
			generateTreeScriptD(child,child*4+1,st,st2,first.getChild().elementAt(i));
			child++;
			i--;
		}
	}
	
	private void generateTreeScript(int node,int numbOfChild,StringBuilder st,Node first){
		int i = first.getChild().size()-1;
		int child = numbOfChild+1;
		while(!first.getChild().isEmpty() && i>-1){
			st.append(node + "[label=\"" + first.getId() + "\"];\n");
			st.append(child + "[label=\"" + first.getChild().elementAt(i).getId() + "\"];\n");
			st.append(node + " -- " + child + ";\n");
			generateTreeScript(child,child*4+1,st,first.getChild().elementAt(i));
			child++;
			i--;
		}
	}
}