package AST;

public class AST_DEC_LIST extends AST_Node {

	public AST_DEC head;
	public AST_DEC_LIST tail;

	public AST_DEC_LIST(AST_DEC head, AST_DEC_LIST tail) {
		if (tail != null) {
			System.out.print("====================== decs -> dec decs\n");
		}
		else {
			System.out.print("====================== decs -> dec      \n");
		}

		this.head = head;
		this.tail = tail;
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

}
