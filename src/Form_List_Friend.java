import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ymsg.network.Session;
import ymsg.network.SessionAdapter;
import ymsg.network.SessionEvent;
import ymsg.network.SessionFriendEvent;
import ymsg.network.SessionNewMailEvent;
import ymsg.network.StatusConstants;
import ymsg.network.YahooGroup;
import ymsg.network.YahooUser;
import ymsg.support.MessageElement;

public class Form_List_Friend extends JFrame
{
	private JTree friendTree;
	private Session session;
	private Form_Add_Friend formAddFriend;
	private boolean showForm = false;
	private JMenu buddyMenu;
	private JMenu statusMenu;
	private JMenuItem logoutItem;
	private JMenuItem addfriendItem;
	private JMenuItem online;
	private JMenuItem invisible;
	private JMenuItem customStatus;
	private JMenuItem busy;
	private Form_Login formLogin;
	private JMenuBar mnuBar;	
	private ListPopupMenu popup;
	public JPanel topPanel;
	public JLabel lbMail;
	private JLabel lbMyStatus;
	
	public Hashtable <String, Form_Message> listFormMessages;
	
	public Form_List_Friend(Session session)
	{
		super("Buddy List");
		this.session = session;		
		
		this.friendTree = new JTree();
		this.friendTree.setCellRenderer(new CellRenderer());
		this.friendTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);		
		this.listFormMessages = new Hashtable<String, Form_Message>();
		this.popup = new ListPopupMenu();
		
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		    	//if the pressed button is not right button.
				if(e.isMetaDown())
					return;
		    	 
