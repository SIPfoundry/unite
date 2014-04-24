package org.ezuce.im.ui.location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.thoughtworks.xstream.XStream;

public class LocationManager {

	private static File mLocationsFile = new File(
	// SparkManager.getUserDirectory()
			"/home/slava/.eZuce/Unite/user/2066@ezuce.com", "locations.xml");

	private static XStream xstream = new XStream();
	private static LocationManager mInstance;
	private List<Location> mLocations = new ArrayList<Location>();
	static {
		xstream.alias("location-items", List.class);
		xstream.alias("location", Location.class);
	}
	private LocationWindow mLocationWindow;

	public LocationManager() {
		mLocationWindow = new LocationWindow();
	}

	public static LocationManager getInstance() {
		// sync
		if (mInstance == null) {
			mInstance = new LocationManager();
		}
		return mInstance;
	}

	public static List<Location> getLocations() {
		getInstance().mLocations.addAll(load());
		return getInstance().mLocations;
	}

	@SuppressWarnings("unchecked")
	public static List<Location> load() {
		List<Location> list = null;

		if (mLocationsFile.exists()) {
			try {
				list = (List<Location>) xstream.fromXML(new FileReader(
						mLocationsFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (list == null) {
			list = new ArrayList<Location>();
		}

		// Sort locations
		Collections.sort(list, new Comparator<Location>() {
			public int compare(final Location a, final Location b) {
				return (a.getName().compareToIgnoreCase(b.getName()));
			}
		});

		return list;
	}

	public static void save(List<Location> list) {
		try {
			xstream.toXML(list, new FileWriter(mLocationsFile));
		} catch (IOException e) {
			// Log.error("Could not save locations.", e);
			e.printStackTrace();
		}
	}

	public static void show() {
		getInstance().mLocationWindow.setLocations(getLocations());
		getInstance().mLocationWindow.setVisible(true);
	}

	public static void hide() {
		getInstance().mLocationWindow.setVisible(false);
	}

}
