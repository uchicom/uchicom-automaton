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
public class ClearAction extends AbstractAction {


    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ProgramView mouseFrame;
    public ClearAction(ProgramView mouseFrame) {
        putValue(NAME, "クリア");
        this.mouseFrame = mouseFrame;
    }
    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        mouseFrame.clear();
    }

}
