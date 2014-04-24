package org.ezuce.tasks;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.ezuce.call.HistoryItemsUtils;
import org.ezuce.common.rest.Cdr;
import org.ezuce.common.rest.Voicemail;
import org.ezuce.panels.CallHistoryGroupPanel;
import org.ezuce.panels.CallHistoryPanel2;
import org.ezuce.panels.CallPanel2;
import org.ezuce.utils.DateUtils;
import org.jdesktop.application.Task;

/**
 *
 * @author Razvan
 */
public class PopulateHistoryListTask extends Task<List<CallHistoryGroupPanel>,Void>
{
     private final String TODAY="today.text";
     private final String YESTERDAY="yesterday.text";
     private final String LAST_WEEK="last.week.text";
     private final String LAST_MONTH="last.month.text";
     private final CallPanel2 callPanel2;
     private final CallHistoryPanel2 callHistoryPanel2;
     private final boolean showMissedCalls, showReceivedCalls, showDialedCalls, showVoiceMails;

    public PopulateHistoryListTask(org.jdesktop.application.Application app,
                                   CallPanel2 cp2,CallHistoryPanel2 chp2,
                                   boolean showMissedCalls,
                                   boolean showReceivedCalls,
                                   boolean showDialedCalls,
                                   boolean showVoiceMails)
    {
        //EDT - read GUI info:
       super(app);
       this.callPanel2=cp2;
       this.callHistoryPanel2=chp2;
       this.showMissedCalls=showMissedCalls;
       this.showReceivedCalls=showReceivedCalls;
       this.showDialedCalls=showDialedCalls;
       this.showVoiceMails=showVoiceMails;

       this.callPanel2.setCursor(hourglassCursor);
       //First, clear history contents (in case there are any):
       this.callHistoryPanel2.removeAllHistoryGroups();
       this.callHistoryPanel2.showLoading();
    }
    @Override
    protected List<CallHistoryGroupPanel> doInBackground(){
        //NOT EDT:
        System.gc();
        try
        {
          HistoryItemsUtils hiu=new HistoryItemsUtils();
          HashMap<String, List> results=hiu.getHistoryList();
          //Get all voicemails:
          List<Voicemail> voicemails = null;
          voicemails=(List<Voicemail>)results.get(hiu.VOICEMAILS);

          //Get all cdrs:
          List<Cdr> cdrs  = null;
          cdrs=(List<Cdr>)results.get(hiu.CALLS);          

          List<Cdr> cdrsToday=new ArrayList<Cdr>();
          List<Voicemail> voiceMailsToday=new ArrayList<Voicemail>();

          List<Cdr> cdrsYesterday=new ArrayList<Cdr>();
          List<Voicemail> voiceMailsYesterday=new ArrayList<Voicemail>();

          List<Cdr> cdrsLastWeek=new ArrayList<Cdr>();
          List<Voicemail> voiceMailsLastWeek=new ArrayList<Voicemail>();

          List<Cdr> cdrsLastMonth=new ArrayList<Cdr>();
          List<Voicemail> voiceMailsLastMonth=new ArrayList<Voicemail>();

          //Group Cdrs by date (today, last week, last month):
          for (Cdr cdr:cdrs)
          {
            //cdr.isToday() throws ParseException !
            // => replacing with Utils.isToday(...)
            // and reformat the cdr.getDate() result.
            if (DateUtils.isToday(cdr)) cdrsToday.add(cdr);
            else if (DateUtils.isYesterday(cdr)) cdrsYesterday.add(cdr);
                else if (DateUtils.isLastWeek(cdr)) cdrsLastWeek.add(cdr);
                    else cdrsLastMonth.add(cdr);
          }
          //Group Voicemails by date (today, last week, last month):
          for (Voicemail vm:voicemails)
          {
            if (!vm.isDeleted())
            {
                if (DateUtils.isToday(vm)) voiceMailsToday.add(vm);
                else if (DateUtils.isYesterday(vm)) voiceMailsYesterday.add(vm);
                    else if (DateUtils.isLastWeek(vm)) voiceMailsLastWeek.add(vm);
                        else voiceMailsLastMonth.add(vm);
            }
          }
            List<CallHistoryGroupPanel> chgPanels=new ArrayList<CallHistoryGroupPanel>(4);
            CallHistoryGroupPanel chgp=new CallHistoryGroupPanel(
                                                    cdrsToday,
                                                    voiceMailsToday,TODAY);
            chgPanels.add(chgp);

            chgp=new CallHistoryGroupPanel(
                                cdrsYesterday,
                                voiceMailsYesterday,YESTERDAY);
            chgPanels.add(chgp);

            chgp=new CallHistoryGroupPanel(
                                cdrsLastWeek,
                                voiceMailsLastWeek,LAST_WEEK);
            chgPanels.add(chgp);

            chgp=new CallHistoryGroupPanel(
                                cdrsLastMonth,
                                voiceMailsLastMonth,LAST_MONTH);
            chgPanels.add(chgp);


            voicemails=null;
            cdrs=null;

            return chgPanels;
        }
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            return null;
        }
    }
    @Override
    protected void succeeded(List<CallHistoryGroupPanel> historyGroups){
        //EDT:
        callHistoryPanel2.hideLoading();
        if (historyGroups!=null)
        {
            for (CallHistoryGroupPanel chgp:historyGroups)
            {
                chgp.filterHistoryItems(showMissedCalls, showReceivedCalls, showDialedCalls, showVoiceMails);
                callHistoryPanel2.addHistoryGroupPanel(chgp);
            }
        }
    }
    @Override
    protected void finished() {
         callPanel2.setCursor(defaultCursor);
         callHistoryPanel2.setReload(true);
    }

    private final Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
    private final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

}