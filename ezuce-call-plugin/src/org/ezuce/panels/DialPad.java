package org.ezuce.panels;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.ezuce.common.ConfigureVoice;
import org.ezuce.common.VoiceUtil;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.spark.util.log.Log;

/**
 * Contains the dial keys used to dial a number in the 'Make a call' panel.
 *
 */
public class DialPad extends javax.swing.JPanel implements ConfigureVoice {
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButtonDial0;
    private JButton jButtonDial1;
    private JButton jButtonDial2;
    private JButton jButtonDial3;
    private JButton jButtonDial4;
    private JButton jButtonDial5;
    private JButton jButtonDial6;
    private JButton jButtonDial7;
    private JButton jButtonDial8;
    private JButton jButtonDial9;
    private JButton jButtonDialCall;
    private JButton jButtonDialDiez;
    private JButton jButtonDialRedial;
    private JButton jButtonDialStar;
    private JButton jButtonDialVideoCall = new JButton();
    private JPanel jPanelBottom;
    private JPanel jPanelColCenter;
    private JPanel jPanelColLeft;
    private JPanel jPanelColRight;
    private ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(DialPad.class);
    // End of variables declaration//GEN-END:variables
    /** Creates new form DialPad */
    public DialPad() {
        initComponents();
        try {
			VoiceUtil.configure(this);
		} catch (Exception e) {
			Log.error("Exception during config ", e);
		}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanelColLeft = new JPanel();
        jButtonDial1 = new JButton();
        jButtonDial4 = new JButton();
        jButtonDial7 = new JButton();
        jButtonDialStar = new JButton();
        jPanelColCenter = new JPanel();
        jButtonDial2 = new JButton();
        jButtonDial5 = new JButton();
        jButtonDial8 = new JButton();
        jButtonDial0 = new JButton();
        jPanelColRight = new JPanel();
        jButtonDial3 = new JButton();
        jButtonDial6 = new JButton();
        jButtonDial9 = new JButton();
        jButtonDialDiez = new JButton();
        jPanelBottom = new JPanel();
        jButtonDialRedial = new JButton();
        jButtonDialCall = new JButton();
        jButtonDialVideoCall = new JButton();

        setBackground(new Color(0, 0, 0));
        setMaximumSize(new Dimension(239, 202));
        setMinimumSize(new Dimension(239, 202));
        setOpaque(false);
        setPreferredSize(new Dimension(239, 202));
        setLayout(new BorderLayout());

        jPanelColLeft.setMinimumSize(new Dimension(0, 0));
        jPanelColLeft.setName("jPanelColLeft"); // NOI18N
        jPanelColLeft.setOpaque(false);
        jPanelColLeft.setPreferredSize(new Dimension(81, 160));
        jPanelColLeft.setLayout(new BoxLayout(jPanelColLeft, BoxLayout.Y_AXIS));
        jButtonDial1.setIcon(resourceMap.getIcon("jButtonDial1.icon")); // NOI18N
        jButtonDial1.setBorder(BorderFactory.createEmptyBorder());
        jButtonDial1.setBorderPainted(false);
        jButtonDial1.setContentAreaFilled(false);
        jButtonDial1.setDoubleBuffered(true);
        jButtonDial1.setFocusPainted(false);
        jButtonDial1.setHideActionText(true);
        jButtonDial1.setMaximumSize(new Dimension(112, 40));
        jButtonDial1.setMinimumSize(new Dimension(112, 40));
        jButtonDial1.setPreferredSize(new Dimension(112,40));
        jButtonDial1.setName("jButtonDial1"); // NOI18N
        jButtonDial1.setRolloverIcon(resourceMap.getIcon("jButtonDial1.rolloverIcon")); // NOI18N
        jPanelColLeft.add(jButtonDial1);

        jButtonDial4.setIcon(resourceMap.getIcon("jButtonDial4.icon")); // NOI18N
        jButtonDial4.setBorder(BorderFactory.createEmptyBorder());
        jButtonDial4.setBorderPainted(false);
        jButtonDial4.setContentAreaFilled(false);
        jButtonDial4.setDoubleBuffered(true);
        jButtonDial4.setFocusPainted(false);
        jButtonDial4.setHideActionText(true);
        jButtonDial4.setMaximumSize(new Dimension(112, 39));
        jButtonDial4.setMinimumSize(new Dimension(112, 39));
        jButtonDial4.setPreferredSize(new Dimension(112,39));
        jButtonDial4.setName("jButtonDial4"); // NOI18N
        jButtonDial4.setRolloverIcon(resourceMap.getIcon("jButtonDial4.rolloverIcon")); // NOI18N
        jPanelColLeft.add(jButtonDial4);

        jButtonDial7.setIcon(resourceMap.getIcon("jButtonDial7.icon")); // NOI18N
        jButtonDial7.setBorder(BorderFactory.createEmptyBorder());
        jButtonDial7.setBorderPainted(false);
        jButtonDial7.setContentAreaFilled(false);
        jButtonDial7.setDoubleBuffered(true);
        jButtonDial7.setFocusPainted(false);
        jButtonDial7.setHideActionText(true);
        jButtonDial7.setMaximumSize(new Dimension(112, 39));
        jButtonDial7.setMinimumSize(new Dimension(112, 39));
        jButtonDial7.setPreferredSize(new Dimension(112,39));
        jButtonDial7.setName("jButtonDial7"); // NOI18N
        jButtonDial7.setRolloverIcon(resourceMap.getIcon("jButtonDial7.rolloverIcon")); // NOI18N
        jPanelColLeft.add(jButtonDial7);

        jButtonDialStar.setIcon(resourceMap.getIcon("jButtonDialStar.icon")); // NOI18N
        jButtonDialStar.setBorder(BorderFactory.createEmptyBorder());
        jButtonDialStar.setBorderPainted(false);
        jButtonDialStar.setContentAreaFilled(false);
        jButtonDialStar.setDoubleBuffered(true);
        jButtonDialStar.setFocusPainted(false);
        jButtonDialStar.setHideActionText(true);
        jButtonDialStar.setMaximumSize(new Dimension(112, 39));
        jButtonDialStar.setMinimumSize(new Dimension(112, 39));
        jButtonDialStar.setPreferredSize(new Dimension(112,39));
        jButtonDialStar.setName("jButtonDialStar"); // NOI18N
        jButtonDialStar.setRolloverIcon(resourceMap.getIcon("jButtonDialStar.rolloverIcon")); // NOI18N
        jPanelColLeft.add(jButtonDialStar);
        
        // redial
        jButtonDialRedial.setBorder(BorderFactory.createEmptyBorder());
        jButtonDialRedial.setBorderPainted(false);
        jButtonDialRedial.setContentAreaFilled(false);
        jButtonDialRedial.setDoubleBuffered(true);
        jButtonDialRedial.setFocusPainted(false);
        jButtonDialRedial.setHideActionText(true);
        jButtonDialRedial.setIconTextGap(0);
        jButtonDialRedial.setName("jButtonDialRedial"); // NOI18N
        jButtonDialRedial.setPreferredSize(new Dimension(112, 44));
    	jButtonDialRedial.setIcon(resourceMap.getIcon("jButtonDialRedial.icon2"));
    	jButtonDialRedial.setRolloverIcon(resourceMap.getIcon("jButtonDialRedial.rolloverIcon2"));
        jPanelColLeft.add(jButtonDialRedial);
        
        add(jPanelColLeft, BorderLayout.WEST);

        jPanelColCenter.setMinimumSize(new Dimension(0, 0));
        jPanelColCenter.setName("jPanelColCenter"); // NOI18N
        jPanelColCenter.setOpaque(false);
        jPanelColCenter.setPreferredSize(new Dimension(80, 160));
        jPanelColCenter.setLayout(new BoxLayout(jPanelColCenter, BoxLayout.Y_AXIS));

        jButtonDial2.setIcon(resourceMap.getIcon("jButtonDial2.icon")); // NOI18N
        jButtonDial2.setBorder(BorderFactory.createEmptyBorder());
        jButtonDial2.setBorderPainted(false);
        jButtonDial2.setContentAreaFilled(false);
        jButtonDial2.setDoubleBuffered(true);
        jButtonDial2.setFocusPainted(false);
        jButtonDial2.setHideActionText(true);
        jButtonDial2.setMaximumSize(new Dimension(110, 40));
        jButtonDial2.setMinimumSize(new Dimension(110, 40));
        jButtonDial2.setName("jButtonDial2"); // NOI18N
        jButtonDial2.setPreferredSize(new Dimension(110, 40));
        jButtonDial2.setRolloverIcon(resourceMap.getIcon("jButtonDial2.rolloverIcon")); // NOI18N
        jPanelColCenter.add(jButtonDial2);

        jButtonDial5.setIcon(resourceMap.getIcon("jButtonDial5.icon")); // NOI18N
        jButtonDial5.setBorder(BorderFactory.createEmptyBorder());
        jButtonDial5.setBorderPainted(false);
        jButtonDial5.setContentAreaFilled(false);
        jButtonDial5.setDoubleBuffered(true);
        jButtonDial5.setFocusPainted(false);
        jButtonDial5.setHideActionText(true);
        jButtonDial5.setMaximumSize(new Dimension(110, 40));
        jButtonDial5.setMinimumSize(new Dimension(110, 39));
        jButtonDial5.setName("jButtonDial5"); // NOI18N
        jButtonDial5.setPreferredSize(new Dimension(110, 39));
        jButtonDial5.setRolloverIcon(resourceMap.getIcon("jButtonDial5.rolloverIcon")); // NOI18N
        jPanelColCenter.add(jButtonDial5);

        jButtonDial8.setIcon(resourceMap.getIcon("jButtonDial8.icon")); // NOI18N
        jButtonDial8.setBorder(BorderFactory.createEmptyBorder());
        jButtonDial8.setBorderPainted(false);
        jButtonDial8.setContentAreaFilled(false);
        jButtonDial8.setDoubleBuffered(true);
        jButtonDial8.setFocusPainted(false);
        jButtonDial8.setHideActionText(true);
        jButtonDial8.setMaximumSize(new Dimension(110, 40));
        jButtonDial8.setMinimumSize(new Dimension(110, 39));
        jButtonDial8.setName("jButtonDial8"); // NOI18N
        jButtonDial8.setPreferredSize(new Dimension(110, 39));
        jButtonDial8.setRolloverIcon(resourceMap.getIcon("jButtonDial8.rolloverIcon")); // NOI18N
        jPanelColCenter.add(jButtonDial8);

        jButtonDial0.setIcon(resourceMap.getIcon("jButtonDial0.icon")); // NOI18N
        jButtonDial0.setBorder(BorderFactory.createEmptyBorder());
        jButtonDial0.setBorderPainted(false);
        jButtonDial0.setContentAreaFilled(false);
        jButtonDial0.setDoubleBuffered(true);
        jButtonDial0.setFocusPainted(false);
        jButtonDial0.setHideActionText(true);
        jButtonDial0.setMaximumSize(new Dimension(110, 40));
        jButtonDial0.setMinimumSize(new Dimension(110, 39));
        jButtonDial0.setName("jButtonDial0"); // NOI18N
        jButtonDial0.setPreferredSize(new Dimension(110, 39));
        jButtonDial0.setRolloverIcon(resourceMap.getIcon("jButtonDial0.rolloverIcon")); // NOI18N
        jPanelColCenter.add(jButtonDial0);

        // dial
        jButtonDialCall.setBorder(BorderFactory.createEmptyBorder());
        jButtonDialCall.setBorderPainted(false);
        jButtonDialCall.setContentAreaFilled(false);
        jButtonDialCall.setDoubleBuffered(true);
        jButtonDialCall.setFocusPainted(false);
        jButtonDialCall.setHideActionText(true);
        jButtonDialCall.setIconTextGap(0);
        jButtonDialCall.setName("jButtonDialCall"); // NOI18N
    	jButtonDialCall.setPreferredSize(new Dimension(112, 44));
    	jButtonDialCall.setIcon(resourceMap.getIcon("jButtonDialCall.icon2"));
    	jButtonDialCall.setRolloverIcon(resourceMap.getIcon("jButtonDialCall.rolloverIcon2")); // NOI18N        	
        jPanelColCenter.add(jButtonDialCall);
        
        add(jPanelColCenter, BorderLayout.CENTER);

        jPanelColRight.setMinimumSize(new Dimension(0, 0));
        jPanelColRight.setName("jPanelColRight"); // NOI18N
        jPanelColRight.setOpaque(false);
        jPanelColRight.setPreferredSize(new Dimension(80, 160));
        jPanelColRight.setLayout(new BoxLayout(jPanelColRight, BoxLayout.Y_AXIS));

        jButtonDial3.setIcon(resourceMap.getIcon("jButtonDial3.icon")); // NOI18N
        jButtonDial3.setBorder(BorderFactory.createEmptyBorder());
        jButtonDial3.setBorderPainted(false);
        jButtonDial3.setContentAreaFilled(false);
        jButtonDial3.setDoubleBuffered(true);
        jButtonDial3.setFocusPainted(false);
        jButtonDial3.setHideActionText(true);
        jButtonDial3.setMaximumSize(new Dimension(112, 40));
        jButtonDial3.setMinimumSize(new Dimension(112, 40));
        jButtonDial3.setName("jButtonDial3"); // NOI18N
        jButtonDial3.setPreferredSize(new Dimension(112, 40));
        jButtonDial3.setRolloverIcon(resourceMap.getIcon("jButtonDial3.rolloverIcon")); // NOI18N
        jPanelColRight.add(jButtonDial3);

        jButtonDial6.setIcon(resourceMap.getIcon("jButtonDial6.icon")); // NOI18N
        jButtonDial6.setBorder(BorderFactory.createEmptyBorder());
        jButtonDial6.setBorderPainted(false);
        jButtonDial6.setContentAreaFilled(false);
        jButtonDial6.setDoubleBuffered(true);
        jButtonDial6.setFocusPainted(false);
        jButtonDial6.setHideActionText(true);
        jButtonDial6.setMaximumSize(new Dimension(112, 40));
        jButtonDial6.setMinimumSize(new Dimension(112, 39));
        jButtonDial6.setName("jButtonDial6"); // NOI18N
        jButtonDial6.setPreferredSize(new Dimension(112, 39));
        jButtonDial6.setRolloverIcon(resourceMap.getIcon("jButtonDial6.rolloverIcon")); // NOI18N
        jPanelColRight.add(jButtonDial6);

        jButtonDial9.setIcon(resourceMap.getIcon("jButtonDial9.icon")); // NOI18N
        jButtonDial9.setBorder(BorderFactory.createEmptyBorder());
        jButtonDial9.setBorderPainted(false);
        jButtonDial9.setContentAreaFilled(false);
        jButtonDial9.setDoubleBuffered(true);
        jButtonDial9.setFocusPainted(false);
        jButtonDial9.setHideActionText(true);
        jButtonDial9.setMaximumSize(new Dimension(112, 40));
        jButtonDial9.setMinimumSize(new Dimension(112, 39));
        jButtonDial9.setName("jButtonDial9"); // NOI18N
        jButtonDial9.setPreferredSize(new Dimension(112, 39));
        jButtonDial9.setRolloverIcon(resourceMap.getIcon("jButtonDial9.rolloverIcon")); // NOI18N
        jPanelColRight.add(jButtonDial9);

        jButtonDialDiez.setIcon(resourceMap.getIcon("jButtonDialDiez.icon")); // NOI18N
        jButtonDialDiez.setBorder(BorderFactory.createEmptyBorder());
        jButtonDialDiez.setBorderPainted(false);
        jButtonDialDiez.setContentAreaFilled(false);
        jButtonDialDiez.setDoubleBuffered(true);
        jButtonDialDiez.setFocusPainted(false);
        jButtonDialDiez.setHideActionText(true);
        jButtonDialDiez.setMaximumSize(new Dimension(112, 40));
        jButtonDialDiez.setMinimumSize(new Dimension(112, 39));
        jButtonDialDiez.setName("jButtonDialDiez"); // NOI18N
        jButtonDialDiez.setPreferredSize(new Dimension(112, 39));
        jButtonDialDiez.setRolloverIcon(resourceMap.getIcon("jButtonDialDiez.rolloverIcon")); // NOI18N
        jPanelColRight.add(jButtonDialDiez);

    	jButtonDialVideoCall.setBorder(BorderFactory.createEmptyBorder());
    	jButtonDialVideoCall.setBorderPainted(false);
    	jButtonDialVideoCall.setContentAreaFilled(false);
    	jButtonDialVideoCall.setDoubleBuffered(true);
    	jButtonDialVideoCall.setFocusPainted(false);
    	jButtonDialVideoCall.setHideActionText(true);
    	jButtonDialVideoCall.setIconTextGap(0);
    	jButtonDialVideoCall.setName("jButtonDialVideoCall"); // NOI18N
    	jButtonDialVideoCall.setPreferredSize(new Dimension(112, 44));

    	jPanelColRight.add(jButtonDialVideoCall);
        
        add(jPanelColRight, BorderLayout.EAST);

        jPanelBottom.setMinimumSize(new Dimension(0, 0));
        jPanelBottom.setName("jPanelBottom"); // NOI18N
        jPanelBottom.setOpaque(false);
        jPanelBottom.setPreferredSize(new Dimension(336, 45));
        jPanelBottom.setMaximumSize(new Dimension(336, 45));
        jPanelBottom.setRequestFocusEnabled(false);        
        jPanelBottom.setLayout(new BoxLayout(jPanelBottom, BoxLayout.X_AXIS));

        

    }//GEN-END:initComponents

