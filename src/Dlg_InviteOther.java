import javax.swing.JPanel;
import java.awt.Frame;
import javax.swing.JLabel;

import java.awt.Dialog;
import java.awt.Rectangle;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Dlg_InviteOther extends GLobalDialog 
{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane = null;

	private JLabel lblInfo = null;

	private JTextField txtYahooID = null;

	private JButton btnOK = null;

	private JButton btnCancel = null;

	/**
	 * @param owner
	 */
	public Dlg_InviteOther(Frame owner, boolean model) 
	{
		super(owner, model);
		this.initialize();
	}
	
	/**
	 * @param owner
	 * 	The parent dialog that showed this dialog
	 */
	public Dlg_InviteOther(Dialog owner, boolean model) 
	{
		super(owner, model);
		this.initialize();
	}
	
	/**
	 * @param owner
	 * 	The parent window that showed this dialog
	 */
	public Dlg_InviteOther(Window owner)
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
		this.setSize(332, 147);
		this.setTitle("Invite Other");
		this.setResizable(false);
		
		lblInfo = new JLabel();
		lblInfo.setText("Enter the Yahoo ID of the person you want to invite:");
		lblInfo.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		lblInfo.setBounds(new Rectangle(9, 8, 301, 24));
		contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.add(lblInfo, null);
		
		//
		//txtYahooID
		//
		txtYahooID = new JTextField();
		txtYahooID.setBounds(new Rectangle(10, 40, 215, 20));
		txtYahooID.addKeyListener(new AcceptKeyAdapter());
		
		contentPane.add(txtYahooID, null);
		
		//
		//btnOK
		//
		btnOK = new JButton();
		btnOK.setText("OK");
		btnOK.setBounds(new Rectangle(145, 80, 75, 22));
		btnOK.addActionListener(new AcceptActionListener());
		
		contentPane.add(btnOK, null);
		
		//
		//btnCancel
		//
		btnCancel = new JButton();
		btnCancel.setText("Cancel");
		btnCancel.setSize(new Dimension(75, 22));
		btnCancel.setLocation(new Point(238, 80));
		btnCancel.addActionListener(new CancelActionListener());
		
		contentPane.add(btnCancel, null);
		
		this.setContentPane(contentPane);
	}
	
	/**
	 * Get the entered Yahoo ID.
	 * @return
	 */
	public String getMessengerID()
	{
		return this.txtYahooID.getText();
	}
	
	private class AcceptKeyAdapter extends KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				selectedValue = JOptionPane.OK_OPTION;
				
				//Hide this dialog.
				Dlg_InviteOther.this.setVisible(false);
			}
		}
	}
	
	private class AcceptActionListener implements ActionListener
	{		
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
			//Check if the Yahoo ID is invalid.
			if(txtYahooID.getText() == null ||
					txtYahooID.getText().trim().isEmpty())
				selectedValue = JOptionPane.CANCEL_OPTION;
			else
				selectedValue = JOptionPane.OK_OPTION;

			//Hide this dialog.
			Dlg_InviteOther.this.setVisible(false);
		}
	}
	
	private class CancelActionListener implements ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent e)
		{
			selectedValue = JOptionPane.CANCEL_OPTION;

			//Hide this dialog.
			Dlg_InviteOther.this.setVisible(false);
		}
	}
}
