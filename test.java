package com.liuchenyu.test;

import java.util.Scanner;

public class test {
    static void main(String[] args) {
        //彩票中奖案例
        //1，生成一个7位随机数
        int number = (int)(Math.random()*10000000);

        //2，键盘录入一个7位数表示用户购买的彩票
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入7位数的彩票：");
        int userNumber = sc.nextInt();


        //3，判断用户输入的彩票是否和系统生成的彩票一致
        if(number == userNumber){
            System.out.println("恭喜你，中奖了");
        }
        else{
            System.out.println("请重新输入");
        }

        System.out.println("系统生成的彩票是："+number);

    }
}
