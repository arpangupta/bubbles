package com.arpan;



import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class GamePage extends Canvas {
	private Sprite bg;

	BubblesMidlet midlet;
	Display disp;
	public GamePage(BubblesMidlet midlet) {
		
		this.midlet = midlet;
		disp = midlet.getDisplay();
		setFullScreenMode(true);
		
		try {
			
			bg = new Sprite(Image.createImage("/secondScreen1.png"));
			
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	protected void paint(Graphics g) {
		bg.paint(g);
		
	}
	
	protected void pointerPressed(int x,int y)
    {
		if(x >= bg.getX() && x <= (bg.getX()+ bg.getWidth()) && y >= bg.getY() && y<= (bg.getY()+ bg.getHeight()))
		{
			disp.setCurrent(new GameStarts(midlet));
			repaint();
			
		}
		
		
    }
}
