package server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ChatServer extends JFrame {

	private JPanel contentPane;
	private JTextArea textField;
	private static JTextArea textArea;
	private static JTextField portNumber;
	static PrintWriter out;
	static BufferedReader in;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatServer frame = new ChatServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatServer() {
				
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 51, 414, 140);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		textField = new JTextArea();
		textField.setBounds(10, 202, 210, 48);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton send = new JButton("Send");
		send.setBounds(230, 202, 194, 48);
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				textArea.append("\n"+textField.getText());
				out.println(textField.getText());
				
			}
		});
		contentPane.add(send);
		
		JButton turnOn = new JButton("Turn ON");
		turnOn.setBounds(236, 5, 89, 23);
		turnOn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				ChatServer.run();
			}
		});
		contentPane.add(turnOn);
		
		JButton turnOff = new JButton("Turn OFF");
		turnOff.setBounds(335, 5, 89, 23);
		turnOn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				ChatServer.stop();
			}
		});
		contentPane.add(turnOff);
		
		JLabel lblUsePort = new JLabel("Use port:");
		lblUsePort.setBounds(10, 9, 46, 14);
		contentPane.add(lblUsePort);
		
		portNumber = new JTextField();
		portNumber.setBounds(85, 6, 86, 20);
		contentPane.add(portNumber);
		portNumber.setColumns(10);
	}
	
	protected static void stop() {
				
	}

	public static void run() {
		try {//
        	ServerSocket serverSocket = new ServerSocket(Integer.parseInt(portNumber.getText()));
        	textArea.append("\nHost: " + serverSocket.getInetAddress());       
        	textArea.append("\nPort: " + serverSocket.getLocalPort());
            textArea.append("\nWaiting for connections...");
//            ArrayList<Socket> sockets = new ArrayList<Socket>(); 
//            for (int i = 0; i < 10; i++) {
//            	sockets.add(serverSocket.accept());
//			}
            Socket clientSocket = serverSocket.accept();
            textArea.append("\nWaiting for connections...");
            out = new PrintWriter(clientSocket.getOutputStream(), true);                   
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));            
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
            	if(inputLine.equals("bye") || inputLine.equals("BYE")) {
            		textArea.append("\n"+ inputLine);
            		out.println("It was nice talking to you... Bye...");
            		serverSocket.close();
            		break;
            	}
            	out.println(inputLine);
            }
		} catch (Exception e) {}
	}
}
