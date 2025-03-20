package com.dumbmuttstudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.dumbmuttstudios.entities.Bullet;
import com.dumbmuttstudios.entities.Enemy;
import com.dumbmuttstudios.entities.Entity;
import com.dumbmuttstudios.entities.Player;
import com.dumbmuttstudios.graphics.PauseMenu;
import com.dumbmuttstudios.graphics.Spritesheet;
import com.dumbmuttstudios.graphics.UI;
import com.dumbmuttstudios.world.World;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = false;
	private static boolean reloadingLevel = false;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	private final int SCALE = 3;
	public static String gameState = "";
	
	private BufferedImage image;

	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<Bullet> bullets;
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public MainMenu menu;
	public PauseMenu pauseMenu;
	
	private static int currentLevel = 1;
	private int maxLevel = 2;
	
	public Game() {
		// Sound.musicBackground.loop();
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		
		// Inicializando objetos.
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();
		spritesheet = new Spritesheet("/sprites/spritesheet.png");
		player = new Player(0, 0, 12, 8, spritesheet.getSprite(32, 0, 16, 16));
		entities.add(player);
		world = new World("/sprites/levels/level1.png");
		Game.gameState = "MENU";
		
		menu = new MainMenu();
		pauseMenu = new PauseMenu();
	}
	
	public static void main(String args[]) {
		Game game = new Game();
		
		game.start();
	}
	
	public void tick() {
		if(Game.gameState.equals("RUNNING")) {
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				e.tick();
			}
			
			for(int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}
			
			if(Game.enemies.size() == 0 && !reloadingLevel) {
				Game.currentLevel++;
				
				if(Game.currentLevel > this.maxLevel) {
					Game.currentLevel = 1;
				}
				
				String levelName = "level" + Game.currentLevel + ".png";
				Game.loadLevel(levelName);
			}
		} else if(Game.gameState.equals("MENU")) {
			menu.tick(); 
		} else if(Game.gameState.equals("PAUSED")) {
			pauseMenu.tick();
		}
	}
	
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		// Renderização do jogo
		// Graphics2D g2 = (Graphics2D) g;
		/**/
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}
		
		ui.render(g);
		
		if(Game.gameState.equals("GAME_OVER")) {
			ui.renderGameOverScreen(g);
		} else if(gameState == "MENU") {
			menu.render(g);
		} else if(gameState == "PAUSED") {
			pauseMenu.render(g);
		}
		
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		ui.renderSlingshotAmmo(g);
		bs.show();
	}

	@Override
	public void run() {
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		// int frames = 0;
		double timer = System.currentTimeMillis();
		
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				render();
				// frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				// System.out.println("FPS: " + frames);
				// frames = 0;
				timer += 1000;
			}
		}
		
		stop();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void initFrame() {
		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.setRight(true);
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.setLeft(true);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			if(gameState == "RUNNING") {
				player.setUp(true);
			} else if(gameState == "MENU") {
				menu.up = true;
			}
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			if(gameState == "RUNNING") {
				player.setDown(true);
			} else if(gameState == "MENU") {
				menu.down = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X && player.hasSlingshot && player.ammo > 0) {
			player.shotSlingshot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER && Game.gameState == "MENU") {
			menu.enter = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if(Game.gameState == "RUNNING") {
				Game.gameState = "PAUSED";
			} else if(Game.gameState == "PAUSED") {
				Game.gameState = "RUNNING";
			}
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.setRight(false);
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.setLeft(false);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.setUp(false);
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.setDown(false);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_R) {
			ui.setRestartTextAnimationTimer(0);
			reloadingLevel = true;
			Game.reloadCurrentLevel();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(player.hasSlingshot && player.ammo > 0) {
			player.shotSlingshotUsingMouse = true;	
			player.cursorX = e.getX() / 3;	
			player.cursorY = e.getY() / 3;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public static void loadLevel(String level) {
		loadGameVariables();
		Game.world = new World("/sprites/levels/" + level);
		Game.gameState = "RUNNING";
		
		return;
	}
	
	public static void reloadCurrentLevel() {
		loadGameVariables();
		Game.world = new World("/sprites/levels/level" + currentLevel + ".png");
		reloadingLevel = false;
		Game.gameState = "RUNNING";
		
		return;
	}
	
	private static void loadGameVariables() {
		Game.gameState = "";
		Game.entities.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies.clear();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/sprites/spritesheet.png");
		Game.player = new Player(0, 0, 12, 8, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
	}
}

