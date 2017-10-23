package com.meme.jordan.messageTestJava;

import javax.swing.*;
import java.awt.*;

public class OptionsFrame extends JFrame {
	
	JTextField nameField;
	JCheckBox serverCheck;
	JTextField ipField;
	
	String name;
	boolean isServer;
	String ip;
	
    public OptionsFrame() {

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 3, 3, 3);

         nameField = new JTextField();
         serverCheck = new JCheckBox();
         ipField = new JTextField();
         
        JLabel nameLabel = new JLabel("Chat Name");
        JLabel serverLabel = new JLabel("Run as Server");
        JLabel ipLabel = new JLabel("Host IP Address");
        
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        serverCheck.addItemListener(e -> {
        	boolean disable = ((JCheckBox) e.getItem()).isSelected();
        	ipField.setEnabled(!disable);
        	ipLabel.setEnabled(!disable);
        });
        
        okButton.addActionListener(e -> {
        	name = nameField.getText();
        	isServer = serverCheck.isSelected();
        	ip = ipField.getText();
        	setVisible(false);
        });
        
        cancelButton.addActionListener(e -> setVisible(false));

        gbc.weightx = 0;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(nameLabel, gbc);
        add(nameLabel);

        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(nameField, gbc);
        add(nameField);

        gbc.weightx = 0;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(serverLabel, gbc);
        add(serverLabel);

        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(serverCheck, gbc);
        add(serverCheck);

        gbc.weightx = 0;
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(ipLabel, gbc);
        add(ipLabel);

        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(ipField, gbc);
        add(ipField);

        gbc.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(okButton, gbc);
        add(okButton);

        gbc.gridwidth = GridBagConstraints.RELATIVE;
        layout.setConstraints(cancelButton, gbc);
        add(cancelButton);

        setLayout(layout);
        setSize(300, 150);
        setResizable(false);
    }

    public String getName(){
    	return name;
    }
    
    public boolean isServer() {
    	return isServer;
    }
    
    public String getIp(){
    	return ip;
    }
    
}
