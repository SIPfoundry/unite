/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.call;
import java.util.HashMap;
import java.util.List;
import org.ezuce.common.rest.Cdr;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.rest.Voicemail;

/**
 *
 * @author Razvan
 */
public class HistoryItemsUtils
{
    public final String CALLS="calls";
    public final String VOICEMAILS="voicemails";

    public HashMap<String,List> getHistoryList() throws Exception
    {
        HashMap<String,List> result=new HashMap<String,List>(2);
        Thread[] threads=new Thread[2];

        CallRetrievalThread cdrThread=new CallRetrievalThread();
        VoicemailRetrievalThread vmThread=new VoicemailRetrievalThread();

        threads[0]=new Thread(cdrThread);
        threads[1]=new Thread(vmThread);

        threads[0].start();
        threads[1].start();

        threads[0].join();
        threads[1].join();

        result.put(CALLS, cdrThread.getResult());
        result.put(VOICEMAILS, vmThread.getResult());

        return result;
    }

    private class CallRetrievalThread implements Runnable
    {
        private List<Cdr> cdrs;
        public CallRetrievalThread()
        {
        }
        @Override
        public void run()
        {
            try
            {
                cdrs  = RestManager.getInstance().getLastMonthCalls();
            }
            catch(Exception e)
            {
              e.printStackTrace(System.err);
            }
        }
        public List<Cdr> getResult()
        {
            return cdrs;
        }
    }

    private class VoicemailRetrievalThread implements Runnable
    {
        private List<Voicemail> voicemails;
        public VoicemailRetrievalThread()
        {
        }
        @Override
        public void run()
        {
            try
            {
                voicemails  = RestManager.getInstance().getLastMonthVoicemails();
            }
            catch(Exception e)
            {
              e.printStackTrace(System.err);
            }
        }
        public List<Voicemail> getResult()
        {
            return voicemails;
        }
    }


}
