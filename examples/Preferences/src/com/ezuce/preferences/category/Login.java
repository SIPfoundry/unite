package com.ezuce.preferences.category;

import static com.ezuce.util.GraphicUtils.createImageIcon;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class Login extends Preference {

	/**  */
	private static final long serialVersionUID = 1803617032322406575L;
	private static final String iconName = "/resources/images/prefs/login";
	private static final String TITLE = "Login";

	public Login() {
		super(TITLE, createImageIcon(iconName + "_on.png"),
				createImageIcon(iconName + "_off.png"),
				createImageIcon(iconName + ".png"));
	}

	@Override
	public JComponent getGUI() {
		JPanel panel = new JPanel();
		// panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

		panel.setLayout(new GridLayout(2, 3));
		panel.add(new JCheckBox("Test login checkbox 1"));
		panel.add(new JCheckBox("Test login checkbox 2"));
		panel.add(new JCheckBox("Test login checkbox 3"));
		panel.add(new JCheckBox("Test login checkbox 4"));
		panel.add(new JCheckBox("Test login checkbox 5"));

		Integer value = new Integer(0);
		Integer min = new Integer(0);
		Integer max = new Integer(60);
		Integer step = new Integer(1);
		SpinnerModel model = new SpinnerNumberModel(value, min, max, step);
		JSpinner spinner = new JSpinner(model);

		spinner.setValue(10);
		panel.add(spinner);

		JPanel p = new JPanel();
		p.add(panel);
		return p;
	}
}
