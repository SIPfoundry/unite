package org.ezuce.common.ui;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.ezuce.common.ComponentFactory;
import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.async.VCardLoaderCallback;
import org.ezuce.common.impl.DummyCommonInterface;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.actions.ContactActions;
import org.ezuce.common.ui.actions.task.MakeCallTask;
import org.ezuce.common.ui.listener.ChatStartListener;
import org.ezuce.common.ui.panels.UserGroupPanel;
import org.ezuce.common.ui.panels.UserMiniPanelGloss;
import org.ezuce.common.ui.popup.CallMenuBuilder;
import org.ezuce.common.ui.popup.UserMiniPanelPopup;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.ui.wrappers.interfaces.UserGroupCommonInterface;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.Workspace;
import org.jivesoftware.spark.ui.ContactGroup;
import org.jivesoftware.spark.ui.ContactInfoWindow;
import org.jivesoftware.spark.ui.ContactItem;
import org.jivesoftware.spark.ui.ContactList;
import org.jivesoftware.spark.util.GraphicUtils;
import org.jivesoftware.spark.util.log.Log;

public class EzuceContactInfoWindow extends ContactInfoWindow implements MouseListener, MouseMotionListener, VCardLoaderCallback, RosterListener {

    private static final long serialVersionUID = 8409694344721276453L;
    private static EzuceContactInfoWindow singletonEZ;
    final Image bgImage;
    protected boolean isDestroyed;
    protected boolean pinned;
    private final MugshotPanel imgPanel;
    private final JButton closeButton;
    private final JToggleButton pinButton;
    private final JButton emailButton;
    private final JButton chatButton;
    protected final JButton callButton;
    private final JButton alertButton;
    protected final JButton moreButton;
    private final JLabel locLabel;
    private final JLabel mgrTextLabel;
    private final JLabel locTextLabel;
    private final JLabel internLabel;
    private final JLabel didLabel;
    private final JLabel mobLabel;
    private final JLabel intNoLabel;
    private final JLabel didNoLabel;
    private final JLabel mobNoLabel;
    private final JLabel faxLabel;
    private final JLabel nameLabel;
    private final JLabel homeLabel;
    private final JLabel assistantLabel;
    private final JLabel faxNoLabel;
    private final JLabel homeNoLabel;
    private final JLabel assistantNameLabel;
    private final JLabel assistantNoLabel;
    private final JLabel bAddrLabel;
    private final JLabel hAddrLabel;
    private final JLabel bAddrTextLabel;
    private final JLabel hAddrTextLabel;
    private final JLabel roleLabel;
    private final JLabel socMedLabel;
    private final JLabel twtLabel;
    private final JLabel liLabel;
    private final JLabel fbLabel;
    private final JLabel xingLabel;
    private final JLabel locationLabel;
    private final JLabel deptLabel;
    private final JLabel companyLabel;
    private final JLabel deptTextLabel;
    private final JLabel companyTextLabel;
    private final JLabel mgrLabel;
    private final JPanel twtIconPanel;
    private final JPanel liIconPanel;
    private final JPanel fbIconPanel;
    private final JPanel othIconPanel;
    private final JSeparator jSeparator1;
    private final JSeparator jSeparator2;
    private final JSeparator jSeparator3;
    private final JSeparator jSeparator4;
    protected UserMiniPanelPopup dropdown;
    protected final JPopupMenu callMenu = new JPopupMenu();
    // we don't need the full functionality
    protected ContactActions actions;
    private ActionMap actionMap;
    private int pressX;
    private int pressY;
    final Timer timer = new Timer();
    private final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(getClass());
    final MouseListener labelListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            MakeCallTask mct = new MakeCallTask(
                    org.jdesktop.application.Application.getInstance(), false);
            mct.setCalee(((JLabel) e.getSource()).getText());
            mct.execute();
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

    public static EzuceContactInfoWindow getInstance() {
        // Synchronize on LOCK to ensure that we don't end up creating
        // two singletons.
        synchronized (getLock()) {
            if (null == singletonEZ) {
                final EzuceContactInfoWindow controller = new EzuceContactInfoWindow();
                singletonEZ = controller;
                return controller;
            }
        }
        return singletonEZ;
    }

