package org.ezuce.video.window;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class DragLocalVideoListener implements MouseMotionListener {

	private int px;
	private int py;
	private JLayeredPane layeredPane;
	private JPanel localVideoPanel;
	private JPanel localVideoCloseBtnPanel;

	public DragLocalVideoListener(JLayeredPane layeredPane,
			JPanel localVideoPanel, JPanel localVideoCloseBtnPanel) {
		this.layeredPane = layeredPane;
		this.localVideoPanel = localVideoPanel;
		this.localVideoCloseBtnPanel = localVideoCloseBtnPanel;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		px = e.getX();
		py = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// local video

		int xMax = layeredPane.getWidth() - localVideoPanel.getWidth();
		int yMax = layeredPane.getHeight() - localVideoPanel.getHeight();

		int x = e.getX();
		int y = e.getY();

		// close button
		int closeX = localVideoCloseBtnPanel.getLocation().x + x - px;
		int closeY = localVideoCloseBtnPanel.getLocation().y + y - py;
		closeX = Math.max(Math.min(closeX, xMax), 0);
		closeY = Math.max(Math.min(closeY, yMax), 0);
		localVideoCloseBtnPanel.setLocation(closeX, closeY);

		// the entire panel
		int panelX = localVideoPanel.getLocation().x + x - px;
		int panelY = localVideoPanel.getLocation().y + y - py;
		panelX = Math.max(Math.min(panelX, xMax), 0);
		panelY = Math.max(Math.min(panelY, yMax), 0);
		localVideoPanel.setLocation(panelX, panelY);
	}

}
