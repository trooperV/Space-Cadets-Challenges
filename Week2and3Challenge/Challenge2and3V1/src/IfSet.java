public class IfSet {
    int lineNumber;
    boolean goInto;

    public IfSet(int lineNumber, boolean goInto) {

        this.lineNumber = lineNumber;
        this.goInto = goInto;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public boolean getGoInto() {
        return goInto;
    }
}