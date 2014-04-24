package com.ezuce.widgets.login;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.ezuce.im.ui.location.Location;
import org.ezuce.im.ui.location.LocationManager;
import org.jivesoftware.smack.packet.Presence;

import com.ezuce.PresenceManager;
import com.ezuce.util.GraphicUtils;
import com.ezuce.util.Res;
import com.ezuce.widgets.BackgroundPanel;
import com.ezuce.widgets.CustomComboboxItem;
import com.ezuce.widgets.LoginPassPair;
import com.ezuce.widgets.builder.WidgetBuilder;

public class LoginDialogFrame extends JFrame {

	private static final ImageIcon gCheckboxBgSelectedIconPath = GraphicUtils
			.createImageIcon("/resources/images/login/tick-box-on_20x20.png");
	private static final ImageIcon gCheckboxBgPath = GraphicUtils
			.createImageIcon("/resources/images/login/tick-box_20x20.png");
	private static final ImageIcon gLoginDialogBg = GraphicUtils
			.createImageIcon("/resources/images/login/login-screen_245x410.png");
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
	private static final Dimension SIZE = new Dimension(245, 460);
	private static final Font gDefaultFont = new Font("Droid", Font.PLAIN, 12);

	private JButton mBtnLogin;
	private JComboBox mNameField;
	private JPasswordField mPasswordField;
	private JButton mBtnAdvanced;
	private JComboBox mPresenceBox;
	private JComboBox mLocationBox;
	private ComboBoxModel mPresenceModel;
	private ComboBoxModel mLocationModel;
	private JCheckBox mChkLoginAsInvisible;
	private JCheckBox mChkRegisterAsPhone;
	private JCheckBox mChkSavePasswd;
	private JCheckBox mChkAutoLogin;
	private JPanel mComponentsPanel;
	private DefaultComboBoxModel mUsernamesModel;

	public LoginDialogFrame() {
		setSize(SIZE);
		setContentPane(new BackgroundPanel(gLoginDialogBg));
		setResizable(false);

		initComponents();
		initLayout();
	}

	private void initLayout() {

		setLayout(new GridBagLayout());

		// presence & location
		mComponentsPanel.add(mPresenceBox, new GridBagConstraints(0, 0, 1, 1,
				0.5, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 10, 10, 0), 0, 0));

		mComponentsPanel.add(mLocationBox, new GridBagConstraints(1, 0, 1, 1,
				0.5, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				new Insets(0, 10, 10, 10), 0, 0));

