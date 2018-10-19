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
	
	String clearV = "(^| )clear (.+);";
	String incrV = "(^| )incr (.+);";
	String decrV = "(^| )decr (.+);";
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
			if(m.find()) variables.putIfAbsent(m.group(2), 0);
			
			m = i.matcher(lines.get(j));
			if(m.find()) variables.put(m.group(2), variables.get(m.group(2)).intValue()+1);
			
			m = d.matcher(lines.get(j));
			if(m.find()) variables.put(m.group(2), variables.get(m.group(2)).intValue()-1);
			
			//fix this
			shouldBr = false;
			m = s.matcher(lines.get(j));
			if(m.find()) {
				whileLoops.push(new WhileSet(m.group(1), Integer.parseInt(m.group(2)), j));
				if(whileLoops.peek().getValue() == variables.get(whileLoops.peek().getVaribale())) {
					m = e.matcher(lines.get(j));
					for (int i = j; i < lines.size(); i++) {
						m = e.matcher(lines.get(i));
						if(m.find()) {
							j = i;
							whileLoops.pop();
							shouldBr = true;
							break;
						}
					}
				}
			}
			
			if(shouldBr) break;
			//if the while is done before even going into it it will loop forever - fix this (not anymore but still buggy).
			
			m = e.matcher(lines.get(j));
			if(m.find()) {
				if(whileLoops.peek().getValue() != variables.get(whileLoops.peek().getVaribale())) {
					j = whileLoops.peek().getLineNumber();
				}else {
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
