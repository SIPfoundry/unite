package org.ezuce.lafs;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

/**
 *
 * @author Razvan
 */
public class EzuceVmSliderUI extends BasicSliderUI
{
        ResourceMap resourceMap = Application.getInstance().getContext().
                                    getResourceMap(EzuceVmSliderUI.class);
        ImageIcon knobImage;
        BufferedImage trackImage;
        BufferedImage trackBgImage;

        public EzuceVmSliderUI( JSlider aSlider ) {
            super( aSlider );
            try
            {
                trackBgImage=ImageIO.read(resourceMap.getClassLoader().getResource(resourceMap.getResourcesDir()+resourceMap.getString("slider.track.background")));
                trackImage=ImageIO.read(resourceMap.getClassLoader().getResource(resourceMap.getResourcesDir()+resourceMap.getString("slider.track")));
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace(System.err);
            }
            knobImage=resourceMap.getImageIcon("slider.knob");
        }
        @Override
        public void paintThumb(Graphics g)
        {
            Rectangle knobBounds = thumbRect;
            g.translate(knobBounds.x, knobBounds.y);
            knobImage.paintIcon(slider, g, 0, 0);
            g.translate(-knobBounds.x, -knobBounds.y);
        }
        @Override
        public Dimension getThumbSize()
        {
            Dimension d=new Dimension(knobImage.getIconWidth(), knobImage.getIconHeight());
            return d;
        }
        @Override
        public void paintTrack(Graphics g)
        {
            Graphics2D g2d=((Graphics2D)g);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

            Rectangle trackBounds = trackRect;
            int cy = (trackBounds.height / 2) - 2;

            //Trying to draw the thick BG:
            int valuePosition = xPositionForValue(slider.getValue());
	    int startX = valuePosition - (thumbRect.width / 2);
            try
            {
                BufferedImage partialBg=trackBgImage.getSubimage(0, 0, startX, trackBgImage.getHeight());
                BufferedImage partialTrack=trackImage.getSubimage(startX+1, 0, trackImage.getWidth()-startX-1, trackImage.getHeight());

                g.translate(trackBounds.x, trackBounds.y + cy);

                //Draw the actual track:
                g2d.drawImage(partialBg, 0, -1, slider);
                g2d.drawImage(partialTrack, startX+1, 0 , slider);


                g.translate(-trackBounds.x, -trackBounds.y-cy);
            }
            catch(Exception e){}
        }

        @Override
        public Dimension getPreferredHorizontalSize()
        {
            Dimension horizDim = new Dimension(trackImage.getWidth(), trackImage.getHeight());
            return horizDim;
        }

        @Override
        public Dimension getMinimumHorizontalSize()
        {
            return getPreferredHorizontalSize();
        }

        public Dimension getMaximumHorizontalSize()
        {
            return getPreferredHorizontalSize();
        }
}
