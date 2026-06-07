package com.liuchenyu.text;

import jdk.jfr.Threshold;

import javax.swing.*;
import java.util.Random;
import java.util.Scanner;

public class Exercise {
    static void main(String[] args) {

        //test 1--------------------------------------------------------------------------------------

        /*String name = JOptionPane.showInputDialog("请输入你的名字：");
        JOptionPane.showMessageDialog(null, "hello," + name);
        int age = Integer.parseInt(JOptionPane.showInputDialog("请输入你的年龄："));
        JOptionPane.showMessageDialog(null, "hello," + name + "，你今年" + age + "岁");*/

        //test 2--------------------------------------------------------------------------------------

        /*double a ;

        double b ;

        a = Double.parseDouble(JOptionPane.showInputDialog("请输入第一个数字："));
        b = Double.parseDouble(JOptionPane.showInputDialog("请输入第二个数字："));

        double c =Math.max(a,b);
        if (c == a){
            JOptionPane.showMessageDialog(null, c +"比" + b + "大");
        }
        else{
            JOptionPane.showMessageDialog(null, c +"比" + a + "大");
        }*/

        //test 3--------------------------------------------------------------------------------------

        /*double a, b, c;
        a = Double.parseDouble(JOptionPane.showInputDialog("请输入第一个数字："));
        b = Double.parseDouble(JOptionPane.showInputDialog("请输入第二个数字："));

        c = Math.sqrt(a * a + b * b);
        JOptionPane.showMessageDialog(null, "斜边长为：" + c);*/

        //test 4--------------------------------------------------------------------------------------

//        Random R = new Random();
//        int a = R.nextInt();
        /*while(true){
            Random R = new Random();
//            double a = R.nextDouble(10)*(-1);
            double a = R.nextDouble(-10,10);
            System.out.println(a);
        }
//        System.out.println(a);*/

        //test 5--------------------------------------------------------------------------------------


        /*Scanner sc = new Scanner(System.in);
        System.out.println("you re playing a game, press 'q' or 'Q' to quit ");
        String a = sc.next();*/

//        System.out.println("you re playing a game, press 'q' or 'Q' to quit ");
        /*JOptionPane.showMessageDialog(null, "you re playing a game\npress 'q' or 'Q' to quit ");
        String res = JOptionPane.showInputDialog(null);


        if (res.equals("q") || res.equals("Q")){
//            System.out.println("you quit the game");
            JOptionPane.showMessageDialog(null, "you quit the game");
        }
        else{
//            System.out.println("you are still playing the game");
            JOptionPane.showMessageDialog(null, "you are still playing the game");*/
        Scanner sc = new Scanner(System.in);
        String name = "";
        while(name.isBlank()){
            System.out.println("请输入姓名");
            name = sc.nextLine();
        }

        System.out.println("Good fucking morning,"+ name);



    }
}

/*
//fuck you
class test{
    public static void main(String[] args) {
        System.out.println("fuck you");
    }
}*/
