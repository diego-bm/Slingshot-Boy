package com.dumbmuttstudios.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	
	public static boolean saveExists = false;
	
	public void tick() {
		File file = new File("save.txt");
		
		if(file.exists()) {
			saveExists = true;
		} else {
			saveExists = false;
		}
		
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
				
				file = new File("save.txt");
				file.delete();
			} else if(options[currentOption] == "LOAD") {
				file = new File("save.txt");
				
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			} else if(options[currentOption] == "EXIT") {
				System.exit(1);
			} 
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			
			switch(spl2[0]) {
				case "level":
					Game.loadLevel("level" + spl2[1] + ".png");
					Game.gameState = "RUNNING";
					break;
				case "hp":
					// TODO: A vida salva, mas o jogo sempre reseta a vida na troca de
					// mapas.
					Game.player.hp = Integer.parseInt(spl2[1]);
					break;
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						
						for(int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans[1] += val[i];
						}
						
						line += trans[0];
						line += ":";
						line += trans[1];
						line += "/";
					}
				} catch(IOException e) {
					System.err.println(e);
				}
			} catch(FileNotFoundException e) {
				System.err.println(e);
			}
		}
		
		return line;
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
