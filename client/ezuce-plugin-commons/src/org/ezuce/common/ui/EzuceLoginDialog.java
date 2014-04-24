package org.ezuce.common.ui;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.ezuce.common.phone.notifications.EzuceNotification;
import org.ezuce.common.phone.tray.EzuceTrayManager;
import org.ezuce.common.preference.audio.AudioSettings;
import org.ezuce.common.preference.codec.CodecSettings;
import org.ezuce.common.preference.troubleshooting.TroubleshootingSettings;
import org.ezuce.common.preference.video.VideoSettings;
import org.ezuce.common.resource.Config;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.login.EzuceLoginPanel;
import org.ezuce.common.ui.wrappers.interfaces.UserGroupCommonInterface;
import org.ezuce.media.manager.PhoneManager;
import org.ezuce.media.manager.StreamingManager;
import org.ezuce.unitemedia.context.ServiceContext;
import org.jivesoftware.LoginDialog;
import org.jivesoftware.resource.Default;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.ui.ChatRoom;
import org.jivesoftware.spark.ui.GlobalMessageListener;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public final class EzuceLoginDialog extends LoginDialog {
	
	private boolean success = false;

	public EzuceLoginDialog() {
		super();
	}

	@Override
	protected boolean beforeLoginValidations() {
		Properties props = SettingsManager.getLocalPreferences().getProperties();
		props.setProperty("registerAsPhoneEnabled", Boolean.toString(((EzuceLoginPanel) loginPanel).isRegisteredAsPhone()));
		
		final Config instance = Config.getInstance();
		instance.setSipUserId(loginPanel.getUsername());
		instance.setPassword(loginPanel.getPassword());
		instance.setServerAddress(loginPanel.getServerName());
		try {
			EventQueue.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					success = new CertificateDialog().downloadCertificate();
					RestManager restManager = null;
					if (success) {
						restManager = RestManager.getInstance();
						restManager.init();
					} else {
						return;
					}
					if (!restManager.testSipAuthentication()) {
						JOptionPane.showMessageDialog(null,
								Res.getString("verge.sip.error"),
								Res.getString("verge.error"),
								JOptionPane.ERROR_MESSAGE);
						success = false;
						return;
					}
					if (!restManager.isOpenUc()) {
						JOptionPane.showMessageDialog(null,
								Res.getString("verge.product.error"),
								Res.getString("verge.error"),
								JOptionPane.ERROR_MESSAGE);
						success = false;
						return;
					}
				}
			});
		} catch (Exception ex) {
			Log.error(ex);
			JOptionPane.showMessageDialog(null, ex.getMessage(),
					Res.getString("verge.error"), JOptionPane.ERROR_MESSAGE);
		}
		return success;
	}

	@Override
	protected ConnectionConfiguration retrieveConnectionConfiguration() {
		ConnectionConfiguration config = super
				.retrieveConnectionConfiguration();
		config.setSASLAuthenticationEnabled(false);
		return config;
	}

	@Override
	protected void afterLogin() {
		SparkManager.getChatManager().addGlobalMessageListener(
				new GlobalMessageListener() {
					@Override
					public void messageSent(ChatRoom room, Message message) {
						// TODO Auto-generated method stub
					}

					@Override
					public void messageReceived(ChatRoom room, Message message) {
						MessageListenerRegistry.processAll(room, message);
					}
				});
		try {
		    ServiceContext.start();
			// Init screen sharing based on XMPP signaling
			if (Config.getInstance().isVideobridgeAvailable()) {
				Log.warning("Videobridge is available and running, proceed with streaming context initialization");
				StreamingManager.getInstance();
			}
		
			if (Config.getInstance().isRegisterAsPhone()) {
				//Init voice/video context
				PhoneManager.getInstance().loadUser();
				PhoneManager.getInstance().registerUser();					
			}
		} catch (Exception ex) {
			Log.error("Cannot initialize media", ex);
		}
	}	

	@Override
	protected void afterWorkspaceBuilt() {
		super.afterWorkspaceBuilt();
		initializePreferencesAfterPlugins();
	}
	
	private void initializePreferencesAfterPlugins() {
		((UserGroupCommonInterface)SparkManager.getContactList()).updateListInterval();

		// add new settings
		AudioSettings audioSettings = new AudioSettings();
		SparkManager.getPreferenceManager().addPreference(audioSettings);
		audioSettings.load();
		
		VideoSettings videoSettings = new VideoSettings();
		SparkManager.getPreferenceManager().addPreference(videoSettings);
		videoSettings.load();
		
		CodecSettings codecSettings  = new CodecSettings();
		SparkManager.getPreferenceManager().addPreference(codecSettings);
		codecSettings.load();
		
		TroubleshootingSettings troubleshootingSettings = new TroubleshootingSettings();
		SparkManager.getPreferenceManager().addPreference(troubleshootingSettings);
		troubleshootingSettings.load();
		EzuceNotification.getInstance().initialize();
		// TrayManager can have already set register/unregister state or any others settings
		// but only now it is shown in tray spot.
		EzuceTrayManager.getInstance().initialize();
	}	

	@Override
	public void invoke(final JFrame parentFrame) {

		try {
			updateProxyConfig();
		} catch (Exception e) {
			Log.error(e);
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				loginDialog = new JFrame(Default
						.getString(Default.APPLICATION_NAME));
				loginPanel = new EzuceLoginPanel(EzuceLoginDialog.this);
				loginDialog.setContentPane(loginPanel);
				loginDialog.setTitle(Default
						.getString(Default.APPLICATION_NAME));
				loginDialog.setIconImage(SparkManager.getApplicationImage()
						.getImage());
				loginDialog.setResizable(false);
				loginDialog.setPreferredSize(EzuceLoginPanel.SIZE);
				loginDialog.pack();

				loginDialog.setLocationRelativeTo(parentFrame);
				org.jivesoftware.spark.util.GraphicUtils
						.centerWindowOnScreen(loginDialog);

				loginDialog.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						quitLogin();
					}
				});

				if (!localPref.isStartedHidden() || !localPref.isAutoLogin()) {
					loginDialog.setVisible(true);
				}
			}
		});
	}
}
