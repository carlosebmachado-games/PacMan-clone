package com.humanaxe.overall;

import com.humanaxe.systems.Camera;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.humanaxe.entities.Entity;
import com.humanaxe.entities.Food;
import com.humanaxe.entities.Power;
import com.humanaxe.main.Game;
import javax.swing.JOptionPane;

public class World {

    public final int FLOOR = 0xFF000000;
    public final int WALL = 0xFFFFFFFF;
    public final int PLAYER = 0xFF0026FF;
    public final int FOOD = 0xFF429058;
    public final int POWER = 0xFF153C4A;
    public final int RGHOST = 0xFFFF6A00;
    public final int BGHOST = 0xFF00FFFF;
    public final int PGHOST = 0xFFFB00FF;
    public final int YGHOST = 0xFFFFD000;

    public static Tile[] tiles;
    public static int WIDTH, HEIGHT;
    public static final int TILE_SIZE = 16;

    // criar as constantes das cores
    public World(String path) {
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            int[] pixels = new int[map.getWidth() * map.getHeight()];
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            tiles = new Tile[map.getWidth() * map.getHeight()];
            map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
            for (int xx = 0; xx < map.getWidth(); xx++) {
                for (int yy = 0; yy < map.getHeight(); yy++) {
                    int pixelAtual = pixels[xx + (yy * map.getWidth())];
                    tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
                    switch (pixelAtual) {
                        case FLOOR:
                            tiles[xx + (yy * WIDTH)] = new FloorTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_FLOOR);
                            break;
                        case WALL:
                            tiles[xx + (yy * WIDTH)] = new WallTile(xx * TILE_SIZE, yy * TILE_SIZE, Tile.TILE_WALL);
                            break;
                        case FOOD: {
                            Food fruta = new Food(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, 0, Entity.FOOD);
                            Game.entities.add(fruta);
                            Game.totalFruit++;
                            break;
                        }
                        case POWER: {
                            Power fruta = new Power(xx * TILE_SIZE, yy * TILE_SIZE, TILE_SIZE, TILE_SIZE, 0, Entity.POWER);
                            Game.entities.add(fruta);
                            Game.totalFruit++;
                            break;
                        }
                        default:
                            break;
                    }
                }
            }
            setEntitiesDefaultPosition();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred.\nMessage: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void setEntitiesDefaultPosition() {
        setPlayerDefaultPosition();
        setRedGhostDefaultPosition();
        setBlueGhostDefaultPosition();
        setPinkGhostDefaultPosition();
        setOrangeGhostDefaultPosition();
        Game.redGhost.stopped = false;
    }

    public static void setPlayerDefaultPosition() {
        Game.player.setX((int) (13.5 * TILE_SIZE));
        Game.player.setY((int) (23 * TILE_SIZE));
    }

    public static void setRedGhostDefaultPosition() {
        Game.redGhost.setX((int) (13.5 * TILE_SIZE));
        Game.redGhost.setY((int) (11 * TILE_SIZE));
        Game.redGhost.path = null;
    }

    public static void setRedGhostInsidePosition() {
        Game.redGhost.setX((int) (13.5 * TILE_SIZE));
        Game.redGhost.setY((int) (14 * TILE_SIZE));
        Game.redGhost.path = null;
    }

    public static void setBlueGhostDefaultPosition() {
        Game.blueGhost.setX((int) (11.5 * TILE_SIZE));
        Game.blueGhost.setY((int) (14 * TILE_SIZE));
        Game.blueGhost.path = null;
    }

    public static void setPinkGhostDefaultPosition() {
        Game.pinkGhost.setX((int) (13.5 * TILE_SIZE));
        Game.pinkGhost.setY((int) (14 * TILE_SIZE));
        Game.pinkGhost.path = null;
    }

    public static void setOrangeGhostDefaultPosition() {
        Game.orangeGhost.setX((int) (15.5 * TILE_SIZE));
        Game.orangeGhost.setY((int) (14 * TILE_SIZE));
        Game.orangeGhost.path = null;
    }

    public static boolean isFree(int xnext, int ynext) {

        int x1 = xnext / TILE_SIZE;
        int y1 = ynext / TILE_SIZE;

        int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
        int y2 = ynext / TILE_SIZE;

        int x3 = xnext / TILE_SIZE;
        int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

        int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
        int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

        return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
                || (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
                || (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
                || (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
    }

    public void render(Graphics g) {
        int xstart = Camera.x >> 4;
        int ystart = Camera.y >> 4;

        int xfinal = xstart + (Game.SCREEN_WIDTH >> 4);
        int yfinal = ystart + (Game.SCREEN_HEIGHT >> 4);

        for (int xx = xstart; xx <= xfinal; xx++) {
            for (int yy = ystart; yy <= yfinal; yy++) {
                if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
                    continue;
                }
                Tile tile = tiles[xx + (yy * WIDTH)];
                tile.render(g);
            }
        }
    }

}
