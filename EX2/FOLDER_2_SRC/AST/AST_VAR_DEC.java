package AST;

public abstract class AST_VAR_DEC extends AST_DEC{

    public String name;
    public String type;

    public AST_VAR_DEC(String type, String name) {
        this.type = type;
        this.name = name;
    }

    
    public String toString() {
        return "varDec";
    }
}
