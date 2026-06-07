package com.liuchenyu.text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Exercise_4 {
    static void main(String[] args) {
        /*String name = "lewishamilton";
        int a = name.length();
        char b = name.charAt(4);
        boolean c = name.equals("Luchenyu");

        String d = name.toUpperCase();
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);*/

        //============================================================================================================
        ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
        ArrayList<String> foodlist = new ArrayList<String>();
        foodlist.add("apple");
        foodlist.add("banana");
        foodlist.add("orange");
        ArrayList<String> drinklist = new ArrayList<String>();
        drinklist.add("water");
        drinklist.add("milk");
        drinklist.add("coffee");
        list.add(foodlist);
        list.add(drinklist);

        /*System.out.println(foodlist);
        System.out.println(drinklist);*/
        System.out.println(list);
        System.out.println(list.get(0));
        System.out.println(list.get(1));
        System.out.println(list.get(0).get(1));
    }
}
