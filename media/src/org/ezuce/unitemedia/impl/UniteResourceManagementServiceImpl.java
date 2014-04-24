package org.ezuce.unitemedia.impl;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;

import javax.swing.ImageIcon;

import org.ezuce.unitemedia.service.UniteResourceManagementService;

public class UniteResourceManagementServiceImpl implements UniteResourceManagementService {
	
	private String APPLICATION_NAME_KEY = "service.gui.APPLICATION_NAME";
	
	@Override
	public int getColor(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getColorString(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getImageInputStreamForPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getImageInputStream(String streamKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getImageURL(String urlKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getImageURLForPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImagePath(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Locale> getAvailableLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getI18NString(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getI18NString(String key, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getI18NString(String key, String[] params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getI18NString(String key, String[] params, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getI18nMnemonic(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char getI18nMnemonic(String key, Locale l) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public URL getSettingsURL(String urlKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getSettingsInputStream(String streamKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getSettingsInputStream(String streamKey,
			Class<?> resourceClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSettingsString(String key) {
		if (key.equals(APPLICATION_NAME_KEY)) {
			return "Unite";
		}
		return null;
	}

	@Override
	public int getSettingsInt(String key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public URL getSoundURL(String urlKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getSoundURLForPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSoundPath(String soundKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageIcon getImage(String imageID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getImageInBytes(String imageID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File prepareSkinBundleFromZip(File zipFile) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
