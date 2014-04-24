package com.ezuce;

import java.awt.TrayIcon;

import javax.swing.ImageIcon;

import com.ezuce.SparkManager.Workspace;

public class SparkManager {

	private static Workspace workspace = new Workspace();

	public static class Workspace {

		private static Workspace instance = new Workspace();
		private StatusBar statusBar = new StatusBar();

		public StatusBar getStatusBar() {
			return statusBar;
		}

		public static Workspace getInstance() {
			return instance;
		}
	}

	public static Workspace getWorkspace() {
		return workspace;
	}

	public static SessionManager getSessionManager() {
		return SessionManager.getInstance();
	}

	public static ImageIcon getApplicationImage() {
		return GraphicUtils.createImageIcon("/resources/images/tray/communicator_24x24.png");
	}

	public static XMPPConnection getConnection() {
		return XMPPConnection.getInstance();
	}

	public static NativeManager getNativeManager() {
		return NativeManager.getInstance();
	}
}
