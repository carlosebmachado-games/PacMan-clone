package com.humanaxe.overall;

import com.humanaxe.systems.Camera;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.humanaxe.main.Game;

public class Tile {

    public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(World.TILE_SIZE * 0, World.TILE_SIZE * 0, World.TILE_SIZE, World.TILE_SIZE);
    public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(World.TILE_SIZE * 1, World.TILE_SIZE * 0, World.TILE_SIZE, World.TILE_SIZE);

    private final BufferedImage sprite;
    private int x;
    private int y;

    public Tile(int x, int y, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void render(Graphics g) {
        g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
    }

}
