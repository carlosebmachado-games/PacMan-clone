package com.humanaxe.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.humanaxe.main.Game;
import com.humanaxe.systems.Camera;
import com.humanaxe.overall.World;
import com.humanaxe.systems.Sound;

public class Player extends Entity {

    public boolean press_right, press_up, press_left, press_down;

    public BufferedImage sprite_left;
    public BufferedImage sprite_right;
    public BufferedImage sprite_up;
    public BufferedImage sprite_down;

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int BALL = 4;
    public int dir = BALL;
    public int newDir = BALL;
    public boolean opened = false;

    private int ox = 0, oy = 0;

    private int animCurFrame = 0, animMaxFrames = 5;

    public static int lifes = 3;

    public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
        super(x, y, width, height, speed, sprite);
        sprite_left = Game.spritesheet.getSprite(World.TILE_SIZE * 2, World.TILE_SIZE * 1, World.TILE_SIZE, World.TILE_SIZE);
        sprite_right = Game.spritesheet.getSprite(World.TILE_SIZE * 0, World.TILE_SIZE * 1, World.TILE_SIZE, World.TILE_SIZE);
        sprite_up = Game.spritesheet.getSprite(World.TILE_SIZE * 1, World.TILE_SIZE * 1, World.TILE_SIZE, World.TILE_SIZE);
        sprite_down = Game.spritesheet.getSprite(World.TILE_SIZE * 3, World.TILE_SIZE * 1, World.TILE_SIZE, World.TILE_SIZE);
    }

    @Override
    public void tick() {
        depth = 1;

        if (press_right) {
            newDir = RIGHT;
        } else if (press_left) {
            newDir = LEFT;
        } else if (press_up) {
            newDir = UP;
        } else if (press_down) {
            newDir = DOWN;
        }

        if (newDir != dir
                && (newDir == RIGHT && World.isFree((int) (x + speed), this.getY())
                || newDir == LEFT && World.isFree((int) (x - speed), this.getY())
                || newDir == UP && World.isFree(this.getX(), (int) (y - speed))
                || newDir == DOWN && World.isFree(this.getX(), (int) (y + speed)))) {
            dir = newDir;
        }

        if (dir == RIGHT && World.isFree((int) (x + speed), this.getY())) {
            x += speed;
        } else if (dir == LEFT && World.isFree((int) (x - speed), this.getY())) {
            x -= speed;
        } else if (dir == UP && World.isFree(this.getX(), (int) (y - speed))) {
            y -= speed;
        } else if (dir == DOWN && World.isFree(this.getX(), (int) (y + speed))) {
            y += speed;
        }

        // verifica se existe comida
        for (Entity current : Game.entities) {
            if (current instanceof Food || current instanceof Power) {
                if (Entity.isColidding(this, current)) {
                    if (current instanceof Power) {
                        Game.entities.stream().filter((e) -> (e instanceof Enemy)).forEachOrdered((e) -> {
                            ((Enemy) e).ghostMode = true;
                        });
                    }
                    Game.curFruit++;
                    Game.entities.remove(current);
                    Sound.chomp.play();
                    break;
                }
            }
        }

        // verifica se acabou as frutas no mapa
        if (Game.curFruit >= Game.totalFruit) {
            Sound.intermission.play();
            Game.state = Game.WIN;
            return;
        }
        // verifica se ainda possui vidas
        if (lifes <= 0) {
            Game.state = Game.LOSE;
            return;
        }

        // verifica se chegou nas bordas para troca de lado no mapa
        if (x >= Game.SCREEN_WIDTH * Game.SCALE - World.TILE_SIZE - 1) {
            Game.player.setX((int) (0 * World.TILE_SIZE + 2));
            Game.player.setY((int) (14 * World.TILE_SIZE));
        } else if (x <= 1 ) {
            Game.player.setX((int) (27 * World.TILE_SIZE - 2));
            Game.player.setY((int) (14 * World.TILE_SIZE));
        }
        
        // cria a anim do pac-man
        if (moving()) {
            animCurFrame++;
            if (animCurFrame >= animMaxFrames) {
                opened = !opened;
                animCurFrame = 0;
            }
        } else {
            opened = true;
        }

        updateCamera();
    }

    private boolean moving() {
        boolean moving = false;
        if (ox != getX() || oy != getY()) {
            moving = true;
        }
        ox = getX();
        oy = getY();
        return moving;
    }

    public void updateCamera() {
        Camera.x = Camera.clamp(getX() - (Game.SCREEN_WIDTH / 2), 0,
                World.WIDTH * World.TILE_SIZE - Game.SCREEN_WIDTH);
        Camera.y = Camera.clamp(getY() - (Game.SCREEN_HEIGHT / 2), 0,
                World.HEIGHT * World.TILE_SIZE - Game.SCREEN_HEIGHT);
    }

    @Override
    public void render(Graphics g) {
        if (opened) {
            switch (dir) {
                case UP:
                    g.drawImage(sprite_up, this.getX() - Camera.x, this.getY() - Camera.y, null);
                    break;
                case DOWN:
                    g.drawImage(sprite_down, this.getX() - Camera.x, this.getY() - Camera.y, null);
                    break;
                case LEFT:
                    g.drawImage(sprite_left, this.getX() - Camera.x, this.getY() - Camera.y, null);
                    break;
                case RIGHT:
                    g.drawImage(sprite_right, this.getX() - Camera.x, this.getY() - Camera.y, null);
                    break;
                case BALL:
                    super.render(g);
                    break;
                default:
                    super.render(g);
                    break;
            }
        } else {
            super.render(g);
        }
    }
}
