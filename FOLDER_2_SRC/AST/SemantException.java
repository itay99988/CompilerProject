package AST;

public class SemantException extends Exception {

    int lineNumber;
    String message;

    public SemantException(int line, String message) {
        this.lineNumber = line;
        this.message= String.format("ERROR(%d): %s", line, message);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public void setLineNumber(int line) {
        this.lineNumber = line;
    }
}
