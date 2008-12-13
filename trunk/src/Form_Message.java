import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import sun.font.Decoration;

import ymsg.network.Session;
import ymsg.network.SessionAdapter;
import ymsg.network.SessionEvent;
import ymsg.support.EmoticonLoader;
import ymsg.support.MessageDecoder;
import ymsg.support.MessageDecoderSettings;
import ymsg.support.MessageElement;


public class Form_Message extends JFrame
{
	private JTextArea txtMessage;
	private JTextPane txtDisplayMessage;	
	private JScrollPane spPanelDisplay;
	private JScrollPane spPanelMessage;
	private Container container;
	private JButton btnSend;
	private JTextField txtTo;
	private Session session;
	private JLabel lbTo;
	public  Document DisplayDoc;
	private MessageDecoder decoder;
	
	public Form_Message(Session session)
	{
		super("Chat Window");
		this.session = session;		
		this.session.addSessionListener(new SessionHandler());
		
		//
		//btnSend
		//
		this.btnSend = new JButton("Send");
		this.btnSend.setBounds(270, 360, 70, 70);
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
					}
				}
				catch(Exception ex)
				{}
			}
		});
		
		//
		//lbTo
		//
		this.lbTo = new JLabel("To:");
		this.lbTo.setBounds(10, 335, 30, 25);
		
		//
		//txtTo
		//
		this.txtTo = new JTextField();
		this.txtTo.setBounds(35, 333, 225, 25);
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
				int keycode = e.getKeyCode();
				if(e.getKeyText(keycode) == "Enter")
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
						}
					}
					catch(Exception ex)
					{}
				}
			}
			
			public void keyTyped(KeyEvent e)
			{
				
			}
			
			public void keyReleased(KeyEvent e)
			{
				
			}
		});
		
		
		//
		//spPanelMessage
		//
		this.spPanelMessage = new JScrollPane(this.txtMessage);
		this.spPanelMessage.setBounds(10, 360, 250, 70);
		
		//
		//txtDisplayMessage
		//
		this.txtDisplayMessage = new JTextPane();		
		this.txtDisplayMessage.setEditable(false);
		this.DisplayDoc = this.txtDisplayMessage.getDocument();
		//
		//spPanelDisplay
		//
		this.spPanelDisplay = new JScrollPane(this.txtDisplayMessage);
		this.spPanelDisplay.setBounds(10, 10, 250, 300);
		
		

		//
		//Decoder setting
		//		
		MessageDecoderSettings sets = new MessageDecoderSettings();		
		sets.setEmoticonsDecoded(true);
		//sets.setRespectTextFade(true);
		//sets.setRespectTextAlt(true);
		//sets.setOverrideFont(null,10,18,null);		
		
		decoder = new MessageDecoder(sets);	
		
		//
		//container
		//
		this.container = this.getContentPane();
		this.container.add(this.spPanelMessage);
		this.container.add(this.spPanelDisplay);
		this.container.add(this.lbTo);
		this.container.add(this.txtTo);
		this.container.add(this.btnSend);
		
		this.setLayout(null);
		this.pack();
		this.setSize(350,500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}	
	
	/*
	 * send nick to textbox To
	 */
	public void setTo(String to)
	{
		this.txtTo.setText(to);
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
		}catch(Exception e) {}
	}
	
	public void addInstantMessage(String strSender, String message)
	{		
		appendtoDisplay(strSender + ": ");			
		MessageElement me = decoder.decode(message);			
		me.appendToDocument(DisplayDoc);
		pushDown();
	}
	
	private class SessionHandler extends SessionAdapter
	{
		/**
		 * listen when the message is comming
		 */
		public void messageReceived(SessionEvent ev)
		{
			String strFriend = ev.getFrom();
			String strTo = txtTo.getText();
			if(strFriend.equals(strTo)){
				addInstantMessage(strFriend, ev.getMessage());
			}
		}
	}
}


