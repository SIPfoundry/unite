import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ezuce.SparkManager;
import com.ezuce.history.HistoryWindow;

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
	 * @throws ParseException
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		// testDateFormat();
		launch();
	}

	private static void testDateFormat() throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.S z");

		Date d = dateFormat.parse("2013-02-06 16:12:58.559 NOVT");
		System.out.println("date=" + d);
	}

	private static void launch() {
		setLookAndFeel();
		SparkManager.setSessionJID("2067@openuc.ezuce.com");

		// String roomName = "dev@conference.openuc.ezuce.com";
		// String roomName = "2066@openuc.ezuce.com";
		String roomName = "mirceac@openuc.ezuce.com";
		try {
			HistoryWindow hw = new HistoryWindow(
					SparkManager.getUserDirectory(), roomName);
			hw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			hw.showWindow();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

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
