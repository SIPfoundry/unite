package org.ezuce.common.ui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;



public class AlphabetFilterPanel extends JPanel {
	
	private Dimension MIN_SIZE=new Dimension(28,92);	
	private Dimension MAX_SIZE=new Dimension(28,32600);
	private final int VERTICAL_UNIT_INCREMENT=20;
	private final int VERTICAL_STRUT_HEIGHT=2;
	
	private Timer timerUpScroll;
    private Timer timerDownScroll;
	
	private JButton btnScrollup;
	private JButton btnScrolldown;
	private JScrollPane scrollPane;
	private JPanel lettersPanel;
	private JToggleButton btnA;	
	private JToggleButton btnB;
	private JToggleButton btnC;
	private JToggleButton btnD;
	private JToggleButton btnE;
	private JToggleButton btnF;
	private JToggleButton btnG;
	private JToggleButton btnH;
	private JToggleButton btnI;
	private JToggleButton btnJ;
	private JToggleButton btnK;
	private JToggleButton btnL;
	private JToggleButton btnM;
	private JToggleButton btnN;
	private JToggleButton btnO;
	private JToggleButton btnP;
	private JToggleButton btnQ;
	private JToggleButton btnR;
	private JToggleButton btnS;
	private JToggleButton btnT;
	private JToggleButton btnU;
	private JToggleButton btnV;
	private JToggleButton btnW;
	private JToggleButton btnX;
	private JToggleButton btnY;
	private JToggleButton btnZ;
	private JToggleButton btnDiez;

	/**
	 * Create the panel.
	 */
	public AlphabetFilterPanel() {
			
		initComponents();

	}
	
