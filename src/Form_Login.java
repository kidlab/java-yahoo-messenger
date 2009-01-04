import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
public class Form_Login extends JFrame implements ISessionEventHandler
{
	private JLabel lbUserName;
	private JLabel lbPassWords;
	private JLabel lbLogo;
	private JTextField txtUserName;
	private JPasswordField txtPassWords;
	private JButton	btnLogin;
	private JButton btnExit;
	private Container container;
	private Session session;
	private SessionHandler sessionHandler;
	private Form_List_Friend formListFriend;
	private JCheckBox chkInvisible;
	
	/**
	 * Initialize Component with default setting
	 */
	public void initilizeComponent()
	{	
		//
		// lbLogo
		//
		this.lbLogo = new JLabel(new ImageIcon(getClass().getResource("image/logo.gif")));
		this.lbLogo.setBounds(50, 30, 235, 200);		
		
		//
		// lbUserName
		//
		this.lbUserName = new JLabel("Username:");
		this.lbUserName.setBounds(40, 250, 80, 20);
		
		//
		// lbPassWords
		//
		this.lbPassWords = new JLabel("Passwords:");
		this.lbPassWords.setBounds(40, 280, 90, 20);
		
		//
		// txtUserName
		//
		this.txtUserName = new JTextField();
		this.txtUserName.setBounds(135,245,160,23);
		this.txtUserName.addKeyListener(new LoginKeyAdapter());
		
		//
		// txtPassWords
		//
		//
		this.txtPassWords = new JPasswordField();
		this.txtPassWords.setBounds(135, 275, 160, 23);
		this.txtPassWords.enableInputMethods(true);
		this.txtPassWords.addKeyListener(new LoginKeyAdapter());
		
		//
		// btnLogin
		//
		this.btnLogin = new JButton("Login");
		this.btnLogin.setBounds(135, 335, 75, 25);
		ButtonLoginAction bntLoginAction = new ButtonLoginAction();
		this.btnLogin.addActionListener(bntLoginAction);
		
		//
		// btnExit
		//
		this.btnExit = new JButton("Exit");
		this.btnExit.setBounds(220, 335, 75, 25);
		this.btnExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		}
		);
		
		//
		//chkInvisible
		//
		this.chkInvisible = new JCheckBox("Invisible to everyone");
		this.chkInvisible.setBounds(132, 305, 250, 25);
		
		// container
		//
		this.container = this.getContentPane();
		this.container.add(this.lbLogo);
		this.container.add(this.lbUserName);
		this.container.add(this.lbPassWords);
		this.container.add(this.txtUserName);
		this.container.add(this.txtPassWords);
		this.container.add(this.chkInvisible);
		this.container.add(this.btnLogin);
		this.container.add(this.btnExit);
		this.setResizable(false);
		this.setLayout(null);
		this.setTitle(Constant.PROGRAM_NAME);
	}
	
	/**
	 * Constructor of Form_Login Class
	 */
	public Form_Login()
	{
		this.initilizeComponent();
		
		//Set up the path of log file to store all runtime exception.
		Tracer.setLogFile(Constant.LOG_FILE);
	}
	
	public SessionHandler getSessionHandler()
	{
		return this.sessionHandler;
	}
	
	public boolean login() throws Exception
	{
		this.session = new Session();
		this.sessionHandler = new SessionHandler();
		this.sessionHandler.addEventHandler(this);
		this.session.addSessionListener(this.sessionHandler);
		
		
		this.formListFriend = new Form_List_Friend(this.session, this.sessionHandler);
		String userName = this.txtUserName.getText().trim();
		String passWord = "";
		char[] tempPass = this.txtPassWords.getPassword();
		
		for(int i = 0; i < tempPass.length; i++)
		{
			passWord += tempPass[i];
		}
		
		if(this.chkInvisible.isSelected())
			this.session.setStatus(StatusConstants.STATUS_INVISIBLE);
		else
			this.session.setStatus(StatusConstants.STATUS_AVAILABLE);
		try
		{
			this.session.login(userName, passWord);
		}
		catch(LoginRefusedException ex)
		{
			Tracer.Log(this.getClass(), ex);
			
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
			if(e.getSource() == btnLogin)
			{
				try
		        {  
					if(login())
					{	
						//YahooGroup[] yg = session.getGroups();
						//Vector<YahooUser> list = yg[0].getMembers();						
						if(session.getSessionStatus() == StatusConstants.MESSAGING)
						{											
							SwingModelFactory factory = new SwingModelFactory(session);							
							formListFriend.setModel(factory.createTreeModel(true));								
							Form_Login.this.setVisible(false);
						}
						else
							JOptionPane.showMessageDialog(null, "Sorry, there was a problem connecting");
					}
		        }
				catch(Exception ex) 
		        { 
		        	session.reset();
		        	
		        	Tracer.Log(this.getClass(), ex);
		        }				
			}
		}
	}
	
	public void offlineMessageReceived(SessionEvent ev) 
	{
		String strFriend = ev.getFrom();
		Form_Message formMessage = new Form_Message(session, this.sessionHandler);
		formMessage.setTo(strFriend);
		formMessage.setEditableForMessageField(true);
		//formListFriend.listFormMessages.put(strFriend, formMessage);
		formMessage.appendtoDisplay("Offline message at " + ev.getTimestamp().toLocaleString() + "\n");
		String message = ev.getMessage();
		formMessage.addInstantMessage(strFriend, message);
	   	formMessage.setVisible(true);
	}	
		
	public void contactRequestReceived(SessionEvent ev)
	{
		String friendId = ev.getFrom();
		int result = JOptionPane.showConfirmDialog(Form_Login.this, friendId + " want to make friend with you");
		if(result == JOptionPane.OK_OPTION)
		{
			Form_Add_Friend formAddFriend = new Form_Add_Friend(session);
			formAddFriend.txtUserId.setText(friendId);
		}
		else if(result == JOptionPane.CANCEL_OPTION)
		{
			try
			{
				session.rejectContact(ev, "");
			}
			catch(IOException ex)
			{
				Tracer.Log(this.getClass(), ex);
			}
		}
	}
		
	public void contactRejectionReceived(SessionEvent ev) 
	{
		JOptionPane.showMessageDialog(Form_Login.this, ev.getFrom() + " has decline your request.");
	}		

	@Override
	public void doEvent(int eventType, SessionEvent e)
	{
		switch (eventType)
		{
			case ServiceConstants.SERVICE_X_OFFLINE:
				this.offlineMessageReceived(e);
				break;			
				
			case ServiceConstants.SERVICE_CONTACTNEW:
				this.contactRequestReceived(e);
				break;
				
			case ServiceConstants.SERVICE_CONTACTREJECT:
				this.contactRejectionReceived(e);
				break;
				
			default:
				break;
		}
	}
	
	private class LoginKeyAdapter extends KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{
			if((e.getKeyCode() != KeyEvent.VK_ENTER)
					|| txtUserName.getText().trim().isEmpty()
					|| txtPassWords.getPassword().length <= 0)
				return;
			
			try
	        {  
				if(login())
				{						
					if(session.getSessionStatus() == StatusConstants.MESSAGING)
					{								
						SwingModelFactory factory = new SwingModelFactory(session);							
						formListFriend.setModel(factory.createTreeModel(true));								
						Form_Login.this.setVisible(false);
					}
					else
						JOptionPane.showMessageDialog(null, "Sorry, there was a problem connecting");
				}
	        }
			catch(Exception ex) 
	        { 
	        	session.reset();
	        	
	        	Tracer.Log(this.getClass(), ex);
	        }		
		}
	}
}
