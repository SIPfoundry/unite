/**
 * 
 */
package org.ezuce.media.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.ShortLookupTable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.jivesoftware.spark.util.log.Log;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public class GraphicUtils {

	/**
	 * Returns a scaled down image if the height or width is smaller than the
	 * image size.
	 * 
	 * @param icon
	 *            the image icon.
	 * @param newHeight
	 *            the preferred height.
	 * @param newWidth
	 *            the preferred width.
	 * @return the icon.
	 */
	public static ImageIcon scale(ImageIcon icon, int newHeight, int newWidth) {
		Image img = icon.getImage();
		int height = icon.getIconHeight();
		int width = icon.getIconWidth();
		boolean scaleHeight = height * newWidth > width * newHeight;
		if (height > newHeight) {
			// Too tall
			if (width <= newWidth || scaleHeight) {
				// Width is okay or height is limiting factor due to aspect
				// ratio
				height = newHeight;
				width = -1;
			} else {
				// Width is limiting factor due to aspect ratio
				height = -1;
				width = newWidth;
			}
		} else if (width > newWidth) {
			// Too wide and height is okay
			height = -1;
			width = newWidth;
		} else if (scaleHeight) {
			// Height is limiting factor due to aspect ratio
			height = newHeight;
			width = -1;
		} else {
			// Width is limiting factor due to aspect ratio
			height = -1;
			width = newWidth;
		}
		img = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(img);
	}

	public static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = GraphicUtils.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, path);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public static LookupTable darkenLUT(int factor) {
		short brighten[] = new short[256];
		for (int i = 0; i < 256; i++) {
			short pixelValue = (short) (i - factor);
			if (pixelValue > 255)
				pixelValue = 255;
			else if (pixelValue < 0)
				pixelValue = 0;
			brighten[i] = pixelValue;
		}
		return new ShortLookupTable(0, brighten);
	}

	public static Image applyFilter(BufferedImage img1, LookupTable table) {
		LookupOp lop = new LookupOp(table, null);
		return lop.filter(img1, null);
	}

	public static BufferedImage toBufferedImage(Image img) {
		BufferedImage bmp = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bmp.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
		return bmp;
	}

	public static List<Component> getChildrenRecursively(final Container c) {
		Component[] comps = c.getComponents();
		List<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			compList.addAll(getChildrenRecursively((Container) comp));
		}
		return compList;
	}

	public static boolean isMac() {
		String lcOSName = System.getProperty("os.name").toLowerCase();
		return lcOSName.indexOf("mac") != -1;
	}
	
	public static Dimension getScaledDimension(Dimension originalSize, Dimension limitSize) {

	    int originalWidth = originalSize.width;
	    int originalHeight = originalSize.height;
	    int limitWidth = limitSize.width;
	    int limitHeight = limitSize.height;
	    int newWidth = originalWidth;
	    int newHeight = originalHeight;
        float originalRatio = (float)originalHeight/originalWidth;
        float limitRatio = (float)limitHeight/limitWidth;
        Log.error("VVVVVVVVVV " + originalWidth + " CC " +originalHeight + " BB " + limitWidth + " BBV " + limitHeight);
        Log.error("GIGEA    >>> " + originalRatio + " CC " + limitRatio);
	    if (originalWidth > limitWidth || newHeight > limitHeight) {
	    	//newHeight = (limitHeight * limitWidth) / originalWidth;
	    	if (originalRatio > limitRatio) {
	    		newHeight = limitHeight;
	    		newWidth = Math.round((limitWidth*limitRatio)/originalRatio);
	    		Log.error("HEIGHT 1********** " + newHeight + " XX " + newWidth);
	    	} else {
	    		newHeight = Math.round((limitHeight * originalRatio)/limitRatio);
	    		newWidth = limitWidth;
	    		Log.error("HEIGHT 12********** " + newHeight + " XX " + newWidth);
	    	}
	    }

	    return new Dimension(newWidth, newHeight);
	}	

}