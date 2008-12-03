import java.awt.Container;
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

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import sun.font.Decoration;

import ymsg.network.Session;
import ymsg.network.SessionAdapter;
import ymsg.network.SessionEvent;
import ymsg.support.MessageDecoder;
import ymsg.support.MessageDecoderSettings;
import ymsg.support.MessageElement;


public class Form_Message extends JFrame
{
	private JTextArea txtMessage;
	private JTextArea txtDisplayMessage;	
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
						appendtoDisplay("me: " + txtMessage.getText().trim() + "\n");	
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
							appendtoDisplay("me: " + txtMessage.getText().trim() + "\n");	
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
		this.txtDisplayMessage = new JTextArea();
		this.txtDisplayMessage.setLineWrap(true);
		this.txtDisplayMessage.setWrapStyleWord(true);
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
		sets.setRespectTextFade(true);
		sets.setRespectTextAlt(true);
		sets.setOverrideFont(null,10,18,null);
		
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
		pack();
		this.setSize(350,500);
		this.setVisible(true);
	}
	
	public void  setToFriend(String nick)
	{
		this.txtTo.setText(nick);
	}
	
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
	
	private class SessionHandler extends SessionAdapter
	{
		public void messageReceived(SessionEvent ev)
		{
			appendtoDisplay(ev.getFrom() + ":");
			MessageElement me = decoder.decode(ev.getMessage());
			//decoder.appendToDocument(ev.getMessage(), DisplayDoc);
			me.appendToDocument(DisplayDoc);
		}
	}
}


