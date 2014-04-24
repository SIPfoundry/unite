import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.ezuce.im.ui.location.LocationManager;


/**
 * 
 */

/**
 * @author slava
 * 
 */
public class UITest {

	static {

	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		setLookAndFeel();
		LocationManager.show();
		// LoginDialogFrame f = new LoginDialogFrame();
		// f.setLocation(900, 150);
		// f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		// f.setVisible(true);
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
