package com.dumbmuttstudios.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MainMenu {
	public String[] options = {"START", "LOAD", "EXIT"};
	
	public int currentOption = 0, maxOption = options.length - 1;
	
	public boolean up = false, down = false, enter = false;
	
	private BufferedImage titleLogo = Game.spritesheet.getSprite(160, 48, 64, 16);
	private BufferedImage startLogo = Game.spritesheet.getSprite(160, 64, 32, 16);
	private BufferedImage loadLogo = Game.spritesheet.getSprite(160, 80, 32, 16);
	private BufferedImage exitLogo = Game.spritesheet.getSprite(160, 96, 32, 16);
	private BufferedImage selectorArrowIcon = Game.spritesheet.getSprite(16, 16, 16, 16);
	
	public void tick() {
		if(up) {
			up = false;
			currentOption--;
			
			if(currentOption < 0) {
				currentOption = maxOption;
			}
		}
		
		if(down) {
			down = false;
			currentOption++;
			
			if(currentOption > maxOption) {
				currentOption = 0;
			}
		}
		
		if(enter) {
			enter = false;
			
			if(options[currentOption] == "START") {
				Game.gameState = "RUNNING";
			} else if(options[currentOption] == "EXIT") {
				System.exit(1);
			}
		}
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		} catch(IOException e) {
			System.err.println(e);
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			
			for(int n = 0; n < value.length; n++) {
				value[n] += encode;
				current += value[n];
			}
			
			try {
				write.write(current);
				
				if(i < val1.length - 1) {
					write.newLine();
				}
			} catch(IOException e) {
				System.err.println(e);
			}
		}
		
		try {
			write.flush();
			write.close();
		} catch(IOException e) {
			System.err.println(e);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(15, 56, 15));
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

		g.drawImage(titleLogo, Game.WIDTH / 2 - 32, Game.HEIGHT / 4 - 8, 64, 16, null);
		g.drawImage(startLogo, Game.WIDTH / 2 - 16, Game.HEIGHT / 2 - 8, 32, 16, null);
		g.drawImage(loadLogo, Game.WIDTH / 2 - 16, Game.HEIGHT / 2 - 8 + 16, 32, 16, null);
		g.drawImage(exitLogo, Game.WIDTH / 2 - 16, Game.HEIGHT / 2 - 8 + 32, 32, 16, null);
		
		if(options[currentOption] == "START") {
			g.drawImage(selectorArrowIcon, Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 8, 16, 16, null);
		}
		
		if(options[currentOption] == "LOAD") {
			g.drawImage(selectorArrowIcon, Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 8 + 16, 16, 16, null);
		}

		if(options[currentOption] == "EXIT") {
			g.drawImage(selectorArrowIcon, Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 8 + 32, 16, 16, null);
		}
	}
}
