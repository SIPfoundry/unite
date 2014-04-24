package org.ezuce.common.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.async.VCardLoaderCallback;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.util.Base64;
import org.jivesoftware.spark.util.ModelUtil;
import org.jivesoftware.spark.util.log.Log;
import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Utils {
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static DateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
    private static DateFormat formatter3 = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
    private static boolean avatarRecentlyChanged = false;
    
    
    public static boolean isAvatarRecentlyChanged()
    {
        return avatarRecentlyChanged;
    }
    
    public static void setAvatarRecentlyChanged(boolean avatarChanged)
    {
        avatarRecentlyChanged=avatarChanged;
    }
    
    private static Calendar getCalendar(DateFormat formatter, String dateString) throws ParseException {
        Date date = formatter.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar getCalendar(String dateString) throws ParseException {
    	return getCalendar(formatter, dateString);
    }
    
    public static Calendar getCalendar3(String dateString) throws ParseException {
    	return getCalendar(formatter3, dateString);
    }
    
	public static long getDaysDiff(long t1, long t2) {
		if (t1 > t2) {
			long tmp = t1;
			t1 = t2;
			t2 = tmp;
		}
		long diff = t2 - t1;
		return diff / (24 * 60 * 60 * 1000);		
	}

    private static Calendar lastMonth() {
        Calendar lastMonth = Calendar.getInstance();
        lastMonth.add(Calendar.MONTH, -1);
        return lastMonth;
    }

    private static Calendar lastWeek() {
        Calendar lastWeek = Calendar.getInstance();
        lastWeek.add(Calendar.WEEK_OF_YEAR, -1);
        return lastWeek;
    }

    public static String getLastMonth() {
        Calendar lastMonth =lastMonth();
        return formatter2.format(lastMonth.getTime());
    }

    public static String getDateToDisplay(long time) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(time);
        return formatter3.format(date.getTime());
    }

    private static boolean isSameDay(Calendar date1, Calendar date2) {
        return date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)
        && date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)
        && date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH);
    }
    
    private static boolean isLast24Hours(Calendar date) {
    	date.get(Calendar.DAY_OF_YEAR);
        return (System.currentTimeMillis() - date.getTimeInMillis()) < 24*60*60*1000;
    }
    
    public static boolean isLast24Hours3(String dateString) throws ParseException {
    	Calendar date = getCalendar3(dateString);
    	return isLast24Hours(date);
    }

    public static boolean isLastWeek(String dateString) throws ParseException {
        Calendar date = getCalendar(dateString);
        Calendar lastWeek = lastWeek();
        return date.getTimeInMillis() > lastWeek.getTimeInMillis();
    }

    public static boolean isLastMonth(String dateString) throws ParseException {
        Calendar date = getCalendar(dateString);
        Calendar lastMonth = lastMonth();
        return date.getTimeInMillis() > lastMonth.getTimeInMillis();
    }

    public static boolean isLastMonth3(String dateString) throws ParseException {
        Calendar date = getCalendar3(dateString);
        Calendar lastMonth = lastMonth();
        return date.getTimeInMillis() > lastMonth.getTimeInMillis();
    }
    
    public static boolean isToday(String dateString) throws ParseException {
        Calendar date = getCalendar(dateString);
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        return isSameDay(date, today);
    }

    public static boolean isYesterday(String dateString) throws ParseException {
        Calendar date = getCalendar(dateString);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        return isSameDay(date, yesterday);
    }

    public static boolean isInsideDays(String dateString, int days) throws ParseException {
        Calendar date = getCalendar(dateString);
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, (-1)*days);
        return isSameDay(date, today);
    }
    
    public static String getImId(String jabberFull) {
    	return getFirstSubstring(jabberFull, "@");
    }
    
    public static String getFirstSubstring(String value, String separator) {
    	int index = value.lastIndexOf(separator);
    	if (index != -1) {
    		return value.substring(0, index);
    	} else {
    		return value;
    	}
    }
    
    public static String getLastSubstring(String value, String separator) {
    	int index = value.lastIndexOf(separator);
    	if (index != -1) {
    		return value.substring(index+1, value.length());
    	} else {
    		return null;
    	}
    }    
    
    public static String removeDotIfLast(String value) {
    	int dotIndex = value.lastIndexOf(".");
    	int length = value.length();    	
    	return (dotIndex == length -1) ? value.substring(0, dotIndex) : value;
    }
    
    public static String getJidSuffix() {
    	return "@"+SparkManager.getSessionManager().getServerAddress();
    }
    
    public static String getJidFromId(String id) {
    	return id + getJidSuffix();
    }
    
    public static ImageIcon retrieveAvatar(VCard vCard) {
		byte[] avatarBytes = null;
		try {                    
			avatarBytes = vCard.getAvatar();
			if (avatarBytes != null && avatarBytes.length > 0) {			
				return new ImageIcon(avatarBytes);
			} else {
				return getDefaultAvatar();
			}
		} catch (final Exception e) {
			Log.warning("Avatar cannot be retrieved from cache");
			return getDefaultAvatar();
		}		
    }
    
    public static ImageIcon getAvatar(byte[] avatarBytes) {
    	if (avatarBytes != null && avatarBytes.length > 0) {			
			return new ImageIcon(avatarBytes);
		} else {
			return getDefaultAvatar();
		}
	}

    public static String getAlias(VCard vCard) {
    	String displayName = vCard.getField("FN");
    	if (ModelUtil.hasLength(displayName)) {
    		return displayName;
    	}
		String firstName = vCard.getFirstName();
		String lastName = vCard.getLastName();
		if (ModelUtil.hasLength(firstName) && ModelUtil.hasLength(lastName)) {
			return firstName + " " + lastName;
		} else if (ModelUtil.hasLength(firstName)) {
			return firstName;
		} else if (ModelUtil.hasLength(lastName)) {
			return lastName;
		} else {
			return null;
		}
    }
    
	public static VCard getVCardFromFile(String jid) {
		MXParser parser = new MXParser();
		try {
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
			// Unescape JID
			String fileName = Base64.encodeBytes(jid.getBytes());
			File vcardStorageDirectory = new File(
					SparkManager.getUserDirectory(), "vcards");
			final File vcardFile = new File(vcardStorageDirectory, fileName);
			if (!vcardFile.exists() || !vcardFile.isFile()) {
				return null;

			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(vcardFile), "UTF-8"));
			VCardProvider provider = new VCardProvider();
			parser.setInput(in);
			VCard vcard = (VCard) provider.parseIQ(parser);
			return vcard;
		}
		catch (Exception e) {
			Log.error(e);
		}
		return null;
	}
	
	public static boolean isVCardSaved(String jid) {
        String fileName = Base64.encodeBytes(jid.getBytes());
        // remove tab
        fileName   = fileName.replaceAll("\t", "");
        // remove new line (Unix)
        fileName          = fileName.replaceAll("\n", "");
        // remove new line (Windows)
        fileName          = fileName.replaceAll("\r", "");
		return new File(new File(SparkManager.getUserDirectory(), "vcards"), fileName).exists();
	}
    
    public static ImageIcon getAvatarFromCache(String jid) {
    	VCard cachedVCard = getVCardFromFile(jid);
    	return cachedVCard != null ? retrieveAvatar(cachedVCard) : getDefaultAvatar();    	
    }
    
    public static ImageIcon retrieveAvatarFromURL(String url) {
    	ImageIcon avatar = null;
		try {
			avatar = new ImageIcon(new URL(url));
		} catch (MalformedURLException e) {
			Log.warning("Cannot retrieve phonebook member avatar");
		} finally {
			if (avatar == null || avatar.getIconHeight() <= 1) {
				avatar = getDefaultAvatar();
			}
		}
    	return avatar;
    }    
    
    public static ImageIcon getDefaultAvatar() {
		URL defaultAvatar = Utils.class.getClassLoader().getResource("resources/images/default_avatar_64x64.png");
		return new ImageIcon(defaultAvatar);
    }
    
    private static boolean hashExists(String hash) {
    	File contactsDir = new File(SparkManager.getUserDirectory(), "contacts");
        contactsDir.mkdirs();
        final File imageFile = new File(contactsDir, hash);
        return imageFile.exists();
    }
    
    public static boolean isVCardUpdated(Presence presence) {
        final PacketExtension packetExtension = presence.getExtension("x", "vcard-temp:x:update");
        // Handle vCard update packet.
        if (packetExtension != null && packetExtension instanceof DefaultPacketExtension) {
            DefaultPacketExtension o = (DefaultPacketExtension)packetExtension;
            String photo = o.getValue("photo");            
            if (photo != null && !hashExists(photo)) {
            	return true;
            }
        }
        return false;
    }
    
    public static String getExtension(String jid) {
        final VCard targetVCard = SparkManager.getVCardManager().getVCard(jid);
        return targetVCard.getField("X-INTERN");
    }
    
    public static String getAlias(String jid) {
        final VCard targetVCard = SparkManager.getVCardManager().getVCard(jid);
        return getAlias(targetVCard);
    }   
    
    public static ContactItem getContactItemWithExtension(String extension) {
    	ContactList list = Workspace.getInstance().getContactList();
    	List<ContactGroup> groupList = list.getContactGroups();
    	List<ContactItem> listItems = null;
    	for (ContactGroup group : groupList) {
    		listItems = group.getContactItems();
    		for (ContactItem item : listItems) {
    			if(StringUtils.equals(getExtension(item.getJID()), extension)) {
    				return item;
    			}
    		}
    	}
    	return null;
    }
    
    public static String getFullJID(String extension) {
    	ContactItem item = getContactItemWithExtension(extension);
    	if (item != null) {
    		return SparkManager.getUserManager().getFullJID(item.getJID());
    	}
    	return null;
    }
}
