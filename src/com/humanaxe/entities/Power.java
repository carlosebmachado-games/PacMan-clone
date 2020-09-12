package com.humanaxe.entities;

import java.awt.image.BufferedImage;

public class Power extends Entity {

    public Power(double x, double y, int width, int height, double speed, BufferedImage sprite) {
        super(x, y, width, height, speed, sprite);
        depth = 0;
    }

}
