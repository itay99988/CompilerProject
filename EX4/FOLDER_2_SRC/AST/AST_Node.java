package AST;
import TEMP.*;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	public int LineNumber;

	public AST_Node() 
	{
        SerialNumber = AST_Node_Serial_Number.getFresh();
	}
	
	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}
	
	public void setLineNumber(int number)
	{
		this.LineNumber = number;
	}
	
	public int getLineNumber()
	{
		return this.LineNumber;
	}
}
