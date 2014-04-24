package test.web;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SwingDragDrop{
	JPanel txtField;
	JLabel lbl;
	public static void main(String[] args){
		SwingDragDrop sdd = new SwingDragDrop();
	}

	public SwingDragDrop(){
		JFrame frame = new JFrame("Drag Drop Demo");
		txtField = new JPanel();
		txtField.add(new JLabel("GIGEA"));
		txtField.setBorder(BorderFactory.createEtchedBorder());
		lbl = new JLabel("This is the text for drag and drop.");
		lbl.setTransferHandler(new TransferHandler("text"));
		MouseListener ml = new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				JComponent jc = (JComponent)e.getSource();
				TransferHandler th = jc.getTransferHandler();
				th.exportAsDrag(jc, e, TransferHandler.COPY);
			}
		};
		lbl.addMouseListener(ml);
		JPanel panel = new JPanel();
		panel.add(txtField);
		frame.add(lbl, BorderLayout.CENTER);
		frame.add(panel, BorderLayout.NORTH);
		frame.setSize(400, 400);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
	}
}
