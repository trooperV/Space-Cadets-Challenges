import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

	File file;
	Hashtable<Integer, String> lines = new Hashtable<Integer, String>();
	Stack<WhileSet> whileLoops = new Stack<WhileSet>();
	Stack<IfSet> ifSet = new Stack<IfSet>();
	Hashtable<String, Integer> variables = new Hashtable<String, Integer>();
	BufferedReader br;
	int numberOfLines;

	String ifP = "if ([0-9a-zA-Z]) ((?:>|<|==|<=|>=)) ([0-9a-zA-Z]) do;";
	String endIf = "endif;";

	String clearV = "(^|)clear (.+);";
	String incrV = "(^|)incr (.+);";
	String decrV = "(^|)decr (.+);";
	String whileS = "while (.) not ([0-9]+) do(;)";
	String endS = "end;";

	Pattern ifPatt = Pattern.compile(ifP);
	Pattern endIfPatt = Pattern.compile(endIf);

	Pattern c = Pattern.compile(clearV);
	Pattern i = Pattern.compile(incrV);
	Pattern d = Pattern.compile(decrV);
	Pattern s = Pattern.compile(whileS);
	Pattern e = Pattern.compile(endS);

	Matcher m = null;

	public Interpreter(String filepath) throws Exception {
		file = new File(filepath);
		printSource();
		putIntoHashtable();
	}

	public void displayVariableHashtable() {
		for (Map.Entry<String, Integer> entry : variables.entrySet()) {
			String key = entry.getKey();
			int value = entry.getValue();
			System.out.println("Variable Name: " + key + " Value: " + value);
		}
	}

	public void solver() {
		for (int j = 0; j < lines.size(); j++) {
			if (ifSet.isEmpty() || ifSet.peek().getGoInto()) {
				m = c.matcher(lines.get(j));
				if (m.find()) {
					variables.putIfAbsent(m.group(2), 0);
					System.out.println("Init V: " + m.group(2) + "=0");
				}

				m = i.matcher(lines.get(j));
				if (m.find()) {
					variables.put(m.group(2), variables.get(m.group(2)).intValue() + 1);
					System.out.println("Variable: " + m.group(2) + "=" + (variables.get(m.group(2)) - 1) + "; "
							+ m.group(2) + "++ ->" + variables.get(m.group(2)));
				}

				m = d.matcher(lines.get(j));
				if (m.find()) {
					variables.put(m.group(2), variables.get(m.group(2)).intValue() - 1);
					System.out.println("Variable: " + m.group(2) + "=" + (variables.get(m.group(2)) + 1) + "; "
							+ m.group(2) + "-- ->" + variables.get(m.group(2)));
				}

				int fakeWhileC = 0;
				m = s.matcher(lines.get(j));
				if (m.find()) {
					whileLoops.push(new WhileSet(m.group(1), Integer.parseInt(m.group(2)), j));
					System.out.println("pushing while: " + whileLoops.peek().getVaribale() + " = "
							+ variables.get(whileLoops.peek().getVaribale()) + "; while "
							+ whileLoops.peek().getVaribale() + " not " + whileLoops.peek().getValue() + " :"
							+ (whileLoops.peek().getLineNumber() + 1));
					if (whileLoops.peek().getValue() == variables.get(whileLoops.peek().getVaribale())) {
						for (int i = j + 1; i < lines.size(); i++) {
							m = s.matcher(lines.get(i));
							if (m.find()) {
								fakeWhileC++;
							}

							m = e.matcher(lines.get(i));
							if (m.find()) {
								if (fakeWhileC == 0) {
									j = i;
									System.out.println("popping " + (whileLoops.peek().getLineNumber() + 1) + " - "
											+ whileLoops.peek().getVaribale() + " " + whileLoops.peek().getValue());
									whileLoops.pop();
									break;
								}
								fakeWhileC--;
							}
						}
						continue;
					}
				}

				m = e.matcher(lines.get(j));
				if (m.find()) {
					if (whileLoops.peek().getValue() != variables.get(whileLoops.peek().getVaribale())) {
						System.out.println(
								whileLoops.peek().getVaribale() + "=" + variables.get(whileLoops.peek().getVaribale())
										+ " Line: " + (whileLoops.peek().getLineNumber() + 1));
						j = whileLoops.peek().getLineNumber();
					} else {
						System.out.println("popping " + (whileLoops.peek().getLineNumber() + 1) + " - "
								+ whileLoops.peek().getVaribale() + " " + whileLoops.peek().getValue());
						whileLoops.pop();
					}
				}
			}

			m = ifPatt.matcher(lines.get(j));
			if (m.find()) {
				ifGen(m.group(1), m.group(3), m.group(2), j);
			}

			m = endIfPatt.matcher(lines.get(j));
			if (m.find()) {
				ifSet.pop();
			}
		}
		displayVariableHashtable();
	}

	public void printSource() throws Exception {
		br = new BufferedReader(new FileReader(file));
		System.out.println("This is the source code: ");
		System.out.println();
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		System.out.println();
	}

	public void putIntoHashtable() throws Exception {
		br = new BufferedReader(new FileReader(file));
		String line;
		int lineNumber = 0;
		while ((line = br.readLine()) != null) {
			lines.putIfAbsent(lineNumber, line);
			lineNumber++;
		}
		this.numberOfLines = lines.size();
	}

	public boolean ifGen(String A, String B, String sign, int lineNumber) {
		int a = 0;
		if (variables.get(A) != null) {
			a = variables.get(A);
		} else {
			a = Integer.valueOf(A);
		}

		int b = 0;
		if (variables.get(B) != null) {
			b = variables.get(B);
		} else {
			b = Integer.valueOf(B);
		}

		boolean goInto = false;
		switch (sign) {
		case ">":
			if (a > b)
				goInto = true;
			break;
		case "<":
			if (a < b)
				goInto = true;
			break;
		case ">=":
			if (a >= b)
				goInto = true;
			break;
		case "<=":
			if (a <= b)
				goInto = true;
			break;
		case "==":
			if (a == b)
				goInto = true;
			break;
		default:
			break;
		}

		ifSet.push(new IfSet(lineNumber, goInto));

		return goInto;
	}

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Specify source code file: ");
		Interpreter inter = new Interpreter(scanner.nextLine());
		inter.solver();
		scanner.close();
	}

}
