import javax.swing.JFrame;
import ymsg.network.Session;

/**
 * The basic Frame will be used in JYM program.
 */
public class BaseFrame extends JFrame
{
	/**
	 * The session object will be used for this class and all of its derive class.
	 */
	protected static Session session;
	
	/**
	 * The session event handler object will be used for this class and all of its derive class.
	 */
	protected static SessionEventListener sessionHandler;
	
	public BaseFrame()
	{
		super();
	}
	
	public BaseFrame(Session session, SessionEventListener handler)
	{
		super();
		BaseFrame.session = session;
		BaseFrame.sessionHandler = handler;
	}
	
	public BaseFrame(String title)
	{
		super(title);
	}
}
