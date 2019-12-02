package AST;

public class AST_VAR_DEC_EXP extends AST_VAR_DEC
{
	String t;
	String n;
	AST_EXP e;
	
	public AST_VAR_DEC_EXP(String type, String name, AST_EXP exp){
		t = type;
		n = name;
		e = exp;
	}
	
	public void printMe(){
		System.out.println("print me ast vardec exp\n");
	}
}
	
