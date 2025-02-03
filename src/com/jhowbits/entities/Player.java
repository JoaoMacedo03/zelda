package com.jhowbits.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.jhowbits.graphics.Spritesheet;
import com.jhowbits.main.Game;
import com.jhowbits.world.Camera;
import com.jhowbits.world.World;

public class Player extends Entity {
	
	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public double speed = 1.4;
	
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage playerDamage;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	
	public boolean moved = false;
	
	public double hp = 100, maxHp = 100;
	
	public int ammo = 0;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	private boolean hasGun = false;
	public boolean shoot = false, mouseShoot = false;
	
	public int mx, my;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		for(int i = 0; i < 4;  i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
		
		
	}
	
	public void tick() {
		moved = false;
		if(right && World.isFree((int)(x + speed), this.getY())) {
			moved = true;
			x+=speed;
			dir = right_dir;
		} else if(left && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			x-=speed;
			dir = left_dir;
		}
		
		if(up && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			y-=speed;
		} else if(down && World.isFree(this.getX(), (int) (y + speed) )) {
			moved = true;
			y+=speed;
		}
		
		if(moved) {
			frames++;
			if(frames == maxFrames) {
				frames = 0;
				index++;
				
				if(index > maxIndex) index = 0;
			}
		}
		
		this.checkCollisionLifePack();
		this.checkCollisionAmmo();
		this.checkCollisionGun();
		
		if(isDamaged) {
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if(shoot) {
			shoot = false;
			
			if(hasGun && ammo > 0) {
				ammo--;
				
				//Criar bala e atirar
				int dx = 0;
				int px = 0;
				int py = 6;
				
				if(dir == right_dir) {
					px = 18;
					dx = 1;
				} else {
					px = -8;
					dx = -1;
				}
				
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
			}
		}
		
		if(mouseShoot) {
			mouseShoot = false;
			
			if(hasGun && ammo > 0) {
				ammo--;
				
				//Criar bala e atirar
				int px = 0;
				int py = 8;
				double angle = 0;
				
				if(dir == right_dir) {
					px = 18;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));
				} else {
					px = -8;
					angle = Math.atan2(my - (this.getY() + py - Camera.y), mx - (this.getX() + px - Camera.x));					
				}
				
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				
				BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}
		
		if(hp <= 0) {
			//Game over!
			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/spritesheet.png");
			Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(32, 0, 16, 16));
			Game.entities.add(Game.player);
			Game.world = new World("/map.png");
			
			return;
		}
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		if(!isDamaged) {
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
				if(hasGun) {
					//Desenhar arma para a direita
					g.drawImage(Entity.GUN_RIGHT_EN, this.getX() + 8 - Camera.x, this.getY() - Camera.y, null);
				}
			} else if(dir == left_dir) {
				if(hasGun) {
					//Desenhar arma para a esquerda
					g.drawImage(Entity.GUN_LEFT_EN, this.getX() - 8 - Camera.x, this.getY() - Camera.y, null);
				}
				g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
			}
		} else {
			g.drawImage(playerDamage, this.getX() - Camera.x, this.getY() - Camera.y, null);
		}		
	}
	
	public void checkCollisionAmmo() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof Bullet) {
				if(Entity.isColiding(this, current)) {
					ammo += 10;
					System.out.println("Ammo: " + ammo);
					Game.entities.remove(current);
				}
			}
		}
	}
	
	public void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof Weapon) {
				if(Entity.isColiding(this, current)) {
					hasGun = true;
					Game.entities.remove(current);
				}
			}
		}
	}
	
	public void checkCollisionLifePack() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity current = Game.entities.get(i);
			if(current instanceof LifePack) {
				if(Entity.isColiding(this, current)) {
					hp += 15;
					if(hp > maxHp) hp = maxHp;
					Game.entities.remove(current);
				}
			}
		}
	}
	
}
