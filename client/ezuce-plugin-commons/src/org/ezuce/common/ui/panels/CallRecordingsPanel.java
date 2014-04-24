package org.ezuce.common.ui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.ezuce.common.ui.EzuceRecListTabbedPaneUI;
import org.ezuce.common.ui.panels.RecordingMiniPanel;
import org.ezuce.media.manager.CallRecording;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class CallRecordingsPanel extends javax.swing.JPanel {

    /**
     * Creates new form CallRecordingsPanel
     */
    public CallRecordingsPanel() {
        initComponents();
        this.jTabbedPane.setUI(new EzuceRecListTabbedPaneUI());
        this.configureTabHeaders();        
    }        
    
    protected void configureTabHeaders()
    {
        JLabel conferencesLbl = new JLabel("REC");
        conferencesLbl.setIcon(new ImageIcon(getClass().getResource("/resources/images/conf_recs.png")));
        conferencesLbl.setForeground(new Color(46, 85, 102));
        this.jTabbedPane.setTabComponentAt(0,conferencesLbl);
        
        JLabel callsLbl = new JLabel("REC");
        callsLbl.setIcon(new ImageIcon(getClass().getResource("/resources/images/call_recs.png")));
        callsLbl.setForeground(new Color(46, 85, 102));
        this.jTabbedPane.setTabComponentAt(1, callsLbl);              
            
    }
    
    public void addConferenceRecording(Component confRec)
    {
        this.jPanelConferences.add(confRec);
        this.jPanelConferences.revalidate();
        this.jPanelConferences.repaint();
    }
    
    public void addConferenceRecordings(List<Component> confRecs)
    {
        if (confRecs!=null)
        {
            for (Component c : confRecs)
            {
                this.jPanelConferences.add(c);
            }
            
            this.jPanelConferences.revalidate();
            this.jPanelConferences.repaint();
        }
    }
    
    public void removeConferenceRecording(Component confRec)
    {
        this.jPanelConferences.remove(confRec);
        this.jPanelConferences.revalidate();
        this.jPanelConferences.repaint();
    }
    
    public void addCallRecording(Component callRec)
    {
        this.jPanelCalls.add(callRec);
        this.jPanelCalls.revalidate();
        this.jPanelCalls.repaint();
    }
    
    public void addCallRecordings(List<Component> callRecs)
    {
        if (callRecs!=null)
        {
            for (Component c : callRecs)
            {
                this.jPanelCalls.add(c);
            }
            
            this.jPanelCalls.revalidate();
            this.jPanelCalls.repaint();
        }
    }
    
    public void removeCallRecording(Component callRec)
    {
        this.jPanelCalls.remove(callRec);
        this.jPanelCalls.revalidate();
        this.jPanelCalls.repaint();
    }
    

    public JPanel getJPanelCalls()
    {
        return this.jPanelCalls;
    }
    
    public JPanel getJPanelConferences()
    {
        return this.jPanelConferences;
    }
    
    public void retrieveCallRecordings()
    {
       
        File folder = CallRecording.getRecordingsFolder(SparkManager.getUserDirectory());
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles)
        {
            if (file.isFile())
            {
               CallRecording ro = new CallRecording(file);
               if (ro.getIdUser()!=null){
	               final RecordingMiniPanel rmp = new RecordingMiniPanel(ro);
	               rmp.getJButtonDelete().addActionListener(new ActionListener() {				
					@Override
					public void actionPerformed(ActionEvent e) {
						//Log.warning("################# Deleting file:"+rmp.getCallRecording().getFile().getName());
						boolean success=rmp.getCallRecording().getFile().delete();
						if (success){
							removeCallRecording(rmp);
						}
					}
	               });
	               rmp.setDuration(ro.getDuration()+"");
	               rmp.setRecDetails(ro.getRecordingDate());
	               rmp.setDisplayName(ro.getIdUser());
	               rmp.audioLength = ro.getAudioLength();
	               addCallRecording(rmp);
               }
            }
        }
    }
    
    public void retrieveConfRecordings(){
    	
    }
    
    public void clearCallRecordings()
    {
        //Log.warning(">>>Comp count JPC "+this.jPanelCalls.getComponents().length);
        List<Component> toRemove=new ArrayList<Component>();      
        for(Component c : this.jPanelCalls.getComponents())
        {
            //Log.warning(">>>Comp count JPC"+c.getClass()+" "+c.getName());
            if(c instanceof RecordingMiniPanel)
            {
                //Log.warning(">>>>RecordingMiniPanel>>>>>>");
                try
                {
                    if(((RecordingMiniPanel)c).playing)
                    {
                        //Log.warning(">>>>REMOVE>>>>>>");
                        ((RecordingMiniPanel)c).reset();
                    } 
                    toRemove.add(c);
                    c=null;
                }
                catch(Exception e)
                {
                    Log.error(e.getMessage());
                }
            }
        }
        for (Component comp : toRemove){
        	this.jPanelCalls.remove(comp);
        }
        this.jPanelCalls.repaint();
       
    }
   
    public void clearConfRecordings()
    {
       
    }
    
    public void refreshCallRecordings(){
    	clearCallRecordings();
    	retrieveCallRecordings();
    }
    
    public void refreshConfRecordings(){
    	clearConfRecordings();
    	retrieveConfRecordings();
    }
    
    public boolean isCallRecTabSelected(){
    	return (this.jTabbedPane.getSelectedComponent() == jScrollPaneCallRecordings);
    }
    
    public boolean isConfRecTabSelected(){
    	return (this.jTabbedPane.getSelectedComponent() == jScrollPaneConfRecordings);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane = new javax.swing.JTabbedPane();
        jScrollPaneConfRecordings = new javax.swing.JScrollPane();
        jPanelConferences = new javax.swing.JPanel();
        jScrollPaneCallRecordings = new javax.swing.JScrollPane();
        jPanelCalls = new javax.swing.JPanel();

        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        jTabbedPane.setDoubleBuffered(true);
        jTabbedPane.setName("jTabbedPane"); // NOI18N

        jScrollPaneConfRecordings.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPaneConfRecordings.setName("jScrollPaneConfRecordings"); // NOI18N

        jPanelConferences.setBackground(new java.awt.Color(255, 255, 255));
        jPanelConferences.setName("jPanelConferences"); // NOI18N
        org.jdesktop.swingx.VerticalLayout verticalLayout1 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout1.setGap(3);
        jPanelConferences.setLayout(verticalLayout1);
        jScrollPaneConfRecordings.setViewportView(jPanelConferences);

        jTabbedPane.addTab("tab1", jScrollPaneConfRecordings);

        jScrollPaneCallRecordings.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPaneCallRecordings.setName("jScrollPaneCallRecordings"); // NOI18N

        jPanelCalls.setBackground(new java.awt.Color(255, 255, 255));
        jPanelCalls.setName("jPanelCalls"); // NOI18N
        org.jdesktop.swingx.VerticalLayout verticalLayout2 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout2.setGap(3);
        jPanelCalls.setLayout(verticalLayout2);
        jScrollPaneCallRecordings.setViewportView(jPanelCalls);

        jTabbedPane.addTab("tab2", jScrollPaneCallRecordings);

        add(jTabbedPane, java.awt.BorderLayout.CENTER);
        jTabbedPane.getAccessibleContext().setAccessibleName("recTabs");
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelCalls;
    private javax.swing.JPanel jPanelConferences;
    private javax.swing.JScrollPane jScrollPaneCallRecordings;
    private javax.swing.JScrollPane jScrollPaneConfRecordings;
    private javax.swing.JTabbedPane jTabbedPane;
    // End of variables declaration//GEN-END:variables


}
