package org.ezuce.common.ui.panels;

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GroupTitleJPanel extends JPanel {
	private boolean open=true;
	private static javax.swing.ImageIcon bgOpen = 
			new javax.swing.ImageIcon(UserGroupPanel.class.getResource("/resources/images/contact-group_open_bg.png"));
	private static javax.swing.ImageIcon bgClosed = 
			new javax.swing.ImageIcon(UserGroupPanel.class.getResource("/resources/images/contact-group_closed_bg.png"));
	private static javax.swing.ImageIcon btnBgOpen = 
			new javax.swing.ImageIcon(UserGroupPanel.class.getResource("/resources/images/contact-group_open_btn.png"));
	private static javax.swing.ImageIcon btnBgClosed = 
			new javax.swing.ImageIcon(UserGroupPanel.class.getResource("/resources/images/contact-group_closed_btn.png"));
	
	public GroupTitleJPanel(){
		super();
	}
	
	public void setOpen(boolean o){
		open=o;
		SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {
				GroupTitleJPanel.this.revalidate();
				GroupTitleJPanel.this.repaint();
			}
		});
		
	}
	
	public boolean isOpen(){
		return open;
	}
	
	@Override
    public void paintComponent(Graphics g)
    {
		if (!isOpen()){
//			g.drawImage(bgClosed.getImage(), 0, 0, getWidth()-btnBgClosed.getIconWidth(), getHeight(), 
//						0, 0, bgClosed.getIconWidth(), bgClosed.getIconHeight(), null);
			g.drawImage(bgClosed.getImage(), 0, 0, getWidth(), getHeight(), 
					0, 0, bgClosed.getIconWidth(), bgClosed.getIconHeight(), null);
		}
		else{
//			g.drawImage(bgOpen.getImage(), 0, 0, getWidth()-btnBgOpen.getIconWidth(), getHeight(), 
//						0, 0, bgOpen.getIconWidth(), bgOpen.getIconHeight(), null);
			g.drawImage(bgOpen.getImage(), 0, 0, getWidth(), getHeight(), 
					0, 0, bgOpen.getIconWidth(), bgOpen.getIconHeight(), null);
		}

        //super.paintComponent(g);
    }	

}
