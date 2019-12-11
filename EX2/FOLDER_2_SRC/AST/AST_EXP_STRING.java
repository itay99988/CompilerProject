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
		System.out.format("AST NODE: EXP_STR( %s )\n", this.str);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		this.str = this.str.replaceAll("\"", "");
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("EXP_STR( '%s' )", this.str));
	}
    
}