package ticTacToe_Server;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * @author josue
 *
 */
public class GameController{

  protected static String[][] map;

  private static JTextArea display;
  private static ServerSocket gameServer;
  private static Socket connection1;
  private static Socket connection2;
  private static BufferedReader input1;
  private static BufferedReader input2;
  private static PrintWriter output1;
  private static PrintWriter output2;
  private static boolean keepGoing =  true;
  
  private static int exceptionsCatched = 0;

  /**
   * 
   */
  public GameController() {
    JFrame controllerFrame = new JFrame();
    controllerFrame.setTitle("Tic Tac Toe - Game Controller");
    controllerFrame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        if (JOptionPane.showConfirmDialog(controllerFrame, 
              "Are you sure you want to close the game controller?", 
              "Really Closing?",
              JOptionPane.YES_NO_OPTION, 
              JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
          GameController.closeResources();
          System.exit(0);
        }
      }
    });
    controllerFrame.setVisible(true);
    display = new JTextArea();
    display.setEditable(false);
    DefaultCaret caret = (DefaultCaret) display.getCaret();
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
    controllerFrame.add(new JScrollPane(display), BorderLayout.CENTER);
    controllerFrame.setSize(400,400);
    controllerFrame.setVisible(true);
    
    map = new String[][] {{" "," "," "},{" "," "," "},{" "," "," "}};
    
    GameController.setupConnections();
  }

  public static void main(String[] args) {
    new GameController();
  }
  
  private static void setupConnections() {
    try {
      gameServer = new ServerSocket(5555);

      display.setText("Waiting for connection..."
          + "\nIP Address: " + GameController.getLocalAddress()
          + "\nPort......: " + gameServer.getLocalPort()
          + "\n");
      
      connection1 = gameServer.accept();
      connection1.setKeepAlive(true);
      writeInConsole("\nAccepted connection.");
      input1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
      output1 = new PrintWriter(connection1.getOutputStream(), true);
      writeInConsole("I/O Streams Created.");
      writeInConsole("Player 1 >>> Connected!");
      writeInConsole("Player 1 >>> IP Address: " + connection1.getInetAddress().getHostAddress());
      output1.println("O");
      
      connection2 = gameServer.accept();
      connection2.setKeepAlive(true);
      writeInConsole("\nAccepted connection.");
      input2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
      output2 = new PrintWriter(connection2.getOutputStream(), true);
      writeInConsole("I/O Streams Created.");
      writeInConsole("Player 2 >>> Connected!");
      writeInConsole("Player 2 >>> IP Address: " + connection2.getInetAddress().getHostAddress());
      output2.println("X");
      
      GameController.run();
      
    } catch (Exception e) {      
      writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e); 
    }
  }

  protected static void run() {
    String message;
    while (keepGoing) {      
      //Leer 1
      try {
        message = (String) input1.readLine();
        if (message.contains("MOVE:")) {
          String[] arr = message.split(":");
          arr = arr[1].split(",");
          map[Integer.parseInt(arr[0])][Integer.parseInt(arr[1])] = arr[2];
          GameController.sendUpdate(output1);
          GameController.sendUpdate(output2);
        } else if (message.equals("EXIT")) {
          output2.println("EXIT");
        }
      } catch (Exception e) {
        writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e); 
      }
      
      //Leer 2
      try {
        message = (String) input2.readLine();        
        if (message.contains("MOVE:")) {
          String[] arr = message.split(":");
          arr = arr[1].split(",");
          map [Integer.parseInt(arr[0])] [Integer.parseInt(arr[1])] = arr[2];
          GameController.sendUpdate(output1);
          GameController.sendUpdate(output2);
        } else if (message.equals("EXIT")) {
          output1.println("EXIT");
        }
      } catch (Exception e) { 
        writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e); 
      }
      
      if (exceptionsCatched >= 5) {
        keepGoing = false;
      }
    }
    GameController.closeResources();
  }
  
  protected static void sendUpdate(PrintWriter output) {
    String mapString = "";
    for (int row = 0; row < map.length; row++) {
      for (int col = 0; col < map[row].length; col++) {
        if (row == (map.length - 1) && col == (map[row].length - 1)) {
          mapString += map[row][col];
        } else {
          mapString += map[row][col] + ",";
        }
      }      
    }
    output.println("UPDATE:" + mapString);
  }
  
  protected static void closeResources() {
    try {
      input1.close();      
    }catch (Exception e) { 
      writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e);
    }
    
    try {
      input2.close();      
    }catch (Exception e) { 
      writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e);
    }
    
    try {
      output1.close();      
    }catch (Exception e) { 
      writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e);
    }
    
    try {
      output2.close();    
    }catch (Exception e) { 
      writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e);
    }
    
    try {
      connection1.close();    
    }catch (Exception e) { 
      writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e);
    }
    
    try {
      connection2.close();   
    }catch (Exception e) { 
      writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e);
    }
    
    try {
      gameServer.close();  
    }catch (Exception e) { 
      writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e);
    }
  }
  
  protected static void writeInConsole(String str) {
    display.append("\n" + str);
  }
  
  private static InetAddress getLocalAddress(){
    try {
      Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
      while( b.hasMoreElements()){
        for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
          if ( f.getAddress().isSiteLocalAddress())
            return f.getAddress();
      }
    } catch (SocketException e) { 
      writeInConsole("Exception num. " + ++exceptionsCatched + "; " + e); 
    }
    return null;
  }
}





