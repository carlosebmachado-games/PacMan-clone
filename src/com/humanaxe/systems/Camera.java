package com.humanaxe.systems;

public class Camera {

    public static int x = 0;
    public static int y = 0;

    public static int clamp(int cur, int min, int max) {
        if (cur > max) {
            cur = max;
        }
        if (cur < min) {
            cur = min;
        }
        return cur;
    }

}
