package org.ezuce.common.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXSearchField;


public class EzuceContactProfileField extends JXSearchField {
		
    private final ImageIcon bgLeft;
    private final ImageIcon bgCenter;
    private final ImageIcon bgRight;

    public EzuceContactProfileField(ImageIcon findIcon, ImageIcon cancelIcon, ImageIcon cancelIconHover)
    {
        super();
        bgLeft=new ImageIcon(getClass().getResource("/resources/images/FindContactSearchFieldBg_left.png"));
        bgCenter=new ImageIcon(getClass().getResource("/resources/images/FindContactSearchFieldBg_center.png"));
        bgRight=new ImageIcon(getClass().getResource("/resources/images/FindContactSearchFieldBg_right.png"));
        
        if (findIcon!=null){
        	final JButton btn = getFindButton();
        	btn.setText("     ");
        	btn.setHorizontalTextPosition(SwingConstants.LEADING);
        	btn.setIcon(findIcon);
        	btn.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
        }
        
        if (cancelIcon!=null){
        	final JButton btn = getCancelButton();
        	btn.setText(" ");
        	btn.setHorizontalTextPosition(SwingConstants.TRAILING);
        	btn.setIcon(cancelIcon); 
        	btn.setPressedIcon(cancelIcon);
        }
        
        if (cancelIconHover!=null){
        	final JButton btn = getCancelButton();        	
        	btn.setRolloverIcon(cancelIconHover);        	
        }
        
       
        getOuterMargin().right=4;
        getCancelButton().setContentAreaFilled(false);        
        setLayoutStyle(LayoutStyle.MAC);
        setUseNativeSearchFieldIfPossible(false);
        setDoubleBuffered(true);
        setMargin(new Insets(0, 10, 0, 2));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        setPrompt("");
        setOpaque(false);
        setMaximumSize(new Dimension(32000, 30));
        setMinimumSize(new Dimension(100,30));
        setPreferredSize(null);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(bgLeft.getImage(), 0, 0, bgLeft.getIconWidth(), getHeight(), 0, 0, bgLeft.getIconWidth(),
                        bgLeft.getIconHeight(), null);
        g.drawImage(bgCenter.getImage(), bgLeft.getIconWidth(), 0, getWidth() - bgRight.getIconWidth(), getHeight(), 0,
                        0, bgCenter.getIconWidth(), bgCenter.getIconHeight(), null);
        g.drawImage(bgRight.getImage(), getWidth() - bgRight.getIconWidth(), 0, getWidth(), getHeight(), 0, 0,
                        bgRight.getIconWidth(), bgRight.getIconHeight(), null);
        super.paintComponent(g);
    }


}