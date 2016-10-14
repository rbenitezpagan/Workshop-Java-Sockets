package ticTacToc_GUI;

public class Client {

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
      try {
      Client client = new Client();
      window.frame.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosing(java.awt.event.WindowEvent windowEvent) {
      if (JOptionPane.showConfirmDialog(window.frame, "Are you sure you want to close the game?",
      "Really Closing?", JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
      dout.println("CLIENT>>>TERMINATE");
      dout.flush();
      System.exit(0);
      } else {
      ;
      }
      }
      });
      window.frame.setTitle("BattleShip Game");
      window.frame.setVisible(true);
      window.frame.toBack();
      } catch (Exception e) {
      e.printStackTrace();
      }
      }
      });
  }

}
