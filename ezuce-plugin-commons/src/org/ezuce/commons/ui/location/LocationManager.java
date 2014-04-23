package org.ezuce.commons.ui.location;

import static org.ezuce.common.presence.EzucePresenceManager.copy;
import static org.ezuce.common.presence.EzucePresenceManager.getAvailablePresence;
import static org.ezuce.common.presence.EzucePresenceManager.getAwayPresence;
import static org.ezuce.common.presence.EzucePresenceManager.getDndPresence;
import static org.ezuce.common.presence.EzucePresenceManager.getExtendedAwayPresence;
import static org.ezuce.common.presence.EzucePresenceManager.getPresences;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.io.LocationIO;
import org.ezuce.media.ui.GraphicUtils;
import org.jivesoftware.resource.Res;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;

public class LocationManager {

	private static final class LocationComparator implements
			Comparator<Location> {
		public int compare(final Location a, final Location b) {
			return (a.getName().compareToIgnoreCase(b.getName()));
		}
	}

	private static final String EMPTY = "";
	public static final String gLocationNamespace = "http://ezuce.com/location_namespace";
	private static final List<Location> gDefaultLocations = new ArrayList<Location>();
	private static LocationManager mInstance;
	private static File mLocationsFile;

	private List<Location> mLocations = new ArrayList<Location>();
	private LocationWindow mLocationWindow;
	private LocationIO mLocationXml;
	private Location mSelectedLocation;
	private Comparator<? super Location> gLocationComparator = new LocationComparator();
	private static boolean mLocationFileChanged = true; // first loading

	static {

		// TODO: this settings will be retrieved from the server
		// original presences
		Presence available = copy(getAvailablePresence());
		Presence away = copy(getAwayPresence());
		Presence dnd = copy(getDndPresence());
		Presence extendedAway = copy(getExtendedAwayPresence());

		// home
		Location home = new Location(
				Res.getString("resources.home"),
				GraphicUtils
						.createImageIcon("/resources/images/login/icon-home_15x15.png"));

		addPresences(home, getPresences());

		Presence available40 = copy(available);
		available40.setPriority(40);

		Presence away30 = copy(away);
		away30.setPriority(30);

		Presence extendedAway31 = copy(extendedAway);
		extendedAway31.setPriority(31);

		Presence dnd30 = copy(dnd);
		dnd30.setPriority(30);

		replacePresence(home, getAvailablePresence(), available40);
		replacePresence(home, getAwayPresence(), away30);
		replacePresence(home, getExtendedAwayPresence(), extendedAway31);
		replacePresence(home, getDndPresence(), dnd30);

		// work
		Location work = new Location(
				Res.getString("resources.work"),
				GraphicUtils
						.createImageIcon("/resources/images/login/icon-work_15x15.png"));

		addPresences(work, getPresences());

		Presence available41 = copy(available);
		available41.setPriority(41);

		Presence away31 = copy(away);
		away31.setPriority(31);

		Presence dnd31 = copy(dnd);
		dnd31.setPriority(31);

		replacePresence(work, getAvailablePresence(), available41);
		replacePresence(work, getAwayPresence(), away31);
		replacePresence(work, getExtendedAwayPresence(), extendedAway31);
		replacePresence(work, getDndPresence(), dnd31);

		// mobile
		Location mobile = new Location(
				Res.getString("resources.mobile"),
				GraphicUtils
						.createImageIcon("/resources/images/login/icon-mobile_15x15.png"));

		addPresences(mobile, getPresences());

		Presence available42 = copy(available);
		available42.setPriority(42);

		Presence away32 = copy(away);
		away32.setPriority(32);

		Presence extendedAway32 = copy(extendedAway);
		extendedAway32.setPriority(32);

		Presence dnd32 = copy(dnd);
		dnd32.setPriority(32);

		replacePresence(mobile, getAvailablePresence(), available42);
		replacePresence(mobile, getAwayPresence(), away32);
		replacePresence(mobile, getExtendedAwayPresence(), extendedAway32);
		replacePresence(mobile, getDndPresence(), dnd32);

		//
		gDefaultLocations.add(home);
		gDefaultLocations.add(work);
		gDefaultLocations.add(mobile);
	}

	// ##### constructors #####

