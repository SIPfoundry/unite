package org.ezuce.panels;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LinearGradientPaint;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.MattePainter;

/**
 *
 *
 */
public class UserMainPanel extends JXPanel {

    private LinearGradientPaint gradientPaint  =
         new LinearGradientPaint(0.0f, 0.0f, 0, 200,
                                 new float[]{0.0f, 1.0f},
                                 new Color[]{new Color(255,255,255,255),
                                             new Color(238,239,244,200)});

    /** Creates new form UserMainPanel */
    public UserMainPanel() {
        //super(new Color(238,239,244), Color.WHITE);
        initComponents();
    }

//    public UserMainPanel(Color startColor, Color endColor)
//    {
//        super(startColor, endColor);
//        initComponents();
//    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        userPanel = new UserPanel();
        userBtnPanel = new UserBtnPanel();

        setBackgroundPainter(new MattePainter(gradientPaint));
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(4, 4, 1, 4)));
        setMinimumSize(new Dimension(373, 90));

        userPanel.setName("userPanel"); // NOI18N

        userBtnPanel.setName("userBtnPanel"); // NOI18N

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(userPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(userBtnPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(userPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(userBtnPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        );
    }//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private UserBtnPanel userBtnPanel;
    private UserPanel userPanel;
    // End of variables declaration//GEN-END:variables

}
