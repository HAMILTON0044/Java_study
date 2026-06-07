package com.liuchenyu.text;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String name = JOptionPane .showInputDialog("请输入你的名字");
        JOptionPane.showMessageDialog(null, "hello," + name);
    }
}