    protected EzuceContactInfoWindow() {
        super(false);

        bgImage = new ImageIcon(getClass().getClassLoader().getResource("resources/images/contact_card_bg.png")).getImage();
        final ImageIcon closeIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/close.png"));
        closeButton = new LabelButton(closeIcon);
        final ImageIcon pinIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/pin.png"));
        final ImageIcon pinPressedIcon = new ImageIcon(getClass().getClassLoader().getResource(
                "resources/images/pinPressed.png"));
        pinButton = new LabelToggleButton(pinIcon, pinPressedIcon, resourceMap.getString("pin.contact.card"));
        imgPanel = new MugshotPanel(false);
        nameLabel = ComponentFactory.createNameLabel();
        roleLabel = ComponentFactory.createPositionLabel();
        locationLabel = ComponentFactory.createBottomLabel();
        final ImageIcon emailIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/email.png"));
        final ImageIcon emailHoverIcon = new ImageIcon(getClass().getClassLoader().getResource(
                "resources/images/emailHover.png"));
        emailButton = new LabelButton(emailIcon, emailHoverIcon, resourceMap.getString("email.text"));
        final ImageIcon chatIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/chat_card.png"));
        final ImageIcon chatHoverIcon = new ImageIcon(getClass().getClassLoader().getResource(
                "resources/images/chat_cardHover.png"));
        chatButton = new LabelButton(chatIcon, chatHoverIcon, resourceMap.getString("chat.text"));
        final ImageIcon callIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/ccCall.png"));
        final ImageIcon callHoverIcon = new ImageIcon(getClass().getClassLoader().getResource(
                "resources/images/ccCallHover.png"));
        callButton = new LabelButton(callIcon, callHoverIcon, resourceMap.getString("call.text"));
        final ImageIcon alertIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/alert.png"));
        final ImageIcon alertHoverIcon = new ImageIcon(getClass().getClassLoader().getResource(
                "resources/images/alertHover.png"));
        alertButton = new LabelButton(alertIcon, alertHoverIcon, resourceMap.getString("alert.when.online"));
        final ImageIcon moreIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/more.png"));
        moreButton = new LabelButton(moreIcon);
        jSeparator1 = new JSeparator();
        deptLabel = new JLabel(resourceMap.getString("department.text"));
        companyLabel = new JLabel(resourceMap.getString("company.text"));
        deptTextLabel = new JLabel();
        companyTextLabel = new JLabel();
        mgrLabel = new JLabel(resourceMap.getString("manager.name"));
        locLabel = new JLabel(resourceMap.getString("location.name"));
        mgrTextLabel = new JLabel();
        locTextLabel = new JLabel();
        jSeparator2 = new JSeparator();
        internLabel = new JLabel(resourceMap.getString("intern.name"));
        didLabel = new JLabel(resourceMap.getString("did"));
        mobLabel = new JLabel(resourceMap.getString("mobile"));
        intNoLabel = new JLabel();
        didNoLabel = new JLabel();
        mobNoLabel = new JLabel();
        faxLabel = new JLabel(resourceMap.getString("fax"));
        homeLabel = new JLabel(resourceMap.getString("home"));
        assistantLabel = new JLabel(resourceMap.getString("assistant"));
        faxNoLabel = new JLabel();
        homeNoLabel = new JLabel();
        assistantNameLabel = new JLabel();
        assistantNoLabel = new JLabel();
        jSeparator3 = new JSeparator();
        bAddrLabel = new JLabel(resourceMap.getString("business.address"));
        hAddrLabel = new JLabel(resourceMap.getString("home.address"));
        bAddrTextLabel = new JLabel();
        hAddrTextLabel = new JLabel();
        jSeparator4 = new JSeparator();

        socMedLabel = new JLabel(resourceMap.getString("social.media.contacts"));

        final ImageIcon twtIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/twitter.png"));
        twtIconPanel = new JPanel();
        twtIconPanel.add(new JLabel(twtIcon));

        final ImageIcon liIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/linkedin.png"));
        liIconPanel = new JPanel();
        liIconPanel.add(new JLabel(liIcon));

        final ImageIcon fbIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/facebook.png"));
        fbIconPanel = new JPanel();
        fbIconPanel.add(new JLabel(fbIcon));

        final ImageIcon othIcon = new ImageIcon(getClass().getClassLoader().getResource("resources/images/other.png"));
        othIconPanel = new JPanel();
        othIconPanel.add(new JLabel(othIcon));

        twtLabel = new JLabel();
        liLabel = new JLabel();
        fbLabel = new JLabel();
        xingLabel = new JLabel();

        initComponents();
        addMouseListener(this);
        addMouseMotionListener(this);
        EzucePresenceUtils.registerRosterListener(this);
    }

