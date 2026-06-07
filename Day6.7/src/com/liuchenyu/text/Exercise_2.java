package com.liuchenyu.text;

import java.util.Scanner;

public class Exercise_2 {
    static void main(String[] args) {


        int rows, cols;
        String symbol;


        System.out.println("请输入行数：");
        Scanner a = new Scanner(System.in);
        rows = a.nextInt();
        System.out.println("请输入列数：");
        Scanner b = new Scanner(System.in);
        cols = b.nextInt();
        System.out.println("请输入符号：");
        Scanner c = new Scanner(System.in);
        symbol = c.next();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(symbol+" ");
            }
            System.out.println();
        }
    }
}
