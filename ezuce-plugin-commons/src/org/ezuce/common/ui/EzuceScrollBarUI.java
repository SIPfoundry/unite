package org.ezuce.common.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class EzuceScrollBarUI extends BasicScrollBarUI {
	
	private ImageIcon scrollBarThumb=new ImageIcon(EzuceScrollBarUI.class.getResource("/resources/images/scrollThumb.png"));
	private ImageIcon scrollBarThumbCenter=new ImageIcon(EzuceScrollBarUI.class.getResource("/resources/images/scrollThumbCenter.png"));
	private ImageIcon scrollBarThumbLeft=new ImageIcon(EzuceScrollBarUI.class.getResource("/resources/images/scrollThumbLeft.png"));
	private ImageIcon scrollBarThumbRight=new ImageIcon(EzuceScrollBarUI.class.getResource("/resources/images/scrollThumbRight.png"));
	
	public EzuceScrollBarUI(){
		super();		
	}
	
	
	@Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		super.paintTrack(g, c, trackBounds);
		this.scrollbar.setOpaque(false);
    }
	
	

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    	g.translate(thumbBounds.x, thumbBounds.y);
        AffineTransform transform = AffineTransform.getScaleInstance((double)thumbBounds.width/scrollBarThumbCenter.getIconWidth(),
        															 (double)thumbBounds.height/scrollBarThumbCenter.getIconHeight());
        ((Graphics2D)g).drawImage(scrollBarThumbCenter.getImage(), transform, null);    	
        g.translate( -thumbBounds.x, -thumbBounds.y );        
    }
    
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override    
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton jbutton = new JButton();
        jbutton.setPreferredSize(new Dimension(0, 0));
        jbutton.setMinimumSize(new Dimension(0, 0));
        jbutton.setMaximumSize(new Dimension(0, 0));
        return jbutton;
    }
    public ImageIcon getScrollBarThumb(){
    	return scrollBarThumb;
    }
    
    public void setScrollBarThumb(ImageIcon ii){
    	scrollBarThumb=ii;
    }
    
}
