package org.ezuce.panels;

import org.apache.commons.lang3.StringUtils;
import org.ezuce.common.ConfigureVoice;
import org.ezuce.common.VoiceUtil;
import org.ezuce.common.resource.Config;
import org.ezuce.common.rest.RestManager;
import org.ezuce.common.ui.panels.UserMiniPanelGloss;
import org.ezuce.common.ui.panels.UserGroupPanel;
import org.ezuce.common.ui.panels.MakeCallPanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.Beans;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import org.ezuce.common.rest.Cdr;

import org.ezuce.common.ui.actions.task.DownloadPhoneBookTask;
import org.ezuce.common.ui.actions.task.MakeCallTask;
import org.ezuce.media.manager.UIMediaManager;
import org.ezuce.tasks.PopulateHistoryListTask;
import org.ezuce.common.ui.actions.task.SearchTask;
import org.ezuce.common.ui.wrappers.interfaces.CallPanelInterface;
import org.ezuce.common.ui.wrappers.interfaces.SearchInterface;
import org.ezuce.tasks.DeleteTempVoiceMailsTask;
import org.ezuce.tasks.ShowLastCallsTask;
import org.ezuce.wrappers.interfaces.HistoryItem.HistoryItemTypes;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.swingx.JXSearchField;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.component.tabbedPane.SparkTabbedPane;
import org.jivesoftware.spark.util.log.Log;

