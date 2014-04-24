package org.ezuce.common.ui.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.ImageIcon;
import javax.swing.JTextField;

import org.ezuce.media.ui.WidgetBuilder;

public class CustomEditableComboxEditor implements ComboBoxEditor {

	private ImageIcon mBackgroundNormal;
	private ImageIcon mBackgroundHover;
	private Font mFont;
	private Color mForegroundColor;
	private JTextField mEditor;
	private Object oldValue;
	private String mDefaultText;

	public CustomEditableComboxEditor(ImageIcon backgroundNormal,
			ImageIcon backgroundHover, Font font, Color foregroundColor,
			String defaultText) {
		this.mDefaultText = defaultText;
		this.mBackgroundNormal = backgroundNormal;
		this.mBackgroundHover = backgroundHover;
		this.mFont = font;
		this.mForegroundColor = foregroundColor;
		this.mEditor = createEditorComponent();
	}

	protected JTextField createEditorComponent() {
		JTextField field = WidgetBuilder.createTextField(mBackgroundNormal,
				mBackgroundHover, mDefaultText, mFont, mForegroundColor, null);
		field.setBorder(BorderFactory.createCompoundBorder(field.getBorder(),
				BorderFactory.createEmptyBorder(2, 7, 2, 7)));
		return field;
	}

	@Override
	public Component getEditorComponent() {
		return mEditor;
	}

	@Override
	public void setItem(Object anObject) {
		if (anObject != null) {
			mEditor.setText(anObject.toString());
			oldValue = anObject;
		} else {
			mEditor.setText("");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getItem() {
		Object newValue = mEditor.getText();

		if (oldValue != null && !(oldValue instanceof String)) {
			// The original value is not a string. Should return the value in
			// it's
			// original type.
			if (newValue.equals(oldValue.toString())) {
				return oldValue;
			} else {
				// Must take the value from the editor and get the value and
				// cast it to the new type.
				Class cls = oldValue.getClass();
				try {
					Method method = cls.getMethod("valueOf",
							new Class[] { String.class });
					newValue = method.invoke(oldValue,
							new Object[] { mEditor.getText() });
				} catch (Exception ex) {
					// Fail silently and return the newValue (a String object)
				}
			}
		}
		return newValue;
	}

	@Override
	public void selectAll() {
		mEditor.selectAll();
		mEditor.requestFocus();
	}

	@Override
	public void addActionListener(ActionListener l) {
		mEditor.addActionListener(l);
	}

	@Override
	public void removeActionListener(ActionListener l) {
		mEditor.removeActionListener(l);
	}

}
