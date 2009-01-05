import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.JDialog;

/**
 * The basic dialog will be used in JYM program.
 *
 */
public class BaseDialog extends JDialog
{
	/**
	 *  The serial Version UID.
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * The option selected by user.
	 */
	protected int selectedValue = -1;
	
	/**
	 * @param owner
	 * 	The parent frame that showed this dialog
	 */
	public BaseDialog(Frame owner) 
	{
		super(owner);
	}
	
	/**
	 * 
	 * @param owner
	 * 	The parent frame that showed this dialog
	 * @param model
	 * 
	 */
	public BaseDialog(Frame owner, boolean model) 
	{
		super(owner, model);
	}
	
	/**
	 * @param owner
	 * 	The parent dialog that showed this dialog
	 */
	public BaseDialog(Dialog owner)
	{
		super(owner);
	}
	
	/**
	 * 
	 * @param owner
	 * 	The parent dialog that showed this dialog
	 * @param model
	 * 
	 */
	public BaseDialog(Dialog owner, boolean model) 
	{
		super(owner, model);
	}
	
	/**
	 * 
	 * @param owner
	 * 	The parent window that showed this dialog
	 */
	public BaseDialog(Window owner)
	{
		super(owner);
	}
	
	/**
	 * 
	 * @param owner
	 * 	The parent window that showed this dialog
	 * @param modelType
	 */
	public BaseDialog(Window owner, ModalityType modelType)
	{
		super(owner, modelType);
	}
	
	/**
	 * Get the value represents for the button chosen by user.
	 * @return The value chosen by user
	 */
	public int getSelectedValue()
	{
		return this.selectedValue;
	}
}
