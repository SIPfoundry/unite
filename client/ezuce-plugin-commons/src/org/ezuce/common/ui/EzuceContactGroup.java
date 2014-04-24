package org.ezuce.common.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.ezuce.common.ui.panels.GroupTitleJPanel;
import org.ezuce.im.renderer.EzucePanelRenderer;
import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactItem;

public class EzuceContactGroup extends ContactGroup {

    private static final long serialVersionUID = 2595069328318342419L;
    
    private static final javax.swing.ImageIcon BTN_BG_OPEN = 
			new javax.swing.ImageIcon(EzuceContactGroup.class.getResource("/resources/images/contact-group_open_btn.png"));
	private static final javax.swing.ImageIcon BTN_BG_CLOSED = 
			new javax.swing.ImageIcon(EzuceContactGroup.class.getResource("/resources/images/contact-group_closed_btn.png"));
	
    protected GroupTitleJPanel jPanelGroupDescr;
    protected JToggleButton btnOpenClose;
    protected JLabel lblGroupName;
	protected JLabel lblGroupSize;

    public EzuceContactGroup(String groupName) {
        super(groupName, false);
        getContactItemList().setCellRenderer(new EzucePanelRenderer());
        MouseListener[] mouseListeners = getTitlePane().getMouseListeners();
        for (MouseListener listener : mouseListeners) {
        	getTitlePane().removeMouseListener(listener);
        }
        remove(getTitlePane());
        
        /*
        setTitlePane(new EzuceCollapsibleTitlePane(groupName));
        getTitlePane().addMouseListener(this.contactGroupMouseListener);
        add(getTitlePane(), BorderLayout.NORTH);        
        getTitlePane().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!e.isPopupTrigger()) {
                    final boolean isCollapsed = getTitlePane().isCollapsed();
                    setCollapsed(!isCollapsed);
                }
            }
        });
        */
        
        jPanelGroupDescr = new GroupTitleJPanel();		
		btnOpenClose = new JToggleButton();
		lblGroupName = new JLabel();		
		lblGroupSize = new JLabel();
		
		Font font = new Font("Calibri", 0, 14);
		
		lblGroupName.setFont(font);
		lblGroupName.setForeground(Color.GRAY);
		lblGroupName.setText("Group name");
		lblGroupName.setBorder(null);
		lblGroupName.setHorizontalAlignment(SwingConstants.LEADING);
		lblGroupName.setVerticalAlignment(SwingConstants.CENTER);
		lblGroupName.setName("lblGroupName");
		setGroupName(groupName);
		
		lblGroupSize.setFont(font);
		lblGroupSize.setForeground(Color.GRAY);
		lblGroupSize.setText("(0)");
		lblGroupSize.setBorder(null);
		lblGroupSize.setHorizontalAlignment(SwingConstants.LEADING);
		lblGroupSize.setVerticalAlignment(SwingConstants.CENTER);
		lblGroupSize.setName("lblGroupSize");
		
		jPanelGroupDescr.setBackground(new Color(246, 245, 245));
		jPanelGroupDescr.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		jPanelGroupDescr.setMaximumSize(new Dimension(32767, 30));
		jPanelGroupDescr.setMinimumSize(new Dimension(1, 30));
		jPanelGroupDescr.setName("jPanelGroupDescr");
		jPanelGroupDescr.setPreferredSize(new Dimension(264, 30));
		btnOpenClose.setName("btnOpenClose");
		btnOpenClose.setBorder(null);
		btnOpenClose.setBorderPainted(false);
		btnOpenClose.setContentAreaFilled(false);
		btnOpenClose.setDoubleBuffered(true);
		btnOpenClose.setFocusPainted(false);
		btnOpenClose.setHideActionText(true);
		btnOpenClose.setIcon(BTN_BG_CLOSED);
		btnOpenClose.setSelectedIcon(BTN_BG_OPEN);		
		btnOpenClose.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {			                
				if (jPanelGroupDescr instanceof GroupTitleJPanel){
					((GroupTitleJPanel)jPanelGroupDescr).setOpen(!((GroupTitleJPanel)jPanelGroupDescr).isOpen());
					setCollapsed(!((GroupTitleJPanel)jPanelGroupDescr).isOpen());
				}
			}
		});
				
		jPanelGroupDescr.setLayout(new BoxLayout(jPanelGroupDescr, BoxLayout.LINE_AXIS));
		jPanelGroupDescr.add(new javax.swing.Box.Filler(new java.awt.Dimension(8, 0), new java.awt.Dimension(8, 0), new java.awt.Dimension(8, 32767)));
		jPanelGroupDescr.add(lblGroupName);
		jPanelGroupDescr.add(new javax.swing.Box.Filler(new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 0), new java.awt.Dimension(2, 32767)));
		jPanelGroupDescr.add(lblGroupSize);
		jPanelGroupDescr.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 0), new java.awt.Dimension(32767, 32767)));
		jPanelGroupDescr.add(btnOpenClose);
		jPanelGroupDescr.add(new javax.swing.Box.Filler(new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 0), new java.awt.Dimension(1, 32767)));
		
		jPanelGroupDescr.addMouseListener(contactGroupMouseListener);
		
		add(jPanelGroupDescr, BorderLayout.NORTH);
		setCollapsed(!((GroupTitleJPanel)jPanelGroupDescr).isOpen());
		
    }

    public void setGroupName(String groupName) {
		this.lblGroupName.setText(groupName);
	}

	public void setGroupSize() {		
		this.lblGroupSize.setText("(" + getContactItems().size() + "/" + (getContactItems().size() + getOfflineContacts().size()) + ")");
	}

	public String getGroupSize() {
		return getContactItems().size()+"";
	}
    
    @Override
    protected void setOfflineGroupNameFont(ContactItem item) {

    }
    
    @Override
    protected void updateTitle() {
        if (Res.getString("group.offline").equals(getGroupName())) {
            setTitle(Res.getString("group.offline") + "(" + getContactItemList().getModel().getSize() + ")");
            return;
        }
        
        setTitle(getGroupTitle(getGroupName()) + "(" + getContactItems().size() + "/" + (getContactItems().size() + getOfflineContacts().size()) + ")");
        setGroupName(getGroupTitle(getGroupName()));
        setGroupSize();
    }

    /*
     * used for filtering
     */
    public void setVisibility(ContactItem item, boolean visible) {
        if (visible) {
            if (!getModel().contains(item)) {
                int index = 0;
                final Enumeration<?> elements = getModel().elements();

                while (elements.hasMoreElements()) {
                    final ContactItem element = (ContactItem) elements.nextElement();
                    if (itemComparator.compare(item, element) <= 0) {
                        break;
                    }
                    index++;
                }
                getModel().add(index, item);
            }
        } else {
            getModel().removeElement(item);
        }
        updateTitle();
    }
}
