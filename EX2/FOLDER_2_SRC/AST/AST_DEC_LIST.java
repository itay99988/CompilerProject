package AST;

public class AST_DEC_LIST extends AST_Node {

	public AST_DEC head;
	public AST_DEC_LIST tail;

	public AST_DEC_LIST(AST_DEC head, AST_DEC_LIST tail) {
		SerialNumber = AST_Node_Serial_Number.getFresh();

		if (tail != null) System.out.print("====================== decs -> dec decs\n");
		if (tail == null) System.out.print("====================== decs -> dec      \n");

		this.head = head;
		this.tail = tail;
	}


	public void PrintMe()
	{
	}

}
