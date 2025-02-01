package com.jhowbits.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.jhowbits.main.Game;

public class UI {

	public void render(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(8, 4, 50, 8);
		
		g.setColor(Color.GREEN);
		g.fillRect(8, 4, (int) ((Game.player.hp / Game.player.maxHp) * 50), 8);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString((int)Game.player.hp + "/" + (int)Game.player.maxHp, 22, 11);
	}
	
}
