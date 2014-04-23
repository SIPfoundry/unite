package org.ezuce.panels;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import org.ezuce.paints.LinearGradientPainter;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXPanel;

/**
 * Contains a dial pad, featuring the keys used to dial a number, placed on a
 * gradient-capable background.
 *
 */
public class DialPadPanel extends javax.swing.JPanel {


    /** Creates new form DialPadPanel */
    public DialPadPanel() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents

        jLayeredPane = new JLayeredPane();
        jXPanel = new JXPanel();
        dialPad = new DialPad();
        jLabelBackground = new JLabel();

        setMaximumSize(new Dimension(32767, 237));
        setMinimumSize(new Dimension(374, 237));
        setPreferredSize(new Dimension(374, 237));

        jLayeredPane.setName("jLayeredPane"); // NOI18N

        jXPanel.setOpaque(false);
        jXPanel.setMaximumSize(new Dimension(32767, 237));
        jXPanel.setMinimumSize(new Dimension(374, 237));
        jXPanel.setName("jXPanel"); // NOI18N
        jXPanel.setPreferredSize(new Dimension(374, 237));

        dialPad.setMaximumSize(new Dimension(238, 202));
        dialPad.setMinimumSize(new Dimension(238, 202));
        dialPad.setName("dialPad"); // NOI18N
        dialPad.setPreferredSize(new Dimension(238, 202));
        jXPanel.add(dialPad);

        jXPanel.setBounds(0, 20, 374, 200);
        jLayeredPane.add(jXPanel, JLayeredPane.DEFAULT_LAYER);

        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(DialPadPanel.class);
        jLabelBackground.setIcon(resourceMap.getIcon("jLabelBackground.icon")); // NOI18N
        jLabelBackground.setName("jLabelBackground"); // NOI18N
        jLabelBackground.setBounds(0, 0, 374, 237);
        jLayeredPane.add(jLabelBackground, JLayeredPane.DEFAULT_LAYER);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLayeredPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLayeredPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
        );
    }//GEN-END:initComponents

    public DialPad getDialPad()
    {
        return dialPad;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private DialPad dialPad;
    private JLabel jLabelBackground;
    private JLayeredPane jLayeredPane;
    private JXPanel jXPanel;
    // End of variables declaration//GEN-END:variables


}
