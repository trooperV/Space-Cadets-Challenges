import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket serverS = new ServerSocket(8080);
        System.out.println("Server is connected!");

        Socket socket = serverS.accept();

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
            inputMsg = input.readLine();
            if (inputMsg != null)
                System.out.println("Client: " + inputMsg);

            outputMsg = readWords.readLine();
            output.println("Server: " + outputMsg);
            output.flush();
        }
        socket.close();
    }
}
