/**
 * (c) 2012 uchicom
 */
package com.uchicom.automaton;

import java.applet.Applet;

/**
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class MainApplet extends Applet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void start() {
        //URLを指定して以下のクラスを呼び出すようにする。
        //アプレット自体にはたいした処理は書かない
        //クラスはリフレクション呼び出し
        //同じアプレットでもパラメータが異なれば、呼び出すjarが変わり、また、
        //実行するクラスも変わる。
        ProgramView mouse = new ProgramView();
        mouse.pack();
        mouse.setVisible(true);
    }
}
