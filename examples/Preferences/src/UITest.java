import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import org.jitsi.service.libjitsi.LibJitsi;

import com.ezuce.preferences.EzucePreferenceDialog;
import com.ezuce.preferences.PreferenceDataBuilder;

/**
 * 
 */

/**
 * @author slava
 * 
 */
public class UITest {

	static {
		setLookAndFeel();
		//LibJitsi.start();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {

		startUI();

	}

	private static void startUI() {
		EzucePreferenceDialog d = new EzucePreferenceDialog();
		d.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JFrame dialog = new JFrame();
		dialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		d.invoke(dialog, PreferenceDataBuilder.buildPreferensCategories(),
				PreferenceDataBuilder.buildPreferensCategories().next());
	}

	private static void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
