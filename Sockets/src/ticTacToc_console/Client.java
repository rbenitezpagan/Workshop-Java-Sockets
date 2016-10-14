package ticTacToc_console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  private String player;
  private static boolean finished;
  private static boolean keepGoing = true;
  private static int serverExceptionCounter = 0;
  private static int plays = 0;
  protected static String[][] map = new String[][] {{" "," "," "},{" "," "," "},{" "," "," "}};

  private static Socket client;
  private static BufferedReader input;
  private static PrintWriter output;

  public Client() {
    Scanner kb = new Scanner(System.in);
    System.out.print("Enter the address to connect: ");
    String answer = kb.next();

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
      player = input.readLine();
      System.out.println("\nYour symbol is: " + player);
      System.out.println();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    printMap();
  }

  private void run() {
    do {
      try {
        String message = (String) input.readLine();
        if(message.contains("LCTN:")) {
          String[] arr = message.split(":");
          arr = arr[1].split(",");
          int x = Integer.parseInt(arr[0]);
          int y = Integer.parseInt(arr[1]);
          addSelection(x, y);

          //          ask for selection
        }
      } catch (Exception e) {

        if (++serverExceptionCounter > 4) {
          keepGoing = false;
          System.err.println("To many errors linked to the server... Shutting Down.");
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e1) {}
          System.exit(1);
        } else {
          System.err.println("Server is down, check server's status.");
        }
      }

      //      verificar si hay tres corridos para que gane
      if (plays == 9)
        keepGoing = false;
    } while (keepGoing);
  }

  /**
   * @param x
   * @param y
   */
  private void printMap() {
    System.out.println(" ****BOARD****");
    System.out.println(" ***PLAY #"+ plays + "***");
    System.out.println(" *---*---*---*");
    //    System.out.print("* ");
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
  }

  public static void main(String[] args) {
    Client client = new Client();
    if (client.player.equals("O")) {
      client.addSelection(0, 0);
      client.printMap();
      client.addSelection(0, 1);
      client.printMap();
      client.addSelection(0, 2);
      client.printMap();
      client.addSelection(1, 0);
      client.printMap();
      client.addSelection(1, 1);
      client.printMap();
      client.addSelection(1, 2);
      client.printMap();
      client.addSelection(2, 0);
      client.printMap();
      client.addSelection(2, 1);
      client.printMap();
      client.addSelection(2, 2);
      client.printMap();
    }
    client.run();            
  }
}