package org.ezuce.panels.interfaces;

import org.ezuce.panels.FindMeMiniPanel;
import org.ezuce.panels.FindMeRosterPanel;

/**
 *
 * @author Razvan
 */
public interface FindMePanelInterface
{
    public void removeContactFromPanel(FindMeRosterPanel fmrp, FindMeMiniPanel fmmp);
    public void removeRosterPanel(FindMeRosterPanel fmrp);
    public boolean isFirstInRoster(FindMeRosterPanel fmrp);
}
