package AST;

public class AST_VAR_DEC_NEW_EXP extends AST_VAR_DEC
{
	String t;
	String n;
	AST_NEW_EXP ne;
	
	public AST_VAR_DEC_NEW_EXP(String type, String name, AST_NEW_EXP newExp){
		t = type;
		n = name;
		ne = newExp;
	}
	
	public void printMe(){
		System.out.println("print me ast vardec exp\n");
	}
}
	
