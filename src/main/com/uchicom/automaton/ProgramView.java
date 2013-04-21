/**
 * (c) 2012 uchicom
 */
package com.uchicom.automaton;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("com/uchicom/automaton/icon.png")).getImage());
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
    
    private PointerInfo pressPointerInfo = null;
    long pressTime;
    boolean alive;
    /**
     * 記録開始
     */
    public void startLog() {
        alive = true;
        long now = System.currentTimeMillis();
        final JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setBackground(new Color(0x01010101, true));
        frame.setAlwaysOnTop(true);


        frame.pack();
        frame.setVisible(true);
        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                // TODO Auto-generated method stub
                System.out.println(arg0);
                frame.setVisible(false);
                if (pressTime < 0) {
                    if (arg0.getClickCount() == 1) {
                        switch (arg0.getButton()) {
                        case MouseEvent.BUTTON1:
                            robot.mousePress(InputEvent.BUTTON1_MASK);
                            robot.delay(20);
                            robot.mouseRelease(InputEvent.BUTTON1_MASK);
                            break;
                        case MouseEvent.BUTTON2:
                            robot.mousePress(InputEvent.BUTTON2_MASK);
                            robot.delay(20);
                            robot.mouseRelease(InputEvent.BUTTON2_MASK);
                            break;
                        case MouseEvent.BUTTON3:
                            robot.mousePress(InputEvent.BUTTON3_MASK);
                            robot.delay(20);
                            robot.mouseRelease(InputEvent.BUTTON3_MASK);
                            break;
                            default:
                        }
                            
                    } else if (arg0.getClickCount()  == 2) {
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.delay(10);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                        robot.delay(10);
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.delay(10);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    }
                }
                frame.setVisible(true);
                frame.requestFocus();
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                
            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                pressPointerInfo = MouseInfo.getPointerInfo();
                pressTime = System.currentTimeMillis();
//                
            }

            @Override
            public void mouseReleased(MouseEvent arg0) {
                if (pressTime > 0) {
                    Point before = pressPointerInfo.getLocation();
                    Point now = MouseInfo.getPointerInfo().getLocation();
                    if ((before.x - now.x) * (before.x - now.x) + (before.y - now.y) * (before.y - now.y) > 10) {
                        System.out.println("動いてる");
                        frame.setVisible(false);
                        robot.mouseMove(before.x, before.y);
                        robot.mousePress(InputEvent.BUTTON1_MASK);
                        robot.delay(20);
                        robot.mouseMove(now.x, now.y);
                        robot.delay(20);
                        robot.mouseRelease(InputEvent.BUTTON1_MASK);
                        frame.setVisible(true);
                        frame.requestFocus();
                        arg0.consume();
                        pressTime = -1;
                    } else {

                    pressTime = -1;
                    
                }
                }
                
            }
            
        });

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                while (alive) {
                    if (frame.isVisible()) {
                    PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                    Thread.sleep(10);
                    
                    frame.setBounds(pointerInfo.getLocation().x - 5, pointerInfo.getLocation().y - 5, 10, 10);
                    }
                }
                frame.dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        });
        thread.start();
        
        
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
     * 記録停止
     */
    public void stopLog() {
        alive = false;
       
    }
    
    /**
     * 自動実行
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
     * 自動実行停止
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
    
    /**
     * テキストデータの中身確認
     */
    public void validate() {
        
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
    
    
}
