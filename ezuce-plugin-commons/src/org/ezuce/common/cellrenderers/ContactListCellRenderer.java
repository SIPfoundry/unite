/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.common.cellrenderers;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.ezuce.common.ui.panels.UserMiniPanelGloss;


/**
 *
 * @author Razvan
 */
public class ContactListCellRenderer extends UserMiniPanelGloss implements ListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)
    {
        final UserMiniPanelGloss umpg = (UserMiniPanelGloss)value;
        if (isSelected)
        {
            umpg.focusGained(null);
        }
        else
        {
            umpg.focusLost(null);
        }
        return umpg;
    }

}
