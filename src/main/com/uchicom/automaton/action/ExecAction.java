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
public class ExecAction extends AbstractAction {

    public static final String EXEC = "実行";

    public static final String STOP = "停止";
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ProgramView mouseFrame;
    public ExecAction(ProgramView mouseFrame) {
        putValue(NAME, EXEC);
        this.mouseFrame = mouseFrame;
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (getValue(NAME).equals(EXEC)) {
            putValue(NAME, STOP);
            mouseFrame.exec();
        } else {
            mouseFrame.stop();
        }
    }

}
