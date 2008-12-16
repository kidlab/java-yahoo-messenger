import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ymsg.network.Session;
import ymsg.network.StatusConstants;
import ymsg.support.SwingModelFactory;

/**
 * 
 * @author MinhVH
 *
 */
public class Program {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Form_Login formLogin = new Form_Login();
		formLogin.setMaximumSize(new Dimension(350,450));
		formLogin.setMinimumSize(new Dimension(350,450));
		formLogin.setVisible(true);
		formLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
