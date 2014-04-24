package org.ezuce.common.rest;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.ezuce.common.resource.SipUriUtil;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.xml.CallXML;
import org.jivesoftware.spark.util.log.Log;

public class Cdr {
    private String user;
    private CallXML callxml;
    
    private String date;
    public enum CallType {MISSED, RECEIVED, DIALED, UNKNOWN};
    private Boolean last24Hrs;

    public Cdr (String user, CallXML callxml) {
        this.user = user;
        this.callxml = callxml;
    }

    public String getCaller() {
        return SipUriUtil.extractUser(callxml.getCallerAor());
    }

    public String getCallee() {
        return SipUriUtil.extractUser(callxml.getCalleeAor());
    }

    public String getCallerName() {
        return SipUriUtil.extractUserName(callxml.getCallerAor());
    }

    public String getCalleeName() {
        return SipUriUtil.extractUserName(callxml.getCalleeAor());
    }

    public String getCalleeRoute() {        
        return callxml.getCalleeRoute();
    }

	public String getDate() {
		// rest api returns date in UTC format - we need to convert it to system
		// timezone
		String dateStr = callxml.getStartTime();
		try {
			Calendar utcCal = Utils.getCalendar(dateStr);
			Date dateCal = utcCal.getTime();
			TimeZone tz = utcCal.getTimeZone();
			long ms = dateCal.getTime();
			int offsetFromUTC = tz.getOffset(ms);

			Calendar cal = Calendar.getInstance();
			cal.setTime(dateCal);
			cal.add(Calendar.MILLISECOND, offsetFromUTC);

			date = Utils.getDateToDisplay(cal.getTimeInMillis());
		} catch (ParseException ex) {
			Log.error("Cannot parse call date ", ex);
		}

		return date;
	}

    public String getDuration() {       
        return callxml.getDuration();
    }

    public String getTermination() {        
        return callxml.getTermination();
    }

    public boolean isMissed() {
        return getType().equals(CallType.MISSED);
    }

    public boolean isDialed() {
        return getType().equals(CallType.DIALED);
    }

    public boolean isReceived() {
        return getType().equals(CallType.RECEIVED);
    }

    public CallType getType() {
        if (user.equals(getCallee()) && !getTermination().equals("C")) {
            return CallType.MISSED;
        } else if (user.equals(getCaller()) && getTermination().equals("C")) {
            return CallType.DIALED;
        } else if (user.equals(getCallee()) && getTermination().equals("C")) {
            return CallType.RECEIVED;
        } else {
            return CallType.UNKNOWN;
        }
    }
    
    public boolean isLast24Hrs() throws ParseException {
        if (last24Hrs == null) {
        	last24Hrs = Utils.isLast24Hours3(getDate());
        }
        return last24Hrs;
    }
}
