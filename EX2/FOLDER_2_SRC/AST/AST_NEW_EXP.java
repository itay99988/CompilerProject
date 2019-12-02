package AST;

public class AST_NEW_EXP extends AST_Node
{
	String t;
	
	public AST_NEW_EXP (String type){
		t = type;
	}
	
	public void printMe(){
		System.out.println("print me AST_NEW EXP type: " + t + "\n");
	}
}
	
