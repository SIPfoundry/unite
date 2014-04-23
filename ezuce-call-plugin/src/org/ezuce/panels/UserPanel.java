package org.ezuce.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import org.ezuce.paints.GlossAngleGradientPainter;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;



/**
 *
 *
 */
public class UserPanel extends javax.swing.JPanel {

    /** Creates new form UserPanel */
    public UserPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jLayeredPane = new JLayeredPane();
        jXPanelGlass = new JXPanel();
        jPanelPic = new JPanel();
        jXLabelPic = new JXLabel();
        jXLabelStatus = new JXLabel();
        jPanelDetails = new JPanel();
        jXLabel1 = new JXLabel();
        jXLabel2 = new JXLabel();
        jXLabel3 = new JXLabel();

        setOpaque(false);

        jLayeredPane.setBorder(BorderFactory.createEtchedBorder());
        jLayeredPane.setMaximumSize(new Dimension(71, 81));
        jLayeredPane.setMinimumSize(new Dimension(71, 81));
        jLayeredPane.setName("jLayeredPane"); // NOI18N

        jXPanelGlass.setAlpha(0.6F);
        jXPanelGlass.setBackgroundPainter(GlossAngleGradientPainter.painter());
        jXPanelGlass.setMaximumSize(new Dimension(71, 81));
        jXPanelGlass.setMinimumSize(new Dimension(71, 81));
        jXPanelGlass.setName("jXPanelGlass"); // NOI18N
        jXPanelGlass.setPreferredSize(new Dimension(71, 81));

        GroupLayout jXPanelGlassLayout = new GroupLayout(jXPanelGlass);
        jXPanelGlass.setLayout(jXPanelGlassLayout);
        jXPanelGlassLayout.setHorizontalGroup(
            jXPanelGlassLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 71, Short.MAX_VALUE)
        );
        jXPanelGlassLayout.setVerticalGroup(
            jXPanelGlassLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 81, Short.MAX_VALUE)
        );

        jXPanelGlass.setBounds(0, 0, 71, 81);
        jLayeredPane.add(jXPanelGlass, JLayeredPane.DEFAULT_LAYER);

        jPanelPic.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        jPanelPic.setOpaque(false);
        jPanelPic.setMaximumSize(new Dimension(70, 80));
        jPanelPic.setMinimumSize(new Dimension(70, 80));
        jPanelPic.setName("jPanelPic"); // NOI18N
        jPanelPic.setPreferredSize(new Dimension(70, 80));
        jPanelPic.setLayout(new BorderLayout(2, 2));

        jXLabelPic.setHorizontalAlignment(SwingConstants.CENTER);
        jXLabelPic.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/panels/resources/icnRedLarge.png"))); // NOI18N
        jXLabelPic.setVerticalAlignment(SwingConstants.TOP);
        jXLabelPic.setDoubleBuffered(true);
        jXLabelPic.setName("jXLabelPic"); // NOI18N
        jPanelPic.add(jXLabelPic, BorderLayout.CENTER);

        jXLabelStatus.setBackground(new Color(204, 204, 204));
        jXLabelStatus.setBorder(BorderFactory.createEmptyBorder(2, 2, 3, 2));
        jXLabelStatus.setHorizontalAlignment(SwingConstants.TRAILING);
        jXLabelStatus.setIcon(new ImageIcon(getClass().getResource("/org/ezuce/panels/resources/availableIconLarge.png"))); // NOI18N
        jXLabelStatus.setName("jXLabelStatus"); // NOI18N
        jXLabelStatus.setOpaque(true);
        jPanelPic.add(jXLabelStatus, BorderLayout.SOUTH);

        jPanelPic.setBounds(0, 0, 70, 80);
        jLayeredPane.add(jPanelPic, JLayeredPane.DEFAULT_LAYER);

        jPanelDetails.setOpaque(false);
        jPanelDetails.setName("jPanelDetails"); // NOI18N

        jXLabel1.setForeground(new Color(46, 85, 102));
        jXLabel1.setLineWrap(true);
        jXLabel1.setText("User name");
        jXLabel1.setFont(new Font("Arial", 1, 14));
        jXLabel1.setName("jXLabel1"); // NOI18N

        jXLabel2.setForeground(new Color(153, 153, 153));
        jXLabel2.setText("Position");
        jXLabel2.setFont(new Font("Arial", 0, 12));
        jXLabel2.setName("jXLabel2"); // NOI18N

        jXLabel3.setForeground(new Color(204, 204, 204));
        jXLabel3.setText("Status");
        jXLabel3.setFont(new Font("Arial", 0, 12));
        jXLabel3.setName("jXLabel3"); // NOI18N

        GroupLayout jPanelDetailsLayout = new GroupLayout(jPanelDetails);
        jPanelDetails.setLayout(jPanelDetailsLayout);
        jPanelDetailsLayout.setHorizontalGroup(
            jPanelDetailsLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jPanelDetailsLayout.createSequentialGroup()
                .addGroup(jPanelDetailsLayout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jXLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jXLabel2, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                    .addComponent(jXLabel3, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelDetailsLayout.setVerticalGroup(
            jPanelDetailsLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(Alignment.TRAILING, jPanelDetailsLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jXLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jXLabel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(jXLabel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLayeredPane, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jPanelDetails, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLayeredPane, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanelDetails, GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
        );
    }//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLayeredPane jLayeredPane;
    private JPanel jPanelDetails;
    private JPanel jPanelPic;
    private JXLabel jXLabel1;
    private JXLabel jXLabel2;
    private JXLabel jXLabel3;
    private JXLabel jXLabelPic;
    private JXLabel jXLabelStatus;
    private JXPanel jXPanelGlass;
    // End of variables declaration//GEN-END:variables

}
