package org.ezuce.common;

import java.awt.Insets;

import javax.swing.Icon;

import org.jivesoftware.spark.component.RolloverButton;

public class EzuceRolloverButton extends RolloverButton {
	
	public EzuceRolloverButton() {
		super();
	}

    public EzuceRolloverButton(Icon icon) {
        super(icon);
    }
	
	@Override
    protected void decorate() {
        setBorderPainted(false);
        setOpaque(true);
        setHideActionText(true);
        setContentAreaFilled(false);
        setMargin(new Insets(1, 1, 1, 1));
    }

}
