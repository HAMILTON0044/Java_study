package com.liuchenyu.text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyFrame extends JFrame implements ActionListener {

    JButton button;

    MyFrame() {
        this.setLayout(null);

        button = new JButton("按钮");
        button.setBounds(100, 100, 100, 50);
        button.setBackground(Color.BLUE);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.addActionListener(this);

        this.add(button);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 300);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            System.out.println("666");
//            JOptionPane.showMessageDialog(null, "按钮被点击了！");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyFrame::new);
    }
}
