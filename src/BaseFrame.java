import javax.swing.JFrame;
import ymsg.network.Session;

/**
 * The basic Frame will be used in JYM program.
 */
public class BaseFrame extends JFrame
{
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * The session object will be used for this class and all of its derive class.
	 */
	protected static Session session;
	
	/**
	 * The session event handler object will be used for this class and all of its derive class.
	 */
	protected static SessionEventHandler sessionHandler;
	
	public BaseFrame()
	{
		super();
	}
	
	public BaseFrame(Session session, SessionEventHandler handler)
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
