import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LexicalAnalyser {
	
	private static final String PATTERN = "[\\*|\\.|\\+|\\(|\\)|\\$]";
	
	private String source;
    private int startingLexem;
    private int step ;
    
    public LexicalAnalyser(String source){
        this.source = source+"$";
        startingLexem = 0;
    }
    
    public LexicalAnalyser(LexicalAnalyser La){
    	this.source=La.getSource();
    	this.startingLexem=0;
    }

    public Lexem nextLexem(){
    	int next = startingLexem;
    	String terminal = "";
    	step = 1;
    	Lexem lexem = new Lexem("","");
    	while(true){
    		char c = source.charAt(next);
    		switch(step){
    			case 1 :
    						if(!check(String.valueOf(c),PATTERN)){
    							step = 2;
                                lexem.setId("ID");
                                terminal += c;
                                next++;
                                break;

    						}
    						else if(c=='+') {
    							step = 4;
    							break;
    						}
    						else if(c=='.'){
    							step = 5;
    							break;
    						}
    						else if(c=='*'){
    							step = 6;
    							break;
    						}
    						else if(c=='('){
    							step = 7;
    							break;
    						}
    						else if(c==')'){
    							step = 8;
    							break;
    						}
    						else if(c=='$'){
    							startingLexem = next;
                                lexem.setId("$");
                                lexem.setValue(terminal);
                                return lexem;

    						}
    						else{
    							lexem.setId("ERROR") ;
                                lexem.setValue(c+""); 
                                return lexem;

    						}
    			
    			case 2 :
    						if(!check(String.valueOf(c),PATTERN)){
    							terminal += c;
    							next++;
    							break;
    						}
    						else{
    							step = 3;
    							break;
    						}
    			case 3 :
    						startingLexem = next;
    						lexem.setValue(terminal);
    						return lexem;  

    			case 4 :
    						lexem.setId("OPERATOR");
    						lexem.setValue("+");
    						startingLexem = ++next ;
    						return lexem;
    			case 5 :
    						lexem.setId("OPERATOR");
    						lexem.setValue(".");
    						startingLexem = ++next ;
    						return lexem;
    			case 6 :
    						lexem.setId("OPERATOR");
    						lexem.setValue("*");
    						startingLexem = ++next ;
    						return lexem;
    			case 7 :
    						lexem.setId("OPAR");
    						startingLexem = ++next ;
    						return lexem;
    			case 8 :
    						lexem.setId("CPAR");
    						startingLexem = ++next ;
    						return lexem;
    		}
    	}
    }
    
    private static boolean check(String toCheck,String pat){
		Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(toCheck);
        return matcher.matches();          
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}

