package org.ezuce.common.ui.panels;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.graphics.GraphicsUtilities;

public class Images
{
    String imageFileKey;
    private static final ResourceMap resourceMapImages = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(Images.class);

    public static ImageIcon userGreenSmall=resourceMapImages.getImageIcon("icon.small.genericUser.green");
    public static ImageIcon userRedSmall=resourceMapImages.getImageIcon("icon.small.genericUser.red");
    public static ImageIcon userRedLargeIcon=resourceMapImages.getImageIcon("icon.large.genericUser.red");

    public static ImageIcon userStatusAvailable=resourceMapImages.getImageIcon("icon.small.available");
    public static ImageIcon userStatusDnd=resourceMapImages.getImageIcon("icon.small.dnd");
    public static ImageIcon userStatusAway=resourceMapImages.getImageIcon("icon.small.away");
    public static ImageIcon userStatusOffline=resourceMapImages.getImageIcon("icon.small.offline");
    
    public static ImageIcon userStatusAvailableLarge=resourceMapImages.getImageIcon("status.icon.available");
    public static ImageIcon userStatusDndLarge=resourceMapImages.getImageIcon("status.icon.busy");
    public static ImageIcon userStatusAwayLarge=resourceMapImages.getImageIcon("status.icon.away");
    public static ImageIcon userStatusOfflineLarge=resourceMapImages.getImageIcon("status.icon.offline");

    public static ImageIcon searchFieldLensIcon=resourceMapImages.getImageIcon("searchField.icon");

    
    public Images()
    {

    }

    //Methods:

    public Icon getIcon()
    {
        return resourceMapImages.getIcon(imageFileKey);
    }

    public Icon getIcon(int width, int height)
    {
        Image im=resourceMapImages.getImageIcon(imageFileKey).getImage().getScaledInstance(width, height, Image.SCALE_FAST);
        return new ImageIcon(im);
    }

    public ImageIcon getImageIcon()
    {
        return resourceMapImages.getImageIcon(imageFileKey);
    }

    public static BufferedImage getImage(String imageFileName)
    {
      try
      {
          return ImageIO.read(ClassLoader.getSystemResourceAsStream(imageFileName));
      }
      catch (IOException e)
      {
          return null;
      }
    }

    public static BufferedImage getImage(String imageFileName, int width, int height)
    {
        return GraphicsUtilities.createThumbnail(getImage(imageFileName), width, height);
    }

    /**
     * Returns an Icon instance built from the ImageIcon received as an argument,
     * making sure that its size observes the specified width and height, while
     * also constraining the original's proportions. This method returns immediately,
     * even though the processing may take a while. When the resize is complete,
     * it informs the ImageObserver instance received as an argument.
     * The resizing is performed by scaling, if necessary the original image.
     * @param icon The original icon.
     * @param width The desired width.
     * @param height  The desired height.
     * @param io The image observer to update when the result is ready.
     * @return
     * @throws IOException
     */
    public static ImageIcon ensureSizeScaling(ImageIcon icon, int width, int height, ImageObserver io) throws IOException
    {
        BufferedImage bufferedImage=GraphicsUtilities.convertToBufferedImage(icon.getImage());
        BufferedImage thumbnail = Thumbnails.of(bufferedImage).crop(Positions.CENTER).size(width, height).asBufferedImage();
        
        /*
        Image img = icon.getImage();
        int icnW=icon.getIconWidth();
        int icnH=icon.getIconHeight();
        if (icnW>width || icnH>height)
        {
            BufferedImage bi=GraphicsUtilities.convertToBufferedImage(img);
            if (icnW>=icnH)
            {
                bi=GraphicsUtilities.createThumbnail(bi, width);
            }
            else
            {
                 bi=GraphicsUtilities.createThumbnail(bi, height);
            }
            img=null;
            return new ImageIcon(bi);
        }
        else
        {
            img=null;
            return icon;
        }
        */
        return new ImageIcon(thumbnail);
    }

    /**
     * Returns an Icon instance built from the ImageIcon received as an argument,
     * making sure that its size observes the specified width and height, while
     * also constraining the original's proportions. This method returns immediately,
     * even though the processing may take a while. When the resize is complete,
     * it informs the ImageObserver instance received as an argument.
     * The resizing is performed by cropping the original image.
     * @param icon The original icon.
     * @param width The desired width.
     * @param height  The desired height.
     * @param io The image observer to update when the result is ready.
     * @return
     * @throws IOException
     */
    public static Icon ensureSizeCropping(ImageIcon icon, int width, int height, ImageObserver io) throws IOException
    {

        Image img = icon.getImage();
        int icnW=icon.getIconWidth();
        int icnH=icon.getIconHeight();
        if (icnW>width || icnH>height)
        {
            BufferedImage bi=GraphicsUtilities.convertToBufferedImage(img);
            img=null;
            return new ImageIcon(bi.getSubimage(0, 0, width, height));
        }
        else
        {
            img=null;
            return icon;
        }
    }

    public static ImageIcon getImageIcon(URL imageUrl)
    {
        final ImageIcon im=new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageUrl));
            //Toolkit.getDefaultToolkit().prepareImage
        return im;
    }
    
    public static Image getImage(URL imageUrl)
    {
        return Toolkit.getDefaultToolkit().getImage(imageUrl);
    }
    
}//end enum Images