	protected void initComponents(){
		
		setBackground(Color.WHITE);
		setLayout(new BorderLayout(0, 0));
		setMinimumSize(MIN_SIZE);
		setMaximumSize(MAX_SIZE);
		
		btnScrollup = new JButton("");
		btnScrollup.setRolloverIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/upscroll-over.png")));
		btnScrollup.setBorder(null);
		btnScrollup.setBorderPainted(false);
		btnScrollup.setContentAreaFilled(false);
		btnScrollup.setDoubleBuffered(true);
		btnScrollup.setFocusPainted(false);
		btnScrollup.setHideActionText(true);
		btnScrollup.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/upscroll.png")));
		btnScrollup.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButtonLeftMouseReleased(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonLeftMousePressed(evt);
            }
        });
		add(btnScrollup, BorderLayout.NORTH);
		
		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBorder(null);
		scrollPane.setMaximumSize(new Dimension(26, 32767));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(VERTICAL_UNIT_INCREMENT);		
		add(scrollPane, BorderLayout.CENTER);
		add(Box.createHorizontalStrut(2), BorderLayout.WEST);
		
		lettersPanel = new JPanel();
		lettersPanel.setBackground(Color.WHITE);
		lettersPanel.setMaximumSize(new Dimension(26, 32767));
		scrollPane.setViewportView(lettersPanel);
		lettersPanel.setLayout(new BoxLayout(lettersPanel, BoxLayout.Y_AXIS));
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnA = new JToggleButton("");
		btnA.setActionCommand("a");
		btnA.setBorder(null);
		btnA.setBorderPainted(false);
		btnA.setContentAreaFilled(false);
		btnA.setDoubleBuffered(true);
		btnA.setFocusPainted(false);
		btnA.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/A-on.png")));
		btnA.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/A-off.png")));
		lettersPanel.add(btnA);			
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnB = new JToggleButton("");	
		btnB.setActionCommand("b");
		btnB.setBorder(null);
		btnB.setBorderPainted(false);
		btnB.setContentAreaFilled(false);
		btnB.setDoubleBuffered(true);
		btnB.setFocusPainted(false);
		btnB.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/B-on.png")));
		btnB.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/B-off.png")));
		lettersPanel.add(btnB);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnC = new JToggleButton("");
		btnC.setActionCommand("c");
		btnC.setBorder(null);
		btnC.setBorderPainted(false);
		btnC.setContentAreaFilled(false);
		btnC.setDoubleBuffered(true);
		btnC.setFocusPainted(false);
		btnC.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/C-on.png")));
		btnC.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/C-off.png")));
		lettersPanel.add(btnC);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnD = new JToggleButton("");
		btnD.setActionCommand("d");
		btnD.setBorder(null);
		btnD.setBorderPainted(false);
		btnD.setContentAreaFilled(false);
		btnD.setDoubleBuffered(true);
		btnD.setFocusPainted(false);
		btnD.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/D-on.png")));
		btnD.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/D-off.png")));
		lettersPanel.add(btnD);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnE = new JToggleButton("");
		btnE.setActionCommand("e");
		btnE.setBorder(null);
		btnE.setBorderPainted(false);
		btnE.setContentAreaFilled(false);
		btnE.setDoubleBuffered(true);
		btnE.setFocusPainted(false);
		btnE.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/E-on.png")));
		btnE.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/E-off.png")));
		lettersPanel.add(btnE);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnF = new JToggleButton("");
		btnF.setActionCommand("f");
		btnF.setBorder(null);
		btnF.setBorderPainted(false);
		btnF.setContentAreaFilled(false);
		btnF.setDoubleBuffered(true);
		btnF.setFocusPainted(false);
		btnF.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/F-on.png")));
		btnF.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/F-off.png")));
		lettersPanel.add(btnF);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnG = new JToggleButton("");
		btnG.setActionCommand("g");
		btnG.setBorder(null);
		btnG.setBorderPainted(false);
		btnG.setContentAreaFilled(false);
		btnG.setDoubleBuffered(true);
		btnG.setFocusPainted(false);
		btnG.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/G-on.png")));
		btnG.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/G-off.png")));
		lettersPanel.add(btnG);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnH = new JToggleButton("");
		btnH.setActionCommand("h");
		btnH.setBorder(null);
		btnH.setBorderPainted(false);
		btnH.setContentAreaFilled(false);
		btnH.setDoubleBuffered(true);
		btnH.setFocusPainted(false);
		btnH.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/H-on.png")));
		btnH.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/H-off.png")));
		lettersPanel.add(btnH);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnI = new JToggleButton("");
		btnI.setActionCommand("i");
		btnI.setBorder(null);
		btnI.setBorderPainted(false);
		btnI.setContentAreaFilled(false);
		btnI.setDoubleBuffered(true);
		btnI.setFocusPainted(false);
		btnI.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/I-on.png")));
		btnI.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/I-off.png")));
		lettersPanel.add(btnI);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnJ = new JToggleButton("");
		btnJ.setActionCommand("j");
		btnJ.setBorder(null);
		btnJ.setBorderPainted(false);
		btnJ.setContentAreaFilled(false);
		btnJ.setDoubleBuffered(true);
		btnJ.setFocusPainted(false);
		btnJ.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/J-on.png")));
		btnJ.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/J-off.png")));
		lettersPanel.add(btnJ);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnK = new JToggleButton("");
		btnK.setActionCommand("k");
		btnK.setBorder(null);
		btnK.setBorderPainted(false);
		btnK.setContentAreaFilled(false);
		btnK.setDoubleBuffered(true);
		btnK.setFocusPainted(false);
		btnK.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/K-on.png")));
		btnK.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/K-off.png")));
		lettersPanel.add(btnK);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnL = new JToggleButton("");
		btnL.setActionCommand("l");
		btnL.setBorder(null);
		btnL.setBorderPainted(false);
		btnL.setContentAreaFilled(false);
		btnL.setDoubleBuffered(true);
		btnL.setFocusPainted(false);
		btnL.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/L-on.png")));
		btnL.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/L-off.png")));
		lettersPanel.add(btnL);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnM = new JToggleButton("");
		btnM.setActionCommand("m");
		btnM.setBorder(null);
		btnM.setBorderPainted(false);
		btnM.setContentAreaFilled(false);
		btnM.setDoubleBuffered(true);
		btnM.setFocusPainted(false);
		btnM.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/M-on.png")));
		btnM.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/M-off.png")));
		lettersPanel.add(btnM);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnN = new JToggleButton("");
		btnN.setActionCommand("n");
		btnN.setBorder(null);
		btnN.setBorderPainted(false);
		btnN.setContentAreaFilled(false);
		btnN.setDoubleBuffered(true);
		btnN.setFocusPainted(false);
		btnN.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/N-on.png")));
		btnN.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/N-off.png")));
		lettersPanel.add(btnN);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnO = new JToggleButton("");
		btnO.setActionCommand("o");
		btnO.setBorder(null);
		btnO.setBorderPainted(false);
		btnO.setContentAreaFilled(false);
		btnO.setDoubleBuffered(true);
		btnO.setFocusPainted(false);
		btnO.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/O-on.png")));
		btnO.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/O-off.png")));
		lettersPanel.add(btnO);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnP = new JToggleButton("");
		btnP.setActionCommand("p");
		btnP.setBorder(null);
		btnP.setBorderPainted(false);
		btnP.setContentAreaFilled(false);
		btnP.setDoubleBuffered(true);
		btnP.setFocusPainted(false);
		btnP.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/P-on.png")));
		btnP.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/P-off.png")));
		lettersPanel.add(btnP);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnQ = new JToggleButton("");
		btnQ.setActionCommand("q");
		btnQ.setBorder(null);
		btnQ.setBorderPainted(false);
		btnQ.setContentAreaFilled(false);
		btnQ.setDoubleBuffered(true);
		btnQ.setFocusPainted(false);
		btnQ.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/Q-on.png")));
		btnQ.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/Q-off.png")));
		lettersPanel.add(btnQ);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnR = new JToggleButton("");
		btnR.setActionCommand("r");
		btnR.setBorder(null);
		btnR.setBorderPainted(false);
		btnR.setContentAreaFilled(false);
		btnR.setDoubleBuffered(true);
		btnR.setFocusPainted(false);
		btnR.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/R-on.png")));
		btnR.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/R-off.png")));
		lettersPanel.add(btnR);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnS = new JToggleButton("");
		btnS.setActionCommand("s");
		btnS.setBorder(null);
		btnS.setBorderPainted(false);
		btnS.setContentAreaFilled(false);
		btnS.setDoubleBuffered(true);
		btnS.setFocusPainted(false);
		btnS.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/S-on.png")));
		btnS.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/S-off.png")));
		lettersPanel.add(btnS);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnT = new JToggleButton("");
		btnT.setActionCommand("t");
		btnT.setBorder(null);
		btnT.setBorderPainted(false);
		btnT.setContentAreaFilled(false);
		btnT.setDoubleBuffered(true);
		btnT.setFocusPainted(false);
		btnT.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/T-on.png")));
		btnT.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/T-off.png")));
		lettersPanel.add(btnT);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnU = new JToggleButton("");
		btnU.setActionCommand("u");
		btnU.setBorder(null);
		btnU.setBorderPainted(false);
		btnU.setContentAreaFilled(false);
		btnU.setDoubleBuffered(true);
		btnU.setFocusPainted(false);
		btnU.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/U-on.png")));
		btnU.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/U-off.png")));
		lettersPanel.add(btnU);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnV = new JToggleButton("");
		btnV.setActionCommand("v");
		btnV.setBorder(null);
		btnV.setBorderPainted(false);
		btnV.setContentAreaFilled(false);
		btnV.setDoubleBuffered(true);
		btnV.setFocusPainted(false);
		btnV.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/V-on.png")));
		btnV.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/V-off.png")));
		lettersPanel.add(btnV);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnW = new JToggleButton("");
		btnW.setActionCommand("w");
		btnW.setBorder(null);
		btnW.setBorderPainted(false);
		btnW.setContentAreaFilled(false);
		btnW.setDoubleBuffered(true);
		btnW.setFocusPainted(false);
		btnW.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/W-on.png")));
		btnW.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/W-off.png")));
		lettersPanel.add(btnW);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnX = new JToggleButton("");
		btnX.setActionCommand("x");
		btnX.setBorder(null);
		btnX.setBorderPainted(false);
		btnX.setContentAreaFilled(false);
		btnX.setDoubleBuffered(true);
		btnX.setFocusPainted(false);
		btnX.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/X-on.png")));
		btnX.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/X-off.png")));
		lettersPanel.add(btnX);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnY = new JToggleButton("");
		btnY.setActionCommand("y");
		btnY.setBorder(null);
		btnY.setBorderPainted(false);
		btnY.setContentAreaFilled(false);
		btnY.setDoubleBuffered(true);
		btnY.setFocusPainted(false);
		btnY.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/Y-on.png")));
		btnY.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/Y-off.png")));
		lettersPanel.add(btnY);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnZ = new JToggleButton("");
		btnZ.setActionCommand("z");
		btnZ.setBorder(null);
		btnZ.setBorderPainted(false);
		btnZ.setContentAreaFilled(false);
		btnZ.setDoubleBuffered(true);
		btnZ.setFocusPainted(false);
		btnZ.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/Z-on.png")));
		btnZ.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/Z-off.png")));
		lettersPanel.add(btnZ);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnDiez = new JToggleButton("");
		btnDiez.setActionCommand("#");
		btnDiez.setBorder(null);
		btnDiez.setBorderPainted(false);
		btnDiez.setContentAreaFilled(false);
		btnDiez.setDoubleBuffered(true);
		btnDiez.setFocusPainted(false);
		btnDiez.setSelectedIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/#-on.png")));
		btnDiez.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/#-off.png")));
		lettersPanel.add(btnDiez);
		
		lettersPanel.add(Box.createVerticalStrut(VERTICAL_STRUT_HEIGHT));
		
		btnScrolldown = new JButton("");
		btnScrolldown.setRolloverIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/downscroll-over.png")));
		btnScrolldown.setHideActionText(true);
		btnScrolldown.setFocusPainted(false);
		btnScrolldown.setContentAreaFilled(false);
		btnScrolldown.setDoubleBuffered(true);
		btnScrolldown.setBorder(null);
		btnScrolldown.setBorderPainted(false);
		btnScrolldown.setIcon(new ImageIcon(AlphabetFilterPanel.class.getResource("/resources/images/alphabet/downscroll.png")));
		btnScrolldown.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButtonRightMouseReleased(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButtonRightMousePressed(evt);
            }
        });
		add(btnScrolldown, BorderLayout.SOUTH);
		
		initTimers();
	}
	
	private void jButtonLeftMouseReleased(MouseEvent evt) {                                            
        if (this.timerUpScroll.isRunning())
        {
            this.timerUpScroll.stop();
        }
    }                                           

    private void jButtonLeftMousePressed(MouseEvent evt) {                                            
        if (!this.timerUpScroll.isRunning())
        {
            this.timerUpScroll.start();
        }
    }                                           

    private void jButtonRightMouseReleased(MouseEvent evt) {                                            
        if (this.timerDownScroll.isRunning())
        {
            this.timerDownScroll.stop();
        }
    }                                           

    private void jButtonRightMousePressed(MouseEvent evt) {
        if (!this.timerDownScroll.isRunning())
        {
            this.timerDownScroll.start();
        }
    }
	
	 private void initTimers() {
         timerUpScroll = new Timer(250, new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                         AlphabetFilterPanel.this.movePanelUsers(-1);
                 }
         });
         timerUpScroll.setInitialDelay(0);

         timerDownScroll = new Timer(250, new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent e) {
                         AlphabetFilterPanel.this.movePanelUsers(1);
                 }
         });
         timerDownScroll.setInitialDelay(0);
	 }
	 
	 protected void movePanelUsers(int direction)
     {
        int newValue=this.scrollPane.getVerticalScrollBar().getValue();
        newValue+=(Math.abs(direction)/(direction==0?1:direction))*this.scrollPane.getVerticalScrollBar().getUnitIncrement();
        if (newValue<this.scrollPane.getVerticalScrollBar().getMinimum())
        {
            newValue=this.scrollPane.getVerticalScrollBar().getMinimum();
        }
        else if(newValue>this.scrollPane.getVerticalScrollBar().getMaximum())
        {
            newValue=this.scrollPane.getVerticalScrollBar().getMaximum();
        }
        this.scrollPane.getVerticalScrollBar().setValue(newValue);
      } 
	 
	 public void configureActionListenerForAll(ActionListener al){
		 btnA.addActionListener(al);
		 btnB.addActionListener(al);
		 btnC.addActionListener(al);
		 btnD.addActionListener(al);
		 btnE.addActionListener(al);
		 btnF.addActionListener(al);
		 btnG.addActionListener(al);
		 btnH.addActionListener(al);
		 btnI.addActionListener(al);
		 btnJ.addActionListener(al);
		 btnK.addActionListener(al);
		 btnL.addActionListener(al);
		 btnM.addActionListener(al);
		 btnN.addActionListener(al);
		 btnO.addActionListener(al);
		 btnP.addActionListener(al);
		 btnQ.addActionListener(al);
		 btnR.addActionListener(al);
		 btnS.addActionListener(al);
		 btnT.addActionListener(al);
		 btnU.addActionListener(al);
		 btnV.addActionListener(al);
		 btnX.addActionListener(al);
		 btnY.addActionListener(al);
		 btnZ.addActionListener(al);
		 btnW.addActionListener(al);
		 btnDiez.addActionListener(al);
	 }

	public JToggleButton getBtnP() {
		return btnP;
	}
	public JToggleButton getBtnY() {
		return btnY;
	}
	public JToggleButton getBtnA() {
		return btnA;
	}
	public JToggleButton getBtnO() {
		return btnO;
	}
	public JToggleButton getBtnI() {
		return btnI;
	}
	public JToggleButton getBtnJ() {
		return btnJ;
	}
	public JToggleButton getBtnF() {
		return btnF;
	}
	public JToggleButton getBtnS() {
		return btnS;
	}
	public JToggleButton getBtnR() {
		return btnR;
	}
	public JToggleButton getBtnL() {
		return btnL;
	}
	public JToggleButton getBtnW() {
		return btnW;
	}
	public JToggleButton getBtnC() {
		return btnC;
	}
	public JToggleButton getBtnB() {
		return btnB;
	}
	public JToggleButton getBtnH() {
		return btnH;
	}
	public JToggleButton getBtnK() {
		return btnK;
	}
	public JToggleButton getBtnZ() {
		return btnZ;
	}
	public JToggleButton getBtnG() {
		return btnG;
	}
	public JToggleButton getBtnN() {
		return btnN;
	}
	public JToggleButton getBtnU() {
		return btnU;
	}
	public JToggleButton getBtnDiez() {
		return btnDiez;
	}
	public JToggleButton getBtnV() {
		return btnV;
	}
	public JToggleButton getBtnE() {
		return btnE;
	}
	public JToggleButton getBtnQ() {
		return btnQ;
	}
	public JToggleButton getBtnD() {
		return btnD;
	}
	public JToggleButton getBtnM() {
		return btnM;
	}
	public JToggleButton getBtnX() {
		return btnX;
	}
	public JToggleButton getBtnT() {
		return btnT;
	}
}
