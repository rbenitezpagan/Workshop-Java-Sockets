/**
 * 
 */
package ticTacToe_GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * @author josue
 *
 */
public class TicTacToe_GUI {
  protected static char player;
  protected static JFrame gameClient;
  protected static JPanel myBoard;
  protected static JButton[] buttons;

  private static Socket connection;
  private static BufferedReader input;
  private static PrintWriter output;
  private static boolean keepGoing =  true;

  /**
   * 
   */
  public TicTacToe_GUI() {
    TicTacToe_GUI.setupConnections();

    TicTacToe_GUI.buildBoard();

//    if (player == 'X') {
//            
//    }
    String m="";
    while (keepGoing) {
      try {
        if ((m = input.readLine()) != null) {
          JOptionPane.showMessageDialog(gameClient, m);
          if (m.contains("UPDATE:")) {
            String[] arr = m.split(":");
            applyUpdate(arr[1]);
          } else if (m.equals("EXIT")) {
            
          }
        }
      } catch (IOException e) { }
    }
  }

  public static void main(String[] args) {
    new TicTacToe_GUI();
  }

  protected static void buildBoard() {
    gameClient = new JFrame();
    gameClient.setTitle("Tic Tac Toe - Game Client");
    gameClient.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
        if (JOptionPane.showConfirmDialog(gameClient, 
            "Are you sure you want to close the game controller?", 
            "Really Closing?",
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
          //          GameController.closeResources();
          System.exit(0);
        }
      }
    });
    gameClient.setVisible(true);
    gameClient.setBounds(10, 259, 350, 350);
    myBoard = new JPanel();
    myBoard.setLayout(new GridLayout(3, 3));
    gameClient.add(myBoard);


    buttons = new JButton[9];
    for (int i = 0; i < buttons.length; i++) { 
      buttons[i] = new JButton();
      buttons[i].setBackground(Color.BLUE);
      buttons[i].setForeground(Color.WHITE);
      ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          JButton btn = (JButton)event.getSource();
          String location = btn.getText();
          try {
            output.println("MOVE:" + location + "," + player );
//            JOptionPane.showMessageDialog(gameClient, "MOVE:" + location + "," + player );
            JOptionPane.showMessageDialog(gameClient, 
                                          "MOVE:" + location + "," + player,
                                          "Movement Update",
                                          JOptionPane.INFORMATION_MESSAGE);
            
            
            //            if (hit.equals("true")) {
            //              btn.setBackground(Color.GREEN);
            //            } else {
            //              btn.setBackground(Color.RED);
            //            }
            //            btn.setEnabled(false);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      };
      buttons[i].addActionListener(actionListener);
    }
    buttons[0].setText("0,0");
    buttons[1].setText("0,1");
    buttons[2].setText("0,2");
    buttons[3].setText("1,0");
    buttons[4].setText("1,1");
    buttons[5].setText("1,2");
    buttons[6].setText("2,0");
    buttons[7].setText("2,1");
    buttons[8].setText("2,2");

    for (int i = 0; i < buttons.length; i++) {
      myBoard.add(buttons[i]);
    }
  }

  protected static void setupConnections() {
//    String answer = "10.0.0.71:5555";
    String answer = "10.250.11.41:5555";
    String[] data = answer.split(":");
    String ip = data[0];
    int port = Integer.parseInt(data[1]);
    try {

      connection = new Socket(InetAddress.getByName(ip), port);
      connection.setKeepAlive(true);
      input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      output = new PrintWriter(connection.getOutputStream(), true);
      player = input.readLine().charAt(0);
    } catch (Exception e) { }
  }
  
  protected static void applyUpdate(String mapString) {
    String[] map = mapString.split(",");    
    
    if (map[0].equals("X") || map[0].equals("O")) {
      buttons[0].setText(map[0]);
      buttons[0].setEnabled(false);
    }
    
    if (map[1].equals("X") || map[1].equals("O")) {
      buttons[1].setText(map[1]);
      buttons[1].setEnabled(false);
    }
    
    if (map[2].equals("X") || map[2].equals("O")) {
      buttons[2].setText(map[2]);
      buttons[2].setEnabled(false);
    }
    
    if (map[3].equals("X") || map[3].equals("O")) {
      buttons[3].setText(map[3]);
      buttons[3].setEnabled(false);
    }
    
    if (map[4].equals("X") || map[4].equals("O")) {
      buttons[4].setText(map[4]);
      buttons[4].setEnabled(false);
    }
    
    if (map[5].equals("X") || map[5].equals("O")) {
      buttons[5].setText(map[5]);
      buttons[5].setEnabled(false);
    }
    
    if (map[6].equals("X") || map[6].equals("O")) {
      buttons[6].setText(map[6]);
      buttons[6].setEnabled(false);
    }
    
    if (map[7].equals("X") || map[7].equals("O")) {
      buttons[7].setText(map[7]);
      buttons[7].setEnabled(false);
    }
    
    if (map[8].equals("X") || map[8].equals("O")) {
      buttons[8].setText(map[8]);
      buttons[8].setEnabled(false);
    }
    
    myBoard.validate();
    myBoard.updateUI();
  }
}


