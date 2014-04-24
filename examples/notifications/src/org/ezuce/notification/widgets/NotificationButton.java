/**
 * 
 */
package org.ezuce.notification.widgets;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

import com.ezuce.GraphicUtils;

/**
 * @author Vyacheslav Durin (nixspirit@gmail.com)
 * @version 0.1
 */
public class NotificationButton extends JButton {

	private static final int CLICKED_DARK_FACTOR = 60;
	private static final int HOVER_DARK_FACTOR = 30;

	private static final long serialVersionUID = -5956261489004186004L;

	private static final Image bg = GraphicUtils.createImageIcon(
			"/resources/images/call_btn.png").getImage();

	private static final Image bgHover;
	private static final Image bgClicked;
	private Image icoHover;
	private Image icoClicked;
	private Image currentBg = bg;
	private Image currentIco;

	static {
		bgHover = GraphicUtils.applyFilter(GraphicUtils.toBufferedImage(bg),
				GraphicUtils.darkenLUT(HOVER_DARK_FACTOR));
		bgClicked = GraphicUtils.applyFilter(GraphicUtils.toBufferedImage(bg),
				GraphicUtils.darkenLUT(CLICKED_DARK_FACTOR));
	}

	private Image ico;
	private Dimension size = new Dimension(79, 30);
	private int middleX;
	private int middleY;

	public NotificationButton(String ico) {
		this.ico = GraphicUtils.createImageIcon(ico).getImage();
		int iw = this.ico.getWidth(null);
		int ih = this.ico.getHeight(null);
		middleX = (int) (size.getWidth() / 2 - iw / 2);
		middleY = (int) (size.getHeight() / 2 - ih / 2);

		initIcons();
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setBorderPainted(false);

		addMouseListener(mouseListener());
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(currentBg, 0, 0, (int) size.getWidth(),
				(int) size.getHeight(), null);
		g2.drawImage(currentIco, middleX, middleY, null);
	}

	private MouseListener mouseListener() {
		return new MouseListener() {

			public void mouseReleased(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
				currentBg = bg;
				currentIco = ico;
				repaint();
			}

			public void mouseEntered(MouseEvent e) {
				currentBg = bgHover;
				currentIco = icoHover;
				repaint();
			}

			public void mouseClicked(MouseEvent e) {
				currentBg = bgClicked;
				currentIco = icoClicked;
				repaint();
			}
		};
	}

	private void initIcons() {
		currentIco = this.ico;
		icoHover = GraphicUtils.applyFilter(
				GraphicUtils.toBufferedImage(this.ico),
				GraphicUtils.darkenLUT(HOVER_DARK_FACTOR));
		icoClicked = GraphicUtils.applyFilter(
				GraphicUtils.toBufferedImage(this.ico),
				GraphicUtils.darkenLUT(CLICKED_DARK_FACTOR));
	}

}
