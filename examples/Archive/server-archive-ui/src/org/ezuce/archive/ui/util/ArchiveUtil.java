package org.ezuce.archive.ui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

public class ArchiveUtil {

	public static final boolean gDebug = false;

	public final static Color gMainBgColor = new Color(212, 223, 223);
	public final static Color gMainCalendarColor = new Color(230, 236, 236);
	public final static Color gMainBorderColor = new Color(170, 170, 170);

	public static final ImageIcon gSearchImage = createImageIcon("/resources/images/archive/search.png");
	public static final ImageIcon gCloseImage = createImageIcon("/resources/images/archive/close.png");
	public static final ImageIcon gCloseHoverImage = createImageIcon("/resources/images/archive/close_hover.png");
	public static final ImageIcon gCloseSmallImage = createImageIcon("/resources/images/archive/close_small.png");
	public static final ImageIcon gCalendarImage = createImageIcon("/resources/images/archive/calendar.png");
	public static final ImageIcon gCalendarPressedImage = createImageIcon("/resources/images/archive/calendar_pressed.png");
	public static final ImageIcon gCalendarHoverImage = createImageIcon("/resources/images/archive/calendar_hover.png");

	public static final SimpleDateFormat gMessageItemDate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS Z");
	public static final SimpleDateFormat gInfoPanelDateFormat = new SimpleDateFormat(
			"EEEE, dd MMM yyyy");

	public static final Font gFont = new Font("Droid", Font.PLAIN, 12);
	public static final String gRightSign = ">";
	public static final String gLeftSign = "<";
	public static final String gEmpty = "";

	public static final Insets gDefaultButtonMargin = new Insets(0, 0, 0, 0);

	private static final Random mRandomGenerator = new Random();
	private static final Color[] gRandomColors = { Color.black, Color.blue,
			Color.cyan, Color.darkGray, Color.gray, Color.green,
			Color.lightGray, Color.magenta, Color.orange, Color.pink,
			Color.red, Color.white, Color.yellow };

	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = ArchiveUtil.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, path);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public static void debugBorder(JComponent comp) {
		if (!gDebug)
			return;
		comp.setBorder(BorderFactory
				.createLineBorder(gRandomColors[mRandomGenerator
						.nextInt(gRandomColors.length - 1)]));
	}

	public static boolean hasText(String str) {
		return null != str && !"".equals(str.trim());
	}

	public static void normalizeCalendar(Calendar calc) {
		calc.set(Calendar.HOUR_OF_DAY, 0);
		calc.set(Calendar.MINUTE, 0);
		calc.set(Calendar.SECOND, 0);
		calc.set(Calendar.MILLISECOND, 0);
	}

	public static String[] removeEmpty(String[] origArray) {
		List<String> newArr = new ArrayList<String>();
		for (String el : origArray) {
			if (hasText(el)) {
				newArr.add(el);
			}
		}
		return newArr.toArray(new String[newArr.size()]);
	}

	public static boolean isToday(Calendar calc) {
		Calendar today = Calendar.getInstance();
		normalizeCalendar(today);
		return today.compareTo(calc) == 0;
	}

	public static boolean isInFeature(Calendar calc) {
		Calendar today = Calendar.getInstance();
		normalizeCalendar(today);
		return today.compareTo(calc) < 0;
	}

	public static void setSize(JComponent widget, Dimension size) {
		widget.setMinimumSize(size);
		widget.setMaximumSize(size);
		widget.setPreferredSize(size);
	}

}
