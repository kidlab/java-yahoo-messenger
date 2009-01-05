import javax.swing.JOptionPane;

/**
 * Provides all utility methods to be used throughout the program.
 *
 */
public class Helper
{
	/**
	 * Show an error message
	 * @param message
	 */
	public static void Error(String message)
	{
		JOptionPane.showMessageDialog(null, message, 
				Constant.PROGRAM_NAME, JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Show a warning message
	 * @param message
	 */
	public static void Warning(String message)
	{
		JOptionPane.showMessageDialog(null, message, 
				Constant.PROGRAM_NAME, JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Show an information message.
	 * @param message
	 */
	public static void Info(String message)
	{
		JOptionPane.showMessageDialog(null, message, 
				Constant.PROGRAM_NAME, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Show a confirm message with Yes/No options.
	 * @param message
	 * @return
	 * 	The value that selected by user (YES or NO).
	 */
	public static int Confirm(String message)
	{
		int selectedValue = JOptionPane.showOptionDialog(null, message, Constant.PROGRAM_NAME, 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.QUESTION_MESSAGE, 
				null, null, null);
		
		return selectedValue;
	}
	
	/**
	 * Show a confirm message with Yes/No/Cancel options.
	 * @param message
	 * @return
	 * 	The value that selected by user (YES ,NO or CANCEL).
	 */
	public static int ConfirmWithCancel(String message)
	{
		int selectedValue = JOptionPane.showOptionDialog(null, message, Constant.PROGRAM_NAME, 
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE, 
				null, null, null);
		
		return selectedValue;
	}
	
	public static String GetUserID(String yahooUserInfo)
	{
		String userID = "";
		int indexOfID = Constant.ID_FORMAT.length();
		String check = yahooUserInfo.substring(0, indexOfID);		            	 
    	if(check.equals(Constant.ID_FORMAT))
    	{
    		int end = yahooUserInfo.indexOf(" ");
    		userID = yahooUserInfo.substring(indexOfID, end);
    	}
    	
    	return userID;
	}
}
