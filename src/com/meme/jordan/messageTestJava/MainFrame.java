package com.meme.jordan.messageTestJava;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {

        JTextPane displayText = new JTextPane();
        displayText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayText);

        JPanel textPanel = new JPanel();
        JTextField inputText = new JTextField();
        JButton sendBtn = new JButton("Send");

        textPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;

        textPanel.add(inputText, gbc);
        textPanel.add(sendBtn);

        add(scrollPane);
        add(textPanel);

        setLayout(new GridLayout(0, 1));
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String... args) {
        new MainFrame();
    }
}
