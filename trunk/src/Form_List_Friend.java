import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.util.Hashtable;
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
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ymsg.network.ServiceConstants;
import ymsg.network.Session;
import ymsg.network.SessionConferenceEvent;
import ymsg.network.SessionEvent;
import ymsg.network.SessionNewMailEvent;
import ymsg.network.StatusConstants;
import ymsg.network.YahooConference;
import ymsg.network.YahooGroup;
import ymsg.network.YahooUser;
import ymsg.support.SwingModelFactory;

public class Form_List_Friend extends BaseFrame implements ISessionEventHandler
{
	private JTree friendTree;
	
	private Form_Add_Friend formAddFriend;	
	
	private JMenu buddyMenu;
	
	private JMenu statusMenu;
	
	private JMenuItem exitItem;
	
	private JMenuItem addfriendItem;
	
	private JMenuItem conferenceItem;
	
	private JMenuItem logOutItem;
	
	private JMenuItem online;
	
	private JMenuItem invisible;
	
	private JMenuItem customStatus;
	
	private JMenuItem busy;	
	
	private JMenuBar mnuBar;	
	
	private ListPopupMenu popup;
	
	public JPanel topPanel;
	
	private JButton btnMail;
	
	private JLabel lbMyStatus;
	
	private TrayIcon trayIcon;
	
	private Dlg_MakeConference dlgMakeConference;
	
	private Form_Conference frmConference;
	
	public Hashtable <String, Form_Message> listFormMessages;
	
	private Form_Login parent;
	
	public Form_List_Friend(Form_Login parent)
	{
		this.parent = parent;
		this.setTitle("Buddy List");
		sessionHandler.addEventReceiver(this);
		
		this.friendTree = new JTree();
		this.friendTree.setCellRenderer(new CellRenderer());
		this.friendTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);		
		this.listFormMessages = new Hashtable<String, Form_Message>();
		this.popup = new ListPopupMenu();		
		
