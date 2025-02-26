package com.dumbmuttstudios.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dumbmuttstudios.main.Game;

public class UI {
	public static final int HUD_HEIGHT = 20;
	
	private int restartTextAnimationTimer = 0, restartTextAnimationTimerThreshold = 30;

	private BufferedImage slingshotAmmoHUDIcon;
	private BufferedImage fullHeartHUDIcon;
	private BufferedImage emptyHeartAmmoHUDIcon;
	private BufferedImage gameOverTextIcon;
	private BufferedImage[] gameOverRestartTextIcon;
	
	public void setRestartTextAnimationTimer(int value) {
		restartTextAnimationTimer = value;
	}
	
	public void render(Graphics g) {
		fullHeartHUDIcon = Game.spritesheet.getSprite(0, 32, 16, 16);
		emptyHeartAmmoHUDIcon = Game.spritesheet.getSprite(16, 32, 16, 16);
		slingshotAmmoHUDIcon = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		// Hud bar backgound
		g.setColor(new Color(15, 56, 15));
		g.fillRect(0, 0, Game.WIDTH, HUD_HEIGHT);

		for(int i = 0; i < Game.player.maxHP; i++) {
			g.drawImage(emptyHeartAmmoHUDIcon, 8 + (i * 17), 2, 16, 16, null);
		}
		
		for(int i = 0; i < Game.player.hp; i++) {
			if(Game.player.invulnerabilityFrames < 3) {
				
			}
			
			
			g.drawImage(fullHeartHUDIcon, 8 + (i * 17), 2, 16, 16, null);
		}
		
		
		/*
		g.setColor(new Color(48, 98, 48));
		g.fillRect(8, 6, (int)Game.player.maxHP / 2, 8);
		g.setColor(new Color(139, 172, 15));
		g.fillRect(8, 6, (int)((Game.player.hp / Game.player.maxHP) * 50), 8);
		g.setColor(new Color(139, 172, 15));
		g.drawString((int)Game.player.hp + "/" + (int)Game.player.maxHP, 60, 14);
		*/

		g.setColor(new Color(139, 172, 15));
		g.drawImage(slingshotAmmoHUDIcon, 105, 2, 16, 16, null);
		g.drawString(Integer.toString(Game.player.ammo), 125, 14);
	}
	
	// TODO: Change to a sprite
	public void renderSlingshotAmmo(Graphics g) {
		//g.setColor(new Color(139, 172, 15));
		//g.setFont(new Font("arial", Font.BOLD, 17));
		//g.drawString("Ammo: " + Game.player.ammo, 25, 50);
	}
	
	public void renderGameOverScreen(Graphics g) {
		gameOverRestartTextIcon = new BufferedImage[2];
		
		gameOverTextIcon = Game.spritesheet.getSprite(32, 128, 128, 16);
		for(int i = 0; i < 2; i++) {
			gameOverRestartTextIcon[i] = Game.spritesheet.getSprite(32, 144 + (i * 16), 128, 16);
		}
		
		restartTextAnimationTimer++; 
		if(restartTextAnimationTimer > restartTextAnimationTimerThreshold) {
			restartTextAnimationTimer = 0;
		}
		
		g.drawImage(gameOverTextIcon, Game.WIDTH / 2 - 64, Game.HEIGHT / 2 - 16, 128, 16, null);
		if(restartTextAnimationTimer <= 15) {
			g.drawImage(gameOverRestartTextIcon[0], Game.WIDTH / 2 - 64, Game.HEIGHT / 2, 128, 16, null);
		} else {
			g.drawImage(gameOverRestartTextIcon[1], Game.WIDTH / 2 - 64, Game.HEIGHT / 2, 128, 16, null);
		}
	}
}