		// name and pass
		mComponentsPanel.add(mNameField, new GridBagConstraints(0, 1, 2, 1, 0,
				0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 10, 10, 10), 0, 0));
		mComponentsPanel.add(mPasswordField, new GridBagConstraints(0, 2, 2, 1,
				1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 10, 10, 10), 0, 0));

		// checkboxes
		mComponentsPanel.add(mChkRegisterAsPhone, new GridBagConstraints(0, 3,
				2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 7, 1, 10), 0, 0));

		mComponentsPanel.add(mChkSavePasswd, new GridBagConstraints(0, 4, 2, 1,
				0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 7, 1, 10), 0, 0));

		mComponentsPanel.add(mChkAutoLogin, new GridBagConstraints(0, 5, 2, 1,
				0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 7, 1, 10), 0, 0));

		mComponentsPanel.add(mChkLoginAsInvisible, new GridBagConstraints(0, 6,
				2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE,
				new Insets(0, 7, 10, 10), 0, 0));

		// last line
		JPanel lastLinePanel = new JPanel(new GridBagLayout());
		lastLinePanel.setOpaque(false);

		lastLinePanel.add(mBtnAdvanced, new GridBagConstraints(0, 0, 1, 1, 1,
				1, GridBagConstraints.LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		JPanel dummy = new JPanel();
		dummy.setOpaque(false);
		lastLinePanel.add(dummy, new GridBagConstraints(2, 0, 1, 1, 1, 1,
				GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		lastLinePanel.add(mBtnLogin, new GridBagConstraints(3, 0, 1, 1, 0.8, 1,
				GridBagConstraints.LINE_END, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0), 0, 0));

		// add last line
		mComponentsPanel.add(lastLinePanel, new GridBagConstraints(0, 7, 2, 1,
				1, 1, GridBagConstraints.LAST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 10, 5, 10), 0, 0));

		// main panel
		add(mComponentsPanel, new GridBagConstraints(0, 1, 1, 1, 1, 1,
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				new Insets(5, 10, 15, 10), 0, 0));
	}

	private void initComponents() {

		mComponentsPanel = new JPanel(new GridBagLayout());
		mComponentsPanel.setOpaque(false);

		mPresenceModel = new DefaultComboBoxModel(getPresenceModel());
		mLocationModel = new DefaultComboBoxModel(getLocationModel());
		mUsernamesModel = new DefaultComboBoxModel(getUsernamesModel());

		// presence
		mPresenceBox = WidgetBuilder.createCustomCombobox(gComboBoxBg, null,
				gComboBoxBgEditor, mPresenceModel, gDefaultFont, gColorDefault,
				gSeparatorIcon, gArrowIcon, false);

		// location
		mLocationBox = WidgetBuilder.createCustomCombobox(gComboBoxBg, null,
				gComboBoxBgEditor, mLocationModel, gDefaultFont, gColorDefault,
				gSeparatorIcon, gArrowIcon, false);

		// nickname
		mNameField = WidgetBuilder.createCustomCombobox(gTextFieldBgEditor,
				gTextFieldBgEditorFocused, gTextFieldBgEditor, mUsernamesModel,
				gDefaultFont, gColorDefault, gSeparatorIcon,
				gTextFieldEditorArrow, true);
		mNameField.addItemListener(nameFieldChangedListener());

		// password
		mPasswordField = WidgetBuilder.createPasswordField(gTextFieldBg,
				gTextFieldBgFocused, Res.getString("label.password"),
				gDefaultFont, gColorDefault,
				BorderFactory.createEmptyBorder(2, 5, 2, 5));

		// checkboxes
		mChkRegisterAsPhone = WidgetBuilder.createCheckbox(
				Res.getString("checkbox.register.as.phone"), gCheckboxBgPath,
				gCheckboxBgSelectedIconPath, gDefaultFont, gColorDefault);

		mChkSavePasswd = WidgetBuilder.createCheckbox(
				Res.getString("checkbox.save.password"), gCheckboxBgPath,
				gCheckboxBgSelectedIconPath, gDefaultFont, gColorDefault);

		mChkAutoLogin = WidgetBuilder.createCheckbox(
				Res.getString("checkbox.auto.login"), gCheckboxBgPath,
				gCheckboxBgSelectedIconPath, gDefaultFont, gColorDefault);

		mChkLoginAsInvisible = WidgetBuilder.createCheckbox(
				Res.getString("checkbox.login.as.invisible"), gCheckboxBgPath,
				gCheckboxBgSelectedIconPath, gDefaultFont, gColorDefault);

		// login
		mBtnLogin = WidgetBuilder.createButton(gBtnBg, gBtnBgPressed,
				gBtnBgHover, Res.getString("button.login"), gDefaultFont,
				gColorDefault);
		mBtnLogin.setPreferredSize(new Dimension(1, 30));

		// advanced
		mBtnAdvanced = WidgetBuilder.createButton(gBtnBg, gBtnBgPressed,
				gBtnBgHover, Res.getString("button.advanced"), gDefaultFont,
				gColorDefault);
		mBtnAdvanced.setPreferredSize(new Dimension(1, 30));

		updatePasswordField((LoginPassPair) mUsernamesModel.getSelectedItem());
	}

	private Object[] getLocationModel() {
		CustomComboboxItem[] items = new CustomComboboxItem[LocationManager
				.getLocations().size()];
		int i = 0;
		for (Location location : LocationManager.getLocations())
			items[i++] = new CustomComboboxItem(/* location.getIcon() */null,
					location.getName());

		return items;
	}

	private Object[] getPresenceModel() {
		CustomComboboxItem[] items = new CustomComboboxItem[PresenceManager
				.getPresences().size()];
		int i = 0;
		for (Presence presence : PresenceManager.getPresences())
			items[i++] = new CustomComboboxItem(
					PresenceManager.getIconFromPresence(presence),
					presence.getStatus());

		return items;
	}

	private Object[] getUsernamesModel() {
		return new LoginPassPair[] { new LoginPassPair("2222", "2222"),
				new LoginPassPair("3333", "3333"), };
	}

	private ItemListener nameFieldChangedListener() {
		return new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					if (item instanceof LoginPassPair) {
						LoginPassPair pair = (LoginPassPair) item;
						updatePasswordField(pair);
					} else
						mPasswordField.setText("");
				}
			}
		};
	}

	private void updatePasswordField(LoginPassPair pair) {
		if (pair != null)
			mPasswordField.setText(pair.getPassword());
	}
}
