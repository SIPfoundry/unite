package org.ezuce.wrappers.interfaces;
import java.util.Date;


/**
 * This is a marker interface that should be implemented
 * by all components that would be displayed inside a CallHistoryGroupPanel.
 * @author Razvan
 */
public interface HistoryItem extends Comparable
{
    public enum HistoryItemTypes {MISSED_CALL, RECEIVED_CALL, DIALED_CALL, VOICE_MAIL}
    public Date getTimestamp();
    public HistoryItemTypes getHistoryItemType();
    public boolean isDeleted();
}
