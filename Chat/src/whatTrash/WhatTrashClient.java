/**
 * 
 */
package whatTrash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author josue
 *
 */
public class WhatTrashClient {
  private static Socket client;
  private static BufferedReader input;
  private static PrintWriter output;

  public static void main(String[] args) {
    Scanner kb = new Scanner(System.in);
    try {
      client = new Socket("10.250.11.41", 5555);
      input = new BufferedReader(new InputStreamReader(client.getInputStream()));
      output = new PrintWriter(client.getOutputStream(), true);
      while (true) {
        String userMsg = kb.nextLine();
        System.out.println("Yo: " + userMsg);
        output.println("Ramon:" + userMsg);

        String groupInput;
        if ((groupInput = input.readLine()) != null) {
          System.out.println(groupInput);
        }
      }
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    kb.close();
  }  
}