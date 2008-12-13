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
		
		//Form_Add_Friend f = new Form_Add_Friend(new Session());
		
		//Form_Message formMessage = new Form_Message(new Session());
		/*try
		{
			if(formLogin.login())
			{
				Session session = formLogin.getSession();								
				
				if(session.getSessionStatus() == StatusConstants.MESSAGING)
				{
					formLogin.dispose();					
					SwingModelFactory factory = new SwingModelFactory(session);
					Form_List_Friend formListFriend = new Form_List_Friend();
					formListFriend.setModel(factory.createTreeModel(true));
					
				}
				else
					JOptionPane.showMessageDialog(null, "Sorry, there was a problem connecting");
				
			}
		}
		catch(Exception ex)
		{
			
		}*/
		
	}

}
