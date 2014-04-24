import java.io.IOException;

import org.ezuce.panel.MockPanelUI;

import com.ezuce.MainWindow;
import com.ezuce.PresenceManager;
import com.ezuce.tray.TrayManager;

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

		MockPanelUI panel = new MockPanelUI();

		MainWindow.setPanel(panel);

		TrayManager.getInstance().init();
		TrayManager.getInstance().update(PresenceManager.PRESENCES.get(1));
		TrayManager.getInstance().updateRegisteredAsPhone(false);

		panel.setVisible(true);
	}

}
