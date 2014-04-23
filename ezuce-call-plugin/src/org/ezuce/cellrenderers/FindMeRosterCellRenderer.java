/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.cellrenderers;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import org.ezuce.panels.FindMeMiniPanel;

/**
 *
 * @author Razvan
 */
public class FindMeRosterCellRenderer extends FindMeMiniPanel implements ListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)
    {
        return (FindMeMiniPanel)value;
    }
}
