import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8080);

        System.out.println("Client connected to server!");

        // Setting up streams
        OutputStream outStr = socket.getOutputStream();
        InputStream inStr = socket.getInputStream();

        // setting up writer and reader for those streams
        BufferedReader readWords = new BufferedReader(new InputStreamReader(System.in));

        PrintWriter output = new PrintWriter(outStr, true);
        BufferedReader input = new BufferedReader(new InputStreamReader(inStr));

        String inputMsg = "";
        String outputMsg = "";
        while (!outputMsg.equals("quit") || !inputMsg.equals("quit")) {
            outputMsg = readWords.readLine();
            output.println(outputMsg);
            output.flush();

            inputMsg = input.readLine();
            if (inputMsg != null)
                System.out.println(inputMsg);
        }
        socket.close();
    }
}