package ticTacToe_Server;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;                    // SERVER
import java.net.Socket;                          // Socket within SERVER
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;


public class Server extends JFrame{

  /**
   * 
   */
  private static final long serialVersionUID = 5170596293185257867L;

  private static JTextArea display;

  private static ServerSocket serverSocket;
  private static Socket connection1;
  private static Socket connection2;
  private static BufferedReader input1;
  private static BufferedReader input2;
  private static PrintWriter output1;
  private static PrintWriter output2;

  //  private static int plays;

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
      serverSocket = new ServerSocket(5555);

      display.setText("Waiting for connection..."
          + "\nIP Address: " + getLocalAddress()
          + "\nPort......: " + serverSocket.getLocalPort()
          + "\n");

      /* 
       * Here we link our server with our socket object.
       * Each ticTacToe_Server.accept(); connect ticTacToe_Server user to ticTacToe_Server socket.
       */
      connection1 = serverSocket.accept();
      connection1.setKeepAlive(true);
      writeInConsole("\nAccepted connection.");
      input1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
      output1 = new PrintWriter(connection1.getOutputStream(), true);
      writeInConsole("I/O Streams Created.");
      writeInConsole("Player 1 >>> Connected!");
      writeInConsole("Player 1 >>> IP Address: " + connection1.getInetAddress().getHostAddress());
      output1.println("O");

      connection2 = serverSocket.accept();
      connection2.setKeepAlive(true);
      writeInConsole("\nAccepted connection.");
      input2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
      output2 = new PrintWriter(connection2.getOutputStream(), true);
      writeInConsole("I/O Streams Created.");
      writeInConsole("Player 2 >>> Connected!");
      writeInConsole("Player 2 >>> IP Address: " + connection2.getInetAddress().getHostAddress());
      output2.println("X");

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
          } else if (message.contains("WON:")) {
            String[] arr = message.split(":");
            output1.println("Player " + arr[1] + " wins.");
          }
        } catch (Exception e) { writeInConsole(""+e);}

        try {
          message = (String) input2.readLine();
          if(message.contains("LCTN:")) {
            String[] arr = message.split(":");
            arr = arr[1].split(",");
            int x = Integer.parseInt(arr[0]);
            int y = Integer.parseInt(arr[1]);
            sendSelection(x, y, output1);
          } else if (message.contains("WON:")) {
            String[] arr = message.split(":");
            output1.println("Player " + arr[1] + " wins.");
          }
        } catch (Exception e) { writeInConsole(e.getStackTrace().toString()); }
      } while (!message.equals("EXIT"));
      closeResources();
    } catch(Exception e) {
      System.out.println(e);
    }
  }

  private void sendSelection(int x, int y, PrintWriter update) {
    try {
      writeInConsole(x + ","+ y);
      update.println("LCTN:" + x + "," + y);
      //      update.flush();

    } catch (Exception e) { writeInConsole(e.getStackTrace().toString()); }
  }

  private static void writeInConsole(String str) {
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

  private static InetAddress getLocalAddress(){
    try {
      Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
      while( b.hasMoreElements()){
        for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
          if ( f.getAddress().isSiteLocalAddress())
            return f.getAddress();
      }
    } catch (SocketException e) { writeInConsole(e.getStackTrace().toString()); }
    return null;
  }
}