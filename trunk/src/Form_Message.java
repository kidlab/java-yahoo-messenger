import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import ymsg.network.FileTransferFailedException;
import ymsg.network.ServiceConstants;
import ymsg.network.Session;
import ymsg.network.SessionEvent;
import ymsg.network.SessionFileTransferEvent;
import ymsg.support.MessageDecoder;
import ymsg.support.MessageDecoderSettings;
import ymsg.support.MessageElement;

public class Form_Message extends JFrame implements ISessionEventHandler
{
	private JPanel mainContentPane;

	private JToolBar mainToolBar;

	private JButton btnSendFile;

	private JButton btnConference;

	private JPanel contentPanel;

	private JSplitPane jSplitPane;

	private JPanel sentMessagePanel;

	private JButton btnSend;

	private JPanel friendPanel;

	private JTextField txtTo;

	private JLabel lbTo;

	private JScrollPane spPanelDisplay;

	private JTextPane txtDisplayMessage;

	private JScrollPane spPanelMessage;

	private JTextArea txtMessage;

	private Session session;
	
	private SessionHandler sessionHandler;
	
	public  Document DisplayDoc;
	
	private MessageDecoder decoder;
	
	/**
	 * This is the default constructor
	 */
	public Form_Message(Session session, SessionHandler sessionHandler)
	{
		super();
		initializeComponents();
		
		this.session = session;			
		this.sessionHandler = sessionHandler;
		this.sessionHandler.addEventHandler(this);
		
		//
		//Decoder setting
		//		
		MessageDecoderSettings sets = new MessageDecoderSettings();		
		sets.setEmoticonsDecoded(true);
		//sets.setRespectTextFade(true);
		//sets.setRespectTextAlt(true);
		//sets.setOverrideFont(null,10,18,null);
		this.decoder = new MessageDecoder(sets);	
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initializeComponents() 
	{
		//
		//btnSendFile	
		//
		this.btnSendFile = new JButton();
		this.btnSendFile.setText("Send File");
		this.btnSendFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				File file = null;
				JFileChooser choose = new JFileChooser();
				choose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = choose.showOpenDialog(Form_Message.this.mainContentPane);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					file = choose.getSelectedFile();
					String path = file.getAbsolutePath();
					try
					{
						session.sendFileTransfer(txtTo.getText(), path, "");
					}
					catch(IOException ex)
					{}
					catch(FileTransferFailedException ex)
					{
						MessageElement me = decoder.decode("Send file failed");			
						me.appendToDocument(DisplayDoc);
						pushDown();
					}
				}
			}
		});
		
		//
		//btnConference	
		//
		this.btnConference = new JButton();
		this.btnConference.setText("Conference");
				
		//
		//mainToolBar	
		//
		this.mainToolBar = new JToolBar();
		this.mainToolBar.add(this.btnSendFile);
		this.mainToolBar.add(this.btnConference);
		
		//
		//txtDisplayMessage
		//
		this.txtDisplayMessage = new JTextPane();
		this.txtDisplayMessage.setEditable(false);
		this.DisplayDoc = this.txtDisplayMessage.getDocument();
		
		//
		//spPanelDisplay
		//
		this.spPanelDisplay = new JScrollPane();
		this.spPanelDisplay.setViewportView(this.txtDisplayMessage);
		
		//
		//btnSend	
		//
		this.btnSend = new JButton();
		this.btnSend.setText("Send");
		this.btnSend.setPreferredSize(new Dimension(70, 40));
		this.btnSend.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					if(txtMessage.getText().trim().length() > 0)
					{
						MessageElement me = decoder.decode("me: "+ txtMessage.getText());			
						me.appendToDocument(DisplayDoc);							
						String temp = txtMessage.getText().trim();
						txtMessage.setText("");					
						Form_Message.this.session.sendMessage(txtTo.getText().trim(), temp);
						pushDown();
					}
				}
				catch(Exception ex)
				{
					Tracer.Log(this.getClass(), ex);
				}
			}
		});
		
		//
		//lbTo	
		//
		this.lbTo = new JLabel();
		this.lbTo.setText("To: ");
		
		//
		//txtTo
		//
		this.txtTo = new JTextField();
		this.txtTo.setEditable(false);
		this.txtTo.addKeyListener(new KeyAdapter()
		{
			public void keyTyped(KeyEvent e)
			{
				System.out.print(txtTo.getText().length());
				if(txtTo.getText().length() == 0)
				{
					
					txtMessage.setText("");
					setEditableForMessageField(false);					
				}
				else
				{
					setEditableForMessageField(true);
				}
			}
		});
		
		//
		//friendPanel	
		//
		this.friendPanel = new JPanel();
		this.friendPanel.setLayout(new BorderLayout());
		this.friendPanel.setPreferredSize(new Dimension(20, 20));
		this.friendPanel.add(this.txtTo, BorderLayout.CENTER);
		this.friendPanel.add(this.lbTo, BorderLayout.WEST);
		
		//
		//txtMessage
		//
		this.txtMessage = new JTextArea();
		this.txtMessage.setLineWrap(true);	
		this.txtMessage.setWrapStyleWord(true);
		this.txtMessage.setEditable(false);
		this.txtMessage.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					try
					{
						if(txtMessage.getText().trim().length() > 0)
						{
							MessageElement me = decoder.decode("me: "+ txtMessage.getText());			
							me.appendToDocument(DisplayDoc);							
							String temp = txtMessage.getText().trim();
							txtMessage.setText("");										
							Form_Message.this.session.sendMessage(txtTo.getText().trim(), temp);
							pushDown();
						}
					}
					catch(Exception ex)
					{
						Tracer.Log(this.getClass(), ex);
					}
				}
			}
			
			public void keyTyped(KeyEvent e)
			{
				
			}
			
			public void keyReleased(KeyEvent e)
			{
				if(txtMessage.getText().length() > 0)
					btnSend.setEnabled(true);
				else
					btnSend.setEnabled(false);
			}
		});
		
		//
		//spPanelMessage
		//
		this.spPanelMessage = new JScrollPane();
		this.spPanelMessage.setViewportView(this.txtMessage);
				
		//
		//sentMessagePanel
		//
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0;
		gridBagConstraints2.weighty = 1.0;
		gridBagConstraints2.gridx = 0;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.gridwidth = 1;
		gridBagConstraints3.gridy = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.anchor = GridBagConstraints.SOUTH;
		gridBagConstraints1.gridy = 1;
		this.sentMessagePanel = new JPanel();
		this.sentMessagePanel.setLayout(new GridBagLayout());
		this.sentMessagePanel.add(this.btnSend, gridBagConstraints1);
		this.sentMessagePanel.add(this.friendPanel, gridBagConstraints3);
		this.sentMessagePanel.add(this.spPanelMessage, gridBagConstraints2);

		//
		//jSplitPane
		//
		this.jSplitPane = new JSplitPane();
		this.jSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		this.jSplitPane.setDividerSize(8);
		this.jSplitPane.setDividerLocation(100);
		this.jSplitPane.setTopComponent(this.spPanelDisplay);
		this.jSplitPane.setBottomComponent(this.sentMessagePanel);
						
		//
		//contentPanel
		//
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		this.contentPanel = new JPanel();
		this.contentPanel.setLayout(new GridBagLayout());
		this.contentPanel.add(this.jSplitPane, gridBagConstraints);			
				
		//
		//mainContentPane
		//
		this.mainContentPane = new JPanel();
		this.mainContentPane.setLayout(new BorderLayout());
		this.mainContentPane.add(this.mainToolBar, BorderLayout.NORTH);
		this.mainContentPane.add(this.contentPanel, BorderLayout.CENTER);
			
				
		this.setSize(520, 400);
		this.setContentPane(this.mainContentPane);
		this.setTitle("Message");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/*
	 * send nick to text box To
	 */
	public void setTo(String to)
	{
		this.txtTo.setText(to);
		this.setTitle(to);
	}
	
	public String getFriendNick()
	{
		return this.txtTo.getText();
	}
	
	public void setEditableForMessageField(boolean b)
	{
		this.txtMessage.setEditable(b);
	}
	
	public void appendtoDisplay(String str)
	{
		try
		{
			this.DisplayDoc.insertString(this.DisplayDoc.getLength(), str, null);
		}
		catch(BadLocationException ex)
		{
			Tracer.Log(this.getClass(), ex);
		}		
	}
	
	/**
	 * move to the last line in display box
	 */
	public void pushDown()
	{	try
		{	
			this.txtDisplayMessage.setCaretPosition(this.txtDisplayMessage.getText().length());			
			this.txtDisplayMessage.scrollRectToVisible( new Rectangle(0,this.txtDisplayMessage.getSize().height,1,1));			
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
		me.appendToDocument(DisplayDoc);
		pushDown();
	}

	
	public void messageReceived(SessionEvent ev)
	{
		this.addInstantMessage(this.txtTo.getText(), ev.getMessage());			
	}
	
	public void fileTransferReceived(SessionFileTransferEvent ev)
	{
		int result = JOptionPane.showConfirmDialog(Form_Message.this, ev.getFrom() + " send file t you.");
		if(result == JOptionPane.OK_OPTION)
		{
			
		}
		else if(result == JOptionPane.CANCEL_OPTION)
		{
			return;
		}
	}
	
	@Override
	public void doEvent(int eventType, SessionEvent e)
	{
		switch (eventType)
		{
			case ServiceConstants.SERVICE_MESSAGE:
				this.messageReceived(e);
				break;
			
			case ServiceConstants.SERVICE_FILETRANSFER:
				this.fileTransferReceived((SessionFileTransferEvent)e);
				break;
				
			default:
				break;
		}
	}
}