    @Override
    public void configureVoiceEnabled() {       
        
        jButtonDialRedial.setPreferredSize(new Dimension(112, 44));
        jButtonDialRedial.setIcon(resourceMap.getIcon("jButtonDialRedial.icon2"));
        jButtonDialRedial.setRolloverIcon(resourceMap.getIcon("jButtonDialRedial.rolloverIcon2"));                       
        jButtonDialRedial.setBorder(BorderFactory.createEmptyBorder());
        jButtonDialRedial.setBorderPainted(false);
        jButtonDialRedial.setContentAreaFilled(false);
        jButtonDialRedial.setDoubleBuffered(true);
        jButtonDialRedial.setFocusPainted(false);
        jButtonDialRedial.setHideActionText(true);
        jButtonDialRedial.setIconTextGap(0);
        jButtonDialRedial.setName("jButtonDialRedial"); // NOI18N
        jPanelBottom.add(jButtonDialRedial);

        jButtonDialCall.setPreferredSize(new Dimension(108, 44));
        jButtonDialCall.setIcon(resourceMap.getIcon("jButtonDialCall.icon2"));
        jButtonDialCall.setRolloverIcon(resourceMap.getIcon("jButtonDialCall.rolloverIcon2"));
        jButtonDialCall.setBorder(BorderFactory.createEmptyBorder());
        jButtonDialCall.setBorderPainted(false);
        jButtonDialCall.setContentAreaFilled(false);
        jButtonDialCall.setDoubleBuffered(true);
        jButtonDialCall.setFocusPainted(false);
        jButtonDialCall.setHideActionText(true);
        jButtonDialCall.setIconTextGap(0);
        jButtonDialCall.setName("jButtonDialCall"); // NOI18N
        jPanelBottom.add(jButtonDialCall);
        
        
        jButtonDialVideoCall.setIcon(resourceMap.getIcon("jButtonDialVideoCall.icon"));
        jButtonDialVideoCall.setRolloverIcon(resourceMap.getIcon("jButtonDialVideoCall.rolloverIcon"));
        jButtonDialVideoCall.setEnabled(true);
        jButtonDialVideoCall.setIcon(resourceMap.getIcon("jButtonDialVideoCall.icon"));
        jButtonDialVideoCall.setBorder(BorderFactory.createEmptyBorder());
        jButtonDialVideoCall.setBorderPainted(false);
        jButtonDialVideoCall.setContentAreaFilled(false);
        jButtonDialVideoCall.setDoubleBuffered(true);
        jButtonDialVideoCall.setFocusPainted(false);
        jButtonDialVideoCall.setHideActionText(true);
        jButtonDialVideoCall.setIconTextGap(0);
        jButtonDialVideoCall.setName("jButtonDialVideoCall");
        jButtonDialVideoCall.setPreferredSize(new Dimension(112, 44));
        jButtonDialVideoCall.setRolloverIcon(resourceMap.getIcon("jButtonDialVideoCall.rolloverIcon"));        
        jPanelBottom.add(jButtonDialVideoCall);  

        add(jPanelBottom, BorderLayout.SOUTH);
    }

