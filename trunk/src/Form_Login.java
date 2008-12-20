import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
	private Form_List_Friend formListFriend;
	private JCheckBox chkInvisible;
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
		this.bntLogin.setBounds(135, 265, 75, 25);
		ButtonLoginAction bntLoginAction = new ButtonLoginAction();
		this.bntLogin.addActionListener(bntLoginAction);
		
		//
		// btnExit
		//
		this.bntExit = new JButton("Exit");
		this.bntExit.setBounds(220, 265, 75, 25);
		this.bntExit.addActionListener(new ActionListener()
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
		this.chkInvisible = new JCheckBox("Invisible to every one");
		this.chkInvisible.setBounds(132, 235, 250, 25);
		
		// container
		//
		this.container = this.getContentPane();
		this.container.add(this.lbUserName);
		this.container.add(this.lbPassWords);
		this.container.add(this.txtUserName);
		this.container.add(this.txtPassWords);
		this.container.add(this.chkInvisible);
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
		this.formListFriend = new Form_List_Friend(session);
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
		        }				
			}
		}
	}	
	
	private class SessionHandler extends SessionAdapter
	{
		public void offlineMessageReceived(SessionEvent ev) 
		{
			String strFriend = ev.getFrom();
			Form_Message formMessage = new Form_Message(session);
			formMessage.setTo(strFriend);
			formMessage.setEditableForMessageField(true);
			//formListFriend.listFormMessages.put(strFriend, formMessage);
			formMessage.appendtoDisplay("Offline message at " + ev.getTimestamp().toLocaleString() + "\n");
			String message = ev.getMessage();
			formMessage.addInstantMessage(strFriend, message);
	   		formMessage.setVisible(true);
		}
		
		public void newMailReceived(SessionNewMailEvent ev)
		{
			int numberOfMail = ev.getMailCount();
			if(numberOfMail > 0)
				formListFriend.lbMail.setIcon(new ImageIcon(getClass().getResource("image/newmail.png")));
			else
				formListFriend.lbMail.setIcon(new ImageIcon(getClass().getResource("image/nomail.png")));				
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
				{}
			}
		}
		
		public void contactRejectionReceived(SessionEvent ev) 
		{
			JOptionPane.showMessageDialog(Form_Login.this, ev.getFrom() + " has decline your request.");
		}
		
		/**
		 * listen when the message is coming
		 */
		public void messageReceived(SessionEvent ev)
		{			
			String strFriend = ev.getFrom();
			
			if(formListFriend.listFormMessages.containsKey(strFriend)){
				 Form_Message frmTemp = formListFriend.listFormMessages.get(strFriend);
        		 if(frmTemp != null && !frmTemp.isShowing()){
        			 if(strFriend.equals(frmTemp.txtTo.getText()))
        			 {
        				 frmTemp.addInstantMessage(strFriend, ev.getMessage());
        				 frmTemp.setVisible(true);
        				 return;
        			 }
        		 }
        		 else if(frmTemp != null && frmTemp.isShowing()){
        			 if(strFriend.equals(frmTemp.txtTo.getText()))
        			 {
        				 frmTemp.addInstantMessage(strFriend, ev.getMessage());        				 
        				 return;
        			 }
        		 }
			}
			else
			{
				Form_Message formMessage = new Form_Message(session);
				formMessage.setTo(strFriend);
				formMessage.setEditableForMessageField(true);
				formListFriend.listFormMessages.put(strFriend, formMessage);
				formMessage.addInstantMessage(strFriend, ev.getMessage());
				formMessage.setVisible(true);
			}		
		}
	}
}
