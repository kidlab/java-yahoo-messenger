import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import ymsg.network.ServiceConstants;
import ymsg.network.SessionChatEvent;
import ymsg.network.SessionConferenceEvent;
import ymsg.network.SessionErrorEvent;
import ymsg.network.SessionEvent;
import ymsg.network.SessionExceptionEvent;
import ymsg.network.SessionFileTransferEvent;
import ymsg.network.SessionFriendEvent;
import ymsg.network.SessionListener;
import ymsg.network.SessionNewMailEvent;
import ymsg.network.SessionNotifyEvent;

public class SessionEventHandler implements SessionListener
{
	private List<ISessionEventHandler> eventHandlers;
	
	/**
	 * Initialize a new instance of SessionHandler with the default settings.
	 */
	public SessionEventHandler()
	{
		this.eventHandlers = new ArrayList<ISessionEventHandler>();
	}
	
	/**
	 * Get the group of session event listener currently set up for this class.
	 * @return
	 * 	A range of listeners that may be derive from or implement of {@link ISessionEventHandler}.
	 */
	public List<ISessionEventHandler> getEventHandlers()
	{
		return this.eventHandlers;
	}
	
	/**
	 * Set up the session event listeners.
	 * @param handlers
	 * 	A range of listeners that derive from or implement of {@link ISessionEventHandler}.
	 */
	public void setEventHandler(ArrayList<ISessionEventHandler> handlers)
	{
		this.eventHandlers = handlers;
	}
	
	/**
	 * Add a new object that will receive the Session events.
	 * @param handler
	 * 	A listener that derives from or implements of {@link ISessionEventHandler}.
	 */
	public void addEventReciever(ISessionEventHandler handler)
	{		
		try
		{
			List<ISessionEventHandler> safeList = Collections.synchronizedList(this.eventHandlers);
			synchronized (safeList)
			{
				safeList.add(handler);
			}
		}
		catch (Exception exc)
		{
			Tracer.Log(this.getClass(), exc);
		}
	}
	
	/**
	 * Fire the event to all listeners.
	 * @param eventType
	 * 	The type of event was fired.
	 * @param e
	 * 	The arguments of the fired event.
	 */
	protected void fireEvent(int eventType, SessionEvent e)
	{
		try
		{
			List<ISessionEventHandler> safeList = Collections.synchronizedList(this.eventHandlers);
			synchronized (safeList)
			{
				for(ISessionEventHandler handler : safeList)
				{
					handler.doSessionEvent(eventType, e);
				}
			}	
		}
		catch (Exception exc)
		{
			Tracer.Log(this.getClass(), exc);
		}
	}
	
	@Override
	public void buzzReceived(SessionEvent ev) 
	{
	
	}

	@Override
	public void chatCaptchaReceived(SessionChatEvent ev) 
	{
	
	}

	@Override
	public void chatConnectionClosed(SessionEvent ev) 
	{
	
	}

	@Override
	public void chatLogoffReceived(SessionChatEvent ev) 
	{
	
	}

	@Override
	public void chatLogonReceived(SessionChatEvent ev) 
	{
	
	}

	@Override
	public void chatMessageReceived(SessionChatEvent ev) 
	{
	
	}

	@Override
	public void chatUserUpdateReceived(SessionChatEvent ev)
	{
	
	}

	@Override
	public void conferenceInviteDeclinedReceived(SessionConferenceEvent ev) 
	{
	
	}

	@Override
	public void conferenceInviteReceived(SessionConferenceEvent ev) 
	{
	
	}

	@Override
	public void conferenceLogoffReceived(SessionConferenceEvent ev) 
	{
	
	}

	@Override
	public void conferenceLogonReceived(SessionConferenceEvent ev)
	{
	
	}

	@Override
	public void conferenceMessageReceived(SessionConferenceEvent ev) 
	{
	
	}

	@Override
	public void connectionClosed(SessionEvent ev) 
	{
	
	}

	@Override
	public void contactRejectionReceived(SessionEvent ev) 
	{
		this.fireEvent(ServiceConstants.SERVICE_CONTACTREJECT, ev);
	}

	@Override
	public void contactRequestReceived(SessionEvent ev) 
	{
		this.fireEvent(ServiceConstants.SERVICE_CONTACTNEW, ev);
	}

	@Override
	public void errorPacketReceived(SessionErrorEvent ev) 
	{
	
	}

	@Override
	public void fileTransferReceived(SessionFileTransferEvent ev)
	{
		this.fireEvent(ServiceConstants.SERVICE_FILETRANSFER, ev);
	}

	@Override
	public void friendAddedReceived(SessionFriendEvent ev)
	{
	
	}

	@Override
	public void friendRemovedReceived(SessionFriendEvent ev) 
	{
	
	}

	@Override
	public void friendsUpdateReceived(SessionFriendEvent ev)
	{
	
	}

	@Override
	public void inputExceptionThrown(SessionExceptionEvent ev) 
	{
	
	}

	@Override
	public void listReceived(SessionEvent ev) 
	{
	
	}

	@Override
	public void messageReceived(SessionEvent ev)
	{
		this.fireEvent(ServiceConstants.SERVICE_MESSAGE, ev);
	}

	@Override
	public void newMailReceived(SessionNewMailEvent ev) 
	{
		this.fireEvent(ServiceConstants.SERVICE_NEWPERSONMAIL, ev);
	}

	@Override
	public void notifyReceived(SessionNotifyEvent ev) 
	{
		
	}

	@Override
	public void offlineMessageReceived(SessionEvent ev) 
	{
		this.fireEvent(ServiceConstants.SERVICE_X_OFFLINE, ev);		
	}
}
