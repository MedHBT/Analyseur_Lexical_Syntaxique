import java.util.Stack;


public class SyntaxAnalyser{
	/*
	 * Grammar :
	 * 
	 * A->BC
	 * C->+BC|eps
	 * B->DE
	 * E->.DE|eps
	 * D->FG
	 * G->*G|eps
	 * F->id|(A)
	 * */	
	String symbole;
	private boolean NPE ;
	@Override
	public String toString() {
		return "SyntaxAnalyser [symbole=" + symbole + ", NPE=" + NPE
				+ ", lexana=" + lexana + ", lexem=" + lexem + ", re=" + re
				+ ", pile=" + pile + "]";
	}

	private LexicalAnalyser lexana;
    private Lexem lexem;
    private String re;
    private Stack<String> pile;
    private Tree tree;
    public Tree getTree() {
		return tree;
	}

	private Stack<Tree> pileT;
    
    private Stack<Automate> pileA;
    private Automate A;
	
	public SyntaxAnalyser(){
		this.lexana=new LexicalAnalyser(new String());
		initConstructor();		
	}
	public SyntaxAnalyser(String source){
		this.lexana=new LexicalAnalyser(source);
		initConstructor();
	}
	public SyntaxAnalyser(LexicalAnalyser la){
		this.lexana=new LexicalAnalyser(la);
		initConstructor();
	}
	
	private void initConstructor(){
	    
		try{
			NPE = true;
			re = "";
	        pile =new Stack<String>();
	        pileA=new Stack<Automate>();	
	        pileT =new Stack<Tree>();
	        tree = new Tree();
	        this.symbole=nextSymbole();
	        A();
			constructTree("S", "first");
	        
	        if(this.lexem.getId().equals("$") && NPE){
	        	this.A=pileA.pop();
	        	this.tree=pileT.pop();
	        	System.out.println("L'automate a été construit avec succés");
	        }
	        else{
	        	throw new RegularExpressionNotAcceptedException();
	        }
		}
		catch(RegularExpressionNotAcceptedException REE){
        	System.out.println("La construction de l'automate a échoué");
		}
	}
	
	private void A()throws RegularExpressionNotAcceptedException{
		B();
		C();
		constructTree("A", "");
	}
	
	private void B() throws RegularExpressionNotAcceptedException{
		D();
		E();
        constructTree("B", "");
	}
	
	private void C() throws RegularExpressionNotAcceptedException{
		if(symbole.equals("+")){
			this.accept(this.lexem);
			B();
			C();
			Automate temp2=pileA.pop();
			Automate temp1=pileA.pop();
			pileA.push(new Automate(temp1,temp2,"+"));
	        constructTree("C", "");
	        constructTree("C", "+");
		}
		else{
			if(this.lexem.getId().equals("ID")){
				NPE=false;
				throw new RegularExpressionNotAcceptedException("OR");
			}
			else{
				constructTree("C", "EPS");
			}
		}
	}
	
	private void D()throws RegularExpressionNotAcceptedException{		
		F();
		G();
		constructTree("D", "");
	}
	
	private void E()throws RegularExpressionNotAcceptedException{
		if(symbole.equals(".")){
			this.accept(this.lexem);
			D();
			E();
			Automate temp2=pileA.pop();
			Automate temp1=pileA.pop();
			pileA.push(new Automate(temp1,temp2,"."));
			constructTree("E","");
			constructTree("E",".");
		}
		else{
			if(this.lexem.getId().equals("ID")){
				NPE=false;
				throw new RegularExpressionNotAcceptedException("AND");
			}
			else{
	        	constructTree("E", "EPS");
			}
		}
	}
	
	private void F()throws RegularExpressionNotAcceptedException{
		if(this.lexem.getId().equals("OPAR")){
			this.accept(this.lexem);
			A();
			if(this.lexem.getId().equals("CPAR")){
				this.accept(this.lexem);
                constructTree("F", ")");
			}
		}
		else{
			//accepter les id
			if(this.lexem.getId().equals("ID")){
	            constructTree("F", lexem.getValue());
				pileA.push(new Automate(this.lexem.getValue()));
				this.accept(this.lexem);
			}
		}
	}
	
	private void G()throws RegularExpressionNotAcceptedException{
		if(symbole.equals("*")){
			this.accept(this.lexem);
			G();
            constructTree("G", "*");
			pileA.push(new Automate(pileA.pop(),"*"));
		}
		else{
			if(this.lexem.getId().equals("ID")){
				NPE=false;
				throw new RegularExpressionNotAcceptedException();
			}
			else{
	        	constructTree("G", "EPS");
				}
		}
	}
	
	private String nextSymbole() throws RegularExpressionNotAcceptedException{
		this.lexem=this.lexana.nextLexem();
		return(this.lexem.getValue());
	}
	
	private void accept(Lexem l) throws RegularExpressionNotAcceptedException{  
        if(l.getId().equals("ERROR")){
            NPE = false;
            throw new RegularExpressionNotAcceptedException("accept");
        }
        if(l.getId().equals("OPAR")){
        	re += "(";
        }
        else if(l.getId().equals("CPAR")){
        	re += ")";
        }
        else{
        	re += l.getValue();
        }
        this.symbole = nextSymbole();
	}
	
	private void constructTree(String parent,String lexemValue){
		switch(lexemValue){
		case "+" :
			Node node1 = new Node("+");
			Tree tempT1 = pileT.pop();
			tempT1.getRoot().getChild().add(node1);
			tempT1.getRoot().getAction().setAction(parent);
			pileT.push(tempT1);
			break;
		case "." :
			Node node2 = new Node(".");
			Tree tempT2 = pileT.pop();
			tempT2.getRoot().getChild().add(node2);
			tempT2.getRoot().getAction().setAction(parent);
			pileT.push(tempT2);
			break;
		case "*" :
			Tree tempT3 = new Tree(pileT.pop(),"*",parent);
			pileT.push(tempT3);
			break;
		case "first" :
			Tree tempT4 = new Tree(pileT.pop(),parent);
			tempT4.getRoot().getAction().setAction(parent);
			pileT.push(tempT4);
			break;
		case ")" :
			Tree tempT5 = new Tree(pileT.pop(),"()",parent);
			pileT.push(tempT5);
			break;
		case ""  :
			Tree tempT6 = new Tree(pileT.pop(),pileT.pop(),parent);
			pileT.push(tempT6);			
			break;
		default :
			Tree tempT7 = new Tree(parent,lexemValue);
			pileT.push(tempT7);
		}
	}
	
	public Automate getAutomate() {
		return A;
	}

}
