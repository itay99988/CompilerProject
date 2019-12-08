package AST;

public class AST_BINOP extends AST_DEC {

	public enum Op {
		PLUS,
		MINUS,
		TIMES,
		DIVIDE,
		GT,
		LT,
		EQ
	};

	public Op op;
	
	public AST_BINOP(Op op){
		this.op = op;

		System.out.format("====================== binOp -> %s\n", this);
	}
	

	public void PrintMe() {
		/*****************************/
		/* AST NODE TYPE = AST BINOP */
		/*****************************/
		System.out.format("AST NODE: binOp( %s )", this);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("binOp\n( %s )", this));
	}


	public String toString() {
		switch(op) {
			case PLUS:
				return "PLUS";
			case MINUS:
				return "MINUS";
			case TIMES:
				return "TIMES";
			case DIVIDE:
				return "DIVIDE";
			case GT:
				return "GT";
			case LT:
				return "LT";
			case EQ:
				return "EQ";
			default: // shouldn't get here
				return null;
		}
	}
}
	
