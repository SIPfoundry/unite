package org.ezuce.common.ui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ezuce.common.call.events.SoundEvent;
import org.ezuce.common.call.events.SoundListener;
import org.ezuce.common.ui.listener.PlayCallListener;
import org.ezuce.lafs.EzuceVmSliderUI;
import org.ezuce.media.manager.CallRecording;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class RecordingMiniPanel extends javax.swing.JPanel {

    /**
     * Creates new form RecordingMiniPanel
     */
    public RecordingMiniPanel() {
        initComponents();
        customize();
    }
    
    public RecordingMiniPanel(CallRecording cr) {
        initComponents();
        this.callRecording = cr;
        customize();
    }
    
    protected void customize()
    {
        this.setBackground(mouseOutBackground);
        
        jButtonPlayStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButtonPlayActionPerformed(evt);
            }
        });  
        
        
        try {
            //Log.warning(">>>>>>Customize");
            this.soundListener = new PlayCallListener(callRecording.getFile());
            this.timer = new javax.swing.Timer(100, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        tick();
                    } catch (IOException ex) {
                        Log.error(ex);
                    }
                }
            });
            // Whenever the slider value changes, if we're not already
            // at the new position, skip to it.
            this.jSlider.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    if (!jSlider.getValueIsAdjusting()) {
                        //System.out.println("Slider poz "+jSlider.getValue());
                        int value = jSlider.getValue();
                        int secs = value / 1000;
                        //System.out.println("Min "+secs/60);
                        //System.out.println("Sec "+secs%60);
                        jLabelDuration.setText(String.format("%02d", secs / 60) + ":" + String.format("%02d", secs % 60));
                    }
                }
            });
            this.jSlider.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent me) {
                }

                @Override
                public void mousePressed(MouseEvent me) {
                    paused = true;
                    timer.stop();
                }

                @Override
                public void mouseEntered(MouseEvent me) {
                }

                @Override
                public void mouseExited(MouseEvent me) {
                }

                @Override
                public void mouseReleased(MouseEvent me) {
                    skipValue = jSlider.getValue();
                    skip();
                }
            });

            showVoicemailDuration();
            //jLabelDuration.setText("####");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }
    
    
    private void jButtonPlayActionPerformed(ActionEvent evt) {                                            
        //Log.warning("Play Action Performed");
        if (this.soundListener != null)
        {
            //Log.warning(">>>>>>Playiiing");
            if (!this.playing)
            {
                //Log.warning(">>>>>>Playiiing123");
                this.play();
            } else {
                this.stop();
            }
        }
    }
    
    private void tick() throws IOException {
        if (!paused) {
            if (soundListener.isActive()) {
                audioPosition = (int) (soundListener.getMicrosecondPosition());
                jSlider.setValue(audioPosition);
                if (audioPosition==soundListener.getAudioLength())
                {
                    reset();
                }
                //System.out.println("JSlider "+jSlider.getValue());
            } else {
                reset();
            }
        }
    }
    
     public void reset() {
        stop();
        //clip.setMicrosecondPosition(0);
        soundListener.reset();
        audioPosition = 0;
        jSlider.setValue(0);
        paused = false;
        showVoicemailDuration();
    }
    
    
     public void showVoicemailDuration()
     {       
         SwingUtilities.invokeLater(new Runnable()
         {
            @Override
            public void run() {
               
                int secs = audioLength;
                RecordingMiniPanel.this.jLabelDuration.setText(String.format("%02d", secs / 60)
                                                                + ":"
                                                                + String.format("%02d", secs % 60));
            }
         });
    }
     
     private void stop() {
        timer.stop();
        soundListener.stop();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jButtonPlayStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/playRecIcon.png")));
                jButtonPlayStop.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/playRecIcon_over.png")));
               
            }
        });
        playing = false;
        if (soundListener.IsMP3()) {
            soundListener.setInitialized(false);
        } else {
            soundListener.setInitialized(true);
        }
        paused = false;
        showVoicemailDuration();
    }
    
     private void play()
     {
        if (!soundListener.isInitialized()) {
            //Log.warning(">>>>>>Playiiing444");
            soundListener.preparePlay(new SoundEvent(this));
            //Log.warning(">>>>>>Playiiing555");
            jSlider.setMaximum(soundListener.getAudioLength());          
        }
        soundListener.start();

        timer.start();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                jButtonPlayStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/stopPlayRecIcon.png")));
                jButtonPlayStop.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/stopPlayRecIcon_over.png")));
            }
        });
        playing = true;
        paused = false;
    }
   
     private void skip() { // Called when user drags the slider
        soundListener.setStopped(true);
        int position = skipValue;
        audioPosition = position;
        //clip.setMicrosecondPosition(position * 1000);
        int x = soundListener.currentPositionInBytes;
        soundListener.setMicrosecondPosition(position);
        // System.out.println("AudioPosition - scroll position "+position);
        //System.out.println(" "+soundListener.currentPositionInBytes);
        jSlider.setValue(position); // in case skip( ) is called from outside
        soundListener.setInitialized(false);
        //System.out.println(" " + soundListener.currentPositionInBytes);
        soundListener.preparePlay(new SoundEvent(this), soundListener.currentPositionInBytes - x);

        paused = false;

        timer.start();
        soundListener.setStopped(false);
    }
    
    public JSlider getJSlider()
    {
        return this.jSlider;
    }
    
    public JButton getJButtonPlayStop()
    {
        return this.jButtonPlayStop;
    }
    
    public JButton getJButtonDelete()
    {
        return this.jButtonDelete;
    }
    
    public void setDisplayName(String name)
    {
        this.jLabelDisplayName.setText(name);
    }
    
    public String getDisplayName()
    {
        return this.jLabelDisplayName.getText();
    }
    
    public void setRecDetails(String details)
    {
        this.jLabelRecDetails.setText(details);
    }
    
    public String getRecDetails()
    {
        return this.jLabelRecDetails.getText();
    }
    
    public void setDuration(String duration)
    {
        this.jLabelDuration.setText(duration);
    }
    
    public String getDuration()
    {
        return this.jLabelDuration.getText();
    }
    
    public void setRecIcon(Icon icn)
    {
        this.jLabelIcon.setIcon(icn);
    }
    
    public Icon getRecIcon()
    {
        return this.jLabelIcon.getIcon();
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fillerTop = new javax.swing.Box.Filler(new java.awt.Dimension(0, 3), new java.awt.Dimension(0, 3), new java.awt.Dimension(32767, 3));
        jPanelLeft = new javax.swing.JPanel();
        jLabelIcon = new javax.swing.JLabel();
        jPanelCenter = new javax.swing.JPanel();
        jPanelDisplayName = new javax.swing.JPanel();
        jLabelDisplayName = new javax.swing.JLabel();
        jPanelDetails = new javax.swing.JPanel();
        jLabelRecDetails = new javax.swing.JLabel();
        jPanelPlayer = new javax.swing.JPanel();
        jButtonPlayStop = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        jSlider = new javax.swing.JSlider();
        jSlider.setUI(new EzuceVmSliderUI(jSlider));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 0), new java.awt.Dimension(4, 32767));
        jLabelDuration = new javax.swing.JLabel();
        jPanelRight = new javax.swing.JPanel();
        jButtonDelete = new javax.swing.JButton();
        fillerBottom = new javax.swing.Box.Filler(new java.awt.Dimension(0, 5), new java.awt.Dimension(0, 5), new java.awt.Dimension(32767, 5));

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 10));
        setName("Form"); // NOI18N
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
        });
        setLayout(new java.awt.BorderLayout(5, 5));

        fillerTop.setName("fillerTop"); // NOI18N
        add(fillerTop, java.awt.BorderLayout.PAGE_START);

        jPanelLeft.setName("jPanelLeft"); // NOI18N
        jPanelLeft.setOpaque(false);
        jPanelLeft.setLayout(new java.awt.BorderLayout());

        jLabelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/conference_off.png"))); // NOI18N
        jLabelIcon.setName("jLabelIcon"); // NOI18N
        jPanelLeft.add(jLabelIcon, java.awt.BorderLayout.NORTH);

        add(jPanelLeft, java.awt.BorderLayout.WEST);

        jPanelCenter.setName("jPanelCenter"); // NOI18N
        jPanelCenter.setOpaque(false);
        jPanelCenter.setLayout(new javax.swing.BoxLayout(jPanelCenter, javax.swing.BoxLayout.PAGE_AXIS));

        jPanelDisplayName.setName("jPanelDisplayName"); // NOI18N
        jPanelDisplayName.setOpaque(false);
        jPanelDisplayName.setLayout(new java.awt.BorderLayout());

        jLabelDisplayName.setBackground(new java.awt.Color(46, 85, 102));
        jLabelDisplayName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelDisplayName.setForeground(new java.awt.Color(46, 85, 102));
        jLabelDisplayName.setText("User name / extension");
        jLabelDisplayName.setName("jLabelDisplayName"); // NOI18N
        jPanelDisplayName.add(jLabelDisplayName, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelDisplayName);

        jPanelDetails.setName("jPanelDetails"); // NOI18N
        jPanelDetails.setOpaque(false);
        jPanelDetails.setLayout(new java.awt.BorderLayout());

        jLabelRecDetails.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelRecDetails.setForeground(new java.awt.Color(153, 153, 153));
        jLabelRecDetails.setText("01.01.2000 12:00 pm");
        jLabelRecDetails.setName("jLabelRecDetails"); // NOI18N
        jPanelDetails.add(jLabelRecDetails, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelDetails);

        jPanelPlayer.setName("jPanelPlayer"); // NOI18N
        jPanelPlayer.setOpaque(false);
        jPanelPlayer.setLayout(new javax.swing.BoxLayout(jPanelPlayer, javax.swing.BoxLayout.LINE_AXIS));

        jButtonPlayStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/playRecIcon.png"))); // NOI18N
        jButtonPlayStop.setBorder(null);
        jButtonPlayStop.setBorderPainted(false);
        jButtonPlayStop.setContentAreaFilled(false);
        jButtonPlayStop.setDoubleBuffered(true);
        jButtonPlayStop.setFocusPainted(false);
        jButtonPlayStop.setName("jButtonPlayStop"); // NOI18N
        jButtonPlayStop.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/playRecIcon.png"))); // NOI18N
        jButtonPlayStop.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/playRecIcon_over.png"))); // NOI18N
        jButtonPlayStop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonPlayStopMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonPlayStopMouseExited(evt);
            }
        });
        jPanelPlayer.add(jButtonPlayStop);

        filler1.setName("filler1"); // NOI18N
        jPanelPlayer.add(filler1);

        jSlider.setValue(0);
        jSlider.setName("jSlider"); // NOI18N
        jSlider.setOpaque(false);
        jSlider.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jSliderMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jSliderMouseExited(evt);
            }
        });
        jPanelPlayer.add(jSlider);

        filler2.setName("filler2"); // NOI18N
        jPanelPlayer.add(filler2);

        jLabelDuration.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabelDuration.setForeground(new java.awt.Color(153, 153, 153));
        jLabelDuration.setText("00:00");
        jLabelDuration.setName("jLabelDuration"); // NOI18N
        jPanelPlayer.add(jLabelDuration);

        jPanelCenter.add(jPanelPlayer);

        add(jPanelCenter, java.awt.BorderLayout.CENTER);

        jPanelRight.setName("jPanelRight"); // NOI18N
        jPanelRight.setOpaque(false);
        jPanelRight.setLayout(new java.awt.BorderLayout());

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/deleteRec.png"))); // NOI18N
        jButtonDelete.setBorder(null);
        jButtonDelete.setBorderPainted(false);
        jButtonDelete.setContentAreaFilled(false);
        jButtonDelete.setDoubleBuffered(true);
        jButtonDelete.setFocusPainted(false);
        jButtonDelete.setName("jButtonDelete"); // NOI18N
        jButtonDelete.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/deleteRec.png"))); // NOI18N
        jButtonDelete.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/deleteRec_over.png"))); // NOI18N
        jButtonDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonDeleteMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonDeleteMouseExited(evt);
            }
        });
        jPanelRight.add(jButtonDelete, java.awt.BorderLayout.PAGE_END);

        add(jPanelRight, java.awt.BorderLayout.EAST);

        fillerBottom.setName("fillerBottom"); // NOI18N
        add(fillerBottom, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
       mouseEntered();        
    }//GEN-LAST:event_formMouseEntered

    protected void mouseEntered(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RecordingMiniPanel.this.setBackground(mouseOverBackground);
            }
        });
    }
    
    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        mouseExited();
    }//GEN-LAST:event_formMouseExited

    protected void mouseExited(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RecordingMiniPanel.this.setBackground(mouseOutBackground);
            }
        });
    }            
    
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        mouseClicked();
    }//GEN-LAST:event_formMouseClicked

    private void jSliderMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSliderMouseEntered
        mouseEntered();
    }//GEN-LAST:event_jSliderMouseEntered

    private void jSliderMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jSliderMouseExited
        mouseExited();
    }//GEN-LAST:event_jSliderMouseExited

    private void jButtonDeleteMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDeleteMouseEntered
        mouseEntered();
    }//GEN-LAST:event_jButtonDeleteMouseEntered

    private void jButtonDeleteMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonDeleteMouseExited
        mouseExited();
    }//GEN-LAST:event_jButtonDeleteMouseExited

    private void jButtonPlayStopMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPlayStopMouseEntered
        mouseEntered();
    }//GEN-LAST:event_jButtonPlayStopMouseEntered

    private void jButtonPlayStopMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPlayStopMouseExited
        mouseExited();
    }//GEN-LAST:event_jButtonPlayStopMouseExited

    protected void mouseClicked() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RecordingMiniPanel.this.selected = !RecordingMiniPanel.this.selected;
                if (RecordingMiniPanel.this.selected)
                {
                    RecordingMiniPanel.this.setBackground(selectedBackground);
                }
                else
                {
                    RecordingMiniPanel.this.setBackground(mouseOutBackground);
                }
            }
        });
    }    
    
    public CallRecording getCallRecording()
    {
    	return this.callRecording;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler fillerBottom;
    private javax.swing.Box.Filler fillerTop;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonPlayStop;
    private javax.swing.JLabel jLabelDisplayName;
    private javax.swing.JLabel jLabelDuration;
    private javax.swing.JLabel jLabelIcon;
    private javax.swing.JLabel jLabelRecDetails;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelDetails;
    private javax.swing.JPanel jPanelDisplayName;
    private javax.swing.JPanel jPanelLeft;
    private javax.swing.JPanel jPanelPlayer;
    private javax.swing.JPanel jPanelRight;
    private javax.swing.JSlider jSlider;
    // End of variables declaration//GEN-END:variables
    private static Color mouseOverBackground = new Color(255,255,153);
    private static Color mouseOutBackground = new Color(240,240,240);
    private static Color selectedBackground = new Color(255,255,153);
    private boolean selected=false;
    public boolean playing = false;
    private volatile boolean paused = false;    
    private CallRecording callRecording;
    private SoundListener soundListener = null;
    private int audioPosition = 0; // Current position within the sound
    public int audioLength; // Length of the sound.
    private Timer timer = null; //Timer to synchronize the jSlider with the audio clip.
    private int skipValue;
}
