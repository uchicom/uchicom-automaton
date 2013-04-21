/**
 * (c) 2012 uchicom
 */
package com.uchicom.automaton.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.uchicom.automaton.ProgramView;

/**
 * ヘルプ情報を表示するアクション.
 * @author uchicom: Shigeki Uchiyama
 * 
 */
public class HelpAction extends AbstractAction {

    /** ヘルプ情報URL. */
    private static final String HELP_URL = "https://uchicom.com/automaton.htm";

    /** シリアルID. */
    private static final long serialVersionUID = 1L;

    /** 親ウィンドウ. */
    private ProgramView programView;

    /**
     * ヘルプアクションのコンストラクタ.
     * @param mouseFrame
     */
    public HelpAction(ProgramView programView) {
        putValue(NAME, "ヘルプ");
        this.programView = programView;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(HELP_URL));
        } catch (URISyntaxException e) {
            JOptionPane.showMessageDialog(programView, e.getMessage());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(programView, e.getMessage());
        }
    }

}
