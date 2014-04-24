package org.ezuce.popups;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.ezuce.common.rest.Cdr;
import org.ezuce.panels.VoiceMessageMiniPanel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXSearchField;

/**
 * Displays a list of recent calls.
 */
public class LastCallsPopUp extends JPopupMenu implements ActionListener
{
    private JXSearchField fieldToPopulate;
    private Icon phoneIcon=null;
    private final Color popupColor1=new Color(255,255,255,255);
    private final Color popupColor2=new Color(218,218,218,255);
    private final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(LastCallsPopUp.class);

    public LastCallsPopUp()
    {
        super();
        initComponents();
    }

    public LastCallsPopUp(String label)
    {
        super(label);
        initComponents();
    }

    public LastCallsPopUp(List<Cdr> recentCalls, JXSearchField searchField)
    {
        super();
        this.fieldToPopulate=searchField;
        initComponents();
        showLastCalls(recentCalls);
    }

    public LastCallsPopUp(String label, List<Cdr> recentCalls, JXSearchField searchField)
    {
        super(label);
        this.fieldToPopulate=searchField;
        initComponents();
        showLastCalls(recentCalls);
    }

    private void initComponents()
    {
        ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
        phoneIcon=resourceMap.getImageIcon("phone.icon");
        setBackground(new Color(240,240,240,180));
        setDoubleBuffered(true);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setBorderPainted(true);
        setOpaque(true);
    }

    private void showLastCalls(List<Cdr> recentCalls)
    {
        for (Cdr call:recentCalls)
        {
            RecentCallMenuItem callItem=new RecentCallMenuItem(call.getCalleeName());
            callItem.setIcon(phoneIcon);
            callItem.setNumber(call.getCalleeName());
            callItem.addActionListener(this);
            callItem.setForeground(Color.BLACK);
            callItem.setFont(new Font("Arial", 0, 10));
            callItem.setDoubleBuffered(true);
            callItem.setOpaque(false);
            this.add(callItem);
        }
        if (recentCalls==null || recentCalls.isEmpty())
        {
            JMenuItem dummy=new JMenuItem();
            dummy.setText(resourceMap.getString("no.recent.calls"));
            this.add(dummy);
        }
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() instanceof RecentCallMenuItem)
        {
            if (this.fieldToPopulate!=null)
            {
                this.fieldToPopulate.setText(((RecentCallMenuItem)e.getSource()).getNumber());
            }
        }
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
}
