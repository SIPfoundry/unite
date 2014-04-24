package org.ezuce.common.ui.popup;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingWorker;

import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.actions.task.MakeCallTask;
import org.ezuce.common.xml.MyConferenceXML;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.spark.util.GraphicUtils;

public class InviteBuilder extends SwingWorker<List<MyConferenceXML>, Void> {
	private final JMenu menu;
	private final JLabel label;
	private final Action action;
	private final Icon icon;
	private final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(getClass());

	public InviteBuilder(JMenu menu, Action action, Icon icon) {
		this.menu = menu;
		this.label = null;
		this.action = action;
		this.icon = icon;
	}
	
	public InviteBuilder(JLabel label, Action action, Icon icon) {
		this.label = label;
		this.menu = null;
		this.action = action;
		this.icon = icon;
	}

	@Override
	protected List<MyConferenceXML> doInBackground() {
		List<MyConferenceXML> result;

		try {
			result = RestManager.getInstance().getConferenceList();
		} catch (Exception e) {
			e.printStackTrace();
			result = Collections.emptyList();
		}

		return result;
	}

	@Override
	protected void done() {
		if (menu!=null)
		{
			menu.removeAll();
	
			try {
				List<MyConferenceXML> confs = get();
	
				for (MyConferenceXML conf : confs) {
					JMenuItem jmi = new JMenuItem(action);
					jmi.setIcon(icon);
					jmi.setActionCommand(conf.getName());
					jmi.setText(resourceMap.getString("conference.text", conf.getName(), conf.getExtension()));
					menu.add(jmi);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			if (menu.getMenuComponentCount() == 0) {
				JMenuItem dummyJmi = new JMenuItem(resourceMap.getString("no.active.conferences"));
				dummyJmi.setEnabled(false);
				menu.add(dummyJmi);
			}
		}
		else if (label!=null)
		{
			try {
				List<MyConferenceXML> confs = get();
	
				if (confs.size()>0){
					final MyConferenceXML conf = confs.get(0);
					if (action==null){
						label.setText(conf.getExtension());
					}else{
						if (label.getText().equals("...")){
							label.setText(conf.getName());
						}
					}
					
					final MouseListener makeCallLblListener = new MouseAdapter() {						
				        @Override
				        public void mouseClicked(MouseEvent e) {	
				        	if (action==null){
								MakeCallTask mct = new MakeCallTask(
					                    org.jdesktop.application.Application.getInstance(), false);
					            mct.setCalee(conf.getExtension());
					            mct.execute();
				        	}
				        	else{
				        		action.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, null){});				        		
				        	}
				        }

				        @Override
				        public void mouseEntered(MouseEvent e) {
				            ((JLabel) e.getSource()).setCursor(GraphicUtils.HAND_CURSOR);
				        }

				        @Override
				        public void mouseExited(MouseEvent e) {
				            ((JLabel) e.getSource()).setCursor(GraphicUtils.DEFAULT_CURSOR);
				        }
				    };
				    
				    MouseListener[] mls=label.getMouseListeners();
				    for (MouseListener ml : mls){
				    	label.removeMouseListener(ml);
				    }
				    
				    label.addMouseListener(makeCallLblListener);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			if (label.getText().equals("...")) {
				label.setText(resourceMap.getString("no.active.conferences"));				
			}
		}
	}

}
