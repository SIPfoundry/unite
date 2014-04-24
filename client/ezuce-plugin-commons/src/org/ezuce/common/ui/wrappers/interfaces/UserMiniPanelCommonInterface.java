/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.common.ui.wrappers.interfaces;

import java.awt.event.FocusEvent;

/**
 *
 * @author Razvan
 */
public interface UserMiniPanelCommonInterface
{
    public ContactListEntry getContact();
    public String getExtension();
    public String getJID();
    public String getUserDisplayName();
    public void focusGained(FocusEvent e);
    public void focusLost(FocusEvent e);
}
