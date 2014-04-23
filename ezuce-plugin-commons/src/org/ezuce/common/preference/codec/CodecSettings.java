package org.ezuce.common.preference.codec;

import static org.ezuce.media.ui.GraphicUtils.createImageIcon;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

import net.sf.fmj.media.Log;

import org.ezuce.media.manager.PhoneManager;
import org.ezuce.media.ui.WidgetBuilder;
import org.ezuce.unitemedia.phone.EncodingDefaults;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.preference.Preference;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;

public class CodecSettings implements Preference {
	public static final String NAMESPACE = "http://www.jivesoftware.org/spark/codec";
	private static final String iconName = "/resources/images/prefs/codec";
	private ImageIcon activeImage = createImageIcon(iconName + "_on.png");
	private ImageIcon inactiveImage = createImageIcon(iconName + "_off.png");
	private ImageIcon titleImage = createImageIcon(iconName + ".png");

	private String title;

	private JComponent mPanel;
	private JComponent mCodecsPanel;
	private JComponent mCodecsAudioPanel;
	private JComponent mCodecsVideoPanel;
	private JComponent mH264Panel;
	
	private JCheckBox check_H264;
	private JCheckBox check_H263_1998;
	private JCheckBox check_VP8;
	private JCheckBox check_opus_48000;
	private JCheckBox check_SILK_24000;
	private JCheckBox check_SILK_16000;
	private JCheckBox check_G722_8000;
	private JCheckBox check_speex_32000;
	private JCheckBox check_speex_16000;
	private JCheckBox check_PCMU_8000;
	private JCheckBox check_PCMA_8000;
	private JCheckBox check_iLBC_8000;
	private JCheckBox check_GSM_8000;
	private JCheckBox check_speex_8000;
	private JCheckBox check_telephone_event_8000;
	private JCheckBox check_SILK_12000;
	private JCheckBox check_SILK_8000;
	
	private JComboBox comboH264Profile;
	private JComboBox comboH264ReqKeyFrames;
	private JComboBox comboH264Preset;
	private JCheckBox checkPeriodicRefreshH264;
	private JSpinner spinnerIntervalH264;

	public CodecSettings() {
		title = Res.getString("preferences.codec.title");
	}
	
