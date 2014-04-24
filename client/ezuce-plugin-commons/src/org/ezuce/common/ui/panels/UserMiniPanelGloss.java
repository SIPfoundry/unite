package org.ezuce.common.ui.panels;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import org.ezuce.common.async.AsyncLoader;
import org.ezuce.common.async.VCardLoaderCallback;
import org.ezuce.common.resource.EzucePresenceUtils;
import org.ezuce.common.resource.Utils;
import org.ezuce.common.ui.borders.PanelBorders;
import org.ezuce.common.ui.paints.Colors;
import org.ezuce.common.ui.paints.GlossAngleGradientPainter;
import org.ezuce.common.ui.paints.LinearGradientPainter;
import org.ezuce.common.ui.wrappers.ContactItemWrapper;
import org.ezuce.common.ui.wrappers.interfaces.ContactListEntry;
import org.ezuce.common.ui.wrappers.interfaces.UserMiniPanelCommonInterface;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXLabel.TextAlignment;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;
import org.jdesktop.swingx.painter.MattePainter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;
import javax.swing.JSeparator;
import javax.swing.border.MatteBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;


/**
 * Displays brief information about a user.
 * 
 */
public class UserMiniPanelGloss extends JPanel
                                implements UserMiniPanelCommonInterface,
                                           FocusListener, VCardLoaderCallback {
    private JLayeredPane jLayeredPane;
    private JPanel jPanelDetails;
    private JPanel jPanelPic;
    private JXLabel jXLabel2;
    private JXLabel jXLabel3;
    private JLabel jXLabelDisplayName;
    private JXLabel jXLabelPic;
    private JXPanel jXPanelGlass;
    // End of variables declaration                   
    private final MattePainter jxLabelStatusBackground=LinearGradientPainter.jxLabelStatusBackgroundSmall();
    private ContactListEntry contact=null;
    private final Dimension iconDimension=new Dimension(38,38);
    private final Color picBorderColor=Colors.userMiniPanelIconBorderColor;
    private Border borderFocused=BorderFactory.createLineBorder(new Color(39,72,87));
    private Border borderUnfocused=PanelBorders.userMiniPanelGlossCompoundBorder;    
    private Image avatarImage;
    private JList parent = null;
    private MouseListener localMouseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                formMouseEntered(evt);
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                formMouseExited(evt);
            }
        };
    private Dimension savedDimension;
      
        
    /** Creates new form UserMiniPanelGloss */
    public UserMiniPanelGloss() {
        initComponents();
    }

    /**
     * Creates a new UserMiniPanelGloss based on a ContactItem instance.
     * TODO: possible improvement - make use of property change support, binding
     * the elements of this UserMiniPanelGloss to ContactItem's properties. This
     * way, if a change occurs in one ContactItem in the list of ContactItems,
     * where ever it is held, the change will be visible in the UserMiniPanelGloss.
     * @param ci
     */
    public UserMiniPanelGloss(ContactListEntry ci) {
        this.contact=ci;
        initComponents();
        customizeForContact();
    }

    public final void customizeForContact() {
        if (contact!=null) {
        	updateVCardData();
            if (contact.getPresence()==null) {
                setUserStatusIcon(Images.userStatusOffline);
                setUserStatus(EzucePresenceUtils.getUpdatedUserStatus(null));
            }
            else {
                updateStatus(contact.getPresence());
            }
        }
    }
    
    private void updateVCardData() {
        setUserDisplayName(isEmpty(contact.getUserDisplayName()) ? contact.getNumber() : contact.getUserDisplayName());
        setDescription(isEmpty(contact.getDescription()) ? EMPTY : contact.getDescription());
        setUserStatus(isEmpty(contact.getStatus()) ? EMPTY : contact.getStatus());
        final ImageIcon avatar= contact.getUserAvatar();
        if (avatar!=null) {                 
           setUserPicture(avatar.getImage());
        } 
    }
    
    public void setParent(JList parent) {
		this.parent = parent;
	}

	public void updateStatus(Presence presence) {
        setUserStatusIcon(EzucePresenceUtils.getUpdatedUserStatusIcon(presence));
        setUserStatus(EzucePresenceUtils.getUpdatedUserStatus(presence));
    }
    
    @Override
    public String getJID() {        
        return (contact instanceof ContactItemWrapper) ? ((ContactItemWrapper)contact).getContactItem().getJID() : null;
    }

    @Override
    public String getExtension() {
        if (contact instanceof ContactItemWrapper) {
        	String jid = ((ContactItemWrapper)contact).getContactItem().getJID();
        	return Utils.getExtension(jid);
        }
        else {
            return contact.getNumber();
        }
    }

    public void setContact(ContactListEntry ci) {
        this.contact=ci;
    }
    @Override
    public ContactListEntry getContact() {
        return this.contact;
    }

    public MouseListener getLocalMouseListener()
    {
        return localMouseListener;
    }
    
    public void setLocalMouseListener(MouseListener ml)
    {
        this.removeMouseListener(this.localMouseListener);
        this.localMouseListener = ml;        
        this.addMouseListener(this.localMouseListener);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLayeredPane = new JLayeredPane();
        jXPanelGlass = new JXPanel();
        jPanelPic = new JPanel();
        jXLabelPic = new JXLabel();
        jPanelDetails = new JPanel();
        jXLabelDisplayName = new JLabel();
        jXLabel2 = new JXLabel();
        jXLabel3 = new JXLabel();

        setPreferredSize(new Dimension(90,52));
        setBackground(new Color(255, 255, 255));
        setBorder(new CompoundBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(192, 192, 192)), new LineBorder(new Color(255, 255, 255))));
        addMouseListener(localMouseListener);

        jLayeredPane.setMaximumSize(new Dimension(40, 45));
        jLayeredPane.setMinimumSize(new Dimension(40, 45));
        jLayeredPane.setName("jLayeredPane"); // NOI18N

        jXPanelGlass.setAlpha(0.6F);
        jXPanelGlass.setBackgroundPainter(GlossAngleGradientPainter.painter());
        jXPanelGlass.setMaximumSize(new Dimension(40, 45));
        jXPanelGlass.setMinimumSize(new Dimension(40, 45));
        jXPanelGlass.setName("jXPanelGlass"); // NOI18N
        jXPanelGlass.setPreferredSize(new Dimension(40, 45));

        GroupLayout jXPanelGlassLayout = new GroupLayout(jXPanelGlass);
        jXPanelGlass.setLayout(jXPanelGlassLayout);
        jXPanelGlassLayout.setHorizontalGroup(
            jXPanelGlassLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jXPanelGlassLayout.setVerticalGroup(
            jXPanelGlassLayout.createParallelGroup(Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        jXPanelGlass.setBounds(0, 0, 40, 45);
        jLayeredPane.add(jXPanelGlass, JLayeredPane.DEFAULT_LAYER);

        jPanelPic.setBorder(BorderFactory.createLineBorder(new Color(177, 177, 177)));
        jPanelPic.setName("jPanelPic"); // NOI18N
        jPanelPic.setLayout(new BorderLayout());

        jXLabelPic.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, picBorderColor));
        jXLabelPic.setHorizontalAlignment(SwingConstants.CENTER);
        jXLabelPic.setIcon(Images.userGreenSmall);
        jXLabelPic.setAlignmentX(0.5F);
        jXLabelPic.setDoubleBuffered(true);
        jXLabelPic.setIconTextGap(0);
        jXLabelPic.setName("jXLabelPic"); // NOI18N
        jXLabelPic.setOpaque(true);
        jXLabelPic.setTextAlignment(TextAlignment.CENTER);
        jPanelPic.add(jXLabelPic, BorderLayout.CENTER);

        jPanelPic.setBounds(0, 0, 40, 45);
        jLayeredPane.add(jPanelPic, JLayeredPane.DEFAULT_LAYER);

        jPanelDetails.setBackground(new Color(255, 255, 255));
        jPanelDetails.setOpaque(false);
        jPanelDetails.setName("jPanelDetails"); // NOI18N
        jPanelDetails.setLayout(new VerticalLayout());

        LocalPreferences pref = SettingsManager.getLocalPreferences();
	    int fontSize = pref.getContactListFontSize();
	    
        jXLabelDisplayName.setForeground(new Color(46, 85, 102));
        jXLabelDisplayName.setText("User name");
        jXLabelDisplayName.setDoubleBuffered(true);
        jXLabelDisplayName.setFocusable(false);
        jXLabelDisplayName.setFont(deriveFont(new Font("Arial", 1, fontSize),fontSize)); // NOI18N
        jXLabelDisplayName.setName("jXLabelDisplayName"); // NOI18N
        jPanelDetails.add(jXLabelDisplayName);

        jXLabel2.setForeground(new Color(116, 116, 116));
        jXLabel2.setText("Description");
        jXLabel2.setDoubleBuffered(true);
        jXLabel2.setEnabled(false);
        jXLabel2.setFocusable(false);
        jXLabel2.setFont(new Font("Arial", 0, 12)); // NOI18N
        jXLabel2.setName("jXLabel2"); // NOI18N
        jPanelDetails.add(jXLabel2);

        jXLabel3.setForeground(new Color(143, 143, 143));
        jXLabel3.setText("Status");
        jXLabel3.setDoubleBuffered(true);
        jXLabel3.setEnabled(false);
        jXLabel3.setFocusable(false);
        jXLabel3.setFont(new Font("Arial", 0, 12)); // NOI18N
        jXLabel3.setName("jXLabel3"); // NOI18N
        jPanelDetails.add(jXLabel3);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLayeredPane, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jPanelDetails, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
            .addComponent(jLayeredPane, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
            .addComponent(jPanelDetails, 0, 45, Short.MAX_VALUE)
        );
        if (contact instanceof ContactItemWrapper && getJID() != null) {
        	AsyncLoader.getInstance().execute(getJID(), this);
        }
    }                        

    private void formMouseEntered(MouseEvent evt) {                                  
        SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {		
				UserMiniPanelGloss.this.setBorder(PanelBorders.userMiniPanelGlossBorderHoverOn);
			}
		});
    	
    }                                 

    private void formMouseExited(MouseEvent evt) {   
    	SwingUtilities.invokeLater(new Runnable() {			
			@Override
			public void run() {		
				//UserMiniPanelGloss.this.setBorder(PanelBorders.userMiniPanelGlossBorderHoverOff);
				UserMiniPanelGloss.this.setBorder(PanelBorders.userMiniPanelGlossCompoundBorder);
			}
		});  
    	
    }                                

    /**
     * Set the picture of the user to display on the JXLabel (jXLabelPic)
     * inside the JPanel (jPanelPic).
     * @param picture
     */
    public void setUserPicture(Image picture) {
        this.avatarImage=picture;
        ImageIcon imgIcon=new ImageIcon(avatarImage);
        try{
            jXLabelPic.setIcon(Images.ensureSizeScaling(imgIcon,iconDimension.width, iconDimension.height, this.jXLabelPic));
        } catch (Exception ex) {
        	Log.error("Cannot set avatar Image", ex);
        }
    }
    
    public Dimension getAvatarContainerSize(){
    	return this.jPanelPic.getSize();    			
    }
    
    /**
     * Sets the label indicating user status.
     * The status can be taken from StatusLabels fields.
     * @param status
     */
    public void setUserStatusIcon(Icon statusIcon) {
        
    }

    public void setUserDisplayName(final String username) {
        this.jXLabelDisplayName.setText(username);

    }
    @Override
    public String getUserDisplayName() {

        return this.jXLabelDisplayName.getText();
    }

    public void setDescription(final String description) {
        this.jXLabel2.setText(description);
    }
    
    public String getDescription() {
        return this.jXLabel2.getText();
    }

    public void setUserStatusVisible(final boolean v){
    	this.jXLabel3.setVisible(v);
    }
    
    public void setUserStatus(final String status) {
        this.jXLabel3.setText(status);
    }
    public String getUserStatus() {
        return this.jXLabel3.getText();
    }

    @Override
    public void focusGained(FocusEvent e) {
        setBorder(borderUnfocused);        
        revalidate();
    }

    @Override
    public void focusLost(FocusEvent e) {
        setBorder(borderUnfocused);
        revalidate();
    }             

	@Override
	public void vcardLoaded(VCard vCard) {
		updateVCardData();
		if (parent != null) {
	        parent.repaint();
	        parent.revalidate();			
		}
	}
	
	public static Font deriveFont(Font font, int size) {
		if (font == null)
			return null;
		
		if (size < 1)
			size = 1;
		
		return new Font(font.getName(), font.getStyle(), size);
	}
}
