package org.ezuce.popups;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jivesoftware.resource.Res;

/**
 *
 * @author Razvan
 */
public class FrequencyPopup extends JPopupMenu implements ActionListener
{
    private final Color popupColor1=new Color(255,255,255,255);
    private final Color popupColor2=new Color(218,218,218,255);
    private final Color foregroundColor=new Color(153,153,153,255);
    public enum Frequency { Always};

    public FrequencyPopup()
    {
        super();
        initComponents();
    }

    private void initComponents()
    {
        JMenuItem jmi=new JMenuItem(Res.getString("findme.followme.always"));
        jmi.setForeground(foregroundColor);
        jmi.setActionCommand(Frequency.Always.name());
        jmi.addActionListener(this);
        this.add(jmi);

        setBackground(new Color(240,240,240,180));
        setDoubleBuffered(true);
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setBorderPainted(true);
        setOpaque(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        final Component inv=this.getInvoker();
        if (inv instanceof JButton)
        {
            final JButton parent=(JButton)inv;
            parent.setText(Res.getString("findme.followme.always"));
            parent.setActionCommand(e.getActionCommand());
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
