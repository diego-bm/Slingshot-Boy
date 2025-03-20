package com.dumbmuttstudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dumbmuttstudios.graphics.UI;
import com.dumbmuttstudios.main.Game;
import com.dumbmuttstudios.world.Camera;
import com.dumbmuttstudios.world.World;

public class Player extends Entity {
	
	private boolean right, up, left, down;
	private int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	private int dir = right_dir;
	private double speed = 1.4;
	private int maskX = 2, maskY = 10, maskW = 12, maskH = 6;
	public int cursorX, cursorY;
	public int hp = 3;
	public int maxHP = 3;
	private final int PLAYER_WIDTH = 16;
	// private final int PLAYER_HEIGHT = 16;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	private BufferedImage[] rightPlayerSlingshot;
	private BufferedImage[] leftPlayerSlingshot;
	private BufferedImage[] upPlayerSlingshot;
	private BufferedImage[] downPlayerSlingshot;
	private BufferedImage[] damagedRightPlayer;
	private BufferedImage[] damagedLeftPlayer;
	private BufferedImage[] damagedUpPlayer;
	private BufferedImage[] damagedDownPlayer;
	private BufferedImage[] damagedRightPlayerSlingshot;
	private BufferedImage[] damagedLeftPlayerSlingshot;
	private BufferedImage[] damagedUpPlayerSlingshot;
	private BufferedImage[] damagedDownPlayerSlingshot;
	
	private int damagedFrames = 0;
	public int invulnerabilityFrames = 0;
	
	public int ammo = 0;
	
	public boolean isDamaged = false;
	public boolean hasSlingshot = false;
	public boolean shotSlingshot = false;
	public boolean shotSlingshotUsingMouse = false;

	public void setRight(boolean state) {
		this.right = state;
	}

	public void setUp(boolean state) {
		this.up = state;
	}

	public void setLeft(boolean state) {
		this.left = state;
	}

	public void setDown(boolean state) {
		this.down = state;
	}

	public int getMaskX() {
		return this.maskX;
	}

	public int getMaskY() {
		return this.maskY;
	}

	public int getMaskW() {
		return this.maskW;
	}

	public int getMaskH() {
		return this.maskH;
	}

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		rightPlayerSlingshot = new BufferedImage[4];
		leftPlayerSlingshot = new BufferedImage[4];
		upPlayerSlingshot = new BufferedImage[4];
		downPlayerSlingshot = new BufferedImage[4];
		
		damagedRightPlayer = new BufferedImage[4];
		damagedLeftPlayer = new BufferedImage[4];
		damagedUpPlayer = new BufferedImage[4];
		damagedDownPlayer = new BufferedImage[4];
		damagedRightPlayerSlingshot = new BufferedImage[4];
		damagedLeftPlayerSlingshot = new BufferedImage[4];
		damagedUpPlayerSlingshot = new BufferedImage[4];
		damagedDownPlayerSlingshot = new BufferedImage[4];

		this.getPlayerSprites();
		
