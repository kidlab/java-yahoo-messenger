import java.awt.Dimension;
import javax.swing.JFrame;
/**
 * Contains the main method that start JYM.
 */
public class Program
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			// TODO Auto-generated method stub
			Form_Login formLogin = new Form_Login();
			formLogin.setMaximumSize(new Dimension(350,450));
			formLogin.setMinimumSize(new Dimension(350,450));
			formLogin.setVisible(true);
			formLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		catch (Exception exc) 
		{
			Tracer.Log("Program", exc);
			Helper.Error(UserMsg.UNKNOWN_ERROR);
		}
	}

}