		         int selRow = friendTree.getRowForLocation(e.getX(), e.getY());
		         TreePath selPath = friendTree.getPathForLocation(e.getX(), e.getY());		         
		         if(selRow != -1) {
		             if(e.getClickCount() == 1) {		            	 
		            	
		            	 String nick = getNickToAddressField(selPath);
		            	 if(listFormMessages.containsKey(nick))
		            	 {
		            		 Form_Message frmTemp = listFormMessages.get(nick);
		            		 if(frmTemp != null && !frmTemp.isShowing())
		            		 {
		            			 frmTemp.setVisible(true);
		            			 return;
		            		 }
		            		 else if(frmTemp != null && frmTemp.isShowing())
		            		 {
		            			 return;
		            		 }		            		 	            		 
		            	 }
		            	 Form_Message formMessage = new Form_Message(Form_List_Friend.this.session);
		            	 formMessage.setTitle(nick);
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
		     
		     public void mouseReleased(MouseEvent e)
		     {
		    	//if the pressed button is right button.
				if(e.isMetaDown())
				{		   
					int selRow = friendTree.getRowForLocation(e.getX(), e.getY());
					if(selRow <= 0)
					{
						popup.setVisible(false);
						return;
					}
					
					TreePath selPath = friendTree.getPathForLocation(e.getX(), e.getY());		    	 
			    	//Set the current node at mouse location to be selected.
			    	friendTree.setSelectionPath(selPath);
				    if (e.isPopupTrigger())	
				    {	
				    	popup.setLocation(e.getLocationOnScreen());
				    	popup.setVisible(true);	
				    }
				}
				else
				{
					popup.setVisible(false);
				}
		     }
		 };
		 
		 this.friendTree.addMouseListener(ml);	 
		 
		 //
		 //lb Mail
		 //		 
		 this.lbMail = new JLabel();
		 
		 //
		 //lbMyStatus
		 //
		 String status = getMyStatus();
		 this.lbMyStatus = new JLabel("My Status: " + status);
		 
		 
		 //
		 //top Panel
		 //
		 this.topPanel = new JPanel(new BorderLayout());
		 this.topPanel.add(this.lbMyStatus,BorderLayout.LINE_START);
		 this.topPanel.add(this.lbMail,BorderLayout.LINE_END);
		
		 
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
		 //online status item
		 //
		 this.online = new JMenuItem("Available",new ImageIcon(getClass().getResource("image/online.gif")));
		 this.online.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
			 {
				 try
				 {
					 Form_List_Friend.this.session.setStatus(StatusConstants.STATUS_AVAILABLE);
					 setMyStatus();
				 }
				 catch(IOException ex)
				 {
					 
				 }
			 }
		 });
		 
		 //
		 //busy status item
		 //
		 this.busy = new JMenuItem("Busy",new ImageIcon(getClass().getResource("image/busy.gif")));
		 this.busy.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
			 {
				 try
				 {
					 Form_List_Friend.this.session.setStatus(StatusConstants.STATUS_BUSY);
					 setMyStatus();
				 }
				 catch(IOException ex)
				 {
					 
				 }
			 }
		 });
		 
		 //
		 //invisible status item
		 //
		 this.invisible = new JMenuItem("Invisible",new ImageIcon(getClass().getResource("image/offline.gif")));
		 this.invisible.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
			 {
				 try
				 {
					 Form_List_Friend.this.session.setStatus(StatusConstants.STATUS_INVISIBLE);
					 setMyStatus();
				 }
				 catch(IOException ex)
				 {
					 
				 }
			 }
		 });
		 
		 //
		 //custom status item
		 //
		 this.customStatus = new JMenuItem("Custom status...");
		 this.customStatus.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
			 {
				 Form_Custom_Status customForm = new Form_Custom_Status(Form_List_Friend.this.session,Form_List_Friend.this);
				 customForm.setVisible(true);
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
				 formAddFriend = new Form_Add_Friend(Form_List_Friend.this.session);
				 formAddFriend.setVisible(true);
			 }
		 });
		 
		 //
		 //BuddyMenu
		 //
		 this.buddyMenu = new JMenu("Buddy");
		 this.buddyMenu.add(this.addfriendItem);
		 this.buddyMenu.add(this.logoutItem);
		 
		 //
		 //Status Menu
		 //
		 this.statusMenu = new JMenu("Status");
		 this.statusMenu.add(this.online);
		 this.statusMenu.add(this.busy);
		 this.statusMenu.add(this.invisible);
		 this.statusMenu.add(this.customStatus);
		 
		 
		 //
		 //menu bar
		 //
		 this.mnuBar = new JMenuBar();
		 this.mnuBar.add(this.buddyMenu);
		 this.mnuBar.add(this.statusMenu);
		 this.setJMenuBar(this.mnuBar);
		 
		 //
		 //
		 //
		
		Container container = this.getContentPane();
		container.add(new JScrollPane(this.friendTree),BorderLayout.CENTER);
		container.add(topPanel,BorderLayout.PAGE_START);
		
		pack();
		this.setSize(350,550);
		this.setVisible(true);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
	
	private class CellRenderer extends JLabel implements TreeCellRenderer
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
	
	private class ListPopupMenu  extends JPopupMenu 
	{
		JMenuItem menuitem;
		
		public ListPopupMenu()
		{
			 this.menuitem = new JMenuItem("Delete");
			 this.menuitem.addActionListener(new DeleteActionListener());
			 this.add(menuitem);
		}
	}
	
	private void deleteFriend()
	{
		try
		{
			TreePath selPath = friendTree.getSelectionPath();
			
			int count = selPath.getPathCount();
			if(count <= 0)
				return;
			
			String strGroup = selPath.getParentPath().getLastPathComponent().toString();
			String strFriend = getNickToAddressField(selPath);
			this.session.removeFriend(strFriend, strGroup);
		}
		catch (IllegalStateException ex) 
		{
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
		catch (IOException ex) 
		{
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}
	
	private class DeleteActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{	
			deleteFriend();
			popup.setVisible(false);
		}
	}
	
	private String getMyStatus()
	{
		String status = "";
		switch((int)session.getStatus())
		{
			case (int)StatusConstants.STATUS_AVAILABLE:
				status = "Available";
				break;
			case (int)StatusConstants.STATUS_BUSY:
				status = "Busy";
				break;
			case (int)StatusConstants.STATUS_IDLE:
				status = "Idle";
				break;
			case (int)StatusConstants.STATUS_NOTATDESK:
				status = "Not at my desk";
				break;
			case (int)StatusConstants.STATUS_INVISIBLE:
				status = "Invisible";
				break;
			
			case (int)StatusConstants.STATUS_CUSTOM:
				status = session.getCustomStatusMessage();
				break;
		}
		
		return status;
	}
	
	public void setMyStatus()
	{
		this.lbMyStatus.setText("My status: " + getMyStatus());
	}
}