	public LocationManager() {
		mLocationXml = new LocationIO();
		mLocationWindow = new LocationWindow();
		mLocationsFile = new File(SparkManager.getUserDirectory(),
				"locations.xml");
	}

	// ##### public API #####

	public static Location getLocationByNameFromDefaults(String locationName) {
		if (StringUtils.isEmpty(locationName))
			return null;

		for (Location r : gDefaultLocations)
			if (r.getName().equals(locationName))
				return r;

		return null;
	}

	public static Location getCurrentLocation() {
		return getInstance().mSelectedLocation;
	}

	public static String getCurrentLocationNameOrEmpty() {
		return getInstance().mSelectedLocation != null ? getInstance().mSelectedLocation
				.getName() : EMPTY;
	}

	public static List<Location> getDefaultLocations() {
		return gDefaultLocations;
	}

	public static String getLocationNameFromPresence(Presence presence) {
		if (null == presence)
			return EMPTY;

		DefaultPacketExtension ext = (DefaultPacketExtension) presence
				.getExtension(gLocationNamespace);
		if (null == ext)
			return EMPTY;

		return ext.getValue("name");
	}

	public static List<Location> getLocationsFromFile() {
		if (!mLocationFileChanged)
			return getInstance().mLocations;

		getInstance().mLocations.clear();
		getInstance().mLocations.addAll(gDefaultLocations);
		getInstance().mLocations.addAll(getInstance().loadLocations());
		mLocationFileChanged = false;
		return getInstance().mLocations;
	}

	public static void show() {
		getInstance().mLocationWindow.setLocations(getLocationsFromFile());
		getInstance().mLocationWindow.setVisible(true);
	}

	public static void hide() {
		getInstance().mLocationWindow.setVisible(false);
	}

	public static void setCurrentLocation(Location location) {
		getInstance().mSelectedLocation = location;
	}

	public static void setCurrentLocationToPresence(Presence presence) {
		setLocationToPresence(LocationManager.getCurrentLocation(), presence);
	}

	public static void setLocationToPresence(Location location,
			Presence presence) {
		if (presence == null)
			return;

		removeLocationExtension(presence);
		if (location != null) {
			addLocationExtension(presence, location);
		}
	}

	public synchronized static void save(List<Location> list) {
		try {
			String xml = getInstance().mLocationXml.toXml(list);
			FileUtils.writeStringToFile(mLocationsFile, xml);
			mLocationFileChanged = true;
		} catch (IOException e) {
			Log.error("Could not save locations.", e);
		}
	}

	@SuppressWarnings("unchecked")
	private synchronized List<Location> loadLocations() {

		List<Location> list = null;

		if (mLocationsFile.exists()) {
			try {
				list = (List<Location>) mLocationXml.parse(FileUtils
						.readFileToString(mLocationsFile));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (list == null) {
			list = new ArrayList<Location>();
		}

		// Sort locations
		Collections.sort(list, gLocationComparator);

		return list;
	}

	private static void removeLocationExtension(Presence presence) {
		DefaultPacketExtension ext = (DefaultPacketExtension) presence
				.getExtension(gLocationNamespace);
		if (ext != null)
			presence.removeExtension(ext);
	}

	private static void addLocationExtension(Presence presence,
			Location location) {
		DefaultPacketExtension extension = new DefaultPacketExtension("x",
				gLocationNamespace);
		extension.setValue("name", location.getName());
		presence.addExtension(extension);
	}

	public static LocationManager getInstance() {
		LocationManager _instance = mInstance;
		if (_instance == null) {
			synchronized (LocationManager.class) {
				_instance = mInstance;
				if (_instance == null) {
					mInstance = _instance = new LocationManager();
				}
			}
		}
		return _instance;
	}

	// ##### observers management ######

	public static void addObserver(Observer object) {
		getInstance().mLocationWindow.addObserver(object);
	}

	public static void deleteObserver(Observer object) {
		getInstance().mLocationWindow.deleteObserver(object);
	}

	// ##### helpers for the locations from server ######

	private static void addPresences(Location resource, List<Presence> presences) {
		for (Presence p : presences)
			resource.addPresence(p);
	}

	private static void replacePresence(Location home, Presence orig,
			Presence newPresence) {
		int idx = home.getPresences().indexOf(orig);
		if (idx != -1) {
			home.getPresences().set(idx, newPresence);
		}
	}
}
