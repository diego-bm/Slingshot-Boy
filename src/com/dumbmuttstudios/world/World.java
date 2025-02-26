package com.dumbmuttstudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.dumbmuttstudios.entities.*;
import com.dumbmuttstudios.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;
	
	public World(String path) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx = 0; xx < map.getWidth(); xx++) {
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy*map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * World.TILE_SIZE, yy * World.TILE_SIZE, Tile.TILE_FLOOR);
					// TODO: transform into switch/case 
					if(pixelAtual == 0xFF000000) {
						// Floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * World.TILE_SIZE, yy * World.TILE_SIZE, Tile.TILE_FLOOR);
					} else if(pixelAtual == 0xFFFFFFFF) {
						// Wall
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * World.TILE_SIZE, yy * World.TILE_SIZE, Tile.TILE_WALL);
					} else if(pixelAtual == 0xFF0000FF) {
						// Player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if(pixelAtual == 0xFFFF0000) {
						// Enemy
						Enemy en = new Enemy(xx * World.TILE_SIZE, yy * World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE);
						Game.entities.add(en);
						Game.enemies.add(en);
					} else if(pixelAtual == 0xFF2CFF00) {
						// Slingshot
						Game.entities.add(new Slingshot(xx * World.TILE_SIZE, yy * World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE, Entity.SLINGSHOT_ENTITY));
					}  else if(pixelAtual == 0xFF5FCDE4) {
						// Health Pack
						Game.entities.add(new HealthPack(xx * World.TILE_SIZE, yy * World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE, Entity.HEALTHPACK_ENTITY));
					}  else if(pixelAtual == 0xFFFBF236) {
						// Ammo
						Game.entities.add(new SlingshotAmmo(xx * World.TILE_SIZE, yy * World.TILE_SIZE, World.TILE_SIZE, World.TILE_SIZE, Entity.SLINGSHOTAMMO_ENTITY));
					} else {
						// Floor
						
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean isFree(int xNext, int yNext) {
		int x1 = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;
		
		int x2 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE;
		
		int x3 = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		int x4 = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));
	}
	
	public void render(Graphics g) {
		int xStart = Camera.x >> 4;
		int yStart = Camera.y >> 4;
		int xFinal = xStart + (Game.WIDTH >> 4);
		int yFinal = yStart + (Game.HEIGHT >> 4);
		
		for(int xx = xStart; xx <= xFinal; xx++) {
			for(int yy = yStart; yy <= yFinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}
