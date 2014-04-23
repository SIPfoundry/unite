/*
 * LoadingPanel.java
 *
 * Created on Mar 12, 2011, 10:20:14 PM
 */

package org.ezuce.common.ui.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author Razvan
 */
public class LoadingPanel extends javax.swing.JPanel {

    /** Creates new form LoadingPanel */
    public LoadingPanel() {
        initComponents();
        int busyAnimationRate = resourceMap.getInteger("loadingBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("loadingBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });

    }

    @Override
    public void setVisible(boolean flag)
    {
        super.setVisible(flag);
        if (flag)
        {
            if (!busyIconTimer.isRunning())
            {
                statusAnimationLabel.setIcon(busyIcons[0]);
                busyIconIndex = 0;
                busyIconTimer.start();
            }
        }
        else
        {
            busyIconTimer.stop();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jLabel = new JLabel();
        statusAnimationLabel = new JLabel();

        setFocusable(false);
        setOpaque(false);

        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setText(resourceMap.getString("loading.text"));
        jLabel.setVerticalAlignment(SwingConstants.TOP);
        jLabel.setBorder(BorderFactory.createEmptyBorder(4, 1, 1, 1));
        jLabel.setDoubleBuffered(true);
        jLabel.setEnabled(false);
        jLabel.setFocusable(false);
        jLabel.setName("jLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusAnimationLabel.setDoubleBuffered(true);
        statusAnimationLabel.setFocusable(false);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLabel, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(statusAnimationLabel, GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addContainerGap())
        );
    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel jLabel;
    private JLabel statusAnimationLabel;
    // End of variables declaration//GEN-END:variables
    private final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(LoadingPanel.class);
    private int busyIconIndex=0;
    private final Timer busyIconTimer;
    private final Icon[] busyIcons = new Icon[12];
}
