package AST;

public class AST_ARR_DEC extends AST_DEC
{
	String t;
	String n;
	
	public AST_ARR_DEC(String type, String name){
		t = type;
		n = name;
	}
	
	public void printMe(){
		System.out.println("AST_ARR_DEC");
	}
}
	
