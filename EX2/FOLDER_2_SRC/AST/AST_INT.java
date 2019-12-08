package AST;

public class AST_INT extends AST_DEC {

	public int value;
	
	public AST_INT(int value) {
		this.value = value;

		System.out.format("====================== int -> INT( %d )\n", value);
	}


	public void PrintMe() {
		/***************************/
		/* AST NODE TYPE = AST INT */
		/***************************/
		System.out.format("AST NODE: INT( %d )\n", value);


		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("INT( %d )", value));

	}


	public String toString() {
		return "" + value;
	}
}
	
