package com.meme.jordan.messageTestJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrame extends JFrame implements WindowListener {
	
	OptionsFrame options = new OptionsFrame();
	
    public MainFrame() {

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 3, 5);

        JTextPane displayText = new JTextPane();
        displayText.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(displayText);

        JTextField inputText = new JTextField();
        JButton sendBtn = new JButton("Send");

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
    }

    public static void main(String... args) {
        new MainFrame();
    }

    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
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
}
