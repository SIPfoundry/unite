package org.ezuce.common.ui;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.swingx.JXSearchField;

/**
 *
 * @author Razvan
 */
public class EzuceSearchField extends JXSearchField
{
    private final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(EzuceSearchField.class);
    private final ImageIcon bgLeft;
    private final ImageIcon bgCenter;
    private final ImageIcon bgRight;

    public EzuceSearchField()
    {
        super();
        bgLeft=resourceMap.getImageIcon("searchFieldBg.left");
        bgCenter=resourceMap.getImageIcon("searchFieldBg.center");
        bgRight=resourceMap.getImageIcon("searchFieldBg.right");
        getOuterMargin().right=4;
        getCancelButton().setContentAreaFilled(false);
        getCancelButton().setIcon(null);
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
