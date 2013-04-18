/**
 * (c) 2012 uchicom
 */
package com.uchicom.automaton;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.uchicom.automaton.action.ClearAction;
import com.uchicom.automaton.action.ExecAction;
import com.uchicom.automaton.action.HelpAction;
import com.uchicom.automaton.action.LogAction;

/**
 * マウスフレーム
 * @author uchicom: Shigeki Uchiyama
 *
 */
public class ProgramView extends JFrame implements Runnable {

    /** クリックを表す文字列(左) */
    private static final String CLICK = "C";
    /** 左クリックを表す文字列 */
    private static final String CLICK_BUTTON1 = "C1";
    /** 右クリックを表す文字列 */
    private static final String CLICK_BUTTON2 = "C2";
    /** 中央クリックを表す文字列 */
    private static final String CLICK_BUTTON3 = "C3";
    /** ドラッグアンドドロップを表す文字列(左) */
    private static final String DRAGANDDROP = "D";
    /** 左クリックドラッグアンドドロップを表す文字列 */
    private static final String DRAGANDDROP_BUTTON1 = "D1";
    /** 右クリックドラッグアンドドロップを表す文字列 */
    private static final String DRAGANDDROP_BUTTON2 = "D2";
    /** 中央クリックドラッグアンドドロップを表す文字列 */
    private static final String DRAGANDDROP_BUTTON3 = "D3";
    /** マウスカーソル移動を表す文字列 */
    private static final String MOVE = "M";
    /** スリープを表す文字列 */
    private static final String SLEEP = "S";
    /** ホイールを表す文字列 */
    private static final String WHEEL = "W";
    /** マウスボタン押下を表す文字列(左) */
    private static final String PRESS = "P";
    /** 左マウスボタン押下を表す文字列 */
    private static final String PRESS_BUTTON1 = "P1";
    /** 右マウスボタン押下を表す文字列 */
    private static final String PRESS_BUTTON2 = "P2";
    /** 中央マウスボタン押下を表す文字列 */
    private static final String PRESS_BUTTON3 = "P3";
    /** マウスボタン開放を表す文字列(左) */
    private static final String RELEASE = "R";
    /** 右マウスボタン開放を表す文字列 */
    private static final String RELEASE_BUTTON1 = "R1";
    /** 左マウスボタン開放を表す文字列 */
    private static final String RELEASE_BUTTON2 = "R2";
    /** 中央マウスボタン開放を表す文字列 */
    private static final String RELEASE_BUTTON3 = "R3";
    /** キー押下・開放を表す文字列 */
    private static final String KEY = "K";
    /** キー押下を表す文字列 */
    private static final String KEY_PRESS = "KP";
    /** キー開放を表す文字列 */
    private static final String KEY_RELEASE = "KR";
    
    
    private Robot robot;
    private boolean bStop;
    private long startTime;
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** コマンドテキストエリア */
    private JTextArea textArea = new JTextArea();
    /** 繰り返し実行チェックボックス */
    private JCheckBox checkBox = new JCheckBox();
    /** 実行間隔テキストフィールド */
    private JTextField textField = new JTextField();
    /** クリックボタン */
    JButton logButton = new JButton(new LogAction(this));
    
    JButton execButton = new JButton(new ExecAction(this));
    JButton clearButton = new JButton(new ClearAction(this));
    
    Thread thread;
    
    /**
     * 
     */
    public ProgramView() {
        super("Automaton");
        initComponent();
    }
    
