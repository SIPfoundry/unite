package org.ezuce.panels;

import org.ezuce.wrappers.interfaces.HistoryItem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.Beans;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import org.ezuce.common.rest.Cdr;
import org.ezuce.common.ui.actions.task.MakeCallTask;
import org.ezuce.paints.LinearGradientPainter;
import org.ezuce.utils.DateUtils;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.MattePainter;

/**
 * Displays information specific to received call history event.
 *
 */
public class ReceivedCallMiniPanel extends javax.swing.JPanel implements HistoryItem{


    /** Creates new form ReceivedCallMiniPanel */
    public ReceivedCallMiniPanel() {
        initComponents();
    }

    public ReceivedCallMiniPanel(Cdr cdr)
    {
        this.cdr=cdr;
        initComponents();
        customize();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        jXPanelBackground = new JXPanel();
        jLabel = new JLabel();
        jButtonCallBack = new JButton();
        jLabelUsername = new JLabel();
        jLabelTimeOfCall = new JLabel();

        setMaximumSize(new Dimension(32767, 32));
        setMinimumSize(new Dimension(309, 32));
        setPreferredSize(new Dimension(309, 32));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                formMouseExited(evt);
            }
        });

        jXPanelBackground.setBackgroundPainter(this.matteBackgroundLight);
        jXPanelBackground.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204)));
        jXPanelBackground.setMaximumSize(new Dimension(32767, 32));
        jXPanelBackground.setMinimumSize(new Dimension(0, 32));
        jXPanelBackground.setName("jXPanelBackground"); // NOI18N
        jXPanelBackground.setPreferredSize(new Dimension(309, 32));

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(ReceivedCallMiniPanel.class);
        jLabel.setIcon(resourceMap.getIcon("jLabel.icon")); // NOI18N
        jLabel.setDoubleBuffered(true);
        jLabel.setName("jLabel"); // NOI18N

        ActionMap actionMap = Application.getInstance().getContext().getActionMap(ReceivedCallMiniPanel.class, this);
        jButtonCallBack.setAction(actionMap.get("Call")); // NOI18N
        jButtonCallBack.setIcon(resourceMap.getIcon("jButtonCallBack.icon")); // NOI18N
        jButtonCallBack.setBorder(null);
        jButtonCallBack.setBorderPainted(false);
        jButtonCallBack.setContentAreaFilled(false);
        jButtonCallBack.setDoubleBuffered(true);
        jButtonCallBack.setFocusPainted(false);
        jButtonCallBack.setHideActionText(true);
        jButtonCallBack.setName("jButtonCallBack"); // NOI18N
        jButtonCallBack.setRolloverIcon(resourceMap.getIcon("jButtonCallBack.rolloverIcon")); // NOI18N

        jLabelUsername.setFont(new Font("Arial", 1, 12));
        jLabelUsername.setForeground(new Color(46, 85, 102));
        jLabelUsername.setIcon(resourceMap.getIcon("jLabelUsername.icon")); // NOI18N
        jLabelUsername.setText("User name");
        jLabelUsername.setDoubleBuffered(true);
        jLabelUsername.setName("jLabelUsername"); // NOI18N

        jLabelTimeOfCall.setFont(new Font("Arial", 0, 10)); // NOI18N
        jLabelTimeOfCall.setForeground(new Color(153, 153, 153));
        jLabelTimeOfCall.setHorizontalAlignment(SwingConstants.TRAILING);
        jLabelTimeOfCall.setText("00:00 am");
        jLabelTimeOfCall.setDoubleBuffered(true);
        jLabelTimeOfCall.setName("jLabelTimeOfCall"); // NOI18N

        GroupLayout jXPanelBackgroundLayout = new GroupLayout(jXPanelBackground);
        jXPanelBackground.setLayout(jXPanelBackgroundLayout);
        jXPanelBackgroundLayout.setHorizontalGroup(
            jXPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXPanelBackgroundLayout.createSequentialGroup()
                .addComponent(jLabel)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jLabelUsername, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jLabelTimeOfCall, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jButtonCallBack)
                .addContainerGap())
        );
        jXPanelBackgroundLayout.setVerticalGroup(
            jXPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXPanelBackgroundLayout.createSequentialGroup()
                .addGroup(jXPanelBackgroundLayout.createParallelGroup(Alignment.TRAILING)
                    .addComponent(jLabelTimeOfCall, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelUsername, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addGroup(Alignment.LEADING, jXPanelBackgroundLayout.createParallelGroup(Alignment.TRAILING, false)
                        .addComponent(jLabel, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCallBack, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jXPanelBackground, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jXPanelBackground, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }//GEN-END:initComponents

    /**
     * Adjust the GUI elements to display the information provided by the Cdr property.
     */
    private void customize()
    {
        if (this.cdr==null) return;
        this.setUserNameOrNumber(this.cdr.getCallerName());
        try
        {
            if (DateUtils.isToday(this.cdr) || DateUtils.isYesterday(this.cdr))
            {
                this.setTimeOfCall(outputFormatToday.format(inputFormat.parse(this.cdr.getDate())));
            }
            else
            {
                this.setTimeOfCall(outputFormatOlder.format(inputFormat.parse(this.cdr.getDate())));
            }
        }
        catch(ParseException pe)
        {
            pe.printStackTrace(System.err);
        }

        wireupActions();
    }

    /**
     * Compares the current HistoryItem to another HistoryItem, inverting the
     * comparison result such that: [newer event] is smaller than [older event].
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o)
    {
        if (o==null || !(o instanceof HistoryItem)) return 1;
        final HistoryItem hi=(HistoryItem)o;
        return -(this.getTimestamp().compareTo(hi.getTimestamp()));
    }

    @Override
    public Date getTimestamp()
    {
        try
        {
           return inputFormat.parse(cdr.getDate());
        }
        catch(ParseException pe)
        {
            pe.printStackTrace(System.err);
        }
        return null;
    }

    @Override
    public HistoryItemTypes getHistoryItemType()
    {
        return HistoryItemTypes.RECEIVED_CALL;
    }

    private void wireupActions()
    {
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.
                                    getInstance().getContext().getActionMap(this);
        javax.swing.Action action=actionMap.get("Call");
        action.putValue(action.SMALL_ICON, this.jButtonCallBack.getIcon());
        this.jButtonCallBack.setAction(action);
    }

    @Action(block = Task.BlockingScope.COMPONENT)
    public Task Call() {
         if (!Beans.isDesignTime())
         {
            return new MakeCallTask(org.jdesktop.application.Application.getInstance(),ReceivedCallMiniPanel.this.cdr.getCaller());
         }
         else{
             return null;
         }
    }
    /*
    private class MakeCallTask extends org.jdesktop.application.Task<Object, Void> {
        String calee = null;
        MakeCallTask(org.jdesktop.application.Application app) {
            //EDT - read GUI info:
            super(app);
            calee = ReceivedCallMiniPanel.this.cdr.getCaller(); // ?? is this right ??
        }
        @Override
        protected Object doInBackground() {
            //NOT EDT:
            RestManager restManager = RestManager.getInstance();
            try {
                restManager.makeCall(calee);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            return null;
        }
        @Override
        protected void succeeded(Object result) {
            //EDT:
        }
    }
    */

    private void formMouseEntered(MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        this.jXPanelBackground.setBackgroundPainter(matteBackgroundDark);
    }//GEN-LAST:event_formMouseEntered

    private void formMouseExited(MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        this.jXPanelBackground.setBackgroundPainter(matteBackgroundLight);
    }//GEN-LAST:event_formMouseExited

    public Cdr getCdr() {
        return cdr;
    }

    public void setCdr(Cdr cdr) {
        this.cdr = cdr;
    }

    /**
     * Sets the name/number of the caller.
     * @param username
     */
    public void setUserNameOrNumber(String username)
    {
        this.jLabelUsername.setText(username);
    }
    public String getUserNameOrNumber()
    {
        return this.jLabelUsername.getText();
    }

    /**
     * Sets the time of call.
     * @param timeOfcall
     */
    public void setTimeOfCall(String timeOfcall)
    {
        this.jLabelTimeOfCall.setText(timeOfcall);
    }
    public String getTimeOfCall()
    {
        return this.jLabelTimeOfCall.getText();
    }

    /**
     * Gets a reference to the call back JButton.
     * @return
     */
    public JButton getjButtonCallBack()
    {
        return this.jButtonCallBack;
    }

    @Override
    public boolean isDeleted()
    {
        return false;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButtonCallBack;
    private JLabel jLabel;
    private JLabel jLabelTimeOfCall;
    private JLabel jLabelUsername;
    private JXPanel jXPanelBackground;
    // End of variables declaration//GEN-END:variables
    private MattePainter matteBackgroundLight=LinearGradientPainter.missedCallLightPainter();
    private MattePainter matteBackgroundDark=LinearGradientPainter.missedCallDarkPainter();
    private Cdr cdr;
    private final SimpleDateFormat outputFormatToday=DateUtils.outputFormatToday;
    private final SimpleDateFormat outputFormatOlder=DateUtils.outputFormatOlder;
    private final SimpleDateFormat inputFormat = DateUtils.formatterFromCdr;

}
