package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Random;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import MyLoader.MyLoader;
import log.Logger;
import primitives.IRobot;
import primitives.RoboJobo;
import primitives.Target;

/**
 * Что требуется сделать: 1. Метод создания меню перегружен функционалом и трудно читается. Следует
 * разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {

    private GameWindow gameWindow;
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        UIManager.put("OptionPane.yesButtonText"   , "Да");
        UIManager.put("OptionPane.noButtonText"    , "Нет");
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }


            public void windowClosing(WindowEvent e) {
                onClose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
//
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        return menuBar;
//    }

    private JMenuItem createSchemeMenuItem(String title, ActionListener listener) {
        JMenuItem schemeMenuItem = new JMenuItem(title, KeyEvent.VK_S);
        schemeMenuItem.addActionListener(listener);
        return schemeMenuItem;
    }

    private JMenu createMenu(String title, String description, int key) {
        JMenu newMenu = new JMenu(title);
        newMenu.setMnemonic(key);
        newMenu.getAccessibleContext().setAccessibleDescription(description);
        return newMenu;
    }


    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = createMenu("Режим отображения",
                "Управление режимом отображения приложения", KeyEvent.VK_V);

        lookAndFeelMenu.add(createSchemeMenuItem("Системная схема", (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }));

        lookAndFeelMenu.add(createSchemeMenuItem("Универсальная схема", (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        }));

        JMenu testMenu = createMenu("Тесты", "Тестовые команды", KeyEvent.VK_T);

        testMenu.add(createSchemeMenuItem("Сообщения в лог", (event) -> {
            Logger.debug("Новая строка");
        }));
        JMenu exitMenu = createMenu("Выход", "Сюда выходить", KeyEvent.VK_Q);

        exitMenu.add(createSchemeMenuItem("Выйти", (event) -> {onClose();}));

        JMenu logicMenu = createMenu("Логика робота", "Выбрать логику", KeyEvent.VK_L);

        logicMenu.add(createSchemeMenuItem("Выбор логики", (event) -> chooseLogic()));

        menuBar.add(exitMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(logicMenu);
        return menuBar;
    }

    private void onClose()
    {
        int result = JOptionPane.showConfirmDialog(null, "Вы действительно хотите выйти?", "Выход",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void chooseLogic()
    {

        final JFileChooser chooser =  new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                ".class files", "class");
        chooser.addChoosableFileFilter(filter);
        int returnVal = chooser.showDialog(this, "ДА");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            try {
                MyLoader loader = new MyLoader(file);
                var name = file.getName();
                var c = loader.loadClass(name.substring(0, name.length() -6));
                var robot = (IRobot)c.getConstructor(double.class, double.class, double.class, Target.class).newInstance(50, 50, 0, gameWindow.getVisualizer().getTarget());
                var robotJobber = new RoboJobo(robot, 10);
                gameWindow.getVisualizer().addRobotLogic(robot);
                robotJobber.start();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
