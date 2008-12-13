import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ymsg.network.Session;
import ymsg.network.StatusConstants;
import ymsg.network.YahooGroup;
import ymsg.network.YahooUser;

public class Form_List_Friend extends JFrame
{
	private JTree friendTree;
	private Session session;
	private Form_Add_Friend formAddFriend;
	private boolean showForm = false;
	private JMenu buddyMenu;
	private JMenuItem logoutItem;
	private JMenuItem addfriendItem;
	private Form_Login formLogin;
	private JMenuBar mnuBar;	
	
	private Hashtable <String, Form_Message> listFormMessages;
	
	public Form_List_Friend()
	{
		super("Buddy List");
		
		this.friendTree = new JTree();
		this.friendTree.setCellRenderer(new CellRenderer());
		this.friendTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		this.listFormMessages = new Hashtable<String, Form_Message>();
		
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		         int selRow = friendTree.getRowForLocation(e.getX(), e.getY());
		         TreePath selPath = friendTree.getPathForLocation(e.getX(), e.getY());
		         if(selRow != -1) {
		             if(e.getClickCount() == 1) {		            	 
		            	
		            	 String nick = getNickToAddressField(selPath);
		            	 if(listFormMessages.containsKey(nick))
		            	 {
		            		 Form_Message frmTemp = listFormMessages.get(nick);
		            		 if(frmTemp != null && frmTemp.isShowing())
		            			 return;
		            		 else{
		            			 frmTemp.setVisible(true);
		            			 return;
		            		}
		            	 }
		            	 Form_Message formMessage = new Form_Message(session);
		            	 formMessage.setVisible(true);	    	
		            	 
		            	 
		            	 if(nick.length() == 0);
		            	 else
		            	 {
		            		 formMessage.setTo(nick);
		            		 formMessage.setEditableForMessageField(true);
		            		 listFormMessages.put(nick, formMessage);
		            	 }            	 
		             }
		             else if(e.getClickCount() == 2) {
		            	 System.out.print("double hello");
		             }
		         }
		     }
		 };
		 this.friendTree.addMouseListener(ml);	 
		 
		 //
		 //Logout item
		 //
		 this.logoutItem = new JMenuItem("Exit");
		 this.logoutItem.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
			 {
				 try
				 {							
					 System.exit(0);
				 }
				 catch(Exception ex)
				 {}
			 }
		 });
		 
		 //
		 //Add friend Item
		 //
		 this.addfriendItem = new JMenuItem("Add Friend");
		 this.addfriendItem.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
			 {				
				 formAddFriend = new Form_Add_Friend(session);
				 formAddFriend.show();
			 }
		 });
		 
		 //
		 //BuddyMenu
		 //
		 this.buddyMenu = new JMenu("Buddy");
		 this.buddyMenu.add(this.addfriendItem);
		 this.buddyMenu.add(this.logoutItem);
		 
		 
		 //
		 //menu bar
		 //
		 this.mnuBar = new JMenuBar();
		 this.mnuBar.add(this.buddyMenu);
		 this.setJMenuBar(this.mnuBar);
		 
		 //
		 //
		 //
		
		Container container = this.getContentPane();
		container.add(new JScrollPane(this.friendTree),BorderLayout.CENTER);
		
		pack();
		this.setSize(350,550);
		this.setVisible(true);
	}
	
	public String getNickToAddressField(TreePath selPath)
	{
		 String result = "";
		 String temp = selPath.getLastPathComponent().toString();
    	 String check = temp.substring(0, 3);		            	 
    	 if(check.equals("id="))
    	 {
    		 int end = temp.indexOf(" ");
    		 int start = 3;
    		 result = temp.substring(start, end);    		 		            		 
    	 }
		
		return result;
	}
	
	public void setModel(TreeModel treeModel) 
	{ 
		this.friendTree.setModel(treeModel); 
	}
	
	class CellRenderer extends JLabel implements TreeCellRenderer
	{	public Component getTreeCellRendererComponent(JTree tree,Object value,
			boolean selected,boolean expanded,boolean leaf,int row,boolean focus)
		{	if(value instanceof YahooUser)
			{	YahooUser yu = (YahooUser)value;
				String status = "";
				switch((int)yu.getStatus())
				{
					case (int)StatusConstants.STATUS_AVAILABLE:
						status = "(online)";
						break;
					case (int)StatusConstants.STATUS_BUSY:
						status = "(busy)";
						break;
					case (int)StatusConstants.STATUS_IDLE:
						status = "(idle)";
						break;
					case (int)StatusConstants.STATUS_NOTATDESK:
						status = "(not at my desk)";
						break;
					case (int)StatusConstants.STATUS_INVISIBLE:
						status = "(invisible)";
						break;
					case (int)StatusConstants.STATUS_ONPHONE:
						status = "(I'm on mobie)";
						break;
					case (int)StatusConstants.STATUS_ONVACATION:
						status = "(on my vacation)";
						break;
					case (int)StatusConstants.STATUS_OFFLINE:
						status = "(offline)";
						break;
					case (int)StatusConstants.STATUS_BAD:
						status = "(bad)";
						break;
					case (int)StatusConstants.STATUS_BADUSERNAME:
						status = "(badusername)";
						break;					
					case (int)StatusConstants.STATUS_COMPLETE:
						status = "(complete)";
						break;
					case (int)StatusConstants.STATUS_CUSTOM:
						status = "(" + yu.getCustomStatusMessage() + ")";
						break;
				}
				setText(yu.getId() + " " + status);
			}
			else if(value instanceof YahooGroup)
			{	setText( ((YahooGroup)value).getName() );
			}
			else
			{	setText(value.toString());
			}
			setBackground(selected ? Color.lightGray : Color.white);
			return this;
		}
	}
	
	public void setSession(Session session)
	{
		this.session = session;
	}	
}
