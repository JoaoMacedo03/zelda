package com.jhowbits.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

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
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	
	public boolean moved = false;

	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		
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
		
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}
	
	public void render(Graphics g) {
		if(dir == right_dir) {
			g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		} else if(dir == left_dir) {
			g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
		}
	}
	
}
