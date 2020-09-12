package com.humanaxe.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.humanaxe.main.Game;
import com.humanaxe.systems.Camera;
import com.humanaxe.systems.Node;
import com.humanaxe.systems.Vector2i;
import com.humanaxe.overall.World;

public class Entity {

    public static BufferedImage FOOD   = Game.spritesheet.getSprite(World.TILE_SIZE * 1, World.TILE_SIZE * 3, World.TILE_SIZE, World.TILE_SIZE);
    public static BufferedImage POWER  = Game.spritesheet.getSprite(World.TILE_SIZE * 2, World.TILE_SIZE * 3, World.TILE_SIZE, World.TILE_SIZE);
    public static BufferedImage RGHOST = Game.spritesheet.getSprite(World.TILE_SIZE * 0, World.TILE_SIZE * 2, World.TILE_SIZE, World.TILE_SIZE);
    public static BufferedImage BGHOST = Game.spritesheet.getSprite(World.TILE_SIZE * 1, World.TILE_SIZE * 2, World.TILE_SIZE, World.TILE_SIZE);
    public static BufferedImage PGHOST = Game.spritesheet.getSprite(World.TILE_SIZE * 2, World.TILE_SIZE * 2, World.TILE_SIZE, World.TILE_SIZE);
    public static BufferedImage YGHOST = Game.spritesheet.getSprite(World.TILE_SIZE * 3, World.TILE_SIZE * 2, World.TILE_SIZE, World.TILE_SIZE);
    public static BufferedImage GHOST  = Game.spritesheet.getSprite(World.TILE_SIZE * 3, World.TILE_SIZE * 0, World.TILE_SIZE, World.TILE_SIZE);

    protected double x;
    protected double y;
    protected int width;
    protected int height;
    protected double speed;

    public int depth;

    protected List<Node> path;

    public boolean debug = false;

    private BufferedImage sprite;

    public static Random rand = new Random();

    public Entity(double x, double y, int width, int height, double speed, BufferedImage sprite) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.sprite = sprite;
    }

    public static Comparator<Entity> nodeSorter = (Entity n0, Entity n1) -> {
        if (n1.depth < n0.depth) {
            return +1;
        }
        if (n1.depth > n0.depth) {
            return -1;
        }
        return 0;
    };

//    public void updateCamera() {
//        Camera.x = Camera.clamp(this.getX() - (Game.SCREEN_WIDTH / 2), 0, World.WIDTH * World.TILE_SIZE - Game.SCREEN_WIDTH);
//        Camera.y = Camera.clamp(this.getY() - (Game.SCREEN_HEIGHT / 2), 0, World.HEIGHT * World.TILE_SIZE - Game.SCREEN_HEIGHT);
//    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public int getX() {
        return (int) this.x;
    }

    public int getY() {
        return (int) this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void tick() {
    }

    public double calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void followPath(List<Node> path) {
        if (path != null) {
            if (path.size() > 0) {
                Vector2i target = path.get(path.size() - 1).tile;
                //xprev = x;
                //yprev = y;
                if (x < target.x * World.TILE_SIZE) {
                    x++;
                } else if (x > target.x * World.TILE_SIZE) {
                    x--;
                }

                if (y < target.y * World.TILE_SIZE) {
                    y++;
                } else if (y > target.y * World.TILE_SIZE) {
                    y--;
                }

                if (x == target.x * World.TILE_SIZE && y == target.y * World.TILE_SIZE) {
                    path.remove(path.size() - 1);
                }
            }
        }
    }

    public static boolean isColidding(Entity e1, Entity e2) {
        Rectangle e1Mask = new Rectangle(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight());
        Rectangle e2Mask = new Rectangle(e2.getX(), e2.getY(), e2.getWidth(), e2.getHeight());

        return e1Mask.intersects(e2Mask);
    }

    public void render(Graphics g) {
        g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
    }

}
