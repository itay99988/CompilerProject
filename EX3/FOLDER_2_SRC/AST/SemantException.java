package AST;

public class SemantException extends Exception {

    int line;
    String message;

    public SemantException(int line, String message) {
        this.line = line;
        this.message= String.format("ERROR(%d): %s", line, message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getLine() {
        return this.line;
    }
}
