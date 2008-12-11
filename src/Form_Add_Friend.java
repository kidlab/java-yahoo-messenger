import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ymsg.network.Session;
import ymsg.network.YahooGroup;


public class Form_Add_Friend extends JFrame
{
	private JLabel lbUserId;
	private JLabel lbGroup;	
	private JButton btnAccept;
	private JButton btnCancel;	
	private JTextField txtUserId;
	private JComboBox cbGroup;	
	private Session session;
	private Container container;
	private AutoCompleteBox auBox;
	
	public Form_Add_Friend(Session session)
	{
		super("Add friend");
		this.session = session;
		
		//
		//lbUserId
		//
		this.lbUserId = new JLabel("Yahoo Friend Nickname:");
		this.lbUserId.setBounds(10, 10, 200, 20);
		
		//
		//txtUserId
		//
		this.txtUserId = new JTextField();
		this.txtUserId.setBounds(10, 35, 200, 20);
		
		//
		//lbGroup
		//
		this.lbGroup = new JLabel("Group:");
		this.lbGroup.setBounds(10, 65, 50, 20);
		
		
		//
		//cbGroup
		//		
		YahooGroup []yg = this.session.getGroups();
		int length = yg.length;
		String []group = new String[length];
		for(int i = 0; i < length; i++)
			group[i] = yg[i].getName();
		
		this.cbGroup = new JComboBox(group);
		this.cbGroup.setBounds(65, 62, 120, 25);
		this.cbGroup.setEditable(true);
		
		this.auBox = new AutoCompleteBox(this.cbGroup);
		
		//
		//btnAccept
		//
		this.btnAccept = new JButton("Add");
		this.btnAccept.setBounds(10, 90, 85, 23);
		
		//
		//
		//btnCancel
		this.btnCancel = new JButton("Cancel");
		this.btnCancel.setBounds(115, 90, 85, 23);
		this.btnCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				Form_Add_Friend.this.dispose();
			}
		}
		);
		
		
		this.container = this.getContentPane();
		this.container.add(this.lbUserId);
		this.container.add(this.txtUserId);
		this.container.add(this.lbGroup);
		this.container.add(this.cbGroup);
		this.container.add(this.btnAccept);
		this.container.add(this.btnCancel);
		
		this.setLayout(null);
		pack();
		this.setSize(250,200);
		this.setVisible(true);
	}
}