    @Override
    public void dispose() {
        if (!pinned) {
            isDestroyed = true;
            final TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    if (isDestroyed) {
                        superclassDispose();
                    }
                }
            };
            timer.schedule(task, 500);
        }
    }

    protected void superclassDispose() {
        if (dropdown != null) {
            dropdown.setVisible(false);
        }
        intNoLabel.removeMouseListener(labelListener);
        didNoLabel.removeMouseListener(labelListener);
        mobNoLabel.removeMouseListener(labelListener);
        assistantNoLabel.removeMouseListener(labelListener);
        homeNoLabel.removeMouseListener(labelListener);
        faxNoLabel.removeMouseListener(labelListener);
        super.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressX = e.getX();
        pressY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isDestroyed = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!pinned) {
            super.mouseExited(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        final int endX = e.getX();
        final int endY = e.getY();

        final int dx = endX - pressX;
        final int dy = endY - pressY;

        final Point location = getWindow().getLocation();

        location.translate(dx, dy);
        getWindow().setLocation(location);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    private void initComponents() {
        pinButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                pinButtonActionPerformed(evt);
            }
        });

        closeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        moreButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if (dropdown != null) {
                    dropdown.displayPopup(new DummyCommonInterface(getContactItem()), e.getComponent(), 0,
                            moreButton.getHeight());
                }
            }
        });

        if (RestManager.getInstance().isLoggedIn()) {
            callButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent evt) {
                    MakeCallTask mct = new MakeCallTask(org.jdesktop.application.Application.getInstance(), false);
                    mct.setCalee(Utils.getImId(getContactItem().getJID()));
                    mct.execute();
                }
            });
        } else {
            callButton.setEnabled(false);
        }

        alertButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                actions.alertUserAvailableAction();
            }
        });

        emailButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                actions.sendEmailAction(evt);
            }
        });

        boldFont(deptLabel);
        boldFont(companyLabel);
        boldFont(mgrLabel);
        boldFont(locLabel);
        boldFont(internLabel);
        intNoLabel.setForeground(Color.blue);
        boldFont(didLabel);
        didNoLabel.setForeground(Color.blue);
        boldFont(mobLabel);
        mobNoLabel.setForeground(Color.blue);
        boldFont(faxLabel);
        faxNoLabel.setForeground(Color.blue);
        boldFont(homeLabel);
        homeNoLabel.setForeground(Color.blue);
        boldFont(assistantLabel);
        assistantNoLabel.setForeground(Color.blue);
        boldFont(bAddrLabel);
        boldFont(hAddrLabel);
        boldFont(socMedLabel);

        twtIconPanel.setOpaque(false);
        liIconPanel.setOpaque(false);
        fbIconPanel.setOpaque(false);
        othIconPanel.setOpaque(false);

        final GroupLayout jPanel1Layout = new GroupLayout(imgPanel);
        imgPanel.setLayout(jPanel1Layout);
        final GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addContainerGap().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(deptLabel).addComponent(companyLabel)).addGap(18, 18, 18).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(deptTextLabel, 110, 110, 110).addComponent(companyTextLabel, 110, 110, 110)).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(mgrLabel).addComponent(locLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(locTextLabel, 110, 110, 110).addComponent(mgrTextLabel, 110, 110, 110)).addContainerGap(494, Short.MAX_VALUE)).addGroup(
                layout.createSequentialGroup().addContainerGap().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addComponent(imgPanel,
                GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE)).addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addPreferredGap(
                LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addComponent(
                roleLabel).addComponent(
                nameLabel).addComponent(
                locationLabel))).addGroup(
                layout.createSequentialGroup().addGap(300, 300, 300).addComponent(pinButton).addPreferredGap(
                LayoutStyle.ComponentPlacement.RELATED).addComponent(
                closeButton)))).addGroup(
                layout.createSequentialGroup().addComponent(emailButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(chatButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(callButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(alertButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(moreButton, GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE))).addContainerGap(396, Short.MAX_VALUE)).addGroup(
                layout.createSequentialGroup().addContainerGap().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(internLabel).addComponent(didLabel).addComponent(mobLabel)).addGap(18, 18, 18).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(intNoLabel, 139, 139, 139).addComponent(didNoLabel, 139, 139, 139).addComponent(mobNoLabel, 139, 139, 139)).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(assistantLabel).addComponent(faxLabel).addComponent(homeLabel)).addGap(18, 18, 18).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(homeNoLabel).addComponent(faxNoLabel).addComponent(assistantNameLabel).addComponent(assistantNoLabel)).addContainerGap(489, Short.MAX_VALUE)).addGroup(
                layout.createSequentialGroup().addContainerGap().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(bAddrLabel).addComponent(hAddrLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(bAddrTextLabel, GroupLayout.PREFERRED_SIZE, 230,
                GroupLayout.PREFERRED_SIZE).addComponent(hAddrTextLabel, GroupLayout.PREFERRED_SIZE, 217,
                GroupLayout.PREFERRED_SIZE)).addContainerGap(496, Short.MAX_VALUE)).addGroup(
                GroupLayout.Alignment.TRAILING,
                layout.createSequentialGroup().addGroup(
                layout.createSequentialGroup().addContainerGap().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(jSeparator4,
                GroupLayout.Alignment.LEADING,
                GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE).addComponent(jSeparator3,
                GroupLayout.Alignment.LEADING,
                GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE).addComponent(jSeparator2,
                GroupLayout.Alignment.LEADING,
                GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE).addComponent(jSeparator1,
                GroupLayout.Alignment.LEADING,
                GroupLayout.PREFERRED_SIZE, 424,
                GroupLayout.PREFERRED_SIZE))).addGap(398, 398, 398)).addGroup(
                layout.createSequentialGroup().addContainerGap().addComponent(socMedLabel).addContainerGap(716, Short.MAX_VALUE)).addGroup(
                layout.createSequentialGroup().addContainerGap().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(liIconPanel, GroupLayout.Alignment.LEADING,
                GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE).addComponent(twtIconPanel, GroupLayout.Alignment.LEADING,
                GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(twtLabel).addComponent(liLabel)).addGap(39, 39, 39).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(othIconPanel, GroupLayout.DEFAULT_SIZE,
                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(fbIconPanel, GroupLayout.DEFAULT_SIZE,
                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(fbLabel).addComponent(xingLabel)).addContainerGap(576, Short.MAX_VALUE)));

        layout.linkSize(SwingConstants.HORIZONTAL, new java.awt.Component[]{bAddrTextLabel, hAddrTextLabel});

        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addContainerGap().addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.BASELINE).addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.LEADING).addComponent(
                imgPanel,
                GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE).addGroup(
                layout.createSequentialGroup().addGap(20,
                20,
                20).addComponent(
                nameLabel).addPreferredGap(
                LayoutStyle.ComponentPlacement.RELATED).addComponent(
                roleLabel).addPreferredGap(
                LayoutStyle.ComponentPlacement.RELATED).addComponent(
                locationLabel))).addComponent(closeButton).addComponent(pinButton)))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(emailButton).addComponent(chatButton).addComponent(callButton).addComponent(alertButton).addComponent(moreButton, GroupLayout.PREFERRED_SIZE,
                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(
                layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.BASELINE).addComponent(deptLabel).addComponent(deptTextLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.BASELINE).addComponent(companyLabel).addComponent(companyTextLabel))).addGroup(
                layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.BASELINE).addComponent(mgrLabel).addComponent(mgrTextLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.BASELINE).addComponent(locLabel).addComponent(locTextLabel)))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.BASELINE).addComponent(internLabel).addComponent(intNoLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.BASELINE).addComponent(didLabel).addComponent(didNoLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.BASELINE).addComponent(mobLabel).addComponent(mobNoLabel))).addGroup(
                layout.createSequentialGroup().addGroup(
                layout.createSequentialGroup().addGroup(
                layout.createParallelGroup().addComponent(faxLabel).addComponent(
                faxNoLabel)).addPreferredGap(
                LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup().addComponent(homeLabel).addComponent(
                homeNoLabel)).addPreferredGap(
                LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.BASELINE).addComponent(
                assistantLabel).addGroup(
                layout.createSequentialGroup().addComponent(
                assistantNameLabel).addComponent(
                assistantNoLabel)))))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(bAddrLabel).addComponent(bAddrTextLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(hAddrLabel).addComponent(hAddrTextLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(
                layout.createSequentialGroup().addComponent(socMedLabel).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(
                GroupLayout.Alignment.LEADING, false).addComponent(fbIconPanel,
                GroupLayout.DEFAULT_SIZE,
                GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE).addComponent(twtIconPanel,
                GroupLayout.DEFAULT_SIZE,
                GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE).addComponent(twtLabel,
                GroupLayout.Alignment.CENTER).addComponent(fbLabel,
                GroupLayout.Alignment.CENTER)))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(othIconPanel, GroupLayout.DEFAULT_SIZE,
                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(liIconPanel, GroupLayout.DEFAULT_SIZE,
                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(liLabel, GroupLayout.Alignment.CENTER).addComponent(xingLabel, GroupLayout.Alignment.CENTER))).addGap(134, 134, 134)));
    }

    private void boldFont(Component c) {
        final Font f = c.getFont();
        c.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
    }

    @SuppressWarnings("unused")
    protected void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        pinned = false;
        pinButton.setSelected(false);
        superclassDispose();
    }

    @SuppressWarnings("unused")
    protected void pinButtonActionPerformed(java.awt.event.ActionEvent evt) {
        pinned = pinButton.isSelected();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(450, 480);
    }

    @Override
    public void display(ContactGroup group, MouseEvent e) {
        // code from inherited super class (adapted):
        int loc = group.getList().locationToIndex(e.getPoint());
        ContactItem item = (ContactItem) group.getList().getModel().getElementAt(loc);
        if (item == null || item.getJID() == null) {
            return;
        }

        if (getContactItem() != null && getContactItem() == item) {
            return;
        }

        getIconLabel().setIcon(item.getIcon());

        Point point = group.getList().indexToLocation(loc);

        getWindow().setFocusableWindowState(false);
        setContactItem(item);
        getWindow().pack();

        Point mainWindowLocation = SparkManager.getMainWindow().getLocationOnScreen();

        Point listLocation = null;
        try {
            if (group.getList().isShowing()) {
                group.getList().getLocationOnScreen();
            }
        } catch (RuntimeException ex) {
        }

        int x = (int) mainWindowLocation.getX()
                + SparkManager.getMainWindow().getWidth();
        int y = (listLocation != null ? (int) listLocation.getY() : 0)
                + (int) point.getY();
        setWindowLocation(x, y);
        if (!getWindow().isVisible()) {
            getWindow().setVisible(true);
        }
        // end code from inherited super class:

        final Point location = getWindow().getLocation();
        int yAux = (int) location.getY();

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (yAux + getHeight() > screenSize.height - 64) {
            yAux -= yAux + getHeight() - screenSize.height + 64;
        }

        location.y = yAux;

        getWindow().setLocation(location);
    }

    public void display(UserGroupPanel group, MouseEvent e) {
        final int loc = group.getList().locationToIndex(e.getPoint());

        ContactItem item = null;
        ContactListEntry cle = null;
        try {
            cle = ((UserMiniPanelGloss) group.getList().getModel().getElementAt(loc)).getContact();
        } catch (Exception ex) {
            return;
        }
        if (cle instanceof ContactItemWrapper) {
            item = ((ContactItemWrapper) cle).getContactItem();
            item.setPresence(cle.getPresence());
        }
        if (item == null || item.getJID() == null) {
            return;
        }

        if (getContactItem() != null && getContactItem() == item) {
            return;
        }

        final Point point = group.getList().indexToLocation(loc);

        getWindow().setFocusableWindowState(false);
        setContactItem(item);
        getWindow().pack();

        final Point mainWindowLocation = SparkManager.getMainWindow().getLocationOnScreen();
        final Point listLocation = group.getList().getLocationOnScreen();

        final int x = (int) mainWindowLocation.getX()
                + SparkManager.getMainWindow().getWidth();

        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // make sure the window does not show beyond the bottom of the screen:
        int y = (int) listLocation.getY() + (int) point.getY();
        if (y + getWindow().getHeight() >= (int) screenSize.getHeight()) {
            y = (int) screenSize.getHeight() - getWindow().getHeight() - 25;
        }
        if ((int) screenSize.getWidth() - getPreferredSize().getWidth() >= x) {
            getWindow().setLocation(x, y);
            if (!getWindow().isVisible()) {
                getWindow().setVisible(true);
            }
        } else {
            getWindow().setLocation(
                    (int) mainWindowLocation.getX()
                    - (int) getPreferredSize().getWidth(), y);
            if (!getWindow().isVisible()) {
                getWindow().setVisible(true);
            }
        }
    }

    @Override
    protected void hideWindow() {
        if (!pinned) {
            super.hideWindow();
        }
    }

    @Override
    public void customizeUI(ContactItem contactItem) {
        actions = new ContactActions(new DummyCommonInterface(contactItem), null);
        actionMap = Application.getInstance().getContext().getActionMap(ContactActions.class, actions);
        final List<JMenuItem> items = CallMenuBuilder.buildCallMenuItemsFromId(contactItem.getJID(), actionMap);

        callMenu.removeAll();
        for (JMenuItem item : items) {
            callMenu.add(item);
        }

        imgPanel.setPresence(contactItem.getPresence());

        nameLabel.setText(contactItem.getDisplayName());

        if (dropdown != null) {
            dropdown.setVisible(false);
        }

        ContactList list = Workspace.getInstance().getContactList();
        if (list instanceof UserGroupCommonInterface) {
            dropdown = new UserMiniPanelPopup((UserGroupCommonInterface) list, null, null);
        }
        for (final ActionListener listener : chatButton.getActionListeners()) {
            chatButton.removeActionListener(listener);
        }
        chatButton.addActionListener(new ChatStartListener(contactItem));
        AsyncLoader.getInstance().executeJob(contactItem.getJID(), this);
    }

    @Override
    public void vcardLoaded(VCard vCard) {
        ImageIcon avatar = Utils.retrieveAvatar(vCard);
        imgPanel.setMugshot(avatar);

        String title = "";
        String dept = "";
        String homeNo = "";
        String didNo = "";
        String mobNo = "";
        String location = "";
        String company = "";
        String manager = "";
        String intNo = "";
        String faxNo = "";
        String assistNo = "";
        String assistName = "";
        final StringBuilder sbBA = new StringBuilder();
        final StringBuilder sbHA = new StringBuilder();

        String twitterText = null;
        String linkedinText = null;
        String facebookText = null;
        String xingText = null;

        if (vCard != null) {
            title = vCard.getField("TITLE");
            if (title == null || title.trim().isEmpty()) {
                title = resourceMap.getString("no.job");
            }
            dept = vCard.getOrganizationUnit();
            location = vCard.getField("X-LOCATION");
            manager = vCard.getField("X-MANAGER");
            company = vCard.getOrganization();
            addToBuffer(vCard.getAddressFieldWork("STREET"), sbBA);
            addToBuffer(vCard.getAddressFieldWork("LOCALITY"), sbBA);
            addToBuffer(vCard.getAddressFieldWork("REGION"), sbBA);
            addToBuffer(vCard.getAddressFieldWork("PCODE"), sbBA);
            addToBuffer(vCard.getAddressFieldWork("CTRY"), sbBA);
            addToBuffer(vCard.getAddressFieldHome("STREET"), sbHA);
            addToBuffer(vCard.getAddressFieldHome("LOCALITY"), sbHA);
            addToBuffer(vCard.getAddressFieldHome("REGION"), sbHA);
            addToBuffer(vCard.getAddressFieldHome("PCODE"), sbHA);
            addToBuffer(vCard.getAddressFieldHome("CTRY"), sbHA);
            homeNo = vCard.getPhoneHome("VOICE");
            mobNo = vCard.getPhoneHome("CELL");
            faxNo = vCard.getPhoneWork("FAX");
            didNo = vCard.getField("X-DID");
            intNo = vCard.getField("X-INTERN");
            assistNo = vCard.getField("X-ASSISTANT-PHONE");
            assistName = vCard.getField("X-ASSISTANT");
            twitterText = vCard.getField("X-TWITTER");
            linkedinText = vCard.getField("X-LINKEDIN");
            facebookText = vCard.getField("X-FACEBOOK");
            xingText = vCard.getField("X-XING");
        }
        // TODO this is all contact related data. populate accordingly
        roleLabel.setText(title);
        locationLabel.setText(location);
        deptTextLabel.setText(dept);
        companyTextLabel.setText(company);
        mgrTextLabel.setText(manager);
        locTextLabel.setText(location);
        intNoLabel.setText(intNo);
        configureForCalling(intNoLabel);
        didNoLabel.setText(didNo);
        configureForCalling(didNoLabel);
        mobNoLabel.setText(mobNo);
        configureForCalling(mobNoLabel);
        faxNoLabel.setText(faxNo);
        configureForCalling(faxNoLabel);
        homeNoLabel.setText(homeNo);
        configureForCalling(homeNoLabel);
        assistantNameLabel.setText(assistName);
        assistantNoLabel.setText(assistNo);
        configureForCalling(assistantNoLabel);
        bAddrTextLabel.setText(sbBA.toString());
        hAddrTextLabel.setText(sbHA.toString());

        twtLabel.setText(isBlank(twitterText) ? resourceMap.getString("no.twitter") : twitterText);
        liLabel.setText(isBlank(linkedinText) ? resourceMap.getString("no.linkedin") : linkedinText);
        fbLabel.setText(isBlank(facebookText) ? resourceMap.getString("no.facebook") : facebookText);
        xingLabel.setText(isBlank(xingText) ? resourceMap.getString("no.xing") : xingText);
    }

    private void addToBuffer(String token, StringBuilder sb) {
        if (token != null && !token.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(token);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bgImage, 0, 0, null);
    }

    @Override
    public void setWindowLocation(final int x, final int y) {
        if (!pinned) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    EzuceContactInfoWindow.super.setWindowLocation(x, y);
                }
            });            
        }
    }

    private void configureForCalling(JLabel label) {
        if (label != null && !isEmpty(label.getText())) {
            if (RestManager.getInstance().isLoggedIn()) {
                MouseListener[] mListeners = label.getMouseListeners();
                boolean canAddListener = true;
                if (mListeners.length > 0) {
                    for (MouseListener ml : mListeners) {
                        if (ml.equals(labelListener)) {
                            canAddListener = false;
                            break;
                        }
                    }
                }
                if (canAddListener) {
                    label.addMouseListener(labelListener);
                }
            }
        }
    }

	@Override
	public void entriesAdded(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void presenceChanged(Presence arg0) {
		imgPanel.setPresence(arg0);
        if (Utils.isVCardUpdated(arg0)) {
        	AsyncLoader.getInstance().execute(arg0.getFrom(), this);
        }
	}
}
