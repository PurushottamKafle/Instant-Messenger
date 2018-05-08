package pksoft;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;

public class SerSocket extends JFrame {
    private JTextField userText;
    private JTextArea chatWin;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    
    // making constroctor
    public SerSocket() {
    	super("PK Instant Messenger");
    	userText = new JTextField();
    	userText.setEditable(false);
    	
    	// making event of userText
    	userText.addActionListener(
    			new ActionListener() {
    				public void actionPerformed(ActionEvent e) {
    					sendMessage(e.getActionCommand()); // getting text as a command from the Users
    					userText.setText(""); // this is important inorder to send and make path clear for another message
    					// this method is in Facebook, Twitter and other Instant Messenger apps.
    					
    				}
    			}
    			); // ending line of event
    	add(userText, BorderLayout.NORTH); // sitting in the near top of screen
    	
    	// for chatWin
    	chatWin = new JTextArea();
    	add(new JScrollPane(chatWin));
    	setSize(300, 150);
    	setVisible(true);
    	
    }
    public void startRunning() {
    	try { // exception handeling for stsrtRunning Method
    		
    		server = new ServerSocket(6789, 10);
    		while(true) {
    			try {
    				waitConnection();
    				setupStream();
    				whileChatting();
    				
    			}catch(Exception eofException) {
    				showMessage("\n Server Ended the Connection! ");
    			}finally{
    				closeCrap();
    			}
    		}
    	}catch(IOException ioException) {
           ioException.printStackTrace();
    	}
    }
    private void waitConnection() throws IOException{
    	showMessage("Conncetion is waiting for a Client \n");
    	connection = server.accept();
    	showMessage("Connected Successfully! "+connection.getInetAddress().getHostName());
    }
    // Get Stream to get data to  receive or send
    private void setupStream() throws IOException{
    	output = new ObjectOutputStream(connection.getOutputStream());
    	output.flush();
    	/// making input
    	input = new ObjectInputStream(connection.getInputStream());
    	input.close();
    	showMessage("\n Streams are now setup");
    }
    private void whileChatting() throws IOException{
    	String message="You're now Connected";
    	sendMessage(message);
    	abletoType(true);
    	do {
    		try {
    			message = (String)input.readObject();
    			showMessage("\n "+message);
    		}catch(ClassNotFoundException classNotFound) {
    			showMessage("\n Someone Hacked!");
    		}
    	}while(message.equals("CLIENT - end"));
    }
    public void closeCrap() {
    	showMessage("\n This is closing...\n");
    	abletoType(false);
    	try {
    		output.close();
    		input.close();
    		connection.close();
    	}catch(IOException ioException) {
    		ioException.printStackTrace();
    	}
    }
    private void sendMessage(String message) {
    	try {
    		output.writeObject("Server - "+message);
    		output.flush();
    		showMessage("\n Serevr - "+message);
    	}catch(IOException ioExcep) {
    		chatWin.append("Dude I can't send that");
    	}
    }
    private void showMessage(final String txt) {
    	SwingUtilities.invokeLater(
    			new Runnable() {
    				public void run() {
    					chatWin.append(txt);
    				}
    			}
    			);
    }
    // let the users to type if client is online
    private void abletoType(final boolean boo) {
    	SwingUtilities.invokeLater(
    		   new Runnable() {
    			public void run() {
    				userText.setEditable(boo);
    			}
    		}
    	);
    }
}
