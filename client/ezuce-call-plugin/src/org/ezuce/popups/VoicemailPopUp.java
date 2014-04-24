package org.ezuce.popups;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.beans.Beans;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.CounterLabel;
import org.ezuce.panels.VoiceMessageMiniPanel;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jivesoftware.spark.SparkManager;

/**
 *
 * @author Razvan
 */
public class VoicemailPopUp extends JPopupMenu
{

    private VoiceMessageMiniPanel voicemailMiniPanel;
    private final Color popupColor1=new Color(232,233,233,255);
    private final Color popupColor2=new Color(216,226,228,255);

    private JMenuItem markUnheardMenuItem;

    public VoicemailPopUp()
    {
        super();
        voicemailMiniPanel=null;
        initComponents();
    }

    public VoicemailPopUp(VoiceMessageMiniPanel vm)
    {
        super();
        voicemailMiniPanel=vm;
        initComponents();
    }

    public VoicemailPopUp(String label, VoiceMessageMiniPanel vm)
    {
        super(label);
        voicemailMiniPanel=vm;
        initComponents();
    }

    private void initComponents()
    {

        setBackground(new Color(240,240,240,180));
        setDoubleBuffered(true);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setBorderPainted(true);
        setOpaque(true);
        markUnheardMenuItem=new JMenuItem();
        markUnheardMenuItem.setFont(new Font("Arial", 0, 10));
        markUnheardMenuItem.setDoubleBuffered(true);
        markUnheardMenuItem.setOpaque(false);
        customize();
        this.add(markUnheardMenuItem);
    }

    private void customize()
    {
        if(voicemailMiniPanel==null) return;
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
        if (!voicemailMiniPanel.getVoicemail().isHeard())
        {
            markUnheardMenuItem.setForeground(Color.GRAY);
            markUnheardMenuItem.setAction(null);
            markUnheardMenuItem.setText(resourceMap.getString("menuItem.messageHeard.notHeardYet"));
            markUnheardMenuItem.setEnabled(false);
        }
        else
        {
            markUnheardMenuItem.setEnabled(true);
            markUnheardMenuItem.setForeground(Color.BLACK);
            ActionMap actionMap = Application.getInstance().getContext().getActionMap(this);
            javax.swing.Action action=actionMap.get("MarkVoicemailUnheard");
            setMarkMessageUnheardAction(action);
            markUnheardMenuItem.setText(resourceMap.getString("menuItem.messageHeard.alreadyHeard"));
        }
        this.revalidate();
    }
    
    @Override
    public void show(Component invoker, int x, int y) {
        this.customize();
        super.show(invoker, x, y);
    }
    

    @Override
    protected void paintComponent(Graphics g)
    {
        if (!isOpaque()){
            super.paintComponent(g);
            return;
        }

        final int w = getWidth( );
        final int h = getHeight( );
        LinearGradientPaint p=new LinearGradientPaint(0f, 0f, 0, h,
                                        new float[] {0.0f, 1.0f},
                                        new Color[] {popupColor1, popupColor2});
        Graphics2D g2d=(Graphics2D)g;
        g2d.setPaint(p);
        g2d.fillRect(0, 0, w, h );
    }


    public void setMarkMessageUnheardAction(javax.swing.Action action)
    {
        if (markUnheardMenuItem!=null)
        {
            markUnheardMenuItem.setAction(action);
        }
    }

	@Action(block = Task.BlockingScope.COMPONENT)
	public Task MarkVoicemailUnheard() {
		if (!Beans.isDesignTime()) {
			return new MarkVoicemailUnheardTask(
					org.jdesktop.application.Application.getInstance());
		} else {
			return null;
		}
	}
	
    private class MarkVoicemailUnheardTask extends org.jdesktop.application.Task<Boolean, Void> {
        MarkVoicemailUnheardTask(org.jdesktop.application.Application app) {
            //EDT - read GUI info:
            super(app);
        }
        @Override
        protected Boolean doInBackground() {
            //NOT EDT:
            Boolean result=Boolean.FALSE;
            try {
            	result=RestManager.getInstance().markVoicemailUnheard(VoicemailPopUp.this.voicemailMiniPanel.getVoicemail().getId());            	
            }
            catch(Exception e) {
                e.printStackTrace(System.err);
                return Boolean.FALSE;
            }
            return result;
        }
        @Override
        protected void succeeded(Boolean result) {          
            ResourceMap resourceMap = Application.getInstance().getContext().
                                    getResourceMap(VoicemailPopUp.class);
            VoicemailPopUp.this.voicemailMiniPanel.updateMessageHeard(Boolean.FALSE);
            if (!result) {
                JOptionPane.showMessageDialog(SparkManager.getMainWindow(),
                        resourceMap.getString("voicemail.markHeard.errorMessage"),
                        "Problem !", JOptionPane.ERROR_MESSAGE);
            }
            VoicemailPopUp.this.customize();
            CounterLabel.getMissedVoicemailsLabel().refresh();
        }               
    }

}
