import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ymsg.network.Session;



public class Form_Custom_Status extends JFrame
{
	private Session session;
	private JTextArea txtStatus;
	private JButton bntOk;
	private JButton bntCancel;
	private JCheckBox chkBusy;
	private JLabel lbStatus;
	private Container panel;
	private JScrollPane spStatus;
	
	public Form_Custom_Status(Session session) {
		// TODO Auto-generated constructor stub
		super("Your custom status");
		this.session = session;
		//
		//lbStatus
		//
		this.lbStatus = new JLabel("Type your status:");
		this.lbStatus.setBounds(10, 5, 110, 25);
		
		//
		//txtStatus
		//
		this.txtStatus = new JTextArea();
		this.txtStatus.setBounds(10,35,200,60);
		this.txtStatus.setWrapStyleWord(true);
		this.txtStatus.setLineWrap(true);
		this.txtStatus.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{				
								
			}
			
			public void keyTyped(KeyEvent e)
			{
				
			}
			
			public void keyReleased(KeyEvent e)
			{
				if(txtStatus.getText().length() > 0)
					bntOk.setEnabled(true);
				else
					bntOk.setEnabled(false);
			}
		});
		
		//
		//chkBusy
		//
		this.chkBusy = new JCheckBox("Busy");
		this.chkBusy.setBounds(10, 100, 100, 25);
		
		//
		//spStatus
		//
		this.spStatus = new JScrollPane(this.txtStatus);
		this.spStatus.setBounds(10,35,200,60);
		
		//
		//btnOk
		//
		this.bntOk = new JButton("Ok");
		this.bntOk.setBounds(10, 130, 80, 25);
		this.bntOk.setEnabled(false);
		this.bntOk.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String status = txtStatus.getText().trim();
				if(chkBusy.isSelected())
				{
					try
					{
						Form_Custom_Status.this.session.setStatus(status, true);
					}
					catch(IOException ex)
					{}
				}
				else
				{
					try
					{
						Form_Custom_Status.this.session.setStatus(status, false);
					}
					catch(IOException ex)
					{}
				}
				
				
				dispose();
			}
		});
		
		//
		//btnCancel
		//
		this.bntCancel = new JButton("Cancel");
		this.bntCancel.setBounds(95, 130, 80, 25);
		this.bntCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		
		this.panel = this.getContentPane();
		this.panel.add(this.lbStatus);
		this.panel.add(this.spStatus);
		this.panel.add(this.chkBusy);
		this.panel.add(this.bntOk);
		this.panel.add(this.bntCancel);
		
		this.setLayout(null);
		this.setMaximumSize(new Dimension(250,200));
		this.setMinimumSize(new Dimension(250,200));
		this.setResizable(false);
		this.pack();		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
