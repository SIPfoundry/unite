package org.ezuce.common.preference.local;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.LocalPreference;

/**
 * Based on LanguagePlugin by Derek DeMoro
 * 
 * Allows for changing of default languages within Spark.
 * 
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 */
public class LanguageComboBox extends JComboBox {

	private static final long serialVersionUID = 2024661116959222699L;
	private static final Locale[] locales = getAvailableLanguages();

	public LanguageComboBox() {
		super(locales);
		setRenderer(new LanguageComboBoxRenderer());
	}

	private static Locale[] getAvailableLanguages() {
		Locale[] locales = Locale.getAvailableLocales();
		List<String> availableLanguages = loadAvailableLanguages();
		List<Locale> result = new ArrayList<Locale>();
		// determine which of locales we support
		for (Locale locale : locales) {
			for (String language : availableLanguages)
				if (locale.toString().equals(language)
						&& !locale.toString().equals(
								Locale.getDefault().toString())) {
					result.add(locale);
				}
		}
		Collections.sort(result, new LocaleComparator());
		result.add(0, Locale.getDefault());
		return result.toArray(new Locale[result.size()]);
	}

	private static List<String> loadAvailableLanguages() {
		List<String> availableLanguages = new ArrayList<String>();
		URL sparkJar = LocalPreference.class.getClassLoader().getResource(
				"spark.jar");
		if (sparkJar == null) {
			sparkJar = LocalPreference.class.getProtectionDomain()
					.getCodeSource().getLocation();
			if (sparkJar == null)
				return Collections.emptyList();
		}
		try {
			String url = URLDecoder.decode(sparkJar.getPath(), Charset
					.defaultCharset().toString());
			ZipFile zipFile = new JarFile(new File(url));
			for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e
					.hasMoreElements();) {
				JarEntry entry = (JarEntry) e.nextElement();
				String propertiesName = entry.getName();
				// Ignore any manifest.mf entries.
				if (propertiesName.endsWith(".properties")) {
					int lastIndex = propertiesName.lastIndexOf("i18n_");
					int period = propertiesName.lastIndexOf(".");
					if (lastIndex == -1
							&& propertiesName.contains("spark_i18n")) {
						availableLanguages.add("en");
					} else {
						String language = propertiesName.substring(
								lastIndex + 5, period);
						availableLanguages.add(language);
					}
				}
			}
			zipFile.close();
		} catch (Throwable e) {
			Log.error("Error unzipping plugin", e);
		}
		return availableLanguages;
	}

	public static void warn() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int ok = JOptionPane.showConfirmDialog(
						SparkManager.getMainWindow(),
						Res.getString("message.restart.required"),
						Res.getString("title.confirmation"),
						JOptionPane.YES_NO_OPTION);

				if (ok == JOptionPane.YES_OPTION) {
					SparkManager.getMainWindow().closeConnectionAndInvoke(
							"Language Change");
				}
			}
		});
	}

	static final class LocaleComparator implements Comparator<Locale> {
		@Override
		public int compare(Locale o1, Locale o2) {
			return o1.toString().compareTo(o2.toString());
		}
	}

	static class LanguageComboBoxRenderer extends JLabel implements
			ListCellRenderer {

		private static final Color SELECTED = new Color(204, 204, 204);
		private static final long serialVersionUID = -6199864575660667892L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Color bg = isSelected ? Color.WHITE : SELECTED;
			setBackground(bg);
			Locale locale = (Locale) value;
			String label = getLanguageName(locale);
			setText(label);
			return this;
		}

		private String getLanguageName(Locale locale) {
			String label = locale.getDisplayLanguage(locale);
			if (locale.getDisplayCountry(locale) != null
					&& locale.getDisplayCountry(locale).trim().length() > 0) {
				label = label + "-" + locale.getDisplayCountry(locale).trim();
			}
			return label;
		}
	}

	public void setSelectedLanguage(String language) {
		for (Locale locale : locales) {
			if (locale.toString().equals(language)) {
				setSelectedItem(locale);
				return;
			}
		}
	}
}
