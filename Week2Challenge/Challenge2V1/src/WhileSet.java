
public class WhileSet {
	String variable;
	int value;
	int lineNumber;
	public WhileSet(String variable, int value, int lineNumber) {
		this.variable = variable;
		this.value = value;
		this.lineNumber = lineNumber;
	}
	
	public String getVaribale() {
		return this.variable;
	}
	
	public int getValue() {
		return this.value;
	}

	public int getLineNumber() {
		return this.lineNumber;
	}
}
