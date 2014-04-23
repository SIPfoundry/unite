package org.ezuce.common.ui.widget.builder;

import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.ezuce.common.ui.widget.CustomComboboxUI;
import org.ezuce.common.ui.widget.CustomEditableComboboxUI;

public class ComboboxBuilder extends AbstractWidgetBuilder {

	private JComboBox mCombobox;
	private ComboBoxModel mDataModel;
	private ImageIcon mArrowImage;
	private ImageIcon mBackgroundUIEditor;
	private ImageIcon mSeparatorIcon;
	private boolean mIsEditable;
	private String mDefaultText;

	public ComboboxBuilder() {
	}

	@Override
	protected JComponent buildComponent() {
		mCombobox = new JComboBox();

		if (mBackgroundUIEditor != null && mIsEditable) {
			mCombobox.setUI(new CustomEditableComboboxUI(mBackgroundUIEditor,
					mBackgroundHover, mSeparatorIcon, mArrowImage, mFont,
					mForegroundColor, mDefaultText));
			mCombobox.setEditable(true);

		} else if (mBackgroundUIEditor != null) {
			mCombobox.setUI(new CustomComboboxUI(mBackgroundUIEditor,
					mSeparatorIcon, mArrowImage, mFont, mForegroundColor));
			mCombobox.setEditable(true);
		}
		mCombobox.setModel(mDataModel);

		return mCombobox;
	}

	public void setArrowImage(ImageIcon arrowImage) {
		this.mArrowImage = arrowImage;
	}

	public void setDataModel(ComboBoxModel dataModel) {
		this.mDataModel = dataModel;
	}

	public void setBackgroundUIEditor(ImageIcon backgroundUIEditor) {
		this.mBackgroundUIEditor = backgroundUIEditor;
	}

	public void setSeparatorIcon(ImageIcon separatorIcon) {
		this.mSeparatorIcon = separatorIcon;
	}

	public void setEditable(boolean isEditable) {
		this.mIsEditable = isEditable;
	}

	@Override
	protected void repaintComponent() {
		mCombobox.repaint();
	}

	public void setDefaultText(String defaultText) {
		this.mDefaultText = defaultText;
	}

}
