package sources;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

public class AudioVideoScreenSharingPanel extends javax.swing.JPanel {

    /**
     * Creates new form AudioVideoScreenSharingPanel
     */
    public AudioVideoScreenSharingPanel() {
        initComponents();
    }

    /**
     * Gets the panel that hosts either the screen sharing panel, or the video
     * call panel.
     *
     * @return
     */
    public ScreenSharingJPanel getScreenSharingPanel() {
        return this.screenSharingJPanel;
    }

    public AudioCallJPanel getAudioCallJPanel() {
        return this.audioCallJPanel;
    }

    public VideoCallJPanel getVideoCallJPanel() {
        return this.videoCallJPanel;
    }

    /**
     * Displays the screen sharing panel above the user's avatar.
     */
    public void showScreenSharingPanel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (AudioVideoScreenSharingPanel.this.screenSharingJPanel != null) {
                    AudioVideoScreenSharingPanel.this.screenSharingJPanel.setVisible(true);
                }
            }
        });
    }

    public void showAudioCallPanel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (AudioVideoScreenSharingPanel.this.audioCallJPanel != null) {
                    AudioVideoScreenSharingPanel.this.audioCallJPanel.setVisible(true);
                }
            }
        });
    }

    public void showVideoCallPanel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (AudioVideoScreenSharingPanel.this.videoCallJPanel != null) {
                    AudioVideoScreenSharingPanel.this.videoCallJPanel.setVisible(true);
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ImageIcon ii = resourceMap.getImageIcon("Background");
        if (ii != null) {
            Image i = ii.getImage();
            if (i != null) {
                g.drawImage(i, 0, 0, getWidth(), getHeight(),
                        0, 0, ii.getIconWidth(), ii.getIconHeight(), null);
            }
        }
    }
    
                                                 
 private void init()
    {
        this.audioCallJPanel.getTglBtnScreenSharing().addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                tglBtnScreenSharingActionPerformed(evt);
            }
        });
        
        this.screenSharingJPanel.getBtnTglMouse().addActionListener(new java.awt.event.ActionListener() 
        {
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                tglBtnMouseActionPerformed(evt);
            }
        });
        
    }
    
    private void tglBtnScreenSharingActionPerformed(java.awt.event.ActionEvent evt) {                                                
        // TODO add your handling code here:
        startScreenSharing();
    }
    
     private void tglBtnMouseActionPerformed(ActionEvent evt) 
     {
         confirmScreenSharing();
     }
                
    private void startScreenSharing()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() 
            {
                if(audioCallJPanel.getTglBtnScreenSharing().isSelected())
                {
                    screenSharingJPanel.setVisible(true);
                    screenSharingJPanel.getLblStatus().setText(resourceMap.getString("ConnectingStatus"));
                    screenSharingJPanel.getBtnTglMouse().setVisible(false);
                    screenSharingJPanel.getBtnTglPauseVideo().setVisible(false);

                }
                else
                {
                    screenSharingJPanel.setVisible(false);
                }
            }
        });
    }
    
    //cand il incep efectiv
    private void beginScreenSharing()
    {
        //de luat stringul din properties
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            screenSharingJPanel.getLblStatus().setText(resourceMap.getString("ActiveStatus"));
            screenSharingJPanel.getBtnTglMouse().setVisible(true);
            screenSharingJPanel.getBtnTglPauseVideo().setVisible(true);
            screenSharingJPanel.getLblStatusIcon().setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/screen-sharing_active.png")));
            }
        });
    }
    
    public void confirmScreenSharing()
    {
            SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                screenSharingJPanel.getLblStatus().setText(resourceMap.getString("AcceptScreenSharing")); 
                //this.screenSharingJPanel.setStare(false);
                screenSharingJPanel.getjPanel1().removeAll();
                screenSharingJPanel.getjPanel2().removeAll();
                screenSharingJPanel.getjPanel1().add(screenSharingJPanel.getBtnStopScreenSharing());
                screenSharingJPanel.getjPanel1().add(screenSharingJPanel.getBtnTglPauseVideo());
                screenSharingJPanel.getBtnTglMouse().setVisible(false);
                screenSharingJPanel.getjPanel1().setPreferredSize(new Dimension(62, 28));
                screenSharingJPanel.getjPanel1().setMinimumSize(new Dimension(62, 28));
                screenSharingJPanel.getjPanel1().setMaximumSize(new Dimension(62, 28));
                screenSharingJPanel.setStare(true);
                repaint();
                revalidate();
            }
        });
             
    }
    
    public void acceptScreenSharing()
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {        
                screenSharingJPanel.getLblStatus().setText(resourceMap.getString("ActiveStatus"));   
                screenSharingJPanel.getBtnTglMouse().setVisible(false);
                screenSharingJPanel.getBtnTglPauseVideo().setVisible(false);
                screenSharingJPanel.getLblStatusIcon().setIcon(new javax.swing.ImageIcon(getClass().getResource("/sources/resources/screen-sharing_active.png")));
               // this.screenSharingJPanel.setStare(true);
                screenSharingJPanel.setStare(false);
                revalidate();
                repaint();
            }});
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        videoCallJPanel = new sources.VideoCallJPanel();
        screenSharingJPanel = new sources.ScreenSharingJPanel();
        audioCallJPanel = new sources.AudioCallJPanel();

        setName("Form"); // NOI18N

        videoCallJPanel.setName("videoCallJPanel"); // NOI18N

        screenSharingJPanel.setName("screenSharingJPanel"); // NOI18N

        audioCallJPanel.setName("audioCallJPanel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(videoCallJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(screenSharingJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(audioCallJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(videoCallJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(screenSharingJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(audioCallJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private sources.AudioCallJPanel audioCallJPanel;
    private sources.ScreenSharingJPanel screenSharingJPanel;
    private sources.VideoCallJPanel videoCallJPanel;
    // End of variables declaration//GEN-END:variables
    final ResourceMap resourceMap = Application.getInstance().getContext().getResourceMap(AudioVideoScreenSharingPanel.class);
}
