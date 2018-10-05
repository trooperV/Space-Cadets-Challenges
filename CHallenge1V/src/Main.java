import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		
		BufferedReader in = null;
		Scanner scanner = new Scanner(System.in);
		
		//regex
		String pattern = "(property=\"name\">)(.+)(</h1>)";
		Pattern p = Pattern.compile(pattern);
		
		//Input: Email ID + URL
		String emailId = scanner.nextLine();
		String url1 = "https://www.ecs.soton.ac.uk/people/";
		String realUrl = url1 + emailId;
		
		try {
			
			//URL + reading from it
			URL url = new URL(realUrl);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			
			String line;
		    boolean found = false;
		    Matcher m = null;
            while ((line = in.readLine()) != null) {
            	m = p.matcher(line);
            	if(m.find()) {
            		System.out.println(m.group(2));
            		found = true;
            	}
            }
            if(!found) System.out.println("Cannot access this person's name!");
            
            //close
            in.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		scanner.close();
	}
}