		MouseListener ml = new MouseAdapter() 
		{
		     public void mousePressed(MouseEvent e) 
		     {
		    	//if the pressed button is not right button.
				if(e.isMetaDown())
					return;
		    	 
		         TreePath selPath = friendTree.getSelectionPath();		         
		         if(selPath == null)
		        	 return;
		         
		         int pathCount = selPath.getPathCount();
		         
		         if(pathCount <= 2)
		        	 return;
		             
		         if(e.getClickCount() == 1)
		         {            	
		            	 
		         }
		         else if(e.getClickCount() == 2)
		         {
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
		           	 Form_Message formMessage = new Form_Message();
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
		 //btnMail
		 //		 
		 this.btnMail = new JButton();
		 this.btnMail.setBorderPainted(false);
		 this.btnMail.setBackground(Color.lightGray);
		 this.btnMail.setSize(20, 15);
		 //this.btnMail.setOpaque(false);
		 //this.btnMail.setContentAreaFilled(false);
		 this.btnMail.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
			 {
				 try
				 {
					 URI url = new URI("https://login.yahoo.com/config/login_verify2?&.src=ym");
					 Desktop.getDesktop().browse(url);
				 }
				 catch(Exception ex)
				 {}
				 
			 }
		 });		 

		 this.btnMail.addMouseListener(new MouseAdapter()
		 {
			 public void mouseEntered(MouseEvent e)
			 {
				 btnMail.setBackground(Color.gray);
			 }
			 
			 public void mouseExited(MouseEvent e)
			 {
				 btnMail.setBackground(Color.LIGHT_GRAY);
			 }
		 });
		 
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
		 this.topPanel.add(this.btnMail,BorderLayout.LINE_END);		
		 
		 //
		 //Logout item
		 //
		 this.exitItem = new JMenuItem("Exit");
		 this.exitItem.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
			 {
				 try
				 {							
					 System.exit(0);
				 }
				 catch(Exception ex)
				 {
					 Tracer.Log(this.getClass(), ex);
				 }
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
					 session.setStatus(StatusConstants.STATUS_AVAILABLE);
					 setMyStatus();
				 }
				 catch(IOException ex)
				 {
					 Tracer.Log(this.getClass(), ex);
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
					 session.setStatus(StatusConstants.STATUS_BUSY);
					 setMyStatus();
				 }
				 catch(IOException exc)
				 {
					 Tracer.Log(Form_List_Friend.this.getClass(), exc);
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
					 session.setStatus(StatusConstants.STATUS_INVISIBLE);
					 setMyStatus();
				 }
				 catch(IOException ex)
				 {
					 Tracer.Log(this.getClass(), ex);
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
				 Form_Custom_Status customForm = new Form_Custom_Status(Form_List_Friend.this);
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
				 formAddFriend = new Form_Add_Friend();
				 formAddFriend.setVisible(true);
			 }
		 });
		 
		 //
		 //conferenceItem
		 //
		 this.conferenceItem = new JMenuItem("Conference...");
		 this.conferenceItem.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
			 {				
				 showConferenceDialog();
			 }
		 });
		 
		 //
		 //logOutItem
		 //
		 this.logOutItem = new JMenuItem("Logout");
		 this.logOutItem.addActionListener(new ActionListener()
		 {
			 public void actionPerformed(ActionEvent e)
			 {		
				 try
				 {
					 session.logout();
					 Form_List_Friend.this.setVisible(false);
					 Form_List_Friend.this.parent.setVisible(true);
				 }
				 catch (Exception ex)
				 {
					 Tracer.Log(Form_List_Friend.this.getClass(), ex);
				 }
			 }
		 });
		 
		 //
		 //BuddyMenu
		 //
		 this.buddyMenu = new JMenu("Buddy");
		 this.buddyMenu.add(this.addfriendItem);
		 this.buddyMenu.add(this.conferenceItem);
		 //this.buddyMenu.add(this.logOutItem);
		 this.buddyMenu.add(this.exitItem);
		 
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
		 
		 Container container = this.getContentPane();
		container.add(new JScrollPane(this.friendTree),BorderLayout.CENTER);
		container.add(topPanel,BorderLayout.PAGE_START);
		
		pack();
		this.setSize(350,550);
		
		this.showTrayIcon();
		
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public String getNickToAddressField(TreePath selPath)
	{
		 String result = "";
		 String temp = selPath.getLastPathComponent().toString();
    	 String check = temp.substring(0, 3);		            	 
    	 if(check.equals(Constant.ID_FORMAT))
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
			session.removeFriend(strFriend, strGroup);
		}
		catch (IllegalStateException ex) 
		{
			Tracer.Log(this.getClass(), ex);
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
		catch (IOException ex) 
		{
			Tracer.Log(this.getClass(), ex);
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

	public void messageReceived(SessionEvent ev)
	{			
		String strFriend = ev.getFrom();
		
		if(this.listFormMessages.containsKey(strFriend))
		{
			 Form_Message frmTemp = this.listFormMessages.get(strFriend);
    		 if(frmTemp != null && !frmTemp.isShowing()){
    			 if(strFriend.equals(frmTemp.getFriendNick()))
    			 {
    				 //frmTemp.addInstantMessage(strFriend, ev.getMessage());
    				 frmTemp.setVisible(true);
    				 return;
    			 }
    		 }
		}
		else
		{
			Form_Message formMessage = new Form_Message();
			formMessage.setTo(strFriend);
			formMessage.setEditableForMessageField(true);
			this.listFormMessages.put(strFriend, formMessage);
			formMessage.addInstantMessage(strFriend, ev.getMessage());
			formMessage.setVisible(true);
		}		
	}
	
	public void newMailReceived(SessionNewMailEvent ev)
	{
		int numberOfMail = ev.getMailCount();
		if(numberOfMail > 0)
		{
			this.btnMail.setIcon(new ImageIcon(getClass().getResource("image/newmail.png")));
			this.btnMail.setToolTipText(String.valueOf(numberOfMail) + " mail(s) unread");
		}
		else
		{
			this.btnMail.setIcon(new ImageIcon(getClass().getResource("image/nomail.png")));
			this.btnMail.setToolTipText("0 mail unread");
		}
	}
	
	public void conferenceInviteReceived(SessionConferenceEvent ev) 
	{
		try
		{
			String currentUser = session.getLoginIdentity().getId();
			String strFrom = ev.getFrom();
		
			//Check if I invite myself!
			if(currentUser.equals(strFrom))
				return;
			
			String strMessage = ev.getMessage();
			int selectedValue = 
				Helper.ConfirmWithCancel("You are invited to join the conference from: " + strFrom + ". Greeting message: " + strMessage);
			if(selectedValue == JOptionPane.YES_OPTION)
			{
				YahooConference yConference = ev.getRoom();
				session.acceptConferenceInvite(yConference);
				
				//Show the Form_Conference;
				frmConference = new Form_Conference();
				YahooUser[] yUsers = ev.getRoom().getUsersArray();
				int size = yUsers.length;
				String[] users = new String[size];
				for(int id = 0; id < size; id++)
				{
					users[id] = yUsers[id].getId();
				}
				
				frmConference.acceptConference(users, yConference);
				frmConference.setVisible(true);
			}
			else
			{
				
			}
		}
		catch (Exception exc) 
		{
			Tracer.Log(this.getClass(), exc);
			Helper.Error(UserMsg.MAKE_CONFERENCE_FAILED);
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
			
			case ServiceConstants.SERVICE_NEWPERSONMAIL:
				this.newMailReceived((SessionNewMailEvent)e);
				break;
				
			case ServiceConstants.SERVICE_CONFINVITE:
				this.conferenceInviteReceived((SessionConferenceEvent)e);
				break;
				
			default:
				break;
		}
	}
	
	private void showConferenceDialog()
	{
		try
		{
			this.dlgMakeConference = new Dlg_MakeConference(this, true);
			dlgMakeConference.setTreeModel(this.friendTree.getModel());
			
			dlgMakeConference.setLocationRelativeTo(this);
			
			//Set the host name.
			dlgMakeConference.setHost(session.getLoginIdentity().getId());
			
			//Show the conference dialog.
			dlgMakeConference.setVisible(true);
			
			int option = dlgMakeConference.getSelectedValue();
			if(option == JOptionPane.OK_OPTION)
			{
				//Show the Form_Conference;
				Form_Conference frmConference = new Form_Conference();
				String[] users = dlgMakeConference.getUsers();
				String msg = dlgMakeConference.getGreetingMessage();
				frmConference.createConference(users, msg);
				frmConference.setVisible(true);
			}
		}
		catch (Exception exc) 
		{
			Tracer.Log(this.getClass(), exc);
			Helper.Error(UserMsg.MAKE_CONFERENCE_FAILED);
		}
	}
	
	private void showTrayIcon()
	{
		try
		{
			//Check if SystemTray is supported. If not supported we exit
			if (!SystemTray.isSupported()) 
			{
			   System.err.println("Your operating system does not support SystemTray.");
			   return;
			}
			
			SystemTray systemTray = SystemTray.getSystemTray();
			
			//Load an image for the tray icon. loadImage() is a private method
			//not shown here
			Image iconImage = Helper.createImage(Constant.PATH_TRAY_ICON, Constant.PROGRAM_NAME);

			//Create the popup menu and add some menu item to it
			PopupMenu popupMenu = new PopupMenu("Actions");

			MenuItem menuItemShow = new MenuItem("Show JYM");
			
			menuItemShow.addActionListener(new TrayIconShowActionListener());

			MenuItem menuItemExit = new MenuItem("Exit JYM");
			//Set a unique command name for this particular menu item
			
			menuItemExit.addActionListener(new TrayIconExitActionListener());

			//Assume that the class has implemented ActionListener
			//menuItem.addActionListener(this /* handler */);

			//Add the menu item to the popup menu
			popupMenu.add(menuItemShow);
			
			//Add the menu item to the popup menu
			popupMenu.add(menuItemExit);

			//Create the TrayIcon
			this.trayIcon = new TrayIcon(iconImage, Constant.PROGRAM_NAME, popupMenu);
			
			this.trayIcon.addMouseListener(new TrayIconMouseListener());
			
			try 
			{
	            systemTray.add(trayIcon);
	        } 
			catch (AWTException exc) 
			{
				Tracer.Log(this.getClass(), exc);
	            System.out.println("TrayIcon could not be added.");
	            return;
	        }
		}
		catch (Exception exc) 
		{
			Tracer.Log(this.getClass(), exc);
            System.out.println("TrayIcon could not be showed.");
            return;
		}		
	}
	
	private class TrayIconExitActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{	
			System.exit(0);
		}
	}
	
	private class TrayIconShowActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{	
			Form_List_Friend.this.setVisible(true);
		}
	}
	
	private class TrayIconMouseListener extends MouseAdapter
	{
		 public void mousePressed(MouseEvent e) 
	     {
			 //if the pressed button is not right button.
			 if(e.isMetaDown())
				return;	    	 
		             
			 //Allow only double clicks.
		     if(e.getClickCount() == 2)
		     {
		       	 Form_List_Friend.this.setVisible(true);
		     }
	     }
	}
}
