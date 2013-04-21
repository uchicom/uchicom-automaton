/**
 * (c) 2012 uchicom
 */
package com.uchicom.automaton.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.uchicom.automaton.ProgramView;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class LogAction extends AbstractAction {

    public static final String START = "記録開始";
    public static final String STOP = "記録停止";
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ProgramView mouseFrame;
    public LogAction(ProgramView mouseFrame) {
        putValue(NAME, START);
        this.mouseFrame = mouseFrame;
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        mouseFrame.startLog();
    }

}
