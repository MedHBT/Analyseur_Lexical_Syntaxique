public class RegularExpressionNotAcceptedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4824501132131532365L;
	
	public RegularExpressionNotAcceptedException(){
		System.out.println("L'expression r�guli�re saisie n'est pas accept�");
	}
	
	public RegularExpressionNotAcceptedException(String msg){
		System.out.println("L'expression a �chou� au niveau de "+msg);
	}

}
