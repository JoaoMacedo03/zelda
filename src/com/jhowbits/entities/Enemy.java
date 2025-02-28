package com.jhowbits.entities;

import java.awt.Graphics;
// import java.awt.Color;
// import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.jhowbits.main.Game;
import com.jhowbits.world.Camera;
// import com.jhowbits.world.Camera;
import com.jhowbits.world.World;

public class Enemy extends Entity {
	
	public double speed = 0.4;
	private int maskX = 8, maskY = 8, maskW = 10, maskH = 10;
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 1;
	
	private BufferedImage[] sprites;
	
	private int hp = 10;
	
	private boolean isDamaged = false;
	private int damageFrames = 10, damageCurrent = 0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(112 + 16, 16, 16, 16);
	}
	
	public void tick() {
//		maskX = 8; 
//		maskY = 8; 
//		maskW = 5; 
//		maskH = 5;
		
		if(this.isColidingWithPlayer() == false) {
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
		} else {
			// Colidiu com o player
			if(Game.rand.nextInt(100) < 10) {
				Game.player.hp -= Game.rand.nextInt(3);
				Game.player.isDamaged = true;
			}
			
		}
		
		//Animação de mover o inimigo entre sprites
		frames++;
		if(frames == maxFrames) {
			frames = 0;
			index++;
			
			if(index > maxIndex) index = 0;
		}
		
		this.collidingBullet();
		
		if(hp <= 0) {
			this.destroySelf();
			return;
		}
		
		if(isDamaged) {
			this.damageCurrent++;
			if(this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	public void collidingBullet() {
		for(int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			
			if(e instanceof BulletShoot) {
				
				if(Entity.isColiding(this, e)) {
					Game.bullets.remove(i);
					hp--;
					isDamaged = true;
					return;
				}
				
			}
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
	
	public boolean isColidingWithPlayer() {
		Rectangle currentEnemy = new Rectangle(this.getX() + maskX, this.getY() + maskY, maskW, maskH);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), 16, 16);
		
		return currentEnemy.intersects(player);
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {
			g.drawImage(sprites[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else {
			g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
		
//		g.setColor(Color.BLUE);
//		g.fillRect(this.getX() + maskX - Camera.x, this.getY() + maskY - Camera.y, maskW, maskH);
	}
	
}