		this.getDamagedPlayerSprites();
	}
	
	public void tick() {
		moved = false;
		if(right) {
			dir = right_dir;
			
			if(right && World.isFree((int)(x + speed), this.getY())) {
				moved = true;
				x+=speed;
			} 
		} else if(left) {
			dir = left_dir;
			
			if(left && World.isFree((int)(x - speed), this.getY())) {
				moved = true;
				x-=speed;
			}
		}
		
		if(up) {
			dir = up_dir;
			
			if(up && World.isFree(this.getX(), (int)(y - speed))) {
				moved = true;
				y-=speed;
			}
		} else if(down) {
			dir = down_dir;
			
			if(World.isFree(this.getX(), (int)(y + speed))) {
				moved = true;
				y+=speed;
			}
		}
		
		if(moved) {
			frames++;
			
			if(frames == maxFrames) {
				frames = 0;
				index++;
				
				if(index > maxIndex) {
					index = 0;
				}
			}
		} else {
			index = 0;
		}
		
		this.checkCollisionHealthPack();
		this.checkCollisionSlingshotAmmo();
		this.checkCollisionSlingshot();
		
		boolean isEnemyColliding = false;
		
		// System.out.println(this.damagedFrames);
		
		if(isDamaged) {
			this.damagedFrames++;
			if(this.damagedFrames > 120)
				this.damagedFrames = 120;
			
			for(int i = 0; i < Game.enemies.size(); i++) {
				if(Game.enemies.get(i).isCollidingWithPlayer()) {
					isEnemyColliding = true;
					break;
				};
			}
			
			if(this.damagedFrames == 120 && !isEnemyColliding) {
				this.damagedFrames = 0;
				this.isDamaged = false;
			}
		}
		
		if(this.shotSlingshot && this.hasSlingshot && this.ammo > 0) {
			// Create bullet and shoot
			this.shotSlingshot = false;
			ammo--;
			int dx = 0;
			int dy = 0;
			int px = 0;
			int py = 0;
			
			if(dir == right_dir) {
				dx = 1;
				px = 11;
				py = 7;
			} else if(dir == left_dir) {
				dx = -1;
				px = 1;
				py = 7;
			} else if(dir == up_dir) {
				dy = -1;
				px = 0;
				py = 2;
			} else if(dir == down_dir) {
				dy = 1;
				px = 10;
				py = 3;
			}
			
			
			
			Bullet bullet = new Bullet(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
			Game.bullets.add(bullet);
		}

		if(this.shotSlingshotUsingMouse && this.hasSlingshot && this.ammo > 0) {
			// Create bullet and shoot
			this.shotSlingshotUsingMouse = false;
			ammo--;
			double angle = Math.atan2(cursorY - (this.getY() + 8 - Camera.y), cursorX - (this.getX() + 8 - Camera.x));
			double dx = Math.cos(angle);
			double dy = Math.sin(angle);
			int px = 8;
			int py = 8;			
			
			Bullet bullet = new Bullet(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
			Game.bullets.add(bullet);
		}
		
		if(this.hp <= 0) {
			Game.gameState = "GAME_OVER";
			// Game.reloadLevel();
		}

		Camera.x = Camera.clamp(this.getX() - Game.WIDTH / 2 + PLAYER_WIDTH / 2, 0, World.WIDTH * World.TILE_SIZE - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - Game.HEIGHT / 2, -(UI.HUD_HEIGHT), World.HEIGHT * World.TILE_SIZE - Game.HEIGHT);
	}

	public void render(Graphics g) {
		if(!this.isDamaged) {
			if(!this.hasSlingshot) {
				if(dir == up_dir) {
					g.drawImage(upPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				} else if(dir == down_dir) {
					g.drawImage(downPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				} else if(dir == right_dir) {
					g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				} else if(dir == left_dir) {
					g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
			} else {
				if(dir == up_dir) {
					g.drawImage(upPlayerSlingshot[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				} else if(dir == down_dir) {
					g.drawImage(downPlayerSlingshot[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				} else if(dir == right_dir) {
					g.drawImage(rightPlayerSlingshot[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				} else if(dir == left_dir) {
					g.drawImage(leftPlayerSlingshot[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				}
			}
			
		} else {
			invulnerabilityFrames++;
			if(invulnerabilityFrames < 3) {
				if(!this.hasSlingshot) {
					if(dir == up_dir) {
						g.drawImage(damagedUpPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					} else if(dir == down_dir) {
						g.drawImage(damagedDownPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					} else if(dir == right_dir) {
						g.drawImage(damagedRightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					} else if(dir == left_dir) {
						g.drawImage(damagedLeftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					}
				} else {
					if(dir == up_dir) {
						g.drawImage(damagedUpPlayerSlingshot[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					} else if(dir == down_dir) {
						g.drawImage(damagedDownPlayerSlingshot[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					} else if(dir == right_dir) {
						g.drawImage(damagedRightPlayerSlingshot[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					} else if(dir == left_dir) {
						g.drawImage(damagedLeftPlayerSlingshot[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
					}
				}
			} else {
				if(invulnerabilityFrames == 6) {
					invulnerabilityFrames = 0;
				}
			}
		}
		
		
	}
	
	private void getPlayerSprites() {
		// Get normal sprites
		for(int i = 0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(64, 0 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(80, 0 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			upPlayer[i] = Game.spritesheet.getSprite(48, 0 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			downPlayer[i] = Game.spritesheet.getSprite(32, 0 + (i * 16), 16, 16);
		}
		// Get slingshot sprites
		for(int i = 0; i < 4; i++) {
			rightPlayerSlingshot[i] = Game.spritesheet.getSprite(128, 0 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			leftPlayerSlingshot[i] = Game.spritesheet.getSprite(144, 0 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			upPlayerSlingshot[i] = Game.spritesheet.getSprite(112, 0 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			downPlayerSlingshot[i] = Game.spritesheet.getSprite(96, 0 + (i * 16), 16, 16);
		}
	}
	
	private void getDamagedPlayerSprites() {
		// Get normal damaged sprites
		for(int i = 0; i < 4; i++) {
			damagedRightPlayer[i] = Game.spritesheet.getSprite(64, 64 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			damagedLeftPlayer[i] = Game.spritesheet.getSprite(80, 64 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			damagedUpPlayer[i] = Game.spritesheet.getSprite(48, 64 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			damagedDownPlayer[i] = Game.spritesheet.getSprite(32, 64 + (i * 16), 16, 16);
		}
		// Get slingshot damaged sprites
		for(int i = 0; i < 4; i++) {
			damagedRightPlayerSlingshot[i] = Game.spritesheet.getSprite(128, 64 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			damagedLeftPlayerSlingshot[i] = Game.spritesheet.getSprite(144, 64 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			damagedUpPlayerSlingshot[i] = Game.spritesheet.getSprite(112, 64 + (i * 16), 16, 16);
		}
		for(int i = 0; i < 4; i++) {
			damagedDownPlayerSlingshot[i] = Game.spritesheet.getSprite(96, 64 + (i * 16), 16, 16);
		}
	}
	
	public void checkCollisionHealthPack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity currentEntity = Game.entities.get(i);
			if(currentEntity instanceof HealthPack) {
				if(Entity.isColliding(this, currentEntity)) {
					if(this.hp < this.maxHP) {
						this.hp++;
						Game.entities.remove(currentEntity);
						if(this.hp > this.maxHP) {
							hp = this.maxHP;
						}
					}
				}
			}
		}
	}
	
	public void checkCollisionSlingshotAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity currentEntity = Game.entities.get(i);
			if(currentEntity instanceof SlingshotAmmo) {
				if(Entity.isColliding(this, currentEntity)) {
					this.ammo += 3;
					Game.entities.remove(currentEntity);
				}
			}
		}
	}
	
	public void checkCollisionSlingshot() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity currentEntity = Game.entities.get(i);
			if(currentEntity instanceof Slingshot) {
				if(Entity.isColliding(this, currentEntity)) {
					this.hasSlingshot = true;
					Game.entities.remove(currentEntity);
				}
			}
		}
	}
	
	/*
	 * public boolean isCollidingWithEnemy(int xNext, int yNext) { Rectangle player
	 * = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH); for(int i = 0; i
	 * < Game.enemies.size(); i++) { Enemy e = Game.enemies.get(i);
	 * 
	 * Rectangle enemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW,
	 * maskH);
	 * 
	 * if(player.intersects(enemy)) { return true; } }
	 * 
	 * return false; }
	 */
	
}
