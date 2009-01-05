import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import ymsg.network.StatusConstants;
import ymsg.network.YahooGroup;
import ymsg.network.YahooUser;

/**
 * Represents for a node  in the list of buddies of current users.
 *
 */
public class CellRenderer extends JLabel implements TreeCellRenderer
{	
	public Component getTreeCellRendererComponent(JTree tree,Object value,
			boolean selected,boolean expanded,boolean leaf,int row,boolean focus)
		{	if(value instanceof YahooUser)
			{	YahooUser yu = (YahooUser)value;
				String status = "";
				switch((int)yu.getStatus())
				{
					case (int)StatusConstants.STATUS_AVAILABLE:
						status = "(online)";
						break;
					case (int)StatusConstants.STATUS_BUSY:
						status = "(busy)";
						break;
					case (int)StatusConstants.STATUS_IDLE:
						status = "(idle)";
						break;
					case (int)StatusConstants.STATUS_NOTATDESK:
						status = "(not at my desk)";
						break;
					case (int)StatusConstants.STATUS_INVISIBLE:
						status = "(invisible)";
						break;
					case (int)StatusConstants.STATUS_ONPHONE:
						status = "(I'm on mobie)";
						break;
					case (int)StatusConstants.STATUS_ONVACATION:
						status = "(on my vacation)";
						break;
					case (int)StatusConstants.STATUS_OFFLINE:
						status = "(offline)";
						break;
					case (int)StatusConstants.STATUS_BAD:
						status = "(bad)";
						break;
					case (int)StatusConstants.STATUS_BADUSERNAME:
						status = "(badusername)";
						break;					
					case (int)StatusConstants.STATUS_COMPLETE:
						status = "(complete)";
						break;
					case (int)StatusConstants.STATUS_CUSTOM:
						status = "(" + yu.getCustomStatusMessage() + ")";
						break;
				}				
				
				//setIcon(new ImageIcon(getClass().getResource("image/online.gif")));
				setText(yu.getId() + " " + status);
				
			}
			else if(value instanceof YahooGroup)
			{	setText( ((YahooGroup)value).getName() );				
			}
			else
			{	setText(value.toString());
			}
			setBackground(selected ? Color.lightGray : Color.white);
			return this;
		}
}	