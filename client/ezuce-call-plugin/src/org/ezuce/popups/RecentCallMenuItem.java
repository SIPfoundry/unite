
package org.ezuce.popups;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

/**
 *
 * @author Razvan
 */
public class RecentCallMenuItem extends JMenuItem{

    private String number;

    public RecentCallMenuItem()
    {
        super();
    }

    public RecentCallMenuItem(String text, int mnemonic)
    {
        super(text, mnemonic);
    }

    public RecentCallMenuItem(String text, Icon icon)
    {
        super(text, icon);
    }

    public RecentCallMenuItem(Action a)
    {
        super(a);
    }

    public RecentCallMenuItem(String text)
    {
        super(text);
    }

    public RecentCallMenuItem(Icon icon)
    {
        super(icon);
    }

    public void setNumber(String nr)
    {
        this.number=nr;
    }

    public String getNumber()
    {
        return number;
    }


}
