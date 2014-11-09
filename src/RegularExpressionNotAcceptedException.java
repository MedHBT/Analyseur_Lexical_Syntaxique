public class RegularExpressionNotAcceptedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4824501132131532365L;
	
	public RegularExpressionNotAcceptedException(){
		System.out.println("L'expression régulière saisie n'est pas accepté");
	}
	
	public RegularExpressionNotAcceptedException(String msg){
		System.out.println("L'expression a échoué au niveau de "+msg);
	}

}
