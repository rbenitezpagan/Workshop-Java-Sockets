package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {

	private static PrintWriter out;
	private static BufferedReader in;

	public static void main(String[] args) {
		try {
            Socket clientSocket = new Socket("0.0.0.0", 5000);
            out = new PrintWriter(clientSocket.getOutputStream(), true);                   
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));   
            out.println("Hi...");
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	if(inputLine.equals("bye") || inputLine.equals("BYE")) {
            		out.println("It was nice talking to you... Bye...");
            		clientSocket.close();
            		break;
            	}
            	out.println(inputLine);
            }
		} catch (Exception e) {}

	}

}