    /**
     * コンポーネント初期化
     */
    public void initComponent() {
        //アイコン
        setIconImage(new ImageIcon("./icon.png").getImage());
        //メニュー設定
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("ヘルプ");
        JMenuItem item = new JMenuItem(new HelpAction(this));
        menu.add(item);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        //コンポーネントの画面配置
        JPanel northPanel = new JPanel();
        northPanel.add(logButton);
        northPanel.add(execButton);
        northPanel.add(clearButton);
        
        JPanel southPanel = new JPanel();
        southPanel.add(new JLabel("繰り返し実行:"));
        checkBox.setSelected(true);
        southPanel.add(checkBox);
        southPanel.add(new JLabel("実行間隔:"));
        textField.setText("60000");
        southPanel.add(textField);
        
        setLayout(new BorderLayout());
        getContentPane().add(northPanel, BorderLayout.NORTH);
        getContentPane().add(southPanel, BorderLayout.SOUTH);
        
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setAlwaysOnTop(true);
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     */
    public void clear() {
        textArea.setText("");
        startTime = 0;
    }
    
    /**
     * 
     */
    public void log() {
        long now = System.currentTimeMillis();
        final JFrame frame = new JFrame();
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        JLabel label = new JLabel("a");
        label.setOpaque(true);
        label.setBackground(new Color(0x10000000, true));
        
        frame.getContentPane().add(label);
        frame.setBackground(new Color(0x1, true));
        
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        frame.setBounds(env.getMaximumWindowBounds());
        
        frame.setVisible(true);
        
        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                // TODO Auto-generated method stub
                if (arg0.getClickCount() == 2) {
                    frame.dispose();
                    System.out.println("2");
                } else if (arg0.getClickCount() == 1) {
                    frame.setVisible(false);
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    frame.setVisible(true);
                    System.out.println("1");
                } else {

                    System.out.println(arg0.getClickCount());
                }
                    
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            
        });
        Point point = getLocationOnScreen();
        if (startTime > 0) {
            textArea.setText(textArea.getText() + "\nS:" + (now - startTime));
            startTime = now;
            textArea.setText(textArea.getText() + "\nC:" + (point.x - 1) + "," + (point.y - 1));
        } else {
            startTime = now;
            textArea.setText("C:" + (point.x - 1) + "," + (point.y - 1));
        }
        PointerInfo info = MouseInfo.getPointerInfo();
        robot.mouseMove(point.x - 1, point.y - 1);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.mouseMove(info.getLocation().x, info.getLocation().y);
    }
    
    /**
     * 実行
     */
    public void exec() {
        bStop = false;
        if (textArea.getText().length() > 0) {
            thread = new Thread(this);
            thread.start();
        }
        bStop = false;
    }
    
    /**
     * 停止
     */
    public void stop() {
        bStop = true;
        thread.interrupt();
    }
    
    /**
     * 画面終了
     */
    public void dispose() {
        super.dispose();
        checkBox.setSelected(false);
        if (thread != null) {
            stop();
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        textArea.setEnabled(false);
        logButton.setEnabled(false);
        clearButton.setEnabled(false);
        LABEL0: while (checkBox.isSelected()) {
            String[] lines = textArea.getText().split("\n");
            for (String value : lines) {
                if (bStop) break LABEL0;
                String[] command = value.split(":");
                if (CLICK.equals(command[0]) || CLICK_BUTTON1.equals(command[0])) {
                    //マウスボタン1クリック
                    String[] location = command[1].split(",");
                    PointerInfo info = MouseInfo.getPointerInfo();
                    robot.mouseMove(Integer.parseInt(location[0]), Integer.parseInt(location[1]));
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    robot.mouseMove(info.getLocation().x, info.getLocation().y);
                } else if (CLICK_BUTTON2.equals(command[0])) {
                    //マウスボタン2クリック
                    String[] location = command[1].split(",");
                    PointerInfo info = MouseInfo.getPointerInfo();
                    robot.mouseMove(Integer.parseInt(location[0]), Integer.parseInt(location[1]));
                    robot.mousePress(InputEvent.BUTTON2_MASK);
                    robot.mouseRelease(InputEvent.BUTTON2_MASK);
                    robot.mouseMove(info.getLocation().x, info.getLocation().y);
                } else if (CLICK_BUTTON3.equals(command[0])) {
                    //マウスボタン3クリック
                    String[] location = command[1].split(",");
                    PointerInfo info = MouseInfo.getPointerInfo();
                    robot.mouseMove(Integer.parseInt(location[0]), Integer.parseInt(location[1]));
                    robot.mousePress(InputEvent.BUTTON3_MASK);
                    robot.mouseRelease(InputEvent.BUTTON3_MASK);
                    robot.mouseMove(info.getLocation().x, info.getLocation().y);
                } else if (MOVE.equals(command[0])) {
                    //マウスカーソル移動
                    String[] points = command[1].split(",");
                    robot.mouseMove(Integer.parseInt(points[0]), Integer.parseInt(points[1]));
                } else if (DRAGANDDROP.equals(command[0]) || DRAGANDDROP_BUTTON1.equals(command[0])) {
                    //マウスボタン1ドラッグアンドドロップ
                    String[] points = command[1].split("-");
                    String[] start = points[0].split(",");
                    String[] end = points[1].split(",");
                    PointerInfo info = MouseInfo.getPointerInfo();
                    robot.mouseMove(Integer.parseInt(start[0]), Integer.parseInt(start[1]));
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.mouseMove(Integer.parseInt(end[0]), Integer.parseInt(end[1]));
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    robot.mouseMove(info.getLocation().x, info.getLocation().y);
                } else if (DRAGANDDROP_BUTTON2.equals(command[0])) {
                    //マウスボタン2ドラッグアンドドロップ
                    String[] points = command[1].split("-");
                    String[] start = points[0].split(",");
                    String[] end = points[1].split(",");
                    PointerInfo info = MouseInfo.getPointerInfo();
                    robot.mouseMove(Integer.parseInt(start[0]), Integer.parseInt(start[1]));
                    robot.mousePress(InputEvent.BUTTON2_MASK);
                    robot.mouseMove(Integer.parseInt(end[0]), Integer.parseInt(end[1]));
                    robot.mouseRelease(InputEvent.BUTTON2_MASK);
                    robot.mouseMove(info.getLocation().x, info.getLocation().y);
                } else if (DRAGANDDROP_BUTTON3.equals(command[0])) {
                    //マウスボタン3ドラッグアンドドロップ
                    String[] points = command[1].split("-");
                    String[] start = points[0].split(",");
                    String[] end = points[1].split(",");
                    PointerInfo info = MouseInfo.getPointerInfo();
                    robot.mouseMove(Integer.parseInt(start[0]), Integer.parseInt(start[1]));
                    robot.mousePress(InputEvent.BUTTON3_MASK);
                    robot.mouseMove(Integer.parseInt(end[0]), Integer.parseInt(end[1]));
                    robot.mouseRelease(InputEvent.BUTTON3_MASK);
                    robot.mouseMove(info.getLocation().x, info.getLocation().y);
                } else if (SLEEP.equals(command[0])) {
                    //スリープ
                    try {
                        Thread.sleep(Long.parseLong(command[1]));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        break LABEL0;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break LABEL0;
                    }
                } else if (WHEEL.equals(command[0])) {
                    //マウスホイール上下
                    robot.mouseWheel(Integer.parseInt(command[1]));
                } else if (PRESS.equals(command[0]) || PRESS_BUTTON1.equals(command[0])) {
                    //マウスボタン1押下
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                } else if (PRESS_BUTTON2.equals(command[0])) {
                    //マウスボタン2押下
                    robot.mousePress(InputEvent.BUTTON2_MASK);
                } else if (PRESS_BUTTON3.equals(command[0])) {
                    //マウスボタン3押下
                    robot.mousePress(InputEvent.BUTTON3_MASK);
                } else if (RELEASE.equals(command[0]) || RELEASE_BUTTON1.equals(command[0])) {
                    //マウスボタン1開放
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                } else if (RELEASE_BUTTON2.equals(command[0])) {
                    //マウスボタン2開放
                    robot.mouseRelease(InputEvent.BUTTON2_MASK);
                } else if (RELEASE_BUTTON3.equals(command[0])) {
                    //マウスボタン3開放
                    robot.mouseRelease(InputEvent.BUTTON3_MASK);
                } else if (KEY.equals(command[0])) {
                    //キー押下・開放
                    for (char ch : command[1].toCharArray()) {
                        robot.keyPress(ch);
                        robot.keyRelease(ch);
                    }
                } else if (KEY_PRESS.equals(command[0])) {
                    //キー押下
                    for (char ch : command[1].toCharArray()) {
                        robot.keyPress(ch);
                    }
                } else if (KEY_RELEASE.equals(command[0])) {
                    //キー開放
                    for (char ch : command[1].toCharArray()) {
                        robot.keyRelease(ch);
                    }
                } else {
                    //他のコマンドはエラー
                    assert(false);
                }
            }
            try {
                Thread.sleep(Long.parseLong(textField.getText()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        textArea.setEnabled(true);
        logButton.setEnabled(true);
        clearButton.setEnabled(true);
        execButton.getAction().putValue(Action.NAME, ExecAction.EXEC);
    }
    
    /**
     * ヘルプ
     */
    public void help() {
        try {
            //windowsはexplorer
            //maxはopen
            //linuxはfirefoxとか。。既定のはどうやって起動するんだか。
            Process exec = Runtime.getRuntime().exec("explorer http://www.toukei.info");
            if (exec.exitValue() != 0) {
              //エラーの場合は普通に画面を表示する。
                JDialog dialog = new JDialog(this, "ヘルプ");
                JEditorPane pane = new JEditorPane();
                pane.setContentType("text/html");
                try {
                    pane.setPage("http://www.toukei.info");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    //それでもエラーの場合はURLを見てくれと表示する。
                    
                }
                dialog.add(new JScrollPane(pane));
                dialog.pack();
                dialog.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //エラーの場合は普通に画面を表示する。
            JDialog dialog = new JDialog(this, "ヘルプ");
            JEditorPane pane = new JEditorPane();
            pane.setContentType("text/html");
            try {
                pane.setPage("http://www.toukei.info");
            } catch (IOException ioe) {
                ioe.printStackTrace();
                //それでもエラーの場合はURLを見てくれと表示する。
                
            }
            dialog.add(new JScrollPane(pane));
            dialog.pack();
            dialog.setVisible(true);
        }
    }
    
}
