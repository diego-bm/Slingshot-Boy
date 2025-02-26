package com.dumbmuttstudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dumbmuttstudios.main.Game;
import com.dumbmuttstudios.world.Camera;

public class Bullet extends Entity {
	private double dx, dy;
	private double spd = 4;
	private int vanishTimer = 0, vanishThreshold = 30;
	
	public Bullet(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		x += dx * spd;
		y += dy * spd;
		
		vanishTimer++;
		if(vanishTimer == vanishThreshold) {
			Game.bullets.remove(this);
			return;
		}
		
		this.checkEnemyCollision();
	}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, width, height);
	}
	
	public void checkEnemyCollision() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity bullet = Game.bullets.get(i);
			
			for(int x = 0; x < Game.enemies.size(); x++) {
				Enemy enemy = Game.enemies.get(x);
				
				if(Entity.isColliding(enemy, bullet)) {
					enemy.damageEnemy(1);
					Game.bullets.remove(i);
					return;
				}
			}
			
			
		}
	}
}
