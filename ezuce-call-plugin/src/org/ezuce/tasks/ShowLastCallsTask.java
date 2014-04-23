package org.ezuce.tasks;

import java.awt.Cursor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.ezuce.common.rest.Cdr;
import org.ezuce.common.rest.RestManager;
import org.ezuce.panels.CallPanel2;
import org.ezuce.popups.LastCallsPopUp;
import org.jdesktop.application.Task;

/**
 *
 * @author Razvan
 */
public class ShowLastCallsTask extends Task<List<Cdr>,Void>
{
    private final CallPanel2 callPanel2;

    public ShowLastCallsTask(org.jdesktop.application.Application app, CallPanel2 cp2)
    {
        super(app);
        this.callPanel2=cp2;
        callPanel2.setCursor(hourglassCursor);
    }
    @Override
    protected List<Cdr> doInBackground() {
        try {
            List<Cdr> calls=RestManager.getInstance().getLastMonthCalls();
            List<Cdr> recentCalls=new ArrayList<Cdr>();
            int n=0;
            for (Cdr call:calls) {
                //&& (Utils.isToday(cdrDate) || Utils.isYesterday(cdrDate) || Utils.isLastWeek(cdrDate)
                if (call.isDialed())
                {
                    recentCalls.add(call);
                    n++;
                }
                if (n==10) break;
            }
            return recentCalls;
        }
        catch(Exception ex){
            ex.printStackTrace(System.err);
        }
        return null;
    }
    @Override
    protected void succeeded(List<Cdr> result)
    {
        if (result!=null)
        {
            LastCallsPopUp popup=new LastCallsPopUp(result, callPanel2.getjXSearchField());
            popup.show(callPanel2.getMakeCallTopPanel().getjButtonLastCalls(), 0-popup.getWidth(), callPanel2.getMakeCallTopPanel().getjButtonLastCalls().getHeight());
        }
    }
    @Override
    protected void finished()
    {
        callPanel2.setCursor(defaultCursor);
    }

    private final Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
    private final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
}