public class CallPanel2 extends JSplitPane implements SearchInterface, CallPanelInterface, ConfigureVoice {
    private int MAKE_CALL_PANEL_HEIGHT_SHOW_DIAL_PAD = 311;//316;
    private int MAKE_CALL_PANEL_HEIGHT_HIDE_DIAL_PAD = 70;//65;
    // Variables declaration - do not modify                     
    private CallHistoryPanel2 callHistoryPanel2;
    private FindMePanel findMePanel;
    private JPanel jPanelMultiPane;
    private MakeCallPanel makeCallPanel;
    private MakeCallTopPanel makeCallTopPanel;
    // End of variables declaration                   
    private Task searchTask;
    private Task populateHistoryTask;
    private Task deleteTempMp3Task;
    private final Dimension topPanelDimExpanded=new Dimension(382,MAKE_CALL_PANEL_HEIGHT_SHOW_DIAL_PAD);
    private final Dimension topPanelDimRetracted=new Dimension(382,39);
    private final Dimension topPanelDimExpandedWithDP=new Dimension(382,MAKE_CALL_PANEL_HEIGHT_SHOW_DIAL_PAD);
    private final Dimension topPanelDimExpandedWODP=new Dimension(382,79);    
    javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(this);    
	protected KeyListener makeCallKeyListener = new KeyAdapter() {
		@Override
		public void keyReleased(KeyEvent ke) {
			if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
				if (ke.getSource() instanceof JTextField
						&& !((JTextField) ke.getSource()).getText().isEmpty()) {
					Task callTask = Call();
					callTask.execute();
				}
			}
		}
	};	
    
    /** Creates new form CallPanel2 */
    public CallPanel2(SparkTabbedPane parent) {
    	super(JSplitPane.VERTICAL_SPLIT);                
        this.setResizeWeight(0.0);
        initComponents();
        wireupActions();
        try {
			VoiceUtil.configure(this);
		} catch (Exception e) {
			Log.error("Exception during configuration ", e);
		}
        SwingUtilities.invokeLater(new PhonebookRunnable(this));
       this.setDividerSize(1);
        flattenJSplitPane(this);        
        this.makeCallTopPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {                         
                
                //height of Unite window:
                int windowHeight = SparkManager.getMainWindow().getSize().height;
                
                //size of the screen:
                final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                //height of the task bar:
                final Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
                
                //height of taskbar:                
                int taskBarSize = scnMax.bottom;
                
                int availableHeight = screenSize.height-taskBarSize;
                
                if (windowHeight > availableHeight){
                    SparkManager.getMainWindow()
                            .setSize(SparkManager.getMainWindow().getSize().width, 
                                    availableHeight);
                }
                Dimension makeCallTopPanelDim = makeCallTopPanel.getSize();
                CallPanel2.this.setDividerLocation(makeCallTopPanelDim.height);
            }            
        });
        CallPanel2.this.setDividerLocation(makeCallTopPanel.getPreferredSize().height);         
    }
    
    public static void flattenJSplitPane(JSplitPane splitPane) {
        splitPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BasicSplitPaneUI flatDividerSplitPaneUI = new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void setBorder(Border b) {
                    }
                };
            }
        };
        splitPane.setUI(flatDividerSplitPaneUI);
        splitPane.setBorder(null);
    }

    /**
     * Configure an action for each of the buttons displayed in each of the panels.
     */
    private void wireupActions() {
        final ResourceMap callResMap = Application.getInstance().getContext().getResourceMap(CallPanel2.class);
        CallTabBtnPanel ctbp=this.makeCallTopPanel.getCallTabBtnPanel();

        
        javax.swing.Action action=actionMap.get("BringUpMakeACallPanel");
        
        
        action.putValue(action.SMALL_ICON, ctbp.getjToggleButtonMakeACall().getIcon());
        ctbp.getjToggleButtonMakeACall().setAction(action);
        ctbp.getjToggleButtonMakeACall().setBorder(null);                
        ctbp.getjToggleButtonMakeACall().setToolTipText(callResMap.getString("CallPanel.CallToggleButton.Tooltip"));

        action=actionMap.get("BringUpFindMePanel");
        action.putValue(action.SMALL_ICON, ctbp.getjToggleButtonFindMe().getIcon());
        ctbp.getjToggleButtonFindMe().setAction(action);
        ctbp.getjToggleButtonFindMe().setBorder(null);
        ctbp.getjToggleButtonFindMe().setToolTipText(callResMap.getString("CallPanel.FindMeToggleButton.Tooltip"));
        
        action=actionMap.get("BringUpCallHistoryPanel");
        action.putValue(action.SMALL_ICON, ctbp.getjToggleButtonMissedCalls().getIcon());
        ctbp.getjToggleButtonMissedCalls().setActionCommand("");
        ctbp.getjToggleButtonMissedCalls().setAction(action);
        ctbp.getjToggleButtonMissedCalls().setBorder(null);
        ctbp.getjToggleButtonMissedCalls().setToolTipText(callResMap.getString("CallPanel.CallHistoryToggleButton.Tooltip"));
        
        
        DialPad dp=makeCallTopPanel.getDialPadPanel().getDialPad();

        action=actionMap.get("Dial0");
        action.putValue(action.SMALL_ICON, dp.getjButtonDial0().getIcon());
        dp.getjButtonDial0().setAction(action);

        action=actionMap.get("Dial1");
        action.putValue(action.SMALL_ICON, dp.getjButtonDial1().getIcon());
        dp.getjButtonDial1().setAction(action);

        action=actionMap.get("Dial2");
        action.putValue(action.SMALL_ICON, dp.getjButtonDial2().getIcon());
        dp.getjButtonDial2().setAction(action);

        action=actionMap.get("Dial3");
        action.putValue(action.SMALL_ICON, dp.getjButtonDial3().getIcon());
        dp.getjButtonDial3().setAction(action);

        action=actionMap.get("Dial4");
        action.putValue(action.SMALL_ICON, dp.getjButtonDial4().getIcon());
        dp.getjButtonDial4().setAction(action);

        action=actionMap.get("Dial5");
        action.putValue(action.SMALL_ICON, dp.getjButtonDial5().getIcon());
        dp.getjButtonDial5().setAction(action);

        action=actionMap.get("Dial6");
        action.putValue(action.SMALL_ICON, dp.getjButtonDial6().getIcon());
        dp.getjButtonDial6().setAction(action);

        action=actionMap.get("Dial7");
        action.putValue(action.SMALL_ICON, dp.getjButtonDial7().getIcon());
        dp.getjButtonDial7().setAction(action);

        action=actionMap.get("Dial8");
        action.putValue(action.SMALL_ICON, dp.getjButtonDial8().getIcon());
        dp.getjButtonDial8().setAction(action);

        action=actionMap.get("Dial9");
        action.putValue(action.SMALL_ICON, dp.getjButtonDial9().getIcon());
        dp.getjButtonDial9().setAction(action);

        action=actionMap.get("DialAst");
        action.putValue(action.SMALL_ICON, dp.getjButtonDialStar().getIcon());
        dp.getjButtonDialStar().setAction(action);

        action=actionMap.get("DialDiez");
        action.putValue(action.SMALL_ICON, dp.getjButtonDialDiez().getIcon());
        dp.getjButtonDialDiez().setAction(action);

        action=actionMap.get("DialRedial");
        action.putValue(action.SMALL_ICON, dp.getjButtonDialRedial().getIcon());
        dp.getjButtonDialRedial().setAction(action);

        action=actionMap.get("DialCall");
        action.putValue(action.SMALL_ICON, dp.getjButtonDialCall().getIcon());
        dp.getjButtonDialCall().setAction(action);
        
        action=actionMap.get("performSearch");
        makeCallTopPanel.getjXSearchField().setAction(action);
        makeCallTopPanel.getjXSearchField().addKeyListener(makeCallKeyListener);

        action=actionMap.get("ShowLastCalls");
        action.putValue(action.SMALL_ICON, makeCallTopPanel.getjButtonLastCalls().getIcon());
        makeCallTopPanel.getjButtonLastCalls().setAction(action);

        action=actionMap.get("FilterMissedButtonAction");
        action.putValue(action.SMALL_ICON, makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterMissedCalls().getIcon());
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterMissedCalls().setAction(action);
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterMissedCalls().setToolTipText(callResMap.getString("CallPanel.FilterHistory.MissedCalls.Tooltip"));
        

        action=actionMap.get("FilterReceivedButtonAction");
        action.putValue(action.SMALL_ICON, makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterReceivedCalls().getIcon());
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterReceivedCalls().setAction(action);
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterReceivedCalls().setToolTipText(callResMap.getString("CallPanel.FilterHistory.ReceivedCalls.Tooltip"));

        action=actionMap.get("FilterDialedButtonAction");
        action.putValue(action.SMALL_ICON, makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterDialedCalls().getIcon());
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterDialedCalls().setAction(action);
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterDialedCalls().setToolTipText(callResMap.getString("CallPanel.FilterHistory.DialedCalls.Tooltip"));

        action=actionMap.get("FilterVoiceButtonAction");
        action.putValue(action.SMALL_ICON, makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterVoicemails().getIcon());
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterVoicemails().setAction(action);
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterVoicemails().setToolTipText(callResMap.getString("CallPanel.FilterHistory.VoiceMails.Tooltip"));
        
        action=actionMap.get("ShowDialPadAction");
        action.putValue(action.SMALL_ICON, makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonShowDialPad().getIcon());
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonShowDialPad().setAction(action);
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonShowDialPad().setBorder(null);        
        makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonShowDialPad().setToolTipText(callResMap.getString("CallPanel.ConfigCallTab.ShowDialPad.Tooltip"));
        
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {        
        
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        makeCallTopPanel = new MakeCallTopPanel();
        makeCallTopPanel.initComponents();
        makeCallTopPanel.customize();
        jPanelMultiPane = new JPanel();
               
        this.setBackground(new Color(255, 255, 255));
        //this.setMaximumSize(new Dimension(3082, 32767));
        this.setMinimumSize(new Dimension(374, 0));
        this.setPreferredSize(new Dimension(374, 645));

        makeCallTopPanel.setMaximumSize(new Dimension(32767, MAKE_CALL_PANEL_HEIGHT_SHOW_DIAL_PAD));
        makeCallTopPanel.setMinimumSize(new Dimension(374, 40));
        makeCallTopPanel.setName("makeCallTopPanel"); // NOI18N
        makeCallTopPanel.setPreferredSize(new Dimension(374, MAKE_CALL_PANEL_HEIGHT_SHOW_DIAL_PAD));        
        //makeCallTopPanel.setAutoscrolls(true);
        this.setLeftComponent(makeCallTopPanel);        
        //this.add(makeCallTopPanel);
        
        
        jPanelMultiPane.setMinimumSize(new Dimension(356, 40));
        jPanelMultiPane.setName("jPanelMultiPane"); // NOI18N
        //jPanelMultiPane.setPreferredSize(new Dimension(356, 40));
        jPanelMultiPane.setPreferredSize(null);
        jPanelMultiPane.setLayout(new CardLayout());

        makeCallPanel = new MakeCallPanel(mouseListener, false, true); 
        makeCallPanel.setMinimumSize(new Dimension(308, 0));
        makeCallPanel.setPreferredSize(null);
        makeCallPanel.setName("makeCallPanel"); // NOI18N
        jPanelMultiPane.add(makeCallPanel, "card2");
        
        this.buildFindMeFollowMe();

        callHistoryPanel2 = new CallHistoryPanel2();
        callHistoryPanel2.setParentPanel(this);
        callHistoryPanel2.setName("callHistoryPanel2"); // NOI18N
        jPanelMultiPane.add(callHistoryPanel2, "card4");
        
        this.setRightComponent(jPanelMultiPane);        
        //this.add(jPanelMultiPane);
    }

    private void buildFindMeFollowMe() {
        findMePanel = new FindMePanel();
        findMePanel.setMinimumSize(new Dimension(356, 40));
        findMePanel.setName("findMePanel"); // NOI18N
        findMePanel.setPreferredSize(new Dimension(356, 340));
        jPanelMultiPane.add(findMePanel, "card3");
    }
    
    private void filterAction()
    {
        final JToggleButton fltMisCalls=this.makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterMissedCalls();        
        final JToggleButton fltDialCalls=this.makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterDialedCalls();
        final JToggleButton fltRecCalls=this.makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterReceivedCalls();
        final JToggleButton fltVmails=this.makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterVoicemails();

        this.getCallHistoryPanel().filterHistoryItems(fltMisCalls.isSelected(),
                                                      fltRecCalls.isSelected(),
                                                      fltDialCalls.isSelected(),
                                                      fltVmails.isSelected());
    }
    
    private void showDialPad() {
        final JToggleButton showDialPadBtn=this.makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonShowDialPad();
        //DialPadPanel dpp=this.makeCallTopPanel.getDialPadPanel();
        if (showDialPadBtn.isSelected()) {
        	setDividerLocation(MAKE_CALL_PANEL_HEIGHT_HIDE_DIAL_PAD + UIMediaManager.getInstance().getCallPanelWrapper().getHeight());
        	setEnabled(false);
            makeCallTopPanel.getDialPadPanel().setVisible(false);
            makeCallTopPanel.setPreferredSize(topPanelDimExpandedWODP);            
        }
        else {
        	setDividerLocation(MAKE_CALL_PANEL_HEIGHT_SHOW_DIAL_PAD + UIMediaManager.getInstance().getCallPanelWrapper().getHeight());
        	setEnabled(true);
            makeCallTopPanel.getDialPadPanel().setVisible(true);
            makeCallTopPanel.setPreferredSize(topPanelDimExpandedWithDP);            
        }
    }

    @Action
    public void FilterReceivedButtonAction()
    {
        filterAction();
    }

    @Action
    public void FilterMissedButtonAction()
    {
        filterAction();
    }

    @Action
    public void FilterDialedButtonAction()
    {
        filterAction();
    }

    @Action
    public void FilterVoiceButtonAction()
    {
        filterAction();
    }
    
    @Action
    public void ShowDialPadAction()
    {
        showDialPad();
    }
    

    @Action()
    public void Dial0() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("0").toString());
        sb = null;
    }

    @Action
    public void Dial1() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("1").toString());
        sb = null;
    }

    @Action
    public void Dial2() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("2").toString());
        sb = null;
    }

    @Action
    public void Dial3() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("3").toString());
        sb = null;
    }

    @Action
    public void Dial4() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("4").toString());
        sb = null;
    }

    @Action
    public void Dial5() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("5").toString());
        sb = null;
    }

    @Action
    public void Dial6() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("6").toString());
        sb = null;
    }

    @Action
    public void Dial7() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("7").toString());
        sb = null;
    }

    @Action
    public void Dial8() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("8").toString());
        sb = null;
    }

    @Action
    public void Dial9() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("9").toString());
        sb = null;
    }

    @Action
    public void DialAst() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("*").toString());
        sb = null;
    }

    @Action
    public void DialDiez() {
        StringBuilder sb = new StringBuilder(this.makeCallTopPanel.
                                                    getjXSearchField().getText());
        this.makeCallTopPanel.getjXSearchField().setText(sb.append("#").toString());
        sb = null;
    }

	@Action(block = Task.BlockingScope.COMPONENT)
	public Task DialRedial() {
		if (!Beans.isDesignTime()) {
			String latestDialedNumber="";   
                        try
                        {
                            setCursor(new Cursor(Cursor.WAIT_CURSOR));
                            List<Cdr> listCalls =  RestManager.getInstance().getLastMonthCalls();
                            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                            if(listCalls.size()>0)
                            {
                                for (Cdr call:listCalls)
                                {
                                    if (call.isDialed())
                                    {
                                        latestDialedNumber = call.getCalleeName();
                                        break;
                                    }
                                    
                                }
                            }
                        }catch(Exception ex){
                            
                        }
                        if (!StringUtils.isEmpty(latestDialedNumber)) {
                            makeCallTopPanel.getjXSearchField().setText(latestDialedNumber);
                            MakeCallTask makeCallTask = new MakeCallTask(org.jdesktop.application.Application.getInstance(), latestDialedNumber);
                            makeCallTask.setIgnoreNumbersPattern(true);
                            return makeCallTask;
                        }
		}
		return null;
	}

    @Action(block = Task.BlockingScope.COMPONENT)
    public Task DialCall() {
        if (!Beans.isDesignTime()) {
        	String number = makeCallTopPanel.getjXSearchField().getText();
        	if(!StringUtils.isEmpty(number)) {
        		MakeCallTask makeCallTask = new MakeCallTask(org.jdesktop.application.Application.getInstance(), number);
        		makeCallTask.setIgnoreNumbersPattern(true);
                return makeCallTask;
        	}
         }
        return null;
    }
    
    @Action(block = Task.BlockingScope.COMPONENT)
    public Task DialVideoCall() {
        if (!Beans.isDesignTime()) {
        	String number = makeCallTopPanel.getjXSearchField().getText();
        	if(!StringUtils.isEmpty(number)) {
                MakeCallTask makeCallTask = new MakeCallTask(org.jdesktop.application.Application.getInstance(), number, true);
                makeCallTask.setIgnoreNumbersPattern(true);
                return makeCallTask;
        	}
         }
        return null;
    }   

    @Action
    public void BringUpMakeACallPanel() {
        ((CardLayout)jPanelMultiPane.getLayout()).show(jPanelMultiPane,"card2");
        this.makeCallTopPanel.getCallTabBtnPanel().getJPanelHistoryFilters().setVisible(false);
        this.makeCallTopPanel.getCallTabBtnPanel().getJPanelConfigCallTab().setVisible(true);
        expandTopPanel();
        Task cleanMp3s = deleteTemporaryVoicemailMp3s();
        cleanMp3s.execute();
    }

    @Action
    public void BringUpFindMePanel() {
        buildFindMeFollowMe();
        ((CardLayout)jPanelMultiPane.getLayout()).show(jPanelMultiPane,"card3");
        this.makeCallTopPanel.getCallTabBtnPanel().getJPanelHistoryFilters().setVisible(false);
        this.makeCallTopPanel.getCallTabBtnPanel().getJPanelConfigCallTab().setVisible(false);
        retractTopPanel();
        Task cleanMp3s = deleteTemporaryVoicemailMp3s();
        cleanMp3s.execute();
    }

    @Action
    public void BringUpCallHistoryPanel(ActionEvent ae) {
    	callHistoryPanel2.setReload(false);
        boolean showMissedCalls=true;
        boolean showReceivedCalls=true;
        boolean showDialedCalls=true;
        boolean showVoiceMails=true;

        if (ae.getActionCommand()!=null && !ae.getActionCommand().equals(""))
        {
            //configure which of the items must be hidden
            if (ae.getActionCommand().equalsIgnoreCase(HistoryItemTypes.MISSED_CALL.name()))
            {
                showReceivedCalls=false;
                showDialedCalls=false;
                showVoiceMails=false;
            }
            else if (ae.getActionCommand().equalsIgnoreCase(HistoryItemTypes.VOICE_MAIL.name()))
            {
                showMissedCalls=false;
                showReceivedCalls=false;
                showDialedCalls=false;
            }
            else if (ae.getActionCommand().equalsIgnoreCase(HistoryItemTypes.RECEIVED_CALL.name()))
            {
                showMissedCalls=false;
                showDialedCalls=false;
                showVoiceMails=false;
            }
            else if (ae.getActionCommand().equalsIgnoreCase(HistoryItemTypes.DIALED_CALL.name()))
            {
                showMissedCalls=false;
                showReceivedCalls=false;
                showVoiceMails=false;
            }
        }
        
        this.makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonMissedCalls().setSelected(true);
        this.makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterMissedCalls().setSelected(showMissedCalls);
        this.makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterReceivedCalls().setSelected(showReceivedCalls);
        this.makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterDialedCalls().setSelected(showDialedCalls);
        this.makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonFilterVoicemails().setSelected(showVoiceMails);
        this.makeCallTopPanel.getCallTabBtnPanel().getJPanelHistoryFilters().setVisible(true);        
        this.makeCallTopPanel.getCallTabBtnPanel().getJPanelConfigCallTab().setVisible(false);

        ((CardLayout)jPanelMultiPane.getLayout()).show(jPanelMultiPane,"card4");
        retractTopPanel();
        populateHistoryList(showMissedCalls, showReceivedCalls, showDialedCalls, showVoiceMails); //TODO: temporary! Must be removed after implementing
                               //a listener for history list update !
    }

    private void expandTopPanel() {
    	setDividerLocation(lastDividerLocation);
    	setEnabled(true);
        makeCallTopPanel.setPreferredSize(topPanelDimExpanded);        
        makeCallTopPanel.getSearchFieldPanel().setVisible(true);
        makeCallTopPanel.getjButtonLastCalls().setVisible(true);
        makeCallTopPanel.getDialPadPanel().setVisible(true);
        showDialPad();
    }
    private void retractTopPanel() {
    	setDividerLocation(-1);
    	setEnabled(false);
        makeCallTopPanel.setPreferredSize(topPanelDimRetracted);
        makeCallTopPanel.getSearchFieldPanel().setVisible(false);
        makeCallTopPanel.getjButtonLastCalls().setVisible(false);
        makeCallTopPanel.getDialPadPanel().setVisible(false);
    }

    @Action(block = Task.BlockingScope.COMPONENT)
    public Task Call() {
         if (!Beans.isDesignTime())
         {
            return new MakeCallTask(org.jdesktop.application.Application.getInstance(),makeCallTopPanel.getjXSearchField().getText());
         }
         else{
             return null;
         }
    }


    @Action(block = Task.BlockingScope.COMPONENT)
    public Task ShowLastCalls() {
        return new ShowLastCallsTask(Application.getInstance(),this);
    }

   /**
    * Starts a Task that loads the history of call items in background, then
    * displays it.
    */
    private void populateHistoryList(boolean showMissedCalls, boolean showReceivedCalls, boolean showDialedCalls, boolean showVoiceMails) {    	
        if (this.populateHistoryTask==null || this.populateHistoryTask.isDone())
        {
            this.populateHistoryTask=new PopulateHistoryListTask(
                            org.jdesktop.application.Application.getInstance(),
                            this, callHistoryPanel2,
                            showMissedCalls,
                            showReceivedCalls,
                            showDialedCalls,
                            showVoiceMails);
            populateHistoryTask.execute();
        }

    }

    public MakeCallTopPanel getMakeCallTopPanel() {
        return this.makeCallTopPanel;
    }

   private MouseAdapter mouseListener=new MouseAdapter()
                {
                    @Override
                    public void mouseClicked(MouseEvent me)
                    {
                        if (me.getButton()==MouseEvent.BUTTON1)
                        {
//                            if (me.getSource() instanceof UserMiniPanelGloss)
//                              {
//                                UserMiniPanelGloss focusGainer =
//                                            (UserMiniPanelGloss)me.getSource();
//                                CallPanel2.this.makeCallTopPanel.getjXSearchField()
//                                        .setText(focusGainer.getContact().getNumber());
//                              }
                            if (me.getSource() instanceof JList)
                            {
                                final JList usersList=(JList)me.getSource();
                                final int selectedIndex=usersList.locationToIndex(me.getPoint());
                                final UserMiniPanelGloss focusGainer=
                                        (UserMiniPanelGloss)usersList.getModel().getElementAt(selectedIndex);
                                focusGainer.focusGained(null);
                                if (!CallPanel2.this.makeCallTopPanel.getjXSearchField().getText().equals(focusGainer.getContact().getNumber())){
                                	CallPanel2.this.makeCallTopPanel.getjXSearchField().setText(focusGainer.getContact().getNumber());
                                }
                            }
                        }
                    }
                };

                
    public MouseAdapter getMouseListener() {
    	return mouseListener;
    }
    
    @Action
    public Task deleteTemporaryVoicemailMp3s()
    {
        if (this.deleteTempMp3Task!=null)
        {
            this.deleteTempMp3Task.cancel(true);
        }
        this.deleteTempMp3Task = new DeleteTempVoiceMailsTask(Application.getInstance());
        return this.deleteTempMp3Task;
    }

    @Action
    public Task performSearch(){
        if (this.searchTask!=null) { 
        	this.searchTask.cancel(true);
        }
        //make this an EXTENDED search - ie. DON'T use the REST phonebook search, 
        //but retrieve entire phonebook and look in all details of each user.
        //This also enables the search to be performed as per UC-1506, ie. taking into account
        //criteria formed of multiple words, while comparing ALL details of each user. 
        this.searchTask=new SearchTask(Application.getInstance(), this, true, false, true, true);
        return searchTask;
    }

    @Action
    public void cancelSearch()
    {
        if (this.searchTask!=null && !this.searchTask.isDone()) {
            this.searchTask.cancel(true);
        }
    }

    @Override
    public MakeCallPanel getMakeCallPanel()
    {
        return this.makeCallPanel;
    }

    @Override
    public JXSearchField getjXSearchField()
    {
        return this.makeCallTopPanel.getjXSearchField();
    }

    public CallHistoryPanel2 getCallHistoryPanel()
    {
        return callHistoryPanel2;
    }

    public FindMePanel getFindMePanel()
    {
        return findMePanel;
    }

    private class PhonebookRunnable implements Runnable {
    	private CallPanel2 callPanel;
    	
    	public PhonebookRunnable(CallPanel2 callPanel) {
    		this.callPanel = callPanel;
    	}

		@Override
		public void run() {
			try {
				DownloadPhoneBookTask d = new DownloadPhoneBookTask(Application.getInstance());
				d.execute();
			} catch (Exception ex) {
				Logger.getLogger(CallPanel2.class.getName()).log(Level.SEVERE, null, ex);
			}

			Task t = performSearch();
			t.execute();
		}    	
    }
    
    public int getMakeCallPanelHeight() {

    	 final JToggleButton showDialPadBtn=makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonShowDialPad();
         final JToggleButton makeACall=makeCallTopPanel.getCallTabBtnPanel().getjToggleButtonMakeACall();
         if (!showDialPadBtn.isSelected() && makeACall.isSelected()) {
             return MAKE_CALL_PANEL_HEIGHT_SHOW_DIAL_PAD;
         } else {
             return MAKE_CALL_PANEL_HEIGHT_HIDE_DIAL_PAD;
         }

	}

	@Override
	public void configureVoiceEnabled() {
        DialPad dp=this.makeCallTopPanel.getDialPadPanel().getDialPad();
		javax.swing.Action action = actionMap.get("DialVideoCall");
        action.putValue(action.SMALL_ICON, dp.getjButtonDialVideoCall().getIcon());
        dp.getjButtonDialVideoCall().setAction(action);
		
	}

	@Override
	public void configureVoiceDisabled() {

	}
}
