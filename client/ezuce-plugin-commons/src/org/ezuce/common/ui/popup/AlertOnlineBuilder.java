package org.ezuce.common.ui.popup;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.SwingWorker;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.actions.task.MakeCallTask;
import org.ezuce.common.xml.MyConferenceXML;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.spark.PluginManager;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.GraphicUtils;
import org.jivesoftware.sparkimpl.plugin.chat.PresenceChangePlugin;

public class AlertOnlineBuilder extends SwingWorker<Boolean, Void>{
	
	private final String bareJid;
	private final JToggleButton toggleButton;
	private final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(getClass());

	public AlertOnlineBuilder(){
		bareJid=null;
		toggleButton=null;
	}
	
	public AlertOnlineBuilder(JToggleButton tglBtn, String userJid){
		toggleButton = tglBtn;
		bareJid = userJid;
	}
	
	
	
	
	@Override
	protected Boolean doInBackground() {
		boolean result = false; //not listened for coming online.
		try {
			//get reference to PresenceChangePlugin. Useful methods: addWatch, removeWatch, getWatched
			PresenceChangePlugin presChangePlugin = (PresenceChangePlugin)(PluginManager.getInstance().getPlugin(PresenceChangePlugin.class));
			if (presChangePlugin!=null){
				result = presChangePlugin.getWatched(bareJid);
			}				
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	@Override
	protected void done() {

			try {
				Boolean watchedForComingOnline = get();	
				if (watchedForComingOnline){
					//If the user is already being watched for when coming online - show the button as pressed.
					this.toggleButton.setSelected(true);
				}else{
					//If the user is not being watched for when coming online - show the button as depressed.
					this.toggleButton.setSelected(false);
				}
				if (this.toggleButton.getActionListeners()==null || this.toggleButton.getActionListeners().length==0){
					//If no action listener has been configured for this toggle button, set it now:
					this.toggleButton.addActionListener(new ActionListener() {						
						@Override
						public void actionPerformed(ActionEvent arg0) {
							if (toggleButton.isSelected()){
								//If the button is pressed, the user must be watched for coming online:
								PresenceChangePlugin presChangePlugin = (PresenceChangePlugin)(PluginManager.getInstance().getPlugin(PresenceChangePlugin.class));
								if (presChangePlugin!=null){
									presChangePlugin.addWatch(bareJid);
								}				
							}
							else{
								//If the button is depressed, the user must be removed from the watch list:
								PresenceChangePlugin presChangePlugin = (PresenceChangePlugin)(PluginManager.getInstance().getPlugin(PresenceChangePlugin.class));
								if (presChangePlugin!=null){
									presChangePlugin.removeWatch(bareJid);
								}				
							}
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			
		}
	

}
