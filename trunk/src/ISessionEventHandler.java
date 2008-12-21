import ymsg.network.SessionEvent;

/**
 * Standard session event handler.
 * 
 */
public interface ISessionEventHandler 
{
	/**
	 * React with the event was raised.
	 * @param eventType
	 * 	The type of event was fired.
	 * @param e
	 * 	The arguments of the fired event.
	 */
	public void doEvent(int eventType, SessionEvent e);
}
