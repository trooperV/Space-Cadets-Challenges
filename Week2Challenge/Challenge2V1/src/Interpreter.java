import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	Hashtable<String, Integer> variables = new Hashtable<String, Integer>();
	BufferedReader br;
	int numberOfLines;
	
	String clearV = "(^|)clear (.+);";
	String incrV = "(^|)incr (.+);";
	String decrV = "(^|)decr (.+);";
	String whileS = "while (.) not ([0-9]+) do(;)";
	String endS = "end;";
	Pattern c = Pattern.compile(clearV);
	Pattern i = Pattern.compile(incrV);
	Pattern d = Pattern.compile(decrV);
	Pattern s = Pattern.compile(whileS);
	Pattern e = Pattern.compile(endS);
	Matcher m = null;
	
	public Interpreter(String filepath) throws Exception {
		file  = new File(filepath);
		printSource();
		putIntoHashtable();
	}
	
	public void displayVariableHashtable() {
		for (Map.Entry<String, Integer> entry : variables.entrySet()) {
		    String key = entry.getKey();
		    int value = entry.getValue();
		    System.out.println ("Variable Name: " + key + " Value: " + value);
		}
	}
	
	public void solver() {
		boolean shouldBr = false;
		for (int j = 0; j < lines.size(); j++) {
			m = c.matcher(lines.get(j));
			if(m.find()) {
				variables.putIfAbsent(m.group(2), 0);
				System.out.println("Init V: " + m.group(2) + "=0");
			}
			
			m = i.matcher(lines.get(j));
			if(m.find()) {
				variables.put(m.group(2), variables.get(m.group(2)).intValue()+1);
				System.out.println("Variable: " + m.group(2) + "=" + (variables.get(m.group(2))-1)+ "; " + m.group(2) + "++ ->" + variables.get(m.group(2)));
			}
			
			m = d.matcher(lines.get(j));
			if(m.find()) {
				variables.put(m.group(2), variables.get(m.group(2)).intValue()-1);
				System.out.println("Variable: " + m.group(2) + "=" + (variables.get(m.group(2))+1)+ "; " + m.group(2) + "-- ->" + variables.get(m.group(2)));
			}

			//fix this
			//shouldBr = false;
			int fakeWhileC = 0;
			m = s.matcher(lines.get(j));
			if(m.find()) {
				whileLoops.push(new WhileSet(m.group(1), Integer.parseInt(m.group(2)), j));
				System.out.println("pushing while: " + whileLoops.peek().getVaribale() + " = " + variables.get(whileLoops.peek().getVaribale()) + "; while " + whileLoops.peek().getVaribale() + " not " + whileLoops.peek().getValue() + " :" + (whileLoops.peek().getLineNumber()+1));
				if(whileLoops.peek().getValue() == variables.get(whileLoops.peek().getVaribale())) {
					for (int i = j+1; i < lines.size(); i++) {
						m = s.matcher(lines.get(i));
						if(m.find()) {
							fakeWhileC++;
						}
						
						m = e.matcher(lines.get(i));
						if(m.find()) {
							if(fakeWhileC == 0) {
								j = i;
								System.out.println("popping " + (whileLoops.peek().getLineNumber()+1) + " - " + whileLoops.peek().getVaribale() + " " + whileLoops.peek().getValue());
								whileLoops.pop();
								//shouldBr = true;
								break;
							}
							fakeWhileC--;
						}
					}
					continue;
				}
			}
			
			//if(shouldBr) break;
			//if the while is done before even going into it it will loop forever - fix this
			
			m = e.matcher(lines.get(j));
			if(m.find()) {
				//System.out.println(whileLoops.peek().getVaribale() + " :" + variables.get(whileLoops.peek().getVaribale()) + ": " + whileLoops.peek().getLineNumber());
				if(whileLoops.peek().getValue() != variables.get(whileLoops.peek().getVaribale())) {
					System.out.println(whileLoops.peek().getVaribale() + "=" + variables.get(whileLoops.peek().getVaribale()) + " Line: " + (whileLoops.peek().getLineNumber()+1));
					j = whileLoops.peek().getLineNumber();
				}else {
					System.out.println("popping " + (whileLoops.peek().getLineNumber()+1) + " - " + whileLoops.peek().getVaribale() + " " + whileLoops.peek().getValue());
					whileLoops.pop();
				}
			}
		}
		displayVariableHashtable();
	}
	
	public void printSource() throws Exception {
		br = new BufferedReader(new FileReader(file));
		System.out.println("This is the source code: ");
		System.out.println();
		String line;
		while((line = br.readLine()) != null) {
			System.out.println(line);
		}
		System.out.println();
	}
	
	public void putIntoHashtable() throws Exception {
		br = new BufferedReader(new FileReader(file));
		String line;
		int lineNumber = 0;
		while((line = br.readLine()) != null) {
			lines.putIfAbsent(lineNumber, line);
			lineNumber++;
		}
		this.numberOfLines = lines.size();
	}

	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Specify source code file: ");
		Interpreter inter = new Interpreter(scanner.nextLine());
		inter.solver();
		scanner.close();
	}

}
