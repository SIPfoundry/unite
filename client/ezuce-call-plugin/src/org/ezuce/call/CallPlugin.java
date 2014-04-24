package org.ezuce.call;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sf.fmj.media.Log;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.ConfigureVoice;
import org.ezuce.common.windows.CallRecordingsWindow;
import org.ezuce.media.manager.StreamingManager;
import org.ezuce.media.manager.UIMediaManager;
import org.ezuce.media.manager.UnitemediaEventListener;
import org.ezuce.media.manager.UnitemediaEventManager;
import org.ezuce.media.manager.UnitemediaRegistry;
import org.ezuce.media.ui.GraphicUtils;
import org.ezuce.media.ui.VideoWindow;
import org.ezuce.media.ui.VideoWindowManager;
import org.ezuce.panels.CallPanel2;
import org.ezuce.unitemedia.event.UniteCallEvent;
import org.ezuce.unitemedia.event.UniteVideoEvent;
import org.ezuce.unitemedia.event.UniteXMPPVideoEvent;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.VerticalLayout;
import org.jivesoftware.MainWindow;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.component.tabbedPane.SparkTab;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPaneListener;
import org.jivesoftware.spark.plugin.Plugin;
import org.jivesoftware.spark.util.TaskEngine;


public class CallPlugin implements Plugin, ConfigureVoice, UnitemediaEventListener {
	public static String CALL_TAB_TITLE = "callPanel";
	private CallPanel2 panel;
	private SparkTab callTab;
	private SparkTab previousTab;

    /**
     * Called after Spark is loaded to initialize the new plugin.
     */
    @Override
    public void initialize() {
        MainWindow mw=SparkManager.getMainWindow();
        int width=mw.getWidth();
        if (width!=398) {
            ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(CallPlugin.class);
            if (resourceMap.containsKey("mainWindow.default.width")) {
                width=resourceMap.getInteger("mainWindow.default.width");
            }
            else {
                width=398;
            }
            int height=mw.getHeight();
            mw.setSize(width, height);
        }

        addTabToSpark();
        UnitemediaRegistry.getInstance().addUnitemediaEventListener(this); 
    }

    /**
     * Called when Spark is shutting down to allow for persistence of information
     * or releasing of resources.
     */
    @Override
    public void shutdown() {

    }

    /**
     * Return true if the Spark can shutdown on users request.
     * @return true if Spark can shutdown on users request.
     */
    @Override
    public boolean canShutDown() {
        return true;
    }

    /**
    * Is called when a user explicitly asked to uninstall this plugin.
    * The plugin owner is responsible to clean up any resources and
    * remove any components install in Spark.
    */
    @Override
	public void uninstall(){
       // Remove all resources belonging to this plugin.
    }

    private SparkTabbedPane createCallTab() {
        SparkTabbedPane callTab = new SparkTabbedPane();
        callTab.setName(CALL_TAB_TITLE);
        CallPluginHelper cph = new CallPluginHelper();
        if (cph.sipUserIsAuthenticated())
        {
			panel = new CallPanel2(callTab);
			cph.addListener(panel);
			callTab.add(panel);
        }
        else
        {
            final JPanel noSipUserPanel=new JPanel();
            noSipUserPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            final JLabel noSipUserLabel=new JLabel("Could not authenticate you to your SIP account.");
            noSipUserLabel.setForeground(Color.LIGHT_GRAY);
            noSipUserPanel.add(noSipUserLabel);
            callTab.add(noSipUserPanel);
        }

        return callTab;
    }

    /**
     * Adds a tab to Spark
     */
    private void addTabToSpark(){
         // Get Workspace UI from SparkManager
        Workspace workspace = SparkManager.getWorkspace();

        // Retrieve the Tabbed Pane from the WorkspaceUI.
        SparkTabbedPane workspacePane = workspace.getWorkspacePane();

        // Add own Tab.
        callTab = workspacePane.addTab(CALL_TAB_TITLE, Utils.getImageIcon(Utils.CALL_ICON), createCallTab());
        callTab.setName(CALL_TAB_TITLE);
        addRecordingsMenu();
        
        removeItemsFromActionsMenu();
        workspacePane.addSparkTabbedPaneListener(createTabSelectedListener());
    }
	
