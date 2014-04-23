package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import org.ezuce.media.manager.PhoneManager;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;

/**
 *
 * @author Razvan
 */
public class TransferCallListener implements ActionListener{
    
    private String m_callId;
    
    public TransferCallListener(String callId) {
        this.m_callId = callId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       String destination = e.getActionCommand();
       if (destination!=null && destination.length()>0)
       {
            PhoneManager.getInstance().transferCall(m_callId, e.getActionCommand());
       }else{
           JOptionPane.showMessageDialog(SparkManager.getMainWindow(), Res.getString("transfercall.msg.nonumber"), Res.getString("transfercall.caption.nonumber"), JOptionPane.WARNING_MESSAGE);
       }
    }
    
}
