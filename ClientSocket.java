package pksoft;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ClientSocket extends JFrame {

	 private JTextField userText;
	    private JTextArea chatWin;
	    private ObjectOutputStream output;
	    private ObjectInputStream input;
	    private String message ="";
	    private String serverIp;
	    private Socket connection;
	    // making cons..
	    
	    public ClientSocket(String host) {
	    	super("Client Side");
	    	serverIp = host;
	    	userText = new JTextField();
	    	userText.setEditable(false);
	    	userText.addActionListener(
	    			new ActionListener() {
	    				public void actionPerformed(ActionEvent e) {
	    					sendMessage(e.getActionCommand());
	    					userText.setText("");
	    				}
	    			}
	    			);
	    	add(userText, BorderLayout.NORTH);
	    	chatWin = new JTextArea();
	    	add(new JScrollPane(chatWin));
	    	setSize(300, 150);
	    	setVisible(true);
	    	
	    }
	    // connect to the server startrunning...
	    public void startRunning() {
	    	try {
	    		connectToServer();
	    		setupStreams();
	    		whileChatting();
	    	}catch(EOFException eofException) {
	    		showMessage("\n Client Terminated the connection");
	    	}catch(IOException ioException) {
	    		ioException.printStackTrace();
	    	}finally {
	    		closeCrap();
	    	}
	    }
	    // coonect to server
	    private void connectToServer() throws IOException{
	    	showMessage("Attempting Connection...\n ");
	    	connection = new Socket(InetAddress.getByName(serverIp), 6789);
	    	showMessage("Connected Sucessfully"+connection.getInetAddress().getHostName());
	    	
	    }
	    // setup streams and wite and read
	    private void setupStreams() throws IOException{
	    	output = new ObjectOutputStream(connection.getOutputStream());
	    	output.flush();
	    	input = new ObjectInputStream(connection.getInputStream());
	    	input.close();
	    	showMessage(" \n Streams are done! Go to go Guys!!!");
	    	
	    	
	    }
	    // while chatting to server
	    private void whileChatting() throws IOException{
	    	abletoType(true);
	    	do {
	    		try {
	    			message = (String)input.readObject();
	    			showMessage("\n "+message);
	    		}catch(ClassNotFoundException classException) {
	    			showMessage("\n I don't know that object type");
	    		}
	    	}while(!message.equals("Server - End"));
	    		
	    	
	    }
	    private void closeCrap() {
	    	showMessage("\n Closing...");
	    	abletoType(false);
	    	try {
	    		output.close();
	    		input.close();
	    		connection.close();
	    		
	    	}catch(IOException ioException) {
	    		ioException.printStackTrace();
	    	}
	    	
	    }
	    // send messages to server
	    private void sendMessage(String message) {
	    	try {
	    		output.writeObject("Client - "+message);
	    		output.flush();
	    		showMessage("\nClient - "+message);
	    	}catch(IOException ioException) {
	    		chatWin.append("\n Sorry.. Something went Wrong... While sending message");
	    	}
	    }
	    // change the window or update
	    private void showMessage(final String txt) {
	    	SwingUtilities.invokeLater(
	    			new Runnable() {
	    				public void run() {
	    					chatWin.append(txt);
	    				}
	    			}
	    			);
	    	
	    }
	    // this gives users to  permisssion
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
