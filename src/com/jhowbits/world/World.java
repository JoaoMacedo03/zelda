package com.jhowbits.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jhowbits.entities.*;
import com.jhowbits.graphics.Spritesheet;
import com.jhowbits.main.Game;

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
					int currentPixel = pixels[xx + (yy * map.getWidth())];
					
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR); 
					
					if(currentPixel == 0xFF000000) {
						//Floor
						tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR); 
					} else if (currentPixel == 0xFFFFFFFF) {
						//Wall
						tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL); 
					} else if (currentPixel == 0xFF0026FF) {
						// Player
						Game.player.setX(xx * 16);
						Game.player.setY(yy * 16);
					} else if(currentPixel == 0xFFFF0000) {
						//Enemy
						Enemy en = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					} else if(currentPixel == 0xFFFF6A00) {
						//Weapon
						Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON_EN));
					} else if(currentPixel == 0xFFFF7F7F) {
						//Life Pack
						LifePack pack = new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK_EN); 
						Game.entities.add(pack);
					} else if(currentPixel == 0xFFFFD800) {
						// Bullet
						Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET_EN));
					}
					
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static boolean isFree(int xNext, int yNext) {
		int x1  = xNext / TILE_SIZE;
		int y1 = yNext / TILE_SIZE;
		
		int x2  = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = yNext / TILE_SIZE;
		
		int x3  = xNext / TILE_SIZE;
		int y3 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		int x4  = (xNext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (yNext + TILE_SIZE - 1) / TILE_SIZE;
		
		return !(
				(tiles[x1 + (y1 * World.WIDTH) ] instanceof WallTile) || 
				(tiles[x2 + (y2 * World.WIDTH) ] instanceof WallTile) ||
				(tiles[x3 + (y3 * World.WIDTH) ] instanceof WallTile) ||
				(tiles[x4 + (y4 * World.WIDTH) ] instanceof WallTile)
		);
	}
	
	public static void restartGame(String level) {
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/" + level);
	}
	
	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++) {
			for(int yy = ystart; yy <= yfinal; yy++) {
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) continue;
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}
