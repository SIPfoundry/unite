package org.ezuce.common;

import java.io.Closeable;
import org.jivesoftware.spark.util.log.Log;

public class IOUtils {
	public static void closeStreamQuietly(Closeable stream) {
		try {
			if (stream != null) {
				stream.close();
			}
		} catch (Exception ex) {
			Log.warning("Cannot close stream: "+ex.getMessage());
		}
	}
}
