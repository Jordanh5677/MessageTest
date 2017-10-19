package com.meme.jordan.messageTestJava;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

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

        setSize(500, 800);
        setLayout(layout);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String... args) {
        new MainFrame();
    }
}
