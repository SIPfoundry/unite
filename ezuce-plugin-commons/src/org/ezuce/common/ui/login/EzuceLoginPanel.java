package org.ezuce.common.ui.login;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.ezuce.common.presence.EzucePresenceManager;
import org.ezuce.common.ui.widget.CustomComboboxItem;
import org.ezuce.commons.ui.location.Location;
import org.ezuce.commons.ui.location.LocationManager;
import org.ezuce.media.ui.GraphicUtils;
import org.ezuce.media.ui.WidgetBuilder;
import org.jivesoftware.LoginDialog;
import org.jivesoftware.LoginPanel;
import org.jivesoftware.Spark;
import org.jivesoftware.resource.Default;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.spark.util.ModelUtil;
import org.jivesoftware.spark.util.ResourceUtils;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class EzuceLoginPanel extends LoginPanel implements ActionListener,
		KeyListener {

	private static final Dimension gButtonMinSize = new Dimension(1, 30);
	private static final Dimension gFieldMinSize = new Dimension(1, 28);
	private static final String gAt = "@";
	private static final String gEmpty = "";
	private static final String gDefaultServerText = Res.getString(
			"label.server.name").replaceAll("&", gEmpty);
	private static final String gDefaultPasswordText = Res
			.getString("label.password");
	private static final String gDefaultUsernameText = Res.getString(
			"label.username").replaceAll("&", gEmpty);

	private static final ImageIcon gCheckboxBgSelectedIconPath = GraphicUtils
			.createImageIcon("/resources/images/login/tick-box-on_20x20.png");
	private static final ImageIcon gCheckboxBgPath = GraphicUtils
			.createImageIcon("/resources/images/login/tick-box_20x20.png");
	private static final Image gLoginDialogBg = GraphicUtils.createImageIcon(
			"/resources/images/login/login-screen_245x410.png").getImage();
	private static final ImageIcon gBtnBgPressed = GraphicUtils
			.createImageIcon("/resources/images/login/button_55x28_pressed.png");
	private static final ImageIcon gBtnBgHover = GraphicUtils
			.createImageIcon("/resources/images/login/button_55x28_hover.png");
	private static final ImageIcon gBtnBg = GraphicUtils
			.createImageIcon("/resources/images/login/button_55x28.png");
	private static final ImageIcon gTextFieldBg = GraphicUtils
			.createImageIcon("/resources/images/login/textbox_200x28.png");
	private static final ImageIcon gTextFieldBgFocused = GraphicUtils
			.createImageIcon("/resources/images/login/textbox_200x28_focused.png");
	private static final ImageIcon gComboBoxBg = GraphicUtils
			.createImageIcon("/resources/images/login/dropdown_95x28_orig.png");
	private static final ImageIcon gComboBoxBgEditor = GraphicUtils
			.createImageIcon("/resources/images/login/dropdown_95x28_editor.png");
	private static final ImageIcon gSeparatorIcon = GraphicUtils
			.createImageIcon("/resources/images/login/separator.png");
	private static final ImageIcon gArrowIcon = GraphicUtils
			.createImageIcon("/resources/images/login/combo_arrow.png");
	private static final ImageIcon gTextFieldEditorArrow = GraphicUtils
			.createImageIcon("/resources/images/login/textbox_arrow_editor.png");
	private static final ImageIcon gTextFieldBgEditor = GraphicUtils
			.createImageIcon("/resources/images/login/textbox_editor.png");
	private static final ImageIcon gTextFieldBgEditorFocused = GraphicUtils
			.createImageIcon("/resources/images/login/textbox_editor_focused.png");

	private static final Color gColorDefault = new Color(88, 88, 88);
	private static final long serialVersionUID = -2127098007717567709L;
	public static final Dimension SIZE = new Dimension(245, 410);
	private static final Font gDefaultFont = new Font("Droid", Font.PLAIN, 12);
	private static final Dimension gProgresBarSize = new Dimension(130, 50);

	private JButton mBtnLogin;
	private JComboBox mNameField;
	private JPasswordField mPasswordField;
	private JButton mBtnAdvanced;
	private JComboBox mPresenceBox;
	private JComboBox mResourceBox;
	private DefaultComboBoxModel mPresenceModel;
	private ComboBoxModel mResourceModel;
	private JCheckBox mChkRegisterAsPhone;
	private JPanel mComponentsPanel;
	private DefaultComboBoxModel mUsernamesModel;
	private JLabel mProgressBar;
	protected LocalPreferences localPref;
	private final int mBgWidth;
	private final int mBgHeight;
	private JTextField mLoginServerField;
	private JPanel mButtonPanel;

	public EzuceLoginPanel(LoginDialog loginDialog) {
		super(loginDialog);
		mBgWidth = gLoginDialogBg.getWidth(null);
		mBgHeight = gLoginDialogBg.getHeight(null);
		loginDialog.getLoginDialog().addWindowListener(
				addWindowListenerToLoginDialog());

		LoginPassPair pair = (LoginPassPair) mUsernamesModel.getSelectedItem();
		if (pair == null) {
			((JTextField) mNameField.getEditor().getEditorComponent())
					.setText(gDefaultUsernameText);
		}

	}

	@Override
	protected void initLayout() {
		setPreferredSize(SIZE);

		setLayout(new GridBagLayout());

		// presence & resource
		mComponentsPanel.add(mPresenceBox, new GridBagConstraints(0, 0, 1, 1,
				0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 10, 10, 0), 0, 0));

		mComponentsPanel.add(mResourceBox, new GridBagConstraints(1, 0, 1, 1,
				0.5, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 10, 10, 10), 0, 0));

		// name and pass
		mComponentsPanel.add(mNameField, new GridBagConstraints(0, 1, 2, 1, 0,
				0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 10, 10, 10), 0, 0));
		mComponentsPanel.add(mPasswordField, new GridBagConstraints(0, 2, 2, 1,
				1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 10, 10, 10), 0, 0));

		mComponentsPanel
				.add(mLoginServerField, new GridBagConstraints(0, 3, 2, 1, 1,
						0, GridBagConstraints.WEST,
						GridBagConstraints.HORIZONTAL,
						new Insets(0, 10, 10, 10), 0, 0));

		// as phone
		mComponentsPanel.add(mChkRegisterAsPhone, new GridBagConstraints(0, 4,
				2, 1, 1, 0, GridBagConstraints.WEST,
				GridBagConstraints.HORIZONTAL, new Insets(0,
						Spark.isWindows() ? 6 : 10, 10, 10), 0, 0));

		// last line
		mButtonPanel = new JPanel(new GridBagLayout());
		mButtonPanel.setOpaque(false);

		mButtonPanel.add(mBtnAdvanced, new GridBagConstraints(0, 0, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		JPanel dummy = new JPanel();
		dummy.setOpaque(false);
		mButtonPanel.add(dummy, new GridBagConstraints(2, 0, 1, 1, 1, 1,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		mButtonPanel.add(mBtnLogin, new GridBagConstraints(3, 0, 1, 1, 0.8, 1,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// progress line
		mComponentsPanel.add(mProgressBar, new GridBagConstraints(0, 5, 2, 1,
				1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE,
				new Insets(3, 3, 3, 3), 0, 0));

		add(mComponentsPanel, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				new Insets(120, 10, 10, 10), 0, 0));

		add(mButtonPanel, new GridBagConstraints(0, 2, 1, 1, 1, 1,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(7, 20, 10, 20), 0, 0));

	}

	@Override
	protected void initComponents() {
		if (localPref == null)
			localPref = SettingsManager.getLocalPreferences();

		mProgressBar = new JLabel();
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(
				"images/ajax-loader.gif"));
		// Set progress bar description
		mProgressBar.setText(Res.getString("message.autenticating"));
		mProgressBar.setVerticalTextPosition(JLabel.BOTTOM);
		mProgressBar.setHorizontalTextPosition(JLabel.CENTER);
		mProgressBar.setHorizontalAlignment(JLabel.CENTER);
		mProgressBar.setIcon(icon);
		mProgressBar.setVisible(false);

		mProgressBar.setMinimumSize(gProgresBarSize);
		mProgressBar.setPreferredSize(gProgresBarSize);
		mProgressBar.setMaximumSize(gProgresBarSize);

		mComponentsPanel = new JPanel(new GridBagLayout());
		mComponentsPanel.setOpaque(false);

		mPresenceModel = new DefaultComboBoxModel();
		mResourceModel = new DefaultComboBoxModel(getResourcesList());
		mUsernamesModel = new DefaultComboBoxModel(getUsernamesList());

		// presence
		mPresenceBox = WidgetBuilder.createCombobox(gComboBoxBg, null,
				gComboBoxBgEditor, mPresenceModel, gDefaultFont, gColorDefault,
				gSeparatorIcon, gArrowIcon, gEmpty, false);

		// location
		mResourceBox = WidgetBuilder.createCombobox(gComboBoxBg, null,
				gComboBoxBgEditor, mResourceModel, gDefaultFont, gColorDefault,
				gSeparatorIcon, gArrowIcon, gEmpty, false);

		// nickname
		mNameField = WidgetBuilder.createCombobox(gTextFieldBgEditor,
				gTextFieldBgEditorFocused, gTextFieldBgEditor, mUsernamesModel,
				gDefaultFont, gColorDefault, gSeparatorIcon,
				gTextFieldEditorArrow, gDefaultUsernameText, true);
		mNameField.getEditor().addActionListener(nameFieldActionListener());
		mNameField.addItemListener(nameChangedAction());

		// password
		mPasswordField = WidgetBuilder.createPasswordField(gTextFieldBg,
				gTextFieldBgFocused, gDefaultPasswordText, gDefaultFont,
				gColorDefault, BorderFactory.createEmptyBorder(2, 5, 2, 5));
		mPasswordField.setMinimumSize(gFieldMinSize);

		// server
		mLoginServerField = WidgetBuilder.createTextField(gTextFieldBg,
				gTextFieldBgFocused, gDefaultServerText, gDefaultFont,
				gColorDefault, BorderFactory.createEmptyBorder(2, 5, 2, 5));
		mLoginServerField.setMinimumSize(gFieldMinSize);

		// checkboxes
		mChkRegisterAsPhone = WidgetBuilder.createCheckbox(
				Res.getString("checkbox.register.as.phone"), gCheckboxBgPath,
				gCheckboxBgSelectedIconPath, gDefaultFont, gColorDefault);

		// login
		mBtnLogin = WidgetBuilder.createButton(gBtnBg, gBtnBgPressed,
				gBtnBgHover, Res.getString("button.login"), gDefaultFont,
				gColorDefault);
		mBtnLogin.setFocusable(true);
		mBtnLogin.setFocusPainted(true);
		mBtnLogin.setMinimumSize(gButtonMinSize);
		mBtnLogin.setPreferredSize(gButtonMinSize);

		// advanced
		mBtnAdvanced = WidgetBuilder.createButton(gBtnBg, gBtnBgPressed,
				gBtnBgHover, Res.getString("button.advanced"), gDefaultFont,
				gColorDefault);
		mBtnAdvanced.setFocusable(true);
		mBtnAdvanced.setFocusPainted(true);
		mBtnAdvanced.setMinimumSize(gButtonMinSize);
		mBtnAdvanced.setPreferredSize(gButtonMinSize);

		ResourceUtils.resButton(mChkRegisterAsPhone,
				Res.getString("checkbox.register.as.phone"));

		ResourceUtils.resButton(mBtnLogin, Res.getString("button.login"));
		ResourceUtils.resButton(mBtnAdvanced, Res.getString("button.advanced"));

		mNameField.addKeyListener(this);
		mNameField.addItemListener(nameChangedAction());
		mPasswordField.addKeyListener(this);
		mBtnLogin.addActionListener(this);
		mBtnAdvanced.addActionListener(this);
		mResourceBox.addItemListener(resourceChangedAction());
	}

	@Override
	protected void loadPreferences() {
		// username , password, server
		setLastUsername(localPref.getLastUsername());
		// save password
		mBtnLogin.setEnabled(true);

		// auto login
		useSSO(localPref.isSSOEnabled());

		// voice/video
		Properties props = SettingsManager.getLocalPreferences()
				.getProperties();

		// reg as phone
		mChkRegisterAsPhone.setSelected(Boolean.parseBoolean(props.getProperty(
				"registerAsPhoneEnabled", "false")));

		// default resource
		selectCustomComboboxItem(mResourceModel,
				props.getProperty("resourceOnLoginByDefault"));

		updateResourcePresences((CustomComboboxItem) mResourceModel
				.getSelectedItem());

		// default presence
		selectCustomComboboxItem(mPresenceModel,
				props.getProperty("presenceOnLoginByDefault"));

		if (canLoginWithArguments()) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					loginWithArguments();
				}
			});
			return;
		}

		if (isAutoLogin()) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					performLoginAction();
				}
			});
			return;
		}

	}

	private Object[] getResourcesList() {
		CustomComboboxItem[] items = new CustomComboboxItem[LocationManager
				.getDefaultLocations().size()];
		int i = 0;
		for (Location location : LocationManager.getDefaultLocations()) {
			items[i++] = new CustomComboboxItem(location.getIcon(),
					location.getName());
		}

		return items;
	}

	protected Object[] getUsernamesList() {
		if (localPref == null)
			localPref = SettingsManager.getLocalPreferences();

		File file = new File(Spark.getSparkUserHome(), "user");
		File[] userprofiles = file.listFiles();

		List<LoginPassPair> pairs = new ArrayList<LoginPassPair>();
		for (File f : userprofiles) {
			if (f.getName().contains(gAt)) {
				String shortName = f.getName().split(gAt)[0];
				boolean isPreviousServerExist = localPref.getServer() != null;
				String bareJID = isPreviousServerExist ? shortName + gAt
						+ localPref.getServer() : f.getName();
				String passwd = getPasswordValueFromPrefs(bareJID);
				LoginPassPair pair = isPreviousServerExist ? new LoginPassPair(
						shortName, passwd, localPref.getServer())
						: new LoginPassPair(f.getName(), passwd);

				pairs.add(pair);
			} else {
				Log.error("Profile contains wrong format: \"" + f.getName()
						+ "\" located at: " + f.getAbsolutePath());
			}
		}

		return pairs.toArray();
	}

	@Override
	protected void setLastUsername(String userProp) {
		if (userProp == null)
			return;

		for (int i = 0; i < mUsernamesModel.getSize(); i++) {
			LoginPassPair comp = (LoginPassPair) mUsernamesModel
					.getElementAt(i);

			if (comp.getShortUsername().equals(userProp)) {
				mUsernamesModel.setSelectedItem(comp);
				updateLoginDetails(comp);
				return;
			}
		}
	}

	private String getPasswordValueFromPrefs(String name) {
		// Check Settings
		String password = gEmpty;
		if (localPref.isSavePassword()) {
			String bareJid = name;
			String encryptedPassword = localPref.getPasswordForUser(bareJid);
			if (encryptedPassword != null) {
				password = encryptedPassword;
			}
		}
		return password;
	}

	private void updateLoginDetails(LoginPassPair pair) {
		mPasswordField.setText(pair.getPassword());
		mLoginServerField.setText(pair.getServer());
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(gLoginDialogBg, 0, 0, getWidth(), getHeight(), 0, 0,
				mBgWidth, mBgHeight, null);
	}

	@Override
	public String getPassword() {
		return new String(mPasswordField.getPassword());
	}

	@Override
	public String getServerName() {
		return mLoginServerField.getText();
	}

	@Override
	public String getUsername() {
		if (mUsernamesModel == null)
			return gEmpty;

		Object selectedItem = mUsernamesModel.getSelectedItem();

		if (selectedItem == null)
			return gEmpty;

		String username = gEmpty;

		if (selectedItem instanceof LoginPassPair) {
			username = ((LoginPassPair) selectedItem).getUsername().trim();
			int lastIndexOfAt = username.lastIndexOf(gAt);
			lastIndexOfAt = lastIndexOfAt < 0 ? username.length()
					: lastIndexOfAt;
			username = username.substring(0, lastIndexOfAt);
		} else
			username = selectedItem.toString();

		return username;
	}

	@Override
	public boolean isSavePassword() {
		return localPref.isSavePassword();
	}

	@Override
	public boolean isLoginAsInvisible() {
		CustomComboboxItem presence = (CustomComboboxItem) mPresenceModel
				.getSelectedItem();
		return presence.getText().contains(Res.getString("status.invisible"));
	}

	public boolean isRegisteredAsPhone() {
		return mChkRegisterAsPhone.isSelected();
	}

	@Override
	public boolean isAutoLogin() {
		return localPref.isAutoLogin();
	}

	@Override
	protected void setProgressBarVisible(boolean visible) {
		mProgressBar.setVisible(visible);
		mButtonPanel.setVisible(!visible);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mBtnLogin) {
			validateDialog();
			validateLogin();
		} else if (e.getSource() == mBtnAdvanced) {
			performShowSettingsAction();
		}
	}

	@Override
	protected void enableComponents(boolean editable) {
		mChkRegisterAsPhone.setEnabled(editable);

		// Need to set both editable and enabled for best behavior.
		mNameField.setEditable(editable);
		mNameField.setEnabled(editable);

		mPasswordField.setEditable(editable);
		mPasswordField.setEnabled(editable);

		final String lockedDownURL = Default.getString(Default.HOST_NAME);
		if (!ModelUtil.hasLength(lockedDownURL)) {
			mLoginServerField.setEditable(editable);
			mLoginServerField.setEnabled(editable);
		}

		if (editable) {
			mPasswordField.requestFocus();
		}

		mPresenceBox.setEditable(editable);
		mPresenceBox.setEnabled(editable);
		mResourceBox.setEditable(editable);
		mResourceBox.setEnabled(editable);
	}

	@Override
	protected void setVisibilityIfSSODisabled() {
		mNameField.setVisible(true);
		mPasswordField.setVisible(true);
		mLoginServerField.setVisible(true);
	}

	@Override
	protected void setVisibilityIfSSOEnabled() {
		mNameField.setVisible(true);
		mPasswordField.setVisible(false);
		mLoginServerField.setVisible(true);
	}

	@Override
	protected void setProgressBarText(String text) {
		mProgressBar.setText(text);
	}

	@Override
	protected void setAccountSSOName(String name) {
		//
	}

	@Override
	protected void validateDialog() {
		mBtnLogin.setEnabled(ModelUtil.hasLength(getUsername())
				&& (ModelUtil.hasLength(getPassword()) || localPref
						.isSSOEnabled())
				&& ModelUtil.hasLength(getServerName()) && !isDefaultAccount());
	}

	@Override
	protected void loginWithArguments() {
		// Handle arguments
		String username = Spark.getArgumentValue(gDefaultUsernameText);
		String password = Spark.getArgumentValue(gDefaultPasswordText);
		String server = Spark.getArgumentValue("server");

		if (username != null) {
			LoginPassPair comp = getUsernameFromModel(username, server);
			if (comp == null)
				mUsernamesModel.addElement(new LoginPassPair(username,
						password, server));

			mUsernamesModel.setSelectedItem(comp);
			updateLoginDetails(comp);
		}

		if (username != null && server != null && password != null) {
			enableComponents(false);
			performLoginAction();
		}
	}

	private LoginPassPair getUsernameFromModel(String username, String server) {
		for (int i = 0; i < mUsernamesModel.getSize(); i++) {
			LoginPassPair comp = (LoginPassPair) mUsernamesModel
					.getElementAt(i);

			if (comp.getShortUsername().equals(username)
					&& comp.getServer().equals(server)) {
				return comp;
			}
		}
		return null;
	}

	@Override
	protected void savePreferences() {
		super.savePreferences();

		Properties props = SettingsManager.getLocalPreferences()
				.getProperties();

		// props.setProperty("registerAsPhoneEnabled",
		// Boolean.toString(isRegisteredAsPhone()));
		props.setProperty("presenceOnLoginByDefault",
				((CustomComboboxItem) mPresenceBox.getSelectedItem()).getText());
		props.setProperty("resourceOnLoginByDefault",
				((CustomComboboxItem) mResourceBox.getSelectedItem()).getText());

		if (isSavePassword()) {
			try {
				String bareJID = getUsername() + gAt + localPref.getServer();
				localPref.setPasswordForUser(bareJID, getPassword());
			} catch (Exception e) {
				Log.error("Error encrypting password.", e);
			}
		}
		localPref.setSavePassword(isSavePassword());

		SettingsManager.saveSettings();

	}

	private void selectCustomComboboxItem(ComboBoxModel model, String text) {
		if (text == null || gEmpty.equals(text))
			return;

		for (int i = 0; i < model.getSize(); i++) {
			CustomComboboxItem item = (CustomComboboxItem) model
					.getElementAt(i);
			if (item.getText().equals(text)) {
				model.setSelectedItem(item);
				return;
			}
		}
	}

	private void updateResourcePresences(CustomComboboxItem item) {
		List<Presence> presences = LocationManager
				.getLocationByNameFromDefaults(item.getText()).getPresences();

		CustomComboboxItem currentPresence = (CustomComboboxItem) mPresenceModel
				.getSelectedItem();

		mPresenceModel.removeAllElements();

		for (Presence p : presences) {
			CustomComboboxItem newItem = new CustomComboboxItem(
					EzucePresenceManager.getIcon(p), p.getStatus());
			mPresenceModel.addElement(newItem);
			if (currentPresence != null
					&& currentPresence.getText().equals(newItem.getText())) {
				currentPresence = newItem;
			}
		}
		mPresenceBox.setMaximumRowCount(mPresenceModel.getSize());

		if (currentPresence != null)
			mPresenceModel.setSelectedItem(currentPresence);
	}

	private ItemListener nameChangedAction() {
		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					if (item instanceof LoginPassPair) {
						LoginPassPair pair = (LoginPassPair) item;
						updateLoginDetails(pair);
					} else {
						mPasswordField.setText(gEmpty);
					}
					validateDialog();
				}
			}

		};
	}

	private ItemListener resourceChangedAction() {
		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					if (item instanceof CustomComboboxItem)
						updateResourcePresences((CustomComboboxItem) e
								.getItem());
				}
			}
		};
	}

	private WindowListener addWindowListenerToLoginDialog() {
		return new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				validateDialog();
				mBtnLogin.requestFocusInWindow();
			}

			@Override
			public void windowIconified(WindowEvent e) {

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				validateDialog();
			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {

			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {

			}
		};
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			validateDialog();
			validateLogin();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		validateDialog();
	}

	private boolean isDefaultAccount() {
		String username = getUsername();
		String password = getPassword();
		String server = getServerName();
		return gDefaultUsernameText.equals(username)
				&& gDefaultPasswordText.equals(password)
				&& gDefaultServerText.equals(server);
	}

	protected ActionListener nameFieldActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				validateDialog();
				validateLogin();
			}
		};
	}

	private boolean canLoginWithArguments() {
		String username = Spark.getArgumentValue(gDefaultUsernameText);
		String password = Spark.getArgumentValue(gDefaultPasswordText);
		String server = Spark.getArgumentValue("server");
		return username != null && server != null && password != null;
	}

}
