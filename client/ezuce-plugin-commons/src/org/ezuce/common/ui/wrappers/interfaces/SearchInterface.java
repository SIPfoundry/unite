package org.ezuce.common.ui.wrappers.interfaces;

import java.awt.Cursor;
import java.awt.event.MouseListener;

import org.ezuce.common.ui.panels.MakeCallPanel;
import org.jdesktop.swingx.JXSearchField;

/**
 *
 * @author Razvan
 */
public interface SearchInterface
{
    public JXSearchField getjXSearchField();

    public MakeCallPanel getMakeCallPanel();

    public void setCursor(Cursor c);
    
    public MouseListener getMouseListener();

}
