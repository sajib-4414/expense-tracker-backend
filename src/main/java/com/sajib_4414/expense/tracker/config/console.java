package com.sajib_4414.expense.tracker.config;

public class console {
    public static void log(Object o){
        System.out.println(o);
    }
    public static void log(Object... objects) {
        for (Object obj : objects) {
            if (obj == null) {
                System.out.println("null");
            } else {
                System.out.println(obj);
            }
        }
    }
}
