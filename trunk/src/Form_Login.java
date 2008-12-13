import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ymsg.network.*;
import ymsg.support.*;

/**
 * 
 * @author MinhVH
 *
 */
public class Form_Login extends JFrame
{
	private JLabel lbUserName;
	private JLabel lbPassWords;
	private JTextField txtUserName;
	private JPasswordField txtPassWords;
	private JButton	bntLogin;
	private JButton bntExit;
	private Container container;
	private Session session;
	
	/**
	 * Initilize Component with default setting
	 */
	public void initilizeComponent()
	{
		//
		// lbUserName
		//
		this.lbUserName = new JLabel("Username:");
		this.lbUserName.setBounds(40, 180, 80, 20);
		
		//
		// lbPassWords
		//
		this.lbPassWords = new JLabel("Passwords:");
		this.lbPassWords.setBounds(40, 210, 90, 20);
		
		//
		// txtUserName
		//
		this.txtUserName = new JTextField();
		this.txtUserName.setBounds(135,175,160,23);
		
		//
		// txtPassWords
		//
		//
		this.txtPassWords = new JPasswordField();
		this.txtPassWords.setBounds(135, 205, 160, 23);
		this.txtPassWords.enableInputMethods(true);
		//
		// btnLogin
		//
		this.bntLogin = new JButton("Login");
		this.bntLogin.setBounds(135, 235, 75, 25);
		ButtonLoginAction bntLoginAction = new ButtonLoginAction();
		this.bntLogin.addActionListener(bntLoginAction);
		
		//
		// btnExit
		//
		this.bntExit = new JButton("Exit");
		this.bntExit.setBounds(220, 235, 75, 25);
		this.bntExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		}
		);
		
		// container
		//
		this.container = this.getContentPane();
		this.container.add(this.lbUserName);
		this.container.add(this.lbPassWords);
		this.container.add(this.txtUserName);
		this.container.add(this.txtPassWords);
		this.container.add(this.bntLogin);
		this.container.add(this.bntExit);
		this.setResizable(false);
		this.setLayout(null);
	}
	
	/**
	 * Constructer of Form_Login Class
	 */
	public Form_Login()
	{
		this.initilizeComponent();
	}
	
	public boolean login() throws Exception
	{
		this.session = new Session();
		this.session.addSessionListener(new SessionHandler());
		String userName = this.txtUserName.getText().trim();
		String passWord = "";
		char[] tempPass = this.txtPassWords.getPassword();		
		for(int i = 0; i < tempPass.length; i++)
		{
			passWord += tempPass[i];
		}
		this.session.setStatus(StatusConstants.STATUS_AVAILABLE);
		try
		{
			this.session.login(userName, passWord);
		}
		catch(LoginRefusedException ex)
		{
			switch((int)ex.getStatus())
			{
				case (int)StatusConstants.STATUS_BADUSERNAME:
					JOptionPane.showMessageDialog(this, "Yahoo doesn't recognize that username.");
					break;
				case (int)StatusConstants.STATUS_BAD:
					JOptionPane.showMessageDialog(this, "Yahoo refused our connection.  Password incorrect?");
					break;
				case (int)StatusConstants.STATUS_LOCKED:
					JOptionPane.showMessageDialog(this, "Your account has been locked!");
					break;					
			}
			return false;
		}
		
		return true;
	}
	
	private class ButtonLoginAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{			
			if(e.getSource() == bntLogin)
			{
				try
		        {  
					if(login())
					{									
						if(session.getSessionStatus() == StatusConstants.MESSAGING)
						{											
							SwingModelFactory factory = new SwingModelFactory(session);
							Form_List_Friend formListFriend = new Form_List_Friend();
							formListFriend.setModel(factory.createTreeModel(true));	
							formListFriend.setSession(session);
							Form_Login.this.dispose();
						}
						else
							JOptionPane.showMessageDialog(null, "Sorry, there was a problem connecting");
					}
		        }
				catch(Exception ex) 
		        { 
		        	session.reset();		        
		        }				
			}
		}
	}
	
	public Session getSession()
	{
		return this.session;
	}
	
	private class SessionHandler extends SessionAdapter
	{
		
	}
}