package com.humanaxe.main;

import com.humanaxe.entities.Enemy;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;

import com.humanaxe.entities.Entity;
import com.humanaxe.entities.Player;
import com.humanaxe.systems.Spritesheet;
import com.humanaxe.systems.UI;
import com.humanaxe.overall.World;
import static com.humanaxe.overall.World.TILE_SIZE;
import com.humanaxe.systems.Camera;
import com.humanaxe.systems.Sound;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public final class Game extends Canvas
        implements Runnable, KeyListener {

    private static final long serialVersionUID = 1L;
    public static JFrame frame;
    private Thread thread;
    private BufferedImage mapbg;
    private boolean isRunning = true;
    public static final int SCREEN_WIDTH = 28 * World.TILE_SIZE/* + 112*/;
    //public static final int SCREEN_HEIGHT = SCREEN_WIDTH / 16 * 9;
    //public static final int SCALE = 2;
    public static final int SCREEN_HEIGHT = 31 * World.TILE_SIZE + 30;
    public static final int SCALE = 1;

    private BufferedImage curFrame;

    public static List<Entity> entities;
    public static Spritesheet spritesheet;
    public static World world;
    public static Player player;
    public static Enemy redGhost;
    public static Enemy blueGhost;
    public static Enemy pinkGhost;
    public static Enemy orangeGhost;

    public static UI ui;

    public static int curFruit = 0;
    public static int totalFruit = 0;

    public static final int PLAYING = 0;
    public static final int LOSE = 1;
    public static final int WIN = 2;
    public static int state = PLAYING;

    private boolean press_enter = false;

    public Game() {
        addKeyListener(this);
        setPreferredSize(new Dimension(SCREEN_WIDTH * SCALE,
                SCREEN_HEIGHT * SCALE));
        initFrame();
        curFrame = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        // Init game objects
        try {
            mapbg = ImageIO.read(getClass().getResource("/sprite/map_sprite.png"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred.\nMessage: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        spritesheet = new Spritesheet("/sprite/spritesheet.png");
        entities = new ArrayList<>();
        player = new Player(0, 0, World.TILE_SIZE, World.TILE_SIZE, 2,
                spritesheet.getSprite(World.TILE_SIZE * 2, World.TILE_SIZE * 0,
                        World.TILE_SIZE, World.TILE_SIZE));
        redGhost = new Enemy(0, 0, TILE_SIZE, TILE_SIZE, 2, Entity.RGHOST);
        blueGhost = new Enemy(0, 0, TILE_SIZE, TILE_SIZE, 1, Entity.BGHOST);
        pinkGhost = new Enemy(0, 0, TILE_SIZE, TILE_SIZE, 1, Entity.PGHOST);
        orangeGhost = new Enemy(0, 0, TILE_SIZE, TILE_SIZE, 2, Entity.OGHOST);
        world = new World("/sprite/map.png");
        ui = new UI();

        entities.add(player);
        entities.add(redGhost);
        entities.add(blueGhost);
        entities.add(pinkGhost);
        entities.add(orangeGhost);
        
        Sound.beginning.play();
    }

    public void initFrame() {
        frame = new JFrame("Pac-Man");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        try {
            thread.join();
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred.\nMessage: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void restartGame() {
        player = new Player(0, 0, TILE_SIZE, TILE_SIZE, 2,
                spritesheet.getSprite(
                        TILE_SIZE * 2, TILE_SIZE * 0,
                        TILE_SIZE, TILE_SIZE));
        entities.clear();
        redGhost = new Enemy(0, 0, TILE_SIZE, TILE_SIZE, 2, Entity.RGHOST);
        blueGhost = new Enemy(0, 0, TILE_SIZE, TILE_SIZE, 1, Entity.BGHOST);
        pinkGhost = new Enemy(0, 0, TILE_SIZE, TILE_SIZE, 1, Entity.PGHOST);
        orangeGhost = new Enemy(0, 0, TILE_SIZE, TILE_SIZE, 2, Entity.OGHOST);
        entities.add(Game.player);
        entities.add(Game.redGhost);
        entities.add(Game.blueGhost);
        entities.add(Game.pinkGhost);
        entities.add(Game.orangeGhost);
        curFruit = 0;
        totalFruit = 0;
        world = new World("/sprite/map.png");
        state = PLAYING;
        Player.lifes = 3;
        player.dir = Player.BALL;
        player.newDir = Player.BALL;
    }

    public void tick() {
        switch (state) {
            case PLAYING:
                for (int i = 0; i < entities.size(); i++) {
                    Entity e = entities.get(i);
                    e.tick();
                }
                break;
            case WIN:
            case LOSE:
                if (press_enter) {
                    restartGame();
                }
                break;
            default:
                break;
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = curFrame.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        /* render */
        switch (state) {
            case PLAYING:
                world.render(g);
                g.drawImage(mapbg, 0 - Camera.x, 0 - Camera.y, null);
                Collections.sort(entities, Entity.nodeSorter);
                for (int i = 0; i < entities.size(); i++) {
                    Entity e = entities.get(i);
                    e.render(g);
                }
                break;
            case WIN:
                ui.winScreen(g);
                break;
            case LOSE:
                ui.loseScreen(g);
                break;
            default:
                break;
        }
        /* ---- */

        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(curFrame, 0, 0, SCREEN_WIDTH * SCALE,
                SCREEN_HEIGHT * SCALE, null);
        if (state == PLAYING) {
            ui.render(g);
        }
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        double timer = System.currentTimeMillis();
        requestFocus();
        while (isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                timer += 1000;
            }
        }
        stop();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT
                || e.getKeyCode() == KeyEvent.VK_D) {
            player.press_right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT
                || e.getKeyCode() == KeyEvent.VK_A) {
            player.press_left = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP
                || e.getKeyCode() == KeyEvent.VK_W) {
            player.press_up = true;

        } else if (e.getKeyCode() == KeyEvent.VK_DOWN
                || e.getKeyCode() == KeyEvent.VK_S) {
            player.press_down = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            press_enter = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT
                || e.getKeyCode() == KeyEvent.VK_D) {
            player.press_right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT
                || e.getKeyCode() == KeyEvent.VK_A) {
            player.press_left = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP
                || e.getKeyCode() == KeyEvent.VK_W) {
            player.press_up = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN
                || e.getKeyCode() == KeyEvent.VK_S) {
            player.press_down = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            press_enter = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String args[]) {
        new Game().start();
    }

}
