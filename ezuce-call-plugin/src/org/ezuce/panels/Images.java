/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ezuce.panels;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jivesoftware.spark.util.log.Log;
import static org.ezuce.common.IOUtils.closeStreamQuietly;

public class Images
{
    String imageFileKey;
    private static final ResourceMap resourceMapImages = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(Images.class);
    private static final ResourceMap resourceMapReceivedCallMiniPanel = Application.getInstance().getContext().getResourceMap(ReceivedCallMiniPanel.class);
    private static final ResourceMap resourceMapMissedCallMiniPanel = Application.getInstance().getContext().getResourceMap(MissedCallMiniPanel.class);
    private static final ResourceMap resourceMapVoiceMessageMiniPanel = Application.getInstance().getContext().getResourceMap(VoiceMessageMiniPanel.class);
    private static final ResourceMap resourceMapDialledCallMiniPanel = Application.getInstance().getContext().getResourceMap(DialedCallMiniPanel.class);

    public static ImageIcon userGreenSmall=resourceMapImages.getImageIcon("icon.small.genericUser.green");
    public static ImageIcon userRedSmall=resourceMapImages.getImageIcon("icon.small.genericUser.red");
    public static ImageIcon userRedLargeIcon=resourceMapImages.getImageIcon("icon.large.genericUser.red");

    public static ImageIcon userStatusAvailable=resourceMapImages.getImageIcon("icon.small.available");
    public static ImageIcon userStatusDnd=resourceMapImages.getImageIcon("icon.small.dnd");
    public static ImageIcon userStatusAway=resourceMapImages.getImageIcon("icon.small.away");
    public static ImageIcon userStatusOffline=resourceMapImages.getImageIcon("icon.small.offline");

    public static ImageIcon searchFieldLensIcon=resourceMapImages.getImageIcon("searchField.icon");

    public static Icon receivedCallIcon=resourceMapReceivedCallMiniPanel.getIcon("jLabel.icon");
    public static Icon missedCallIcon=resourceMapMissedCallMiniPanel.getIcon("jLabel.icon");
    public static Icon dialledCallIcon=resourceMapDialledCallMiniPanel.getIcon("jLabel.icon");
    public static Icon voicemailIcon=resourceMapVoiceMessageMiniPanel.getIcon("jxLabelIconVoiceMessage.icon");

    public static Icon callPhoneIcon=resourceMapReceivedCallMiniPanel.getIcon("jLabelUsername.icon");
    public static Icon missedCallUserStatusIcon=resourceMapMissedCallMiniPanel.getIcon("jLabelUsername.icon");
    public static Icon voicemailUserStatusIcon=resourceMapVoiceMessageMiniPanel.getIcon("jLabelUsername.icon");

    public static Icon deleteItemIcon=resourceMapVoiceMessageMiniPanel.getIcon("jButtonDeleteMsg.icon");
    public static Icon deleteItemRolloverIcon=resourceMapVoiceMessageMiniPanel.getIcon("jButtonDeleteMsg.rolloverIcon");
    public static Icon callBackIcon=resourceMapReceivedCallMiniPanel.getIcon("jButtonCallBack.icon");
    public static Icon callBackRolloverIcon=resourceMapReceivedCallMiniPanel.getIcon("jButtonCallBack.rolloverIcon");
    public static Icon playVoicemailIcon=resourceMapVoiceMessageMiniPanel.getIcon("jButtonPlay.icon");
    public static Icon playVoicemailRolloverIcon=resourceMapVoiceMessageMiniPanel.getIcon("jButtonPlay.rolloverIcon");
    public static Icon stopVoicemailIcon=resourceMapVoiceMessageMiniPanel.getIcon("jButtonPlay.stopIcon");
    public static Icon stopVoicemailRolloverIcon=resourceMapVoiceMessageMiniPanel.getIcon("jButtonPlay.rolloverStopIcon");

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

    public static BufferedImage getImage(String imageFileName) {
    InputStream stream = null;    
      try {
    	  stream = ClassLoader.getSystemResourceAsStream(imageFileName);
          return ImageIO.read(stream);
      }
      catch (IOException e)
      {
          return null;
      }
      finally {
    	  closeStreamQuietly(stream);
      }
    }

    public static BufferedImage getImage(String imageFileName, int width, int height) {
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
     */
	public static Icon ensureSizeScaling(ImageIcon icon, int width, int height, ImageObserver io) {
		Image img = icon.getImage();
		int icnW = icon.getIconWidth();
		int icnH = icon.getIconHeight();
		if (icnW > width || icnH > height) {
			BufferedImage bi = GraphicsUtilities.convertToBufferedImage(img);
			if (icnW >= icnH) {
				bi = GraphicsUtilities.createThumbnail(bi, width);
			} else {
				bi = GraphicsUtilities.createThumbnail(bi, height);
			}
			img = null;
			return new ImageIcon(bi);
		} else {
			img = null;
			return icon;
		}
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
