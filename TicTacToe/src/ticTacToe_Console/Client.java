package ticTacToe_Console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  private char player;
  @SuppressWarnings("unused")
  private static boolean finished;
  private static boolean keepGoing = true;
  private static int serverExceptionCounter = 0;
  private static int plays = 0;
  protected static String[][] map = new String[][] {{" "," "," "},{" "," "," "},{" "," "," "}};

  private static Socket client;
  private static BufferedReader input;
  private static PrintWriter output;

  private static Scanner kb = new Scanner(System.in);

  public Client() {
        System.out.print("Enter the address to connect: ");
        String answer = kb.next();
//    String answer = "10.0.0.71:5555";
    String[] data = answer.split(":");
    String ip = data[0];
    int port = Integer.parseInt(data[1]);
    try {

      client = new Socket(InetAddress.getByName(ip), port);
      client.setKeepAlive(true);
      System.out.println("Connected to >>> " + data[0] + ":" + port);
      input = new BufferedReader(new InputStreamReader(client.getInputStream()));
      output = new PrintWriter(client.getOutputStream(), true);
      //        System.out.println("Passed I/Os");
      player = input.readLine().charAt(0);
      System.out.println("\nYour symbol is: " + player);
      System.out.println();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    printMap();
  }

  public void run() {

    while (keepGoing) {
      try {

        if(player == 'O' && plays == 0) {
          System.out.println("Enter the position (coordenates) to mark, Example: 0,0.");
          String answer = kb.next();
          String[] vals = answer.split(",");
          int x = Integer.parseInt(vals[0]);
          int y = Integer.parseInt(vals[1]);
          addSelection(x, y);

          //          String message = input.readLine();
          //          if(message.contains("LCTN:")) {
          //            String[] arr = message.split(":");
          //            arr = arr[1].split(",");
          //            x = Integer.parseInt(arr[0]);
          //            y = Integer.parseInt(arr[1]);
          //            addUpdate(x, y);
          //          } 
        }
        String message = input.readLine();
        if(message.contains("LCTN:")) {
          String[] arr = message.split(":");
          arr = arr[1].split(",");
          int x = Integer.parseInt(arr[0]);
          int y = Integer.parseInt(arr[1]);
          addUpdate(x, y);

          System.out.println("Enter the position (coordenates) to mark, Example: 0,0.");
          String answer = kb.next();
          String[] vals = answer.split(",");
          x = Integer.parseInt(vals[0]);
          y = Integer.parseInt(vals[1]);
          addSelection(x, y);
        }
        
        mapChecker();
      } catch (Exception e) {
        if (++serverExceptionCounter > 4) {
          keepGoing = false;
          System.err.println("To many errors linked to the server... Shutting Down.");
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e1) {}
          System.exit(1);
        } else {
          System.err.println(e);
          System.err.println("Server is down, check server's status.");
        }
      }
      //      verificar si hay tres corridos para que gane
      if (player == 'O' && plays == 5 )
        keepGoing = false;
      if (player == 'X' && plays == 4)
        keepGoing = false;
    }
    output.println("EXIT");
  }

  /**
   * @param x
   * @param y
   */
  private void addUpdate(int x, int y) {
    if(player == 'O')
      map[x][y] = "X";
    else
      map[x][y] = "O";
    printMap();

  }

  /**
   * @param x
   * @param y
   */
  private void printMap() {
    System.out.println(" ****BOARD****");
    System.out.println(" ***PLAY #"+ plays + "***");
    System.out.println(" *---*---*---*");
    for (int row = 0; row < map.length; row++) {
      for (int col = 0; col < map[row].length; col++) {
        System.out.print(" * " + map[row][col]);
      }
      System.out.println(" *");
      System.out.println(" *---*---*---*"); 
    }
    System.out.println();
  }

  private void addSelection(int x, int y) {
    map[x][y] = "" + player;
    plays++;
    sendSelection(x, y, output);
  }


  /**
   * @param x
   * @param y
   * @param output2
   */
  private void sendSelection(int x, int y, PrintWriter output) {
    output.println("LCTN:" + x + "," + y);
    printMap();
  }

  public static void main(String[] args) {
    //    Client client = new Client();
    //    client.run();   
    new Client().run();

  }
  
  private void mapChecker() {
    String row1 = "" + map[0][0] + map[0][1] + map[0][2];
    checkWinner(row1);
    String row2 = "" + map[1][0] + map[1][1] + map[1][2];
    checkWinner(row2);
    String row3 = "" + map[2][0] + map[2][1] + map[2][2];
    checkWinner(row3);
    String col1 = "" + map[0][0] + map[1][0] + map[2][0];
    checkWinner(col1);
    String col2 = "" + map[0][1] + map[1][1] + map[2][1];
    checkWinner(col2);
    String col3 = "" + map[0][2] + map[1][2] + map[2][2];
    checkWinner(col3);
    String diag1 = "" + map[0][0] + map[1][1] + map[2][2];
    checkWinner(diag1);
    String diag2 = "" + map[2][0] + map[1][1] + map[0][2];
    checkWinner(diag2);
  }
  
  private void checkWinner(String line) {
    char[] marks = new char[3];
    for (int i = 0; i < line.length(); i++) {
      marks[i] = line.charAt(i);
    }
    if ((marks[0] == marks[1] &&  marks[1] == marks[2]) && marks[0] != ' ') {
      System.out.println("Player " + marks[1] + " won.");
      output.println("WON:" + marks[1]);
      System.exit(0);
    }    
  }
}