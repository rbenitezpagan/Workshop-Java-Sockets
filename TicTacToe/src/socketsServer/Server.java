package socketsServer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;                    // SERVER
import java.net.Socket;                          // Socket within SERVER

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class Server extends JFrame{

  private JTextArea display;

  private static ServerSocket serverSocket;
  private static Socket connection1;
  private static Socket connection2;
  private static BufferedReader input1;
  private static BufferedReader input2;
  private static PrintWriter output1;
  private static PrintWriter output2;

  public Server() {
    super("Server");
    Container c = getContentPane();
    display = new JTextArea();
    display.setEditable(false);
    DefaultCaret caret = (DefaultCaret) display.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    c.add(new JScrollPane(display), BorderLayout.CENTER);
    setSize(400,400);
    setVisible(true);
  }

  public void runServer() {
    try {
      /* Here we will instantiate our server.
       * 
       * And we are using the port 5555 to accept the incoming connection/s
       */
      serverSocket = new ServerSocket(0); 

      display.setText("Waiting for connection..."
          + "\nIP Address: " + serverSocket.getInetAddress().getHostAddress().toString()
          + "\nPort......: " + serverSocket.getLocalPort()
          + "\n");

      /* 
       * Here we link our server with our socket object.
       * Each serverSocket.accept(); connect a user to a socket.
       */
      connection1 = serverSocket.accept();
      connection1.setKeepAlive(true);
      writeInConsole("\nAccepted connection.");
      input1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
      output1 = new PrintWriter(connection1.getOutputStream(), true);
//      output1.flush();
      writeInConsole("I/O Streams Created.");
      writeInConsole("Player 1 >>> Connected!");
      writeInConsole("Player 1 >>> IP Address: " + connection1.getInetAddress().getHostName());
      output1.println("O");

//      connection2 = serverSocket.accept();
//      input2 = new ObjectInputStream(connection2.getInputStream());
//      output2 = new ObjectOutputStream(connection2.getOutputStream());
//      output2.flush();
//      display.append("Player 2 >>> Connected!");
//      display.append("Player 1 >>> IP Address: " + connection2.getInetAddress().getHostName());     
//      output2.writeObject("X");
//      output2.flush();

      String message = "";
      do {
        try {
          message = (String) input1.readLine();
          if(message.contains("LCTN:")) {
            String[] arr = message.split(":");
            arr = arr[1].split(",");
            int x = Integer.parseInt(arr[0]);
            int y = Integer.parseInt(arr[1]);
            sendSelection(x, y, output2);
          }
        } catch (Exception e) {}

//        try {
//          message = (String) input2.readObject();
//          if(message.contains("LCTN:")) {
//            String[] arr = message.split(":");
//            arr = arr[1].split(",");
//            int x = Integer.parseInt(arr[0]);
//            int y = Integer.parseInt(arr[1]);
//            sendSelection(x, y, output1);
//          }
//        } catch (Exception e) {}
      } while (!message.equals("EXIT"));
      closeResources();
    } catch(Exception e) {}
  }

  private void sendSelection(int x, int y, PrintWriter update) {
    try {
      writeInConsole(x + ","+ y);
//      update.writeObject((String) "LCTN:" + x + "," + y);
//      update.flush();
      
    } catch (Exception e) {}
  }
  
  private void writeInConsole(String str) {
    display.append("\n" + str);
  }

  private void closeResources() {
    try {
      input1.close();
      input2.close();
      output1.close();
      output2.close();
      connection1.close();
      connection2.close();
      serverSocket.close();
    } catch (IOException e) {}    
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.addWindowListener( new WindowAdapter() {
      public void windowClosing( WindowEvent evt) {
        System.exit(0);
      }
    });
    server.runServer();
  }
}