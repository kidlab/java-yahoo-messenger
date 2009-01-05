import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.GridBagConstraints;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import ymsg.network.ServiceConstants;
import ymsg.network.SessionConferenceEvent;
import ymsg.network.SessionEvent;
import ymsg.network.SessionFileTransferEvent;
import ymsg.network.YahooConference;
import ymsg.network.YahooIdentity;
import ymsg.support.MessageDecoder;
import ymsg.support.MessageDecoderSettings;
import ymsg.support.MessageElement;

public class Form_Conference extends BaseFrame implements ISessionEventHandler
{
	private static final long serialVersionUID = 1L;

	private YahooConference yahooConference;
	
	private  Document displayDoc;
	
	private MessageDecoder decoder;
	
	private JPanel contentPane = null;

	private JToolBar mainToolBar = null;

	private JButton btnSendFile = null;

	private JButton btnConference = null;

	private JSplitPane slitPanel = null;

	private JPanel panelTop = null;

	private JPanel panelBottom = null;

	private JButton btnSend = null;

	private JScrollPane spMessage = null;

	private JTextArea txtMessage = null;

	private JPanel panelButton = null;

	private JButton btnIgnoreUser = null;

	private JScrollPane spConference = null;

	private JTextPane txtConference = null;

	private JPanel panelUsers = null;

	private JScrollPane spUsers = null;

	private DefaultListModel listModel;
	
	private JList lstUsers = null;

	/**
	 * This is the default constructor
	 */
	public Form_Conference ()
	{
		super();
		initialize();
		
		sessionHandler.addEventReciever(this);
		this.displayDoc = this.txtConference.getDocument();
		
		MessageDecoderSettings sets = new MessageDecoderSettings();		
		sets.setEmoticonsDecoded(true);
		this.decoder = new MessageDecoder(sets);		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() 
	{
		this.setSize(500, 401);
		this.setTitle("Conference");
		
		//
		//jContentPane
		//
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		
		//
		//mainToolBar
		//
		mainToolBar = new JToolBar();
		mainToolBar.setOrientation(JToolBar.HORIZONTAL);
		contentPane.add(mainToolBar, BorderLayout.NORTH);
		
		//
		//btnSendFile
		//
		btnSendFile = new JButton();
		btnSendFile.setText("Send File");
		btnSendFile.addActionListener(new SendFileActionListener());
		mainToolBar.add(btnSendFile);
		
		//
		//btnConference
		//
		btnConference = new JButton();
		btnConference.setText("Conference");
		btnConference.addActionListener(new MakeConferenceActionListener());
		mainToolBar.add(btnConference);		
		
		//
		//slitPanel
		//
		slitPanel = new JSplitPane();
		slitPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		slitPanel.setContinuousLayout(true);
		slitPanel.setDividerLocation(200);
		slitPanel.setDividerSize(5);
		contentPane.add(slitPanel, BorderLayout.CENTER);
		
		//
		//panelTop
		//
		panelTop = new JPanel();
		panelTop.setLayout(new GridBagLayout());
		
		//
		//spConference
		//
		GridBagConstraints gridBagConstraints_spCoference = new GridBagConstraints();
		gridBagConstraints_spCoference.fill = GridBagConstraints.BOTH;
		gridBagConstraints_spCoference.gridy = 0;
		gridBagConstraints_spCoference.weightx = 1.0;
		gridBagConstraints_spCoference.weighty = 1.0;
		gridBagConstraints_spCoference.gridwidth = 1;
		gridBagConstraints_spCoference.gridx = 0;
		
		spConference = new JScrollPane();
		spConference.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		
		txtConference = new JTextPane();
		txtConference.setEditable(false);
		
		spConference.setViewportView(txtConference);
		panelTop.add(spConference, gridBagConstraints_spCoference);
		
		//
		//panelUsers
		//	
		panelUsers = new JPanel();
		panelUsers.setLayout(new BorderLayout());
		panelUsers.setPreferredSize(new Dimension(120, 10));
		panelUsers.setBorder(new SoftBevelBorder(SoftBevelBorder.RAISED));
		panelUsers.setPreferredSize(new Dimension(170, 165));
		
		//
		//btnIgnoreUser
		//
		btnIgnoreUser = new JButton();
		btnIgnoreUser.setText("Ignore User");
		btnIgnoreUser.addActionListener(new IgnoreUserActionListener());
		panelUsers.add(btnIgnoreUser, BorderLayout.SOUTH);
		
		//
		//spUsers
		//
		GridBagConstraints gridBagConstraints_spUsers = new GridBagConstraints();
		gridBagConstraints_spUsers.gridx = 1;
		gridBagConstraints_spUsers.fill = GridBagConstraints.BOTH;
		gridBagConstraints_spUsers.gridy = 0;
		
		spUsers = new JScrollPane();
		spUsers.setPreferredSize(new Dimension(0, 0));
		
		listModel = new DefaultListModel();
		lstUsers = new JList(listModel);
		lstUsers = new JList();
		
		spUsers.setViewportView(lstUsers);
		panelUsers.add(spUsers, BorderLayout.CENTER);
		panelTop.add(panelUsers, gridBagConstraints_spUsers);
		slitPanel.setTopComponent(panelTop);
		
		//
		//panelBottom
		//		
		panelBottom = new JPanel();
		panelBottom.setLayout(new GridBagLayout());
		slitPanel.setBottomComponent(panelBottom);	
		
		//
		//btnSend
		//
		GridBagConstraints gridBagConstraint_btnSend = new GridBagConstraints();
		gridBagConstraint_btnSend.gridx = 1;
		gridBagConstraint_btnSend.gridy = 1;
		
		btnSend = new JButton();
		btnSend.setText("Send");
		btnSend.setPreferredSize(new Dimension(70, 40));
		btnSend.addActionListener(new SendMessageActionListener());
		panelBottom.add(btnSend, gridBagConstraint_btnSend);
		
		//
		//spMessage
		//
		GridBagConstraints gridBagConstraints_spMessage = new GridBagConstraints();
		gridBagConstraints_spMessage.fill = GridBagConstraints.BOTH;
		gridBagConstraints_spMessage.gridy = 1;
		gridBagConstraints_spMessage.weightx = 1.0;
		gridBagConstraints_spMessage.weighty = 1.0;
		gridBagConstraints_spMessage.gridheight = 1;
		gridBagConstraints_spMessage.gridx = 0;
		
		spMessage = new JScrollPane();
		spMessage.setBorder(BorderFactory.createLineBorder(Color.lightGray, 3));
		
		txtMessage = new JTextArea();	
		txtMessage.addKeyListener(new MessageKeyAdapter());
		
		spMessage.setViewportView(txtMessage);
		panelBottom.add(spMessage, gridBagConstraints_spMessage);
		
		//
		//panelButton
		//
		GridBagConstraints gridBagConstraints_panelButton = new GridBagConstraints();
		gridBagConstraints_panelButton.gridx = 0;
		gridBagConstraints_panelButton.fill = GridBagConstraints.BOTH;
		gridBagConstraints_panelButton.gridy = 0;
		
		panelButton = new JPanel();
		panelButton.setLayout(new GridBagLayout());
		panelButton.setPreferredSize(new Dimension(20, 24));			
		panelBottom.add(panelButton, gridBagConstraints_panelButton);
		
		this.setContentPane(contentPane);
	}
	
	public void acceptConference(String[] users, YahooConference yConference)
	{
		this.yahooConference = yConference;
		this.loadUsers(users);
	}
	
	private void loadUsers(String[] users)
	{
		listModel = new DefaultListModel();
		lstUsers.setModel(listModel);
		
		//Add users to list view.
		int size = users.length;
		for(int id = 0; id < size; id++)
		{
			this.listModel.addElement(users[id]);
		}
		YahooIdentity yi = session.getLoginIdentity();
		this.listModel.addElement(yi.getId() + " - me");
	}
	
	public void createConference(String[] users, String msg)
	{
		try
		{
			this.loadUsers(users);
			
			YahooIdentity yi = session.getLoginIdentity();
			this.yahooConference = session.createConference(users, msg, yi);			
		}
		catch (Exception exc) 
		{
			Tracer.Log(this.getClass(), exc);
			Helper.Error(UserMsg.MAKE_CONFERENCE_FAILED);
		}
	}
	
	private class SendFileActionListener implements ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
		}
	}
	
	private class MakeConferenceActionListener implements ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
		}
	}
	
	private class SendMessageActionListener implements ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
			try
			{
				if(txtMessage.getText().trim().length() > 0)
				{
					MessageElement me = decoder.decode("me: "+ txtMessage.getText());			
					me.appendToDocument(displayDoc);							
					String temp = txtMessage.getText().trim();
					txtMessage.setText(null);
					session.sendConferenceMessage(yahooConference, temp);
					pushDown();
				}
			}
			catch(Exception ex)
			{
				Tracer.Log(this.getClass(), ex);
			}
		}
	}
	
	private class IgnoreUserActionListener implements ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
		}
	}
	
	private class MessageKeyAdapter extends KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{			
			try
			{
				if(e.getKeyCode() != KeyEvent.VK_ENTER)
					return;
				
				if(txtMessage.getText().trim().length() > 0)
				{
					MessageElement me = decoder.decode("me: "+ txtMessage.getText());			
					me.appendToDocument(displayDoc);							
					String temp = txtMessage.getText().trim();
					txtMessage.setText(null);
					session.sendConferenceMessage(yahooConference, temp);
					pushDown();
				}
			}
			catch(Exception ex)
			{
				Tracer.Log(this.getClass(), ex);
			}
		}
	}

	/**
	 * move to the last line in display box
	 */
	public void pushDown()
	{	try
		{	
			this.txtConference.setCaretPosition(this.txtConference.getText().length());			
			this.txtConference.scrollRectToVisible( new Rectangle(0,this.txtConference.getSize().height,1,1));			
		}
		catch(Exception e) 
		{
			Tracer.Log(this.getClass(), e);
		}
	}
	
	public void addInstantMessage(String strSender, String message)
	{		
		appendtoDisplay(strSender + ": ");			
		MessageElement me = decoder.decode(message);			
		me.appendToDocument(displayDoc);
		pushDown();
	}
	
	public void appendtoDisplay(String str)
	{
		try
		{
			this.displayDoc.insertString(this.displayDoc.getLength(), str, null);
		}
		catch(BadLocationException ex)
		{
			Tracer.Log(this.getClass(), ex);
		}		
	}
	
	public void messageReceived(SessionEvent ev)
	{
		/*String strTo = ev.getFrom();
		if(!strTo.equals(this.txtTo.getText()))
			return;
		
		this.addInstantMessage(this.txtTo.getText(), ev.getMessage());*/			
	}
	
	public void fileTransferReceived(SessionFileTransferEvent ev)
	{
		int result = JOptionPane.showConfirmDialog(null, ev.getFrom() + " send file to you.");
		if(result == JOptionPane.OK_OPTION)
		{
			
		}
		else if(result == JOptionPane.CANCEL_OPTION)
		{
			return;
		}
	}
	
	public void conferenceMessageReceived(SessionConferenceEvent ev) 
	{
		String strTo = ev.getFrom();
		if(!this.listModel.contains(strTo))
			return;
		
		this.addInstantMessage(strTo, ev.getMessage());	
	}
	
	public void conferenceInviteReceived(SessionConferenceEvent ev) 
	{
		String strFrom = ev.getFrom();
		String strMessage = ev.getMessage();
		int selectedValue = 
			Helper.ConfirmWithCancel("You are invited to join the conference from: " + strFrom + ". Greeting message: " + strMessage);
		if(selectedValue == JOptionPane.YES_OPTION)
		{
			
		}
	}

	
	@Override
	public void doSessionEvent(int eventType, SessionEvent e)
	{
		switch (eventType)
		{
			case ServiceConstants.SERVICE_MESSAGE:
				this.messageReceived(e);
				break;
			
			case ServiceConstants.SERVICE_FILETRANSFER:
				this.fileTransferReceived((SessionFileTransferEvent)e);
				break;
				
			case ServiceConstants.SERVICE_CONFMSG:
				this.conferenceMessageReceived((SessionConferenceEvent) e);
				break;
				
			case ServiceConstants.SERVICE_CONFINVITE:
				this.conferenceInviteReceived((SessionConferenceEvent)e);
				break;
				
			default:
				break;
		}
	}
}
