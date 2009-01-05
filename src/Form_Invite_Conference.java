import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import ymsg.network.Session;
import ymsg.network.YahooGroup;


public class Form_Invite_Conference extends JFrame
{
	private JTree friendTree;
	private JList conferenceList;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnInvite;
	private JButton btnCancel;
	private Session session;
	//private SessionHandler sessionHandler;
	
	public Form_Invite_Conference()
	{
		// TODO Auto-generated constructor stub
		super("Invite Conferences");
		
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Friend Yahoo ID");
		
		
		this.setSize(650, 400);
		this.setResizable(false);
		this.setLayout(null);
	}
	
	private void CreateNodes(DefaultMutableTreeNode top)
	{
		DefaultMutableTreeNode group = null;
	    DefaultMutableTreeNode friendId = null;
	    
	    YahooGroup[] yg = session.getGroups();
	    for(int i = 0; i < yg.length ; i ++)
	    {
	    	group = new DefaultMutableTreeNode(yg[i].getName());
	    	top.add(group);	    	
	    }
	}	
}
