/**
 * 
 */
package whatTrash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author josue
 *
 */
public class WhatTrash {
  private static ServerSocket server;
  private static Socket[] clients;
  private static BufferedReader[] inputs;
  private static PrintWriter[] outputs;

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {
      server = new ServerSocket(5555);
      clients = new Socket[1];
      inputs = new BufferedReader[1];
      outputs = new PrintWriter[1];
      
      System.out.println(
          "Waiting for connection..."
          + "\nIP Address: " + getLocalAddress()
          + "\nPort......: " + server.getLocalPort()
          + "\n");
      
      for (int i = 0; i < clients.length; i++) {
        clients[i] = server.accept();
        inputs[i] = new BufferedReader(new InputStreamReader(clients[i].getInputStream()));
        outputs[i] = new PrintWriter(clients[i].getOutputStream(), true);
      }
      
      while (true) {
        for (BufferedReader oido : inputs) {
          try {
            String msg = oido.readLine();
            System.out.println(msg);
            for (PrintWriter boca : outputs) {
              boca.println(msg);
            }
          } catch (Exception e) {}
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  
  private static InetAddress getLocalAddress(){
    try {
      Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
      while( b.hasMoreElements()){
        for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
          if ( f.getAddress().isSiteLocalAddress())
            return f.getAddress();
      }
    } catch (SocketException e)  {}
    return null;
  }  

}