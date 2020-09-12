package com.humanaxe.systems;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.humanaxe.main.Game;

public class UI {

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("arial", Font.BOLD, 18));
        g.drawString("Food: " + Game.curFruit + "/" + Game.totalFruit, 30, 30);
    }

}
