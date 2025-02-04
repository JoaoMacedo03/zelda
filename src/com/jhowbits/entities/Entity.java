package com.jhowbits.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jhowbits.main.Game;
import com.jhowbits.world.Camera;

public class Entity {
	
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6 * 16, 0, 16, 16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7 * 16, 0, 16, 16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6 * 16, 16, 16, 16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7* 16, 16, 16, 16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(144, 16, 16, 16);
	public static BufferedImage GUN_RIGHT_EN = Game.spritesheet.getSprite(128, 0, 16, 16);
	public static BufferedImage GUN_LEFT_EN = Game.spritesheet.getSprite(128 + 16, 0, 16, 16);
	
	protected double x;
	protected double  y;
	protected int width;
	protected int height;
	
	private BufferedImage sprite;
	
	private int maskX, maskY, maskWidth, maskHeight;

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskX = 0;
		this.maskY = 0;
		this.maskWidth = width;
		this.maskHeight = height;
	}
	
	public void setMask(int maskX, int maskY, int maskWidth, int maskHeight) {
		this.maskX = maskX;
		this.maskY = maskY;
		this.maskWidth = maskWidth;
		this.maskHeight = maskHeight;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int) this.x;
	}
	
	public int getY() {
		return (int) this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void tick() {
		
	}
	
	public static boolean isColiding(Entity e1, Entity e2) {
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskX, e1.getY() + e1.maskY, e1.maskWidth, e1.maskHeight);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskX, e2.getY() + e2.maskY, e2.maskWidth, e2.maskHeight);
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() - Camera.x, this.getY() - Camera.y, null);
		
//		g.setColor(Color.RED);
//		g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskWidth, maskHeight);
	}
	
}
