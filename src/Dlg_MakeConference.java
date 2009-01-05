import javax.swing.JPanel;
import java.awt.Frame;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import java.awt.Dialog;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import ymsg.network.YahooGroup;
import ymsg.network.YahooUser;
import ymsg.support.SwingModelFactory;

/**
 * The conference creator dialog box.
 *
 */
public class Dlg_MakeConference extends BaseDialog 
{
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_MESSAGE = "Join my conference :)";
	
	private JPanel contentPane = null;

	private JButton btnInvite = null;

	private JButton btnCancel = null;

	private JScrollPane spListFriend = null;

	private JScrollPane spMessage = null;

	private JTree friendTree = null;

	private JTextArea txtMessage = null;

	private JScrollPane spLstInvite = null;

	private JList lstInvitedUser = null;

	private DefaultListModel listModel;
	
	private JLabel lblFriend = null;

	private JLabel lblInvite = null;

	private JButton btnAdd = null;

	private JButton btnRemove = null;

	private JButton btnInviteOther = null;
	
	private JLabel lblInviteMsg = null;

	private JComboBox cbbHost = null;

	private JLabel lblHost = null;

	/**
	 * @param owner
	 */
	public Dlg_MakeConference(Frame owner, boolean model) 
	{
		super(owner, model);
		this.initialize();
	}
	
	/**
	 * @param owner
	 * 	The parent dialog that showed this dialog
	 */
	public Dlg_MakeConference(Dialog owner, boolean model) 
	{
		super(owner, model);
		this.initialize();
	}
	
	/**
	 * @param owner
	 * 	The parent window that showed this dialog
	 */
	public Dlg_MakeConference(Window owner)
	{
		super(owner);
		this.initialize();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() 
	{		
		this.setSize(507, 338);
		this.setResizable(false);
		this.setTitle("Create a Conference");		
		
		lblHost = new JLabel();
		lblHost.setBounds(new Rectangle(6, 8, 42, 16));
		lblHost.setText("Host");
		lblInviteMsg = new JLabel();
		lblInviteMsg.setBounds(new Rectangle(7, 210, 63, 16));
		lblInviteMsg.setToolTipText("Message");
		lblInviteMsg.setText("Message");
		lblInvite = new JLabel();
		lblInvite.setBounds(new Rectangle(310, 37, 82, 16));
		lblInvite.setText("Invited Friend");
		lblFriend = new JLabel();
		lblFriend.setBounds(new Rectangle(6, 37, 84, 16));
		lblFriend.setText("Friend List");
		
		//
		//contentPane
		//
		contentPane = new JPanel();
		contentPane.setLayout(null);
		
		//
		//spListFriend
		//
		spListFriend = new JScrollPane();
		spListFriend.setBounds(new Rectangle(5, 60, 189, 144));
		
		contentPane.add(spListFriend, null);
		
		//
		//friendTree
		//
		friendTree = new JTree();
		friendTree.setCellRenderer(new CellRenderer());
		//friendTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		spListFriend.setViewportView(friendTree);	
		
		//
		//spMessage
		//
		spMessage = new JScrollPane();
		spMessage.setBounds(new Rectangle(76, 211, 419, 55));	
		
		contentPane.add(spMessage, null);
		
		//
		//txtMessage
		//
		txtMessage = new JTextArea();
		txtMessage.addKeyListener(new MessageKeyAdapter());
		txtMessage.setText(DEFAULT_MESSAGE);
		spMessage.setViewportView(txtMessage);
		
		//
		//btnCancel
		//
		btnCancel = new JButton();
		btnCancel.setText("Cancel");
		btnCancel.setVerticalAlignment(SwingConstants.CENTER);
		btnCancel.setBounds(new Rectangle(413, 273, 78, 24));
		btnCancel.setHorizontalAlignment(SwingConstants.CENTER);
		btnCancel.addActionListener(new CancelActionListener());
		
		contentPane.add(btnCancel, null);
		
		//
		//btnInvite
		//
		btnInvite = new JButton();
		btnInvite.setText("Invite");
		btnInvite.setHorizontalTextPosition(SwingConstants.CENTER);
		btnInvite.setVerticalAlignment(SwingConstants.CENTER);
		btnInvite.setBounds(new Rectangle(312, 273, 78, 24));
		btnInvite.setHorizontalAlignment(SwingConstants.CENTER);
		btnInvite.addActionListener(new InviteActionListener());
		
		contentPane.add(btnInvite, null);
		
		//
		//spLstInvite
		//
		spLstInvite = new JScrollPane();
		spLstInvite.setLocation(new Point(307, 60));
		spLstInvite.setSize(new Dimension(189, 144));
		contentPane.add(spLstInvite, null);
		
		//
		//lstInvitedUser
		//
		this.listModel = new DefaultListModel();
		this.lstInvitedUser = new JList(listModel);
		this.lstInvitedUser.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.spLstInvite.setViewportView(lstInvitedUser);		
	
		//
		//btnAdd
		//
		btnAdd = new JButton();
		btnAdd.setHorizontalTextPosition(SwingConstants.LEFT);
		btnAdd.setText("Add >");
		btnAdd.setVerticalAlignment(SwingConstants.CENTER);
		btnAdd.setBounds(new Rectangle(204, 71, 94, 26));
		btnAdd.setHorizontalAlignment(SwingConstants.LEFT);
		btnAdd.addActionListener(new AddActionListener());
		
		contentPane.add(btnAdd, null);
		
		//
		//btnRemove
		//
		btnRemove = new JButton();
		btnRemove.setBounds(new Rectangle(204, 106, 94, 26));
		btnRemove.setHorizontalTextPosition(SwingConstants.LEFT);
		btnRemove.setText("< Remove");
		btnRemove.setVerticalAlignment(SwingConstants.CENTER);
		btnRemove.setHorizontalAlignment(SwingConstants.LEFT);
		btnRemove.addActionListener(new RemoveActionListener());
		
		contentPane.add(btnRemove, null);
		
		//
		//btnInviteOther
		//
		btnInviteOther = new JButton();
		btnInviteOther.setHorizontalTextPosition(SwingConstants.LEFT);
		btnInviteOther.setText("Other...");
		btnInviteOther.setVerticalAlignment(SwingConstants.CENTER);
		btnInviteOther.setBounds(new Rectangle(204, 141, 94, 26));
		btnInviteOther.setHorizontalAlignment(SwingConstants.LEFT);
		btnInviteOther.addActionListener(new InviteOtherActionListener());
		
		contentPane.add(btnInviteOther, null);
		
		//
		//cbbHost
		//
		cbbHost = new JComboBox();
		cbbHost.setBounds(new Rectangle(57, 7, 167, 22));
		
		contentPane.add(cbbHost, null);		
		contentPane.add(lblHost, null);
		contentPane.add(lblInviteMsg, null);
		contentPane.add(lblFriend, null);
		contentPane.add(lblInvite, null);				
		
		this.setContentPane(contentPane);
	}
	
	public void setTreeModel(TreeModel treeModel)
	{
		this.friendTree.setModel(treeModel);
	}
	
	/**
	 * Get the array of all users in the conference.
	 */
	public String[] getUsers()
	{		
		int size = this.lstInvitedUser.getModel().getSize();
		String[] users = new String[size];
	    // Get all item objects
	    for (int index = 0; index < size; index++)
	    {
	    	users[index] = this.lstInvitedUser.getModel().getElementAt(index).toString();
	    }
	    
		return users;
	}
	
	public String getGreetingMessage()
	{
		return this.txtMessage.getText();
	}
	
	/**
	 * Adds a user ID to invited friend list.
	 * @param userID The Yahoo ID of the person you want to invite.
	 */
	public void addUser(String userID)
	{
		//Check if the userID has already existed in the list.
		if(this.listModel.contains(userID))
			return;
			
		this.listModel.addElement(userID);
	}
	
	public void setHost(String hostName)
	{
		this.cbbHost.addItem(hostName);
		this.cbbHost.setSelectedItem(hostName);
	}
	
	private class InviteActionListener implements ActionListener
	{		
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
			System.out.println("Invite actionPerformed()");
			closeDialog();
		}
	}
	
