package com.meme.jordan.messageTestJava;

import javax.swing.*;
import java.awt.*;

public class OptionsFrame extends JFrame {
    public OptionsFrame(MainFrame main) {
        JTextField nameField = new JTextField();
        JCheckBox serverCheck = new JCheckBox();
        JTextField ipField = new JTextField();
        JLabel nameLabel = new JLabel("Chat Name");
        JLabel serverLabel = new JLabel("Run as Server");
        JLabel ipLabel = new JLabel("Host IP Address");
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        add(nameLabel);
        add(nameField);
        add(serverLabel);
        add(serverCheck);
        add(ipLabel);
        add(ipField);
        add(okButton);
        add(cancelButton);

        setLayout(new GridLayout(0, 2));
        setSize(300,200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void showFrame(){
        setVisible(true);
    }
}
