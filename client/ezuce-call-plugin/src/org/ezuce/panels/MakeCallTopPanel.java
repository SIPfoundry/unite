package org.ezuce.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

import org.ezuce.common.ConfigureVoice;
import org.ezuce.common.ui.EzuceSearchField;
import org.ezuce.common.VoiceUtil;
import org.ezuce.media.manager.UIMediaManager;
import org.ezuce.paints.LinearGradientPainter;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXSearchField.LayoutStyle;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.painter.MattePainter;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * Displays the three toggle buttons at the top of the Call plugin, together
 * with a search field and a button to see the last called numbers.
 */
public class MakeCallTopPanel extends javax.swing.JPanel implements ConfigureVoice {
    private CallTabBtnPanel callTabBtnPanel;
    private DialPadPanel dialPadPanel;
    private JButton jButtonLastCalls;
    private JPanel jPanelDialPad;
    private JPanel jPanelSearch;
    private JXPanel jXPanelBackground;
    private JXSearchField jXSearchField;
    private MattePainter backgrounPainter=LinearGradientPainter.makeCallTopPanelPainter();

    /**
     * Performs customization of the components inside the panel.
     */
    protected void customize() {
        final Icon lensIcon=Images.searchFieldLensIcon;
        final JButton btn=jXSearchField.getFindButton();
        btn.setIcon(lensIcon);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    protected void initComponents() {//GEN-BEGIN:initComponents
        
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(MakeCallTopPanel.class);
        
        jXPanelBackground = new JXPanel();
        callTabBtnPanel = new CallTabBtnPanel();
        jPanelDialPad = new JPanel();
        dialPadPanel = new DialPadPanel();
        jPanelSearch = new JPanel();
        jXSearchField = new EzuceSearchField();
        jButtonLastCalls = new JButton();

        setMaximumSize(new Dimension(32767, 312));
        setMinimumSize(new Dimension(374, 312));
        setPreferredSize(new Dimension(374, 312));

        jXPanelBackground.setBackgroundPainter(backgrounPainter);
        //jXPanelBackground.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jXPanelBackground.setName("jXPanelBackground"); // NOI18N

        callTabBtnPanel.setName("callTabBtnPanel"); // NOI18N        
        callTabBtnPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        jPanelDialPad.setOpaque(false);
        jPanelDialPad.setMinimumSize(new Dimension(0, 0));
        jPanelDialPad.setName("jPanelDialPad"); // NOI18N
        jPanelDialPad.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));

        dialPadPanel.setName("dialPadPanel"); // NOI18N
        jPanelDialPad.add(dialPadPanel);

        jPanelSearch.setOpaque(false);
        jPanelSearch.setMinimumSize(new Dimension(100, 30));
        jPanelSearch.setName("jPanelSearch"); // NOI18N
        jPanelSearch.setPreferredSize(new Dimension(100, 30));
        jPanelSearch.setLayout(new BoxLayout(jPanelSearch, BoxLayout.LINE_AXIS));

        jXSearchField.setForeground(new Color(153, 153, 153));
        jXSearchField.setInstantSearchDelay(600);
        jXSearchField.setLayoutStyle(LayoutStyle.MAC);
        jXSearchField.setPrompt(resourceMap.getString("jXSearchField.prompt"));
        jXSearchField.setPromptFontStyle(Font.PLAIN);
        jXSearchField.setPromptForeground(new Color(204, 204, 204));
        jXSearchField.setUseNativeSearchFieldIfPossible(false);
        jXSearchField.setDoubleBuffered(true);
        jXSearchField.setFont(new Font("Arial", 0, 14));
        jXSearchField.setMargin(new Insets(2, 6, 2, 2));
        jXSearchField.setMinimumSize(new Dimension(320, 30));
        jXSearchField.setName("jXSearchField"); // NOI18N
        jXSearchField.setOpaque(false);
        jXSearchField.setPreferredSize(new Dimension(215, 30));
        jPanelSearch.add(jXSearchField);
        jXSearchField.setBorder(null);

        
        jButtonLastCalls.setIcon(resourceMap.getIcon("jButtonLastCalls.icon")); // NOI18N
        jButtonLastCalls.setToolTipText("Last calls");
        jButtonLastCalls.setBorder(null);
        jButtonLastCalls.setBorderPainted(false);
        jButtonLastCalls.setContentAreaFilled(false);
        jButtonLastCalls.setDoubleBuffered(true);
        jButtonLastCalls.setFocusPainted(false);
        jButtonLastCalls.setHideActionText(true);
        jButtonLastCalls.setName("jButtonLastCalls"); // NOI18N
        jButtonLastCalls.setRolloverIcon(resourceMap.getIcon("jButtonLastCalls.rolloverIcon")); // NOI18N
        jPanelSearch.add(jButtonLastCalls);
        
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jXPanelBackground, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jXPanelBackground, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        this.setBorder(new DropShadowBorder(Color.LIGHT_GRAY, 1, 0.3f, 1, false, true, true, true));
        try {
			VoiceUtil.configure(this);
		} catch (Exception e) {
			Log.error("Exception during config", e);
		}
    }

    public CallTabBtnPanel getCallTabBtnPanel() {
        return callTabBtnPanel;
    }

    public JButton getjButtonLastCalls() {
        return jButtonLastCalls;
    }

    public JXSearchField getjXSearchField() {
        return jXSearchField;
    }

    public DialPadPanel getDialPadPanel() {
        return this.dialPadPanel;
    }

    public JPanel getSearchFieldPanel() {
        return this.jPanelSearch;
    }

	@Override
	public void configureVoiceEnabled() {
        GroupLayout jXPanelBackgroundLayout = new GroupLayout(jXPanelBackground);
        jXPanelBackground.setLayout(jXPanelBackgroundLayout);
        JPanel audioPanel = UIMediaManager.getInstance().getCallPanelWrapper();
    	jXPanelBackgroundLayout.setHorizontalGroup(
    		jXPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
    		.addComponent (jPanelDialPad, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
    		.addComponent(callTabBtnPanel, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
    		.addGroup(jXPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
    		.addComponent(jPanelSearch, Alignment.TRAILING,  GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
    		.addComponent(audioPanel, Alignment.TRAILING,  GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)));
        jXPanelBackgroundLayout.setVerticalGroup(
            jXPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXPanelBackgroundLayout.createSequentialGroup() 
            .addComponent(callTabBtnPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(audioPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanelDialPad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(241, Short.MAX_VALUE)));		
	}

	@Override
	public void configureVoiceDisabled() {
		GroupLayout jXPanelBackgroundLayout = new GroupLayout(jXPanelBackground);
		jXPanelBackground.setLayout(jXPanelBackgroundLayout);
        jXPanelBackgroundLayout.setHorizontalGroup(
            jXPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addComponent (jPanelDialPad, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
            .addComponent(callTabBtnPanel, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
            .addGroup(jXPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addComponent(jPanelSearch, Alignment.TRAILING,  GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)));
        jXPanelBackgroundLayout.setVerticalGroup(
            jXPanelBackgroundLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(jXPanelBackgroundLayout.createSequentialGroup() 
            .addComponent(callTabBtnPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanelDialPad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(241, Short.MAX_VALUE)));
	}

}
