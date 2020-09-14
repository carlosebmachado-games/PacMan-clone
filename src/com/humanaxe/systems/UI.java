package com.humanaxe.systems;

import com.humanaxe.entities.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.humanaxe.main.Game;
import com.humanaxe.overall.World;
import java.awt.image.BufferedImage;

public class UI {

    public static BufferedImage FOOD_SPRITE
            = Game.spritesheet.getSprite(
                    World.TILE_SIZE * 0, World.TILE_SIZE * 3,
                    World.TILE_SIZE, World.TILE_SIZE);
    public static BufferedImage LIFE_SPRITE
            = Game.spritesheet.getSprite(
                    World.TILE_SIZE * 0, World.TILE_SIZE * 1,
                    World.TILE_SIZE, World.TILE_SIZE);

    public static void drawCentralizedText(Graphics g, String text, int y) {
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text, Game.SCREEN_WIDTH / 2 - width / 2, y);
    }

    public void winScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD,
                (int)((Game.SCREEN_HEIGHT * Game.SCALE) * 0.0411)));
        drawCentralizedText(g, "CONGRATULATIONS!",
                (int)((Game.SCREEN_HEIGHT * Game.SCALE) * 0.1851));
        drawCentralizedText(g, "You won the game!",
                (int)((Game.SCREEN_HEIGHT * Game.SCALE) * 0.2386));
        g.setFont(new Font("arial", Font.BOLD,
                (int)((Game.SCREEN_HEIGHT * Game.SCALE) * 0.0288)));
        drawCentralizedText(g, "Press enter to restart...",
                (int)((Game.SCREEN_HEIGHT * Game.SCALE) * 0.3497));
    }

    public void loseScreen(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("arial", Font.BOLD,
                (int)((Game.SCREEN_HEIGHT * Game.SCALE) * 0.0411)));
        drawCentralizedText(g, "GAME OVER",
                (int)((Game.SCREEN_HEIGHT * Game.SCALE) * 0.1851));
        drawCentralizedText(g, "You lose the game :(",
                (int)((Game.SCREEN_HEIGHT * Game.SCALE) * 0.2386));
        g.setFont(new Font("arial", Font.BOLD,
                (int)((Game.SCREEN_HEIGHT * Game.SCALE) * 0.0288)));
        drawCentralizedText(g, "Press enter to restart...",
                (int)((Game.SCREEN_HEIGHT * Game.SCALE) * 0.3497));
    }

    public void render(Graphics g) {
        //score
        double xScore = 15;
        double yScore = 502;
        double sizeScore = World.TILE_SIZE;
        double sizeFontScore = 13;
        double xFontScore = 40;
        double yFontScore = yScore + sizeFontScore;
        //lifes
        double xLife = 418;
        double yLife = yScore;
        double sizeLife = World.TILE_SIZE;
        double pad = World.TILE_SIZE * 1.6;
        if(Game.SCALE > 1){
            //score
            xScore = (Game.SCREEN_WIDTH * Game.SCALE) * 0.015;
            yScore = (Game.SCREEN_HEIGHT * Game.SCALE) * 0.02;
            sizeScore = World.TILE_SIZE * (Game.SCREEN_WIDTH * Game.SCALE) * 0.0016;
            xFontScore = (Game.SCREEN_WIDTH * Game.SCALE) * 0.058;
            yFontScore = (Game.SCREEN_HEIGHT * Game.SCALE) * 0.054;
            sizeFontScore = (Game.SCREEN_WIDTH * Game.SCALE) * 0.021;
            //lifes
            xLife = (Game.SCREEN_WIDTH * Game.SCALE) * 0.96;
            yLife = (Game.SCREEN_HEIGHT * Game.SCALE) * 0.02;
            sizeLife = World.TILE_SIZE * (Game.SCREEN_WIDTH * Game.SCALE) * 0.0016;
            pad = World.TILE_SIZE * 2;
        }
        // draw score
        g.setColor(Color.white);
        g.drawImage(FOOD_SPRITE, (int) xScore, (int) yScore, (int) sizeScore, (int) sizeScore, null);
        g.setFont(new Font("arial", Font.BOLD, (int) sizeFontScore));
        g.drawString(Game.curFruit + "/" + Game.totalFruit,
                (int) (xFontScore), (int) (yFontScore));
        // draw lifes
        if (Player.lifes > 2)
            g.drawImage(LIFE_SPRITE, (int) (xLife - pad * 2), (int) yLife,
                    (int) sizeLife, (int) sizeLife, null);
        if (Player.lifes > 1)
            g.drawImage(LIFE_SPRITE, (int) (xLife - pad), (int) yLife,
                    (int) sizeLife, (int) sizeLife, null);
        if (Player.lifes > 0)
            g.drawImage(LIFE_SPRITE, (int) xLife, (int) yLife,
                    (int) sizeLife, (int) sizeLife, null);
    }

}
