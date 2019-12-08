package AST;

public class AST_EXP_STRING extends AST_EXP {

    public String str;


    public AST_EXP_STRING(String str) {
        this.str = str;

        System.out.format("====================== exp -> STRING( %s )\n", str);
    }


	public void PrintMe() {
		/**********************************/
		/* AST NODE TYPE = AST EXP STRING */
		/**********************************/
		System.out.format("AST NODE: EXP_STR( %s )\n", this);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("EXP_STR\n( %s )", this));
	}
    
}