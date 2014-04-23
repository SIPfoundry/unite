/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.Cdr;
import org.ezuce.common.rest.Voicemail;

/**
 *
 * @author Razvan
 */
public class DateUtils
{
    public static final SimpleDateFormat formatterForUtils = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final SimpleDateFormat formatterFromCdr = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");

    public static final SimpleDateFormat outputFormatToday=new SimpleDateFormat("hh:mm a");
    public static final SimpleDateFormat outputFormatOlder=new SimpleDateFormat("EEE MM/dd/yyyy");
    public static final SimpleDateFormat outputFormatVoicemailOlder=new SimpleDateFormat("EEE MM/dd/yyyy");

    public static Boolean isToday(Cdr cdr) throws ParseException
    {
        return Utils.isToday(formatterForUtils.format(formatterFromCdr.parse(cdr.getDate())));
    }

    public static Boolean isYesterday(Cdr cdr) throws ParseException
    {
        return Utils.isYesterday(formatterForUtils.format(formatterFromCdr.parse(cdr.getDate())));
    }

    public static Boolean isLastWeek(Cdr cdr) throws ParseException
    {
        return Utils.isLastWeek(formatterForUtils.format(formatterFromCdr.parse(cdr.getDate())));
    }

    public static Boolean isLastMonth(Cdr cdr) throws ParseException
    {
        return Utils.isLastMonth(formatterForUtils.format(formatterFromCdr.parse(cdr.getDate())));
    }

    public static Boolean isToday(Voicemail voiceMail) throws ParseException
    {
        return Utils.isToday(formatterForUtils.format(formatterFromCdr.parse(voiceMail.getDate())));
    }

    public static Boolean isYesterday(Voicemail voiceMail) throws ParseException
    {
        return Utils.isYesterday(formatterForUtils.format(formatterFromCdr.parse(voiceMail.getDate())));
    }

    public static Boolean isLastWeek(Voicemail voiceMail) throws ParseException
    {
        return Utils.isLastWeek(formatterForUtils.format(formatterFromCdr.parse(voiceMail.getDate())));
    }

    public static Boolean isLastMonth(Voicemail voiceMail) throws ParseException
    {
        return Utils.isLastMonth(formatterForUtils.format(formatterFromCdr.parse(voiceMail.getDate())));
    }
}