    @Override
    public void configureVoiceDisabled() {
        jButtonDialVideoCall.setIcon(resourceMap.getIcon("jButtonDialVideoCall.icon.inactive"));
        jButtonDialVideoCall.setRolloverIcon(resourceMap.getIcon("jButtonDialVideoCall.icon.inactive"));
        jButtonDialVideoCall.setEnabled(false);
        
        jButtonDialRedial.setPreferredSize(new Dimension(152, 44));
        jButtonDialRedial.setIcon(resourceMap.getIcon("jButtonDialRedial.icon"));
        jButtonDialRedial.setRolloverIcon(resourceMap.getIcon("jButtonDialRedial.rolloverIcon"));                      
        jButtonDialRedial.setBorder(BorderFactory.createEmptyBorder());
        jButtonDialRedial.setBorderPainted(false);
        jButtonDialRedial.setContentAreaFilled(false);
        jButtonDialRedial.setDoubleBuffered(true);
        jButtonDialRedial.setFocusPainted(false);
        jButtonDialRedial.setHideActionText(true);
        jButtonDialRedial.setIconTextGap(0);
        jButtonDialRedial.setName("jButtonDialRedial"); // NOI18N
        jPanelBottom.add(jButtonDialRedial);

        jButtonDialCall.setPreferredSize(new Dimension(140, 44));
        jButtonDialCall.setIcon(resourceMap.getIcon("jButtonDialCall.icon"));
        jButtonDialCall.setRolloverIcon(resourceMap.getIcon("jButtonDialCall.rolloverIcon"));
        jButtonDialCall.setBorder(BorderFactory.createEmptyBorder());
        jButtonDialCall.setBorderPainted(false);
        jButtonDialCall.setContentAreaFilled(false);
        jButtonDialCall.setDoubleBuffered(true);
        jButtonDialCall.setFocusPainted(false);
        jButtonDialCall.setHideActionText(true);
        jButtonDialCall.setIconTextGap(0);
        jButtonDialCall.setName("jButtonDialCall"); // NOI18N
        jPanelBottom.add(jButtonDialCall);
        
        add(jPanelBottom, BorderLayout.SOUTH);
    }
    
    public JButton getjButtonDial0()
    {
        return jButtonDial0;
    }

    public JButton getjButtonDial1()
    {
        return jButtonDial1;
    }

    public JButton getjButtonDial2()
    {
        return jButtonDial2;
    }

    public JButton getjButtonDial3()
    {
        return jButtonDial3;
    }

    public JButton getjButtonDial4()
    {
        return jButtonDial4;
    }

    public JButton getjButtonDial5()
    {
        return jButtonDial5;
    }

    public JButton getjButtonDial6()
    {
        return jButtonDial6;
    }

    public JButton getjButtonDial7()
    {
        return jButtonDial7;
    }

    public JButton getjButtonDial8()
    {
        return jButtonDial8;
    }

    public JButton getjButtonDial9()
    {
        return jButtonDial9;
    }

    public JButton getjButtonDialCall()
    {
        return jButtonDialCall;
    }
    
    public JButton getjButtonDialVideoCall() {
        return jButtonDialVideoCall;
    }

    public JButton getjButtonDialDiez()
    {
        return jButtonDialDiez;
    }

    public JButton getjButtonDialRedial()
    {
        return jButtonDialRedial;
    }

    public JButton getjButtonDialStar()
    {
        return jButtonDialStar;
    }

	
}
