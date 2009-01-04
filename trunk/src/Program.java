import java.awt.Dimension;
import javax.swing.JFrame;
/**
 * 
 * @author MinhVH
 *
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
			//Form_Invite_Conference f = new Form_Invite_Conference(new Session(),new SessionHandler());
			//f.setVisible(true);
		}
		catch (Exception exc) 
		{
			Tracer.Log("Program", exc);
			Helper.Error(UserMsg.UNKNOWN_ERROR);
		}
	}

}