    private void addRecordingsMenu(){
        final JMenu actionsMenu = SparkManager.getMainWindow().getMenuByName(Res.getString("menuitem.actions"));
        JMenuItem recsMenu = new JMenuItem(Res.getString("recordings.menu.title"), GraphicUtils.createImageIcon("/resources/images/recordings_menu.png"));        
        
        int downloadsIndex = -1;
        int n = actionsMenu.getMenuComponentCount();
        for (int i=0; i<n; i++)
        {
            if (actionsMenu.getMenuComponent(i) instanceof JMenuItem){
                if (((JMenuItem)actionsMenu.getMenuComponent(i)).getText().equals(Res.getString("menuitem.view.downloads")))
                {
                    downloadsIndex = i+1;
                    break;
                }
            }
                
        }
        actionsMenu.add(recsMenu,downloadsIndex);
        
        recsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CallRecordingsWindow.getInstance().setVisible(true);
            }
        });
        
    }

    private void removeItemsFromActionsMenu(){
    	final JMenu actionsMenu = SparkManager.getMainWindow().getMenuByName(Res.getString("menuitem.actions"));
    	int n = actionsMenu.getMenuComponentCount();
    	List<JMenuItem> toRemove = new ArrayList<JMenuItem>();
        for (int i=0; i<n; i++) {
            if (actionsMenu.getMenuComponent(i) instanceof JMenuItem){
                if (((JMenuItem)actionsMenu.getMenuComponent(i)).getText().equals(Res.getString("button.view.tasklist"))
                	|| ((JMenuItem)actionsMenu.getMenuComponent(i)).getText().equals(Res.getString("button.view.notes"))
                   )
                {
                     toRemove.add(((JMenuItem)actionsMenu.getMenuComponent(i)));                   
                }
            }
                
        }
        for (JMenuItem mi : toRemove){
        	actionsMenu.remove(mi);
        }
    	
    }
	
	private void setCallTab() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		        Workspace workspace = SparkManager.getWorkspace();
		        SparkTabbedPane workspacePane = workspace.getWorkspacePane();
		        int currentTabIndex = workspacePane.getSelectedIndex();
		        previousTab = workspacePane.getTabAt(currentTabIndex);
		        int callTabIndex = workspacePane.getTabPosition(callTab);
		        workspacePane.setSelectedIndex(callTabIndex);				
			}
		});
	}
	
	private void setPreviousTab() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
		        Workspace workspace = SparkManager.getWorkspace();
		        SparkTabbedPane workspacePane = workspace.getWorkspacePane();
				int previousTabIndex = workspacePane.getTabPosition(previousTab);
				workspacePane.setSelectedIndex(previousTabIndex);			
			}
		});
	}

	@Override
	public void configureVoiceEnabled() {
        JPanel audioVideoPanel = UIMediaManager.getInstance().getCallPanelWrapper();
        if (audioVideoPanel != null) {
        	panel.setDividerLocation(panel.getMakeCallPanelHeight()  + audioVideoPanel.getHeight());
        }
	}

	@Override
	public void configureVoiceDisabled() {

	}

	@Override
	public void callOutgoing(UniteCallEvent event) {
		customizePanel();
		UIMediaManager.getInstance().cleanAttachedSIPCallTabVideo();
		UIMediaManager.getInstance().setCallTabAudioPanelParentToUpdate(this);
	}

	@Override
	public void callIncoming(UniteCallEvent event) {
		setCallTab();
		customizePanel();
		UIMediaManager.getInstance().cleanAttachedSIPCallTabVideo();
		UIMediaManager.getInstance().setCallTabAudioPanelParentToUpdate(this);
	}

	@Override
	public void callEnded(UniteCallEvent event) {
		if (event.isNoResponse()) {
			setPreviousTab();
		}
		customizePanel();
	}

	@Override
	public void incomingCallInProgress(UniteCallEvent event) {
		setCallTab();
		customizePanel();
	}

	@Override
	public void outgoingCallInProgress(UniteCallEvent event) {
		customizePanel();
	}

	@Override
	public void locallyOnHold(UniteCallEvent event) {
		customizePanel();
	}

	@Override
	public void mutuallyOnHold(UniteCallEvent event) {
		customizePanel();
	}

	@Override
	public void remotelyOnHold(UniteCallEvent event) {
	    customizePanel();
	}

	@Override
	public void localVideoStreaming(UniteVideoEvent event) {
	    customizePanel();
	}

	@Override
	public void remoteScreenAdded(UniteVideoEvent event) {
	    customizePanel();
	}

	@Override
	public void remoteScreenRemoved(UniteVideoEvent event) {
	    customizePanel();
	}

	@Override
	public void localScreenAdded(UniteVideoEvent event) {
	    customizePanel();
	}

	@Override
	public void localScreenRemoved(UniteVideoEvent event) {
		customizePanel();
	}

	@Override
	public void transmitterStreamInitialized(UniteXMPPVideoEvent event) {
		customizePanel();
	}

	@Override
	public void transmitterStreamDenied(UniteXMPPVideoEvent event) {
	    customizePanel();
	}

	@Override
	public void transmitterStreamStarted(UniteXMPPVideoEvent event) {
		customizePanel();
	}

	@Override
	public void transmitterStreamEnded(UniteXMPPVideoEvent event) {
		customizePanel();
	}

	@Override
	public void receiverStreamInitialized(UniteXMPPVideoEvent event) {
		customizePanel();
	}

	@Override
	public void receiverStreamStarted(UniteXMPPVideoEvent event) {
		customizePanel();
	}

	@Override
	public void receiverStreamEnded(UniteXMPPVideoEvent event) {
		customizePanel();
		StreamingManager strManager = StreamingManager.getInstance();
		VideoWindow vWindow = UIMediaManager.getInstance().getVideoWindowManager().
				getVideoWindowByXMPPJid(org.ezuce.common.resource.Utils.getImId(strManager.getFromJID()));
		vWindow.stopDurationTimer();
	}
	
	private void customizePanel() {
		UIMediaManager uiManager = UIMediaManager.getInstance();
		UnitemediaEventManager umManager = UnitemediaEventManager.getInstance();
		VideoWindowManager vwManager = VideoWindowManager.getCreatedInstance();
		
		boolean onCall = umManager.isOnCall();
		boolean transmittingXMPPVideo = umManager.isTransmittingXMPPVideo();
		boolean receivingXMPPVideo = umManager.isReceivingXMPPVideo();
		boolean remoteVideoCallAdded = umManager.isRemoteVideoCallAdded();
		boolean localVideoCallAdded = umManager.isLocalVideoCallAdded();

		boolean displayAudioPanel = onCall;
		boolean displayXMPPVideoPanel = receivingXMPPVideo;
		boolean displaySIPVideoPanel = onCall && (remoteVideoCallAdded || localVideoCallAdded);
		boolean displayBoth = vwManager != null && displaySIPVideoPanel && receivingXMPPVideo;
		
		JPanel audioPanel = !displayAudioPanel ? null : uiManager.getAudioCallTabCallsPanel();
		JPanel scPanel = transmittingXMPPVideo ? uiManager.getTransmitterCallTabScreenSharingPanel() : (receivingXMPPVideo ? uiManager.getReceiverCallTabScreenSharingPanel() : null);
		JPanel videoPanel = null;
        if (displayBoth) {
    		videoPanel = new JPanel(new VerticalLayout(2));
    		videoPanel.add(uiManager.getAttachedSIPCallTabVideo(), 0);
    		videoPanel.add(uiManager.getAttachedXMPPCallTabVideo(), 1);        	
        } else {
        	if (displaySIPVideoPanel) {
        		videoPanel = uiManager.getAttachedSIPCallTabVideo();
        	}
        	if (displayXMPPVideoPanel) {
        		videoPanel = uiManager.getAttachedXMPPCallTabVideo();
        	}
        }
		uiManager.customizeCallPanelWrapper(scPanel, videoPanel, audioPanel);
		customize();

    }

	@Override
	public void customize() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.setDividerLocation(panel.getMakeCallPanelHeight()  + UIMediaManager.getInstance().getCallPanelWrapper().getHeight());	
			}
		});	
	}
	
    // http://track.sipfoundry.org/browse/UN-65
	private SparkTabbedPaneListener createTabSelectedListener() {
		return new SparkTabbedPaneListener() {

			@Override
			public void tabSelected(SparkTab tab, Component component, int index) {
				if (StringUtils.equals(tab.getName(), callTab.getName())) {
					customize();
				}
			}

			@Override
			public void tabRemoved(SparkTab tab, Component component, int index) {
			}

			@Override
			public void tabAdded(SparkTab tab, Component component, int index) {
			}

			@Override
			public boolean canTabClose(SparkTab tab, Component component) {
				return false;
			}

			@Override
			public void allTabsRemoved() {
			}
       };
    }
}
