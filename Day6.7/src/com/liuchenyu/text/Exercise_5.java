package com.liuchenyu.text;

public class Exercise_5 {
    /*static void main(String[] args) {
        String name = "Lewis Hamilton";
        HELLO(name);
    }

    static void HELLO(String ABDDBUYICS) {
        System.out.println("Hello, "+ABDDBUYICS);
    }*/
    static void main(String[] args) {
        String name = "Lewis Hamilton";
        char a = name.charAt(0);
        int b = name.length();
        boolean c = name.contains("H");
        double d = name.indexOf("H");
        System.out.printf("a: %c, b: %d, c: %b, d: %f", a, b, c, d);
        System.out.println();
    }
}
