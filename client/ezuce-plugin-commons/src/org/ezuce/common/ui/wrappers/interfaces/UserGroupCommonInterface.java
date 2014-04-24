/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ezuce.common.ui.wrappers.interfaces;

import java.awt.Component;

import javax.swing.JPopupMenu;

import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;

/**
 *
 * @author Razvan
 */
public interface UserGroupCommonInterface {
    public boolean isInsideRosterGroup();
    public Component getComponent();
    public void addRemoveFromGroupJMenuItem(ContactGroup contactGroup, JPopupMenu popup, ContactItem item);
    public void updateListInterval();
}
