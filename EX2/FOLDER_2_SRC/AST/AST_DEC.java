package AST;

public class AST_DEC extends AST_Node {

    public AST_DEC dec;

    public AST_DEC() {}


    public AST_DEC(AST_DEC dec) {
		this.dec = dec;        
        System.out.println("====================== dec -> " + this.dec);
    }


	public void PrintMe() {
		/**********************************/
		/* AST NODE TYPE = DEC (AST NODE) */
		/**********************************/
		System.out.print("AST NODE: DEC\n");
        
		/*****************************/
		/* RECURSIVELY PRINT dec ... */
		/*****************************/
		if (dec != null) dec.PrintMe();
        
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if(dec != null) {
			AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, dec.SerialNumber);
		}
	}

}
