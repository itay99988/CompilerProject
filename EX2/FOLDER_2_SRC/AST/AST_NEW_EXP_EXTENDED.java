package AST;

public class AST_NEW_EXP_EXTENDED extends AST_NEW_EXP
{
	AST_EXP s;
	
	public AST_NEW_EXP_EXTENDED (String type, AST_EXP size){
		super(type);
		s = size;
	}
	/*
	public void printMe(){
		System.out.println("print me AST_NEW EXP type: " + t + "size = ?\n");
	}*/
}
	
