/**
 * (c) 2012 uchicom
 */
package com.uchicom.automaton;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
    
    /** Execをあらわす文字列 */
    private static final String EXECUTE = "E";
    
    /** ブラウザを起動する文字列 */
    private static final String BROWSE = "B";
    
    private Robot robot;
    private boolean bStop;
    private long startTime;
    /**
     * 
     */
    private static final long serialVersionUID = 2L;

    /** コマンドテキストエリア */
    private JTextArea textArea = new JTextArea();
    /** 繰り返し実行チェックボックス */
    private JCheckBox checkBox = new JCheckBox();
    /** 実行間隔テキストフィールド */
    private JTextField textField = new JTextField();
    /** クリックボタン */
    JButton logButton;
    JButton execButton;
    JButton clearButton;
    
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
        LogAction logAction = new LogAction(this);
        logButton = new JButton(logAction);
        ExecAction execAction = new ExecAction(this);
        execButton = new JButton(execAction);
        ClearAction clearAction = new ClearAction(this);
        clearButton = new JButton(clearAction);
        
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
        southPanel.add(new JLabel("実行間隔[ms]:"));
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
    boolean busy;
    /**
     * 記録開始
     */
    public void startLog() {
        alive = true;
        final JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setBackground(new Color(0x01010101, true));
        frame.setAlwaysOnTop(true);

        frame.pack();
        frame.setVisible(true);
        frame.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                System.out.println(arg0);
                frame.setVisible(false);
                long now = System.currentTimeMillis();
                Point point = MouseInfo.getPointerInfo().getLocation();
                if (pressTime < 0) {
                    //クリック処理
                    click(arg0.getButton(), arg0.getClickCount());
                    if (!ProgramView.this.contains(point)) {
                        //テキストに出力
                        if (startTime > 0) {
                            textArea.setText(textArea.getText() + "\nS:" + (now - startTime));
                            startTime = now;
                            textArea.setText(textArea.getText() + "\nC" + arg0.getButton() +":" + point.x + "," + point.y);
                        } else {
                            startTime = now;
                            textArea.setText("C" + arg0.getButton() +":" + point.x + "," + point.y);
                        }
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
                execButton.setEnabled(false);
                textArea.setEnabled(false);
                clearButton.setEnabled(false);
                try {
                    while (alive) {
                        if (frame.isVisible()) {
                            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
                            frame.setBounds(pointerInfo.getLocation().x - 3, pointerInfo.getLocation().y - 3, 10, 10);
                        }
                        Thread.sleep(1);
                    }
                    frame.dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                logButton.getAction().putValue(Action.NAME, LogAction.START);
                logButton.setEnabled(true);
                execButton.setEnabled(true);
                textArea.setEnabled(true);
                clearButton.setEnabled(true);
            }
            
        });
        thread.start();
        
    }
    
    public void textOut(int button, int clickCount) {
    }
    /**
     * クリック処理
     */
    public void click(int button, int clickCount) {
        int button2 = 0x20 >> button;
        for (int i = 0; i < clickCount; i++) {
            if (i > 0) {
                robot.delay(20);
            }
            robot.mousePress(button2);
            robot.delay(20);
            robot.mouseRelease(button2);
        }
    }
    
    /**
     * ドラッグアンドドロップ処理
     */
    public void dragAndDrop() {
        
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
                String[] command = new String[2];
                int colonIndex = value.indexOf(':');
                command[0] = value.substring(0, colonIndex);
                command[1] = value.substring(colonIndex + 1);
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
                        robot.keyPress(ch - 'a' + KeyEvent.VK_A);
                        robot.keyRelease(ch - 'a' + KeyEvent.VK_A);
                    }
                } else if (KEY_PRESS.equals(command[0])) {
                    //キー押下
                    for (char ch : command[1].toCharArray()) {
                        robot.keyPress(ch - 'a' + KeyEvent.VK_A);
                    }
                } else if (KEY_RELEASE.equals(command[0])) {
                    //キー開放
                    for (char ch : command[1].toCharArray()) {
                        robot.keyRelease(ch - 'a' + KeyEvent.VK_A);
                    }
                } else if (EXECUTE.equals(command[0])) {
                    try {
                        Process process = Runtime.getRuntime().exec(command[1]);
//                        process.waitFor();
//                        System.out.println(process.exitValue());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage());
//                    } catch (InterruptedException e) {
//                        JOptionPane.showMessageDialog(this, e.getMessage());
                    }
                } else if (BROWSE.equals(command[0])) {
                    try {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.browse(new URI(command[1]));
                    } catch (URISyntaxException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage());
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage());
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
