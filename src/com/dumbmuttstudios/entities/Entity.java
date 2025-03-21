package com.dumbmuttstudios.entities;

// import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.dumbmuttstudios.main.Game;
import com.dumbmuttstudios.world.Camera;

public class Entity {

	public static BufferedImage HEALTHPACK_ENTITY = Game.spritesheet.getSprite(176, 0, 16, 16);
	public static BufferedImage SLINGSHOT_ENTITY = Game.spritesheet.getSprite(192, 0, 16, 16);
	public static BufferedImage SLINGSHOTAMMO_ENTITY = Game.spritesheet.getSprite(208, 0, 16, 16);
	// public static BufferedImage ENEMY_ENTITY = Game.spritesheet.getSprite(128, 0, 16, 16);
	// DEPRECATED: Opting for a different sprite array when carrying the slingshot
	// public static BufferedImage SLINGSHOT_LEFT_ENTITY = Game.spritesheet.getSprite(112, 0, 16, 16);
	// public static BufferedImage SLINGSHOT_RIGHT_ENTITY = Game.spritesheet.getSprite(96, 0, 16, 16);

	protected double x;
	protected double y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	private int maskX, maskY, maskW, maskH;
	
	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;

		this.maskX = 0;
		this.maskY = 0;
		this.maskW = width;
		this.maskH = height;
	}
	
	public int getX() {
		return (int)this.x;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public int getY() {
		return (int)this.y;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setMask(int maskX, int maskY, int maskW, int maskH) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.maskW = maskW;
		this.maskH = maskH;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		// DEBUG: Ver colisões de entidades
		// g.setColor(Color.RED);
		// g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
	}
	
	public static boolean isColliding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.maskW, e1.maskH);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.maskW, e2.maskH);
		
		return e1Mask.intersects(e2Mask);
	}
	
}
