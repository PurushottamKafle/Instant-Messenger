package pksoft;
import javax.swing.*;
public class SerTest {
public static void main(String[] args) {
	SerSocket ss = new SerSocket();
	ss.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	ss.startRunning();
  }
}