	private JComponent createGUI() {
		JTabbedPane panel = new JTabbedPane();
		PhoneManager phoneManager = PhoneManager.getInstance();
		mCodecsPanel = new JPanel(new BorderLayout());
		mH264Panel = new JPanel(new GridBagLayout());			
		mCodecsAudioPanel = new JPanel(new GridBagLayout());
		mCodecsVideoPanel = new JPanel(new GridBagLayout());
		mCodecsAudioPanel.setBorder(BorderFactory.createTitledBorder(Res.getString("preferences.codec.audio.general")));
		mCodecsVideoPanel.setBorder(BorderFactory.createTitledBorder(Res.getString("preferences.codec.video.general")));
		mCodecsPanel.add(mCodecsAudioPanel, BorderLayout.WEST);
		mCodecsPanel.add(mCodecsVideoPanel, BorderLayout.CENTER);
		
		mH264Panel.setBorder(BorderFactory.createTitledBorder(Res.getString("preferences.codec.H264.general")));
		panel.add("Codecs", mCodecsPanel);
		panel.add("H264", mH264Panel);
		Insets insets = new Insets(0, 0, 0, 0);
		
		//codec checks
		check_H264 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.h264.check"));
		check_H264.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_H264_90000));
		check_H263_1998 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_H263_1998.check"));
		check_H263_1998.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_H263_1998_90000));
		check_VP8 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_VP8.check"));
		check_VP8.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_VP8_90000));
		check_opus_48000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_opus_48000.check"));
		check_opus_48000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_opus_48000));
		check_SILK_24000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_SILK_24000.check"));
		check_SILK_24000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_SILK_24000));
		check_SILK_16000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_SILK_16000.check"));
		check_SILK_16000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_SILK_16000));
		check_G722_8000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_G722_8000.check"));
		check_G722_8000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_G722_8000));
		check_speex_32000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_speex_32000.check"));
		check_speex_32000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_speex_32000));
		check_speex_16000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_speex_16000.check"));
		check_speex_16000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_speex_16000));
		check_PCMU_8000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_PCMU_8000.check"));
		check_PCMU_8000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_PCMU_8000));
		check_PCMA_8000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_PCMA_8000.check"));
		check_PCMA_8000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_PCMA_8000));
		check_iLBC_8000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_iLBC_8000.check"));
		check_iLBC_8000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_iLBC_8000));
		check_GSM_8000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_GSM_8000.check"));
		check_GSM_8000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_GSM_8000));
		check_speex_8000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_speex_8000.check"));
		check_speex_8000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_speex_8000));
		check_telephone_event_8000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_telephone_event_8000.check"));
		check_telephone_event_8000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_telephone_event_8000));
		check_SILK_12000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_SILK_12000.check"));
		check_SILK_12000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_SILK_12000));
		check_SILK_8000 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.check_SILK_8000.check"));	
		check_SILK_8000.setSelected(phoneManager.isEncodingActive(EncodingDefaults.KEY_SILK_8000));
		//H264 objects
		JLabel lblH264Profile = WidgetBuilder.createLabel(Res
				.getString("preferences.codec.h264.profile.label"));
		comboH264Profile = WidgetBuilder.createComboBox(new String[] {"baseline", "main", "high"});	
		comboH264Profile.setSelectedItem(phoneManager.getH264Profile());
		JLabel lblH264ReqKeyFrames = WidgetBuilder.createLabel(Res
				.getString("preferences.codec.h264.reqKeyFrames.label"));
		comboH264ReqKeyFrames = WidgetBuilder.createComboBox(new String[] {"rtcp", "signaling"});
		comboH264ReqKeyFrames.setSelectedItem(phoneManager.getH264KeyFrameRequester());
		JLabel lblH264Preset = WidgetBuilder.createLabel(Res
				.getString("preferences.codec.h264.preset.label"));
		comboH264Preset = WidgetBuilder.createComboBox(new String[] {"ultrafast", "superfast", "veryfast", "faster", 
				"fast", "medium", "slow", "slower" });
		comboH264Preset.setSelectedItem(phoneManager.getH264Preset());
		checkPeriodicRefreshH264 = WidgetBuilder.createCheckbox(Res
				.getString("preferences.codec.h264.periodicRefresh.check"));
		checkPeriodicRefreshH264.setSelected(phoneManager.getH264DefaultIntraRefresh());
		JLabel lblH264Interval = WidgetBuilder.createLabel(Res
				.getString("preferences.codec.h264.interval.label"));
		spinnerIntervalH264 = new JSpinner(new SpinnerNumberModel(150, 15, 50000, 15));
		spinnerIntervalH264.setValue(phoneManager.getH264KeyInt());
		
		//video codecs objects
		mCodecsVideoPanel.add(check_H264, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsVideoPanel.add(check_VP8, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));		
		mCodecsVideoPanel.add(check_H263_1998, new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		
		//audio codecs objects
		mCodecsAudioPanel.add(check_PCMA_8000, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_PCMU_8000, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_G722_8000, new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_opus_48000, new GridBagConstraints(0, 3, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_iLBC_8000, new GridBagConstraints(0, 4, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_GSM_8000, new GridBagConstraints(0, 5, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_SILK_16000, new GridBagConstraints(0, 6, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_SILK_24000, new GridBagConstraints(0, 7, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));		
		mCodecsAudioPanel.add(check_speex_32000, new GridBagConstraints(0, 8, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_speex_16000, new GridBagConstraints(0, 9, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_speex_8000, new GridBagConstraints(0, 10, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_telephone_event_8000, new GridBagConstraints(0, 11, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_SILK_12000, new GridBagConstraints(0, 12, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mCodecsAudioPanel.add(check_SILK_8000, new GridBagConstraints(0, 13, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		
		//H264
		mH264Panel.add(lblH264Profile, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mH264Panel.add(comboH264Profile, new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mH264Panel.add(lblH264ReqKeyFrames, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mH264Panel.add(comboH264ReqKeyFrames, new GridBagConstraints(1, 1, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));	
		mH264Panel.add(lblH264Preset, new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mH264Panel.add(comboH264Preset, new GridBagConstraints(1, 2, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mH264Panel.add(checkPeriodicRefreshH264, new GridBagConstraints(0, 3, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mH264Panel.add(lblH264Interval, new GridBagConstraints(0, 4, 1, 1, 0, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		mH264Panel.add(spinnerIntervalH264, new GridBagConstraints(1, 4, 1, 1, 1, 0,
				GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, insets,
				0, 0));
		return panel;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Icon getIcon() {
		return inactiveImage;
	}

	@Override
	public String getTooltip() {
		return title;
	}

	@Override
	public String getListName() {
		return title;
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public JComponent getGUI() {
		if (mPanel == null)
			mPanel = createGUI();

		return mPanel;
	}

	@Override
	public void load() {
		mPanel = (JTabbedPane) getGUI();
	}

	@Override
	public void commit() {
		PhoneManager phoneManager = PhoneManager.getInstance();
		Collection<String> codecsToDisable = new ArrayList<String>();
		if (!check_G722_8000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_G722_8000);
		} 
		if (!check_GSM_8000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_GSM_8000);
		}
		if (!check_H263_1998.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_H263_1998_90000);
		}
		if (!check_H264.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_H264_90000);
		}
		if (!check_iLBC_8000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_iLBC_8000);
		}
		if (!check_opus_48000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_opus_48000);
		}
		if (!check_PCMA_8000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_PCMA_8000);
		}
		if (!check_PCMU_8000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_PCMU_8000);
		}
		if (!check_SILK_12000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_SILK_12000);
		}
		if (!check_SILK_16000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_SILK_16000);
		}
		if (!check_SILK_24000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_SILK_24000);
		}
		if (!check_SILK_8000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_SILK_8000);
		}
		if (!check_speex_16000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_speex_16000);
		}
		if (!check_speex_32000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_speex_32000);
		}
		if (!check_speex_8000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_speex_8000);
		}
		if (!check_telephone_event_8000.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_telephone_event_8000);
		}
		if (!check_VP8.isSelected()) {
			codecsToDisable.add(EncodingDefaults.KEY_VP8_90000);
		}
		
		phoneManager.setH264Profile((String)comboH264Profile.getSelectedItem());
		phoneManager.setH264KeyFrameRequester((String)comboH264ReqKeyFrames.getSelectedItem());
		phoneManager.setH264Preset((String)comboH264Preset.getSelectedItem());
		phoneManager.setH264KeyInt((Integer)spinnerIntervalH264.getValue());
		phoneManager.setH264DefaultIntraRefresh(checkPeriodicRefreshH264.isSelected());
		phoneManager.saveConfiguration();
		phoneManager.overrideEncodings(codecsToDisable);
	}

	@Override
	public boolean isDataValid() {
		return true;
	}

	@Override
	public String getErrorMessage() {
		return "";
	}

	@Override
	public Object getData() {
		return SettingsManager.getLocalPreferences();
	}

	@Override
	public void shutdown() {
	}

	@Override
	public Icon getActiveIcon() {
		return activeImage;
	}

	@Override
	public Icon getTitleIcon() {
		return titleImage;
	}
}
