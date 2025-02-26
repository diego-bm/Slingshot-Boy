package com.dumbmuttstudios.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dumbmuttstudios.main.Game;

public class PauseMenu {
	private BufferedImage pausedText = Game.spritesheet.getSprite(160, 112, 32, 16);
	private BufferedImage resumeText = Game.spritesheet.getSprite(160, 128, 32, 16);
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		g.drawImage(pausedText, Game.WIDTH / 2, Game.HEIGHT / 2, 32, 16, null);
		g.drawImage(resumeText, Game.WIDTH / 2, Game.HEIGHT / 2 + 16, 32, 16, null);
		
	}
}
