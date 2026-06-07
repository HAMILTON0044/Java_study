package com.liuchenyu.text;

public class Exercise_3 {
    static void main(String[] args) {
        /*String[] cars = {"Volvo", "BMW", "Ford", "Mazda"};
        cars[0] = "Mercedes";

        System.out.println(cars[0]);
        for (int i = 0; i < cars.length; i++) {
            System.out.println(cars[i]);
        }*/

        String[][] cars = new String[3][3];
        cars[0][0] = "Volvo";
        cars[0][1] = "BMW";
        cars[0][2] = "Ford";
        cars[1][0] = "Mazda";
        cars[1][1] = "Mercedes";
        cars[1][2] = "Audi";
        cars[2][0] = "Honda";
        cars[2][1] = "Tesla";
        cars[2][2] = "Toyota";
        for (int i = 0; i < cars.length; i++) {
            for (int j = 0; j < cars[i].length; j++) {
                System.out.print(cars[i][j] + " ");
            }
            System.out.println();
        }

    }
}
