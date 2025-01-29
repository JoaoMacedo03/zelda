package com.jhowbits.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jhowbits.main.Game;
import com.jhowbits.world.Camera;
import com.jhowbits.world.World;

public class Enemy extends Entity {
	
	public double speed = 1;
	private int maskX = 8, maskY = 8, maskW = 10, maskH = 10;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	public void tick() {
		maskX = 8; 
		maskY = 8; 
		maskW = 5; 
		maskH = 5;
		
		if (
				(int)x < Game.player.getX() 
				&& World.isFree((int) (x + speed), this.getY())
				&& !isColiding((int) (x + speed), this.getY())
		) {
			x += speed;
		} else if(
				(int)x > Game.player.getX() 
				&& World.isFree((int) (x - speed), this.getY())
				&& !isColiding((int) (x - speed), this.getY())
		) {
			x -= speed;
		}
		
		if (
				(int) y < Game.player.getY() 
				&& World.isFree(this.getX(), (int) (y + speed))
				&& !isColiding(this.getX(), (int) (y + speed))
		) {
			y += speed;
		} else if(
				(int)y > Game.player.getY() 
				&& World.isFree(this.getX(), (int) (y - speed))
				&& !isColiding(this.getX(), (int) (y - speed))
		) {
			y -= speed;
		}
				
	}
	
	public boolean isColiding(int xNext, int yNext) {
		Rectangle currentEnemy = new Rectangle(xNext + maskX, yNext + maskY, maskW, maskH);
		
		for(int i = 0; i < Game.enemies.size(); i++) {
			Enemy e = Game.enemies.get(i);
			if(e == this) continue;
			
			Rectangle targetEnemy = new Rectangle(e.getX() + maskX, e.getY() + maskY, maskW, maskH);
			
			if(currentEnemy.intersects(targetEnemy)) return true;
			
		}
		
		return false;
	}
	
	public void render(Graphics g) {
		super.render(g);
		
		g.setColor(Color.BLUE);
		g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
	}
	
}