	private class CancelActionListener implements ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
			selectedValue = JOptionPane.CANCEL_OPTION;
			System.out.println("Cancel actionPerformed()");
			Dlg_MakeConference.this.setVisible(false);
		}
	}
	
	private class AddActionListener implements ActionListener
	{		
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
			TreeModel treeModel = friendTree.getModel();
			TreePath selPath = friendTree.getSelectionPath();
			Object selectedObj = selPath.getLastPathComponent();
			int count = treeModel.getChildCount(selectedObj);			
			
			//If count <= 0, this node is a Yahoo ID, so add right to the list
			if(count <= 0)
			{
				String selectedUser = Helper.GetUserID(selectedObj.toString());
				if(!selectedUser.isEmpty())
					addUser(selectedUser);
			}
			//Else, this node may be a group, then add all its child-nodes to the list.
			else
			{
				int pathCount = selPath.getPathCount();
				if(pathCount == 2)
				{				
					//Add all IDs of this group to the invited list.
					for(int index = 0; index < count; index++)
					{
						Object childNode = treeModel.getChild(selectedObj, index);
						String strTemp = childNode.toString();
						String userID = Helper.GetUserID(strTemp);
						if(!userID.isEmpty())
							addUser(userID);
					}
				}
			}				
		}
	}
	
	private class RemoveActionListener implements ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
			try
			{
				if(lstInvitedUser.getModel().getSize() <= 0 ||
						lstInvitedUser.getSelectedIndex() < 0)
					return;
				Object[] selectedObjs = lstInvitedUser.getSelectedValues();
				int size = selectedObjs.length;
				if(size <= 0)
					return;
				for(int id = 0; id < size; id++)
				{
					listModel.removeElement(selectedObjs[id]);
				}
				//lstInvitedUser.invalidate();
			}
			catch (Exception exc) 
			{
				Tracer.Log(this.getClass(), exc);
				Helper.Error(UserMsg.REMOVE_USER_FAILED);
			}
		}
	}
	
	private class InviteOtherActionListener implements ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
			//Show an input dialog to enter the Yahoo ID.
			Dlg_InviteOther dlgInviteOther = new Dlg_InviteOther(Dlg_MakeConference.this, true);
			dlgInviteOther.setLocationRelativeTo(Dlg_MakeConference.this);
			dlgInviteOther.setVisible(true);
			
			int option = dlgInviteOther.getSelectedValue();
			if(option == JOptionPane.OK_OPTION)
			{
				String userID = dlgInviteOther.getMessengerID();
				addUser(userID);
			}
		}
	}
	
	private class MessageKeyAdapter extends KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() != KeyEvent.VK_ENTER)
				return;
			
			closeDialog();
		}
	}
	
	private void closeDialog()
	{
		if(listModel.size() <= 0)
			selectedValue = JOptionPane.CANCEL_OPTION;
		else
			selectedValue = JOptionPane.OK_OPTION;
		
		//Hide this dialog.
		Dlg_MakeConference.this.setVisible(false);
	}
}
