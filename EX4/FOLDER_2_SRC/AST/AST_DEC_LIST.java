package AST;
import TYPES.*;
import TEMP.*;

public class AST_DEC_LIST extends AST_Node {

	public AST_DEC head;
	public AST_DEC_LIST tail;

	public AST_DEC_LIST(AST_DEC head, AST_DEC_LIST tail, int lineNumber) {
		if (tail != null) {
			System.out.print("====================== decs -> dec decs\n");
		}
		else {
			System.out.print("====================== decs -> dec      \n");
		}

		this.head = head;
		this.tail = tail;

		this.setLineNumber(lineNumber);
	}


	public void PrintMe() {
		/***************************************/
		/* AST NODE TYPE = DEC LIST (AST NODE) */
		/***************************************/
		System.out.print("AST NODE: DEC LIST\n");

		/***************************************/
		/* RECURSIVELY PRINT head and tail ... */
		/***************************************/
		if (head != null) head.PrintMe();
		if (tail != null) tail.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\nLIST");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
		if(tail != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
		}
	}
	
	public TYPE SemantMe() throws SemantException
	{
		if(head instanceof AST_FUNCDEC){
			AST_FUNCDEC funcDec = (AST_FUNCDEC)head;
			funcDec.SemantMe(null); // there is no inclass parameter in this case
		}
		else if(head instanceof AST_VAR_DEC){
			AST_VAR_DEC varDec = (AST_VAR_DEC)head;
			varDec.SemantMe(null); //there is no inclass parameter in this case
		}
		else if (head instanceof AST_ARR_DEC){
			AST_ARR_DEC arrDec = (AST_ARR_DEC)head;
			arrDec.SemantMe();
		}
		else if (head instanceof AST_CLASSDEC){
			AST_CLASSDEC classDec = (AST_CLASSDEC)head;
			classDec.SemantMe();
		}

		if (tail != null){
			tail.SemantMe();
		}

		return null;
	}	
	
	public TEMP IRme()
	{
		if (head != null) head.IRme();
		if (tail != null) tail.IRme();
		
		return null;			
	}

}
