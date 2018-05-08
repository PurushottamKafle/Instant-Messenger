package pksoft;
import javax.swing.*;
import java.io.IOException;

public class ClientTest {
	public static void main(String args[]) throws IOException{ 
		 ClientSocket cs = new ClientSocket("127.0.0.1");
		 cs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 cs.startRunning();
	}
}
