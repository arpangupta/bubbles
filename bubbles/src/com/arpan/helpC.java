package com.arpan;



import java.io.IOException;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;


public class helpC extends Canvas {

	   private  BubblesMidlet midlet;
	   private Sprite help;
	   Display disp;
//	   boolean  help_ = false;
//	   private  Command exitCommand;
	
	public helpC(BubblesMidlet midlet) {
		this.midlet = midlet;
		disp = midlet.getDisplay();
		setFullScreenMode(true);
		try {
			
//			bg = new Sprite((Image.createImage("/fp.png")));
			help = new Sprite((Image.createImage("/helpPage.png"))); 
			help.setPosition(0, 0);
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	

	protected void paint(Graphics g) {
			help.paint(g);
			
	}
	 protected void pointerPressed(int x,int y)
	 {
		 
	
		 if(x>=0 && y>=0)
		 {
			 disp.setCurrent(new HelloScreen(midlet));
				repaint();
		 }
		 
	 }



}
