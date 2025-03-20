package com.dumbmuttstudios.entities;

// import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.dumbmuttstudios.main.Game;
import com.dumbmuttstudios.main.Sound;
import com.dumbmuttstudios.world.Camera;
import com.dumbmuttstudios.world.World;

public class Enemy extends Entity {
	
	private double speed = 1;
	
	private int maskX = 3, maskY = 4, maskW = 10, maskH = 10;
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
	private int damagedSpriteTimer = 0, damagedSpriteTimerThreshold = 6;
	private boolean isDamaged = false;
	public boolean isCollidingWithPlayer = false;
	private int hp = 3;
	private BufferedImage[] sprites;
	private BufferedImage damagedSprite;

	public Enemy(int x, int y, int width, int height) {
		super(x, y, width, height, null);

		sprites = new BufferedImage[2];
		
		for(int i = 0; i < 2; i++) {
			sprites[i] = Game.spritesheet.getSprite(160, 0 + (i * 16), 16, 16);
		}
		
		this.damagedSprite = Game.spritesheet.getSprite(160, 32, 16, 16);
	}
	
	public void tick() {
		if(!this.isCollidingWithPlayer()) {
			this.isCollidingWithPlayer = false;
			if(Game.rand.nextInt(100) < 50) {
				if((int)x < Game.player.getX() && World.isFree((int)(x + speed), this.getY())
						&& !isColliding((int)(x + speed), this.getY())) {
					x += speed;
				} else if((int)x > Game.player.getX() && World.isFree((int)(x - speed), this.getY())
						&& !isColliding((int)(x - speed), this.getY())) {
					x -= speed;
				}
				
				if((int)y < Game.player.getY() && World.isFree(this.getX(), (int)(y + speed))
						&& !isColliding(this.getX(), (int)(y + speed))) {
					y += speed;
				} else if((int)y > Game.player.getY() && World.isFree(this.getX(), (int)(y - speed))
						&& !isColliding(this.getX(), (int)(y - speed))) {
					y -= speed;
				}
			}
		} else {
			this.isCollidingWithPlayer = true;
			if(!Game.player.isDamaged) {
				Sound.sfxHurt.play();
				Game.player.hp--;
				Game.player.isDamaged = true;
			}
		}
		
		frames++;
		
		if(frames == maxFrames) {
			frames = 0;
			index++;
			
			if(index > maxIndex) {
				index = 0;
			}
		}
		
		if(this.hp <= 0) {
			this.destroySelf();
			return;
		}
		
	}
	
	
	
	public void render(Graphics g) {
		if(!this.isDamaged || this.hp == 0) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			g.drawImage(damagedSprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
			this.damagedSpriteTimer++;
			
			if(this.damagedSpriteTimer >= this.damagedSpriteTimerThreshold) {
				this.isDamaged = false;
				this.damagedSpriteTimer = 0;
			}
		}
		
		
		/*
		 * g.setColor(Color.BLUE); g.fillRect(Game.player.getX() - Camera.x +
		 * Game.player.maskX, Game.player.getY() - Camera.y + Game.player.maskY,
		 * Game.player.maskW, Game.player.maskH); g.setColor(Color.RED);
		 * g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y,
		 * maskW, maskH);
		 */
		
		// g.setColor(Color.BLUE);
		// g.fillRect(this.getX() - Camera.x, this.getY() - Camera.y, maskW, maskH);
	}
	
	public boolean isCollidingWithPlayer() {
		Rectangle enemy = new Rectangle(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
		Rectangle player = new Rectangle(Game.player.getX() - Camera.x + Game.player.getMaskX(), Game.player.getY() - Camera.y + Game.player.getMaskY(), Game.player.getMaskW(), Game.player.getMaskH());
		
		return enemy.intersects(player);
	}
	
	public boolean isColliding(int xNext, int yNext) {
		Rectangle currentEnemy = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH);
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			
			if(e == this)
				continue;
			
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY,  maskW, maskH);
			
			if(currentEnemy.intersects(targetEnemy)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}

	public void damageEnemy(int damage) {
		this.hp = this.hp - damage;
		this.isDamaged = true;
	}
}
