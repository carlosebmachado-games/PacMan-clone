package com.humanaxe.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.humanaxe.main.Game;
import com.humanaxe.systems.AStar;
import com.humanaxe.systems.Camera;
import com.humanaxe.systems.Vector2i;
import com.humanaxe.overall.World;

public class Enemy extends Entity {

    public boolean ghostMode = false;

    private int ghostModeCurFrame = 0, ghostModeMaxFrames = 180;

    public Enemy(int x, int y, int width, int height, int speed, BufferedImage sprite) {
        super(x, y, width, height, speed, sprite);
    }

    @Override
    public void tick() {
        if (Game.player.dir == 4) {
            return;
        }

        depth = 0;
        if (ghostMode == false) {
            if (path == null || path.isEmpty()) {
                Vector2i start = new Vector2i(((int) (x / World.TILE_SIZE)), ((int) (y / World.TILE_SIZE)));
                Vector2i end = new Vector2i(((int) (Game.player.x / World.TILE_SIZE)), ((int) (Game.player.y / World.TILE_SIZE)));
                path = AStar.findPath(Game.world, start, end);
            }

            if (new Random().nextInt(100) < 80) {
                followPath(path);
            }

            if (x % World.TILE_SIZE == 0 && y % World.TILE_SIZE == 0) {
                if (new Random().nextInt(100) < 10) {
                    Vector2i start = new Vector2i(((int) (x / World.TILE_SIZE)), ((int) (y / 16)));
                    Vector2i end = new Vector2i(((int) (Game.player.x / World.TILE_SIZE)), ((int) (Game.player.y / World.TILE_SIZE)));
                    path = AStar.findPath(Game.world, start, end);
                }
            }
        }

        if (ghostMode) {
            ghostModeCurFrame++;
            if (ghostModeCurFrame >= ghostModeMaxFrames) {
                ghostModeCurFrame = 0;
                ghostMode = false;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (ghostMode == false) {
            super.render(g);
        } else {
            g.drawImage(Entity.GHOST, this.getX() - Camera.x, this.getY() - Camera.y, null);
        }
    }

}
