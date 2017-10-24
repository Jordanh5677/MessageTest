package com.meme.jordan.messageTestJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame implements WindowListener, MessageListener, ActionListener {

    OptionsFrame options = new OptionsFrame(this);
    MessageSender sender;
    JTextArea displayText;
    JTextField inputText;
    JButton sendBtn;

    public MainFrame() {

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 3, 5);

        displayText = new JTextArea();
        displayText.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(displayText);

        inputText = new JTextField();
        sendBtn = new JButton("Send");

        inputText.addActionListener(this);
        sendBtn.addActionListener(this);

        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(scrollPane, gbc);
        add(scrollPane);

        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(inputText, gbc);
        add(inputText);

        gbc.weightx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(sendBtn, gbc);
        add(sendBtn);

        JMenuItem menuitem = new JMenuItem("Options");
        menuitem.addActionListener(e -> options.setVisible(true));

        JMenu menu = new JMenu("Options");
        menu.add(menuitem);

        JMenuBar menubar = new JMenuBar();
        menubar.add(menu);

        setJMenuBar(menubar);

        setSize(500, 800);
        setLayout(layout);
        addWindowListener(this);
        setVisible(true);

        updateSender();
    }

    public static void main(String... args) {
        new MainFrame();
    }

    void updateSender() {
        if (sender != null)
            sender.stop();
        if (options.isServer())
            sender = new Server(this);
        else
            sender = new Client(this, options.getIp());
        sender.start();
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        if (sender != null)
            sender.stop();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowActivated(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }

    @Override
    public void onMessage(String msg) {
        displayText.append(msg + "\n");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String msg = options.getName() + ": " + inputText.getText();
        sender.send(msg);
        displayText.append(msg + "\n");
        inputText.setText("");
    }
}
