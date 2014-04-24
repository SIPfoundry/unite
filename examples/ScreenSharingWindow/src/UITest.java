import java.io.IOException;

import javax.media.Format;
import javax.media.PlugInManager;
import javax.media.format.VideoFormat;

import org.ezuce.panel.MockPanelUI;

/**
 * 
 */

/**
 * @author slava
 * 
 */
public class UITest {

	static {
		initCodecs();
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {

		MockPanelUI panel = new MockPanelUI();
		panel.setVisible(true);

	}

	private static void initCodecs() {
		Format[] inFormats = { new VideoFormat("MPEG") };

		PlugInManager.addPlugIn("net.sourceforge.jffmpeg.VideoDecoder",
				inFormats, null, PlugInManager.CODEC);

		try {
			PlugInManager.commit();
		} catch (IOException e) {
		}
	}

}
