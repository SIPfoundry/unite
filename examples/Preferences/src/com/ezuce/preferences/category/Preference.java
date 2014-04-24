package com.ezuce.preferences.category;

import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

public abstract class Preference implements Serializable {

	/**  */
	private static final long serialVersionUID = 1366709323610544426L;

	private String name;
	private ImageIcon iconActive;
	private ImageIcon iconInactive;
	private ImageIcon iconTitle;

	public Preference(String title, ImageIcon activeIcon,
			ImageIcon inactiveIcon, ImageIcon titleIcon) {
		this.name = title;
		this.iconActive = activeIcon;
		this.iconInactive = inactiveIcon;
		this.iconTitle = titleIcon;
	}

	/**
	 * @return the name
	 */
	public String getTitle() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the iconActive
	 */
	public ImageIcon getIconActive() {
		return iconActive;
	}

	/**
	 * @param iconActive
	 *            the iconActive to set
	 */
	public void setIconActive(ImageIcon iconActive) {
		this.iconActive = iconActive;
	}

	/**
	 * @return the iconInactive
	 */
	public ImageIcon getIconInactive() {
		return iconInactive;
	}

	/**
	 * @param iconInactive
	 *            the iconInactive to set
	 */
	public void setIconInactive(ImageIcon iconInactive) {
		this.iconInactive = iconInactive;
	}

	public ImageIcon getIcon() {
		return iconTitle;
	}

	public void setIconTitle(ImageIcon iconTitle) {
		this.iconTitle = iconTitle;
	}

	@Override
	public String toString() {
		return name;
	}

	public abstract JComponent getGUI();

	public void load() {
		System.out.println("==TODO: LOAD ==");
	}

	public void commit() {
		System.out.println("==TODO: COMMIT ==");
	}

	public boolean isDataValid() {
		System.out.println("==TODO: isDataValid ==");
		return true;
	}

}
