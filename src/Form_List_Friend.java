import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ymsg.network.Session;
import ymsg.network.SessionAdapter;
import ymsg.network.SessionEvent;
import ymsg.network.YahooGroup;
import ymsg.network.YahooUser;
import ymsg.support.MessageDecoder;
import ymsg.support.MessageDecoderSettings;
import ymsg.support.MessageElement;


public class Form_List_Friend extends JFrame
{
	private JTree friendTree;
	private Session session;
	private Form_Message formMessage; 
	private boolean showForm = false;
	private JMenu buddyMenu;
	private JMenuItem logoutItem;
	private Form_Login formLogin;
	private JMenuBar mnuBar;
	
	
	public Form_List_Friend()
	{
		super("Buddy List");
		
		//this.session.addSessionListener(new SessionHandler());
		
		this.friendTree = new JTree();
		this.friendTree.setCellRenderer(new CellRenderer());
		this.friendTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		         int selRow = friendTree.getRowForLocation(e.getX(), e.getY());
		         TreePath selPath = friendTree.getPathForLocation(e.getX(), e.getY());
		         if(selRow != -1) {
		             if(e.getClickCount() == 1) {
		            	 if(!showForm || !formMessage.isShowing())
		            	 {
		            		 formMessage = new Form_Message(session);
		            		 formMessage.show();
		            		 showForm = true;
		            	 }
		            	 
		            	 String nick = getNickToAddressField(selPath);
		            	 if(nick.length() == 0);
		            	 else
		            	 {
		            		 formMessage.setTo(nick);
		            		 formMessage.setEditableForMessageField(true);
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
		 //BuddyMenu
		 //
		 this.buddyMenu = new JMenu("Buddy");
		 this.buddyMenu.add(this.logoutItem);
		 
		 //
		 //menu bar
		 //
		 this.mnuBar = new JMenuBar();
		 this.mnuBar.add(this.buddyMenu);
		 this.setJMenuBar(this.mnuBar);
		
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
				setText(yu.getId()+" ("+Long.toHexString(yu.getStatus())+")");
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
	
	/*private class SessionHandler extends SessionAdapter
	{
		public void messageReceived(SessionEvent ev)
		{
			 if(!showForm || !formMessage.isShowing())
        	 {
        		 formMessage = new Form_Message(session);
        		 formMessage.show();
        		 showForm = true;
        		 formMessage.appendtoDisplay(ev.getFrom() + ":");
        		 
        		 MessageDecoderSettings sets = new MessageDecoderSettings();
    			 sets.setEmoticonsDecoded(true);
    			 sets.setRespectTextFade(true);
    			 sets.setRespectTextAlt(true);
    			 sets.setOverrideFont(null,10,18,null);
        		 MessageDecoder decorder = new MessageDecoder(sets);
        		 
        		 MessageElement me = decorder.decode(ev.getMessage());
        		 me.appendToDocument(formMessage.DisplayDoc);
        	 }			
		}
	}*/
}
