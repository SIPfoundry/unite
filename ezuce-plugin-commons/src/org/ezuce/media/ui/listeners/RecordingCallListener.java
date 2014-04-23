package org.ezuce.media.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import net.java.sip.communicator.service.protocol.CallPeer;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.media.manager.CallRecording;
import org.ezuce.media.manager.PhoneManager;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jitsi.service.neomedia.MediaException;
import org.jivesoftware.spark.util.log.Log;

/**
 *
 * @author Razvan
 */
public class RecordingCallListener implements ActionListener {

    private File _file;
    private final File userDir;

    public RecordingCallListener(File userDirectory) {
        userDir=userDirectory;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JToggleButton) {
            JToggleButton jbt = (JToggleButton) (e.getSource());
            //Log.warning(">>>IS SELECTED "+jbt.isSelected());
            if (jbt.isSelected()) {
                //Log.warning(">>>>>>>>>>>>>> Start call recording... ");
                String filename=PhoneManager.getInstance().getConnectedCall().getCall().getCallID()+".mp3";
                               
                String recFolderPath = CallRecording.getRecordingsFolder(userDir).getAbsolutePath();
               
                String filePath = recFolderPath+"/"+filename;
                   
                _file = new File(filePath);
               
               
               try{
                PhoneManager.getInstance().startRecording(_file);
               }catch(Exception me){
                   Log.error("COULD NOT RECORD CALL !");
                   Log.error(me);
               }
            } 
            else {
                //Log.error(">>>>>>>>>>>>>> Stop call recording... ");
                PhoneManager.getInstance().stopRecording();
                try{
                    MP3File fl = null;
                   
                    fl = (MP3File) AudioFileIO.read(_file.getAbsoluteFile());
                    Tag t = new ID3v1Tag();
                        String comm = "";
                    Iterator<? extends CallPeer> peers = PhoneManager.getInstance().getConnectedCall().getCall().getCallPeers();
                    CallPeer peer;
                    if (peers.hasNext()) {
                        peer = peers.next();
                        if (peer.getContact() != null
                                && !StringUtils.isEmpty(peer.getContact().getDisplayName())) {
                            CallPeer next = peers.next();
                            comm = next.getContact().getDisplayName();

                        } else {
                            comm = peer.getAddress();
                        }
                    }
                    //Log.warning(">=========Comment "+comm);
                    t.setField(FieldKey.COMMENT,comm);
                    fl.setTag(t);
                    fl.commit();
                }
                catch(Exception ex)
                {
                    Log.error(ex.getMessage());
                }
            }
        }
    }
}