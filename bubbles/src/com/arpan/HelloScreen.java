package com.arpan;



import java.io.IOException;

import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.Sprite;


public class HelloScreen extends Canvas {

	   private  BubblesMidlet midlet;
	   private Sprite bg, help;
	   Display disp;
//	   boolean  help_ = false;
//	   private  Command exitCommand;
	
	public HelloScreen(BubblesMidlet midlet) {
		this.midlet = midlet;
		disp = midlet.getDisplay();
		setFullScreenMode(true);
		try {
			
			bg = new Sprite((Image.createImage("/fp.png")));
			help = new Sprite((Image.createImage("/helpPage.png"))); 
			help.setPosition(0, 0);
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	protected void paint(Graphics g) {
		
		bg.paint(g);
		
		
			
	}
	 protected void pointerPressed(int x,int y)
	 {
		 
		 if(x>=55 && x<=(55+126) && y>=234 && y<=(234+41))
		 {
			disp.setCurrent(new GamePage(midlet));
			repaint();
			 
		 }
		 
		 
		 if(x>=23 && x<=(23+186) && y>=278 && y<=(278+43))
		 {
//			disp.setCurrent(new GamePage(midlet));
//			repaint();
			 disp.setCurrent(new helpC(midlet));
			 
		 }
		 
		 if(x>= 51 && x<=(51+121) && y >=320 && y <=(320+44) )
		 {
		
//				repaint();
		 }
		
		 
	 }


private Image resizeImage(Image src) {
    int srcWidth = src.getWidth();
    int srcHeight = src.getHeight();
    int screenWidth = getWidth();
    int screenHeight = getHeight();
    Image tmp = Image.createImage(screenWidth, srcHeight);
    Graphics g = tmp.getGraphics();
    int ratio = (srcWidth << 16) / screenWidth;
    int pos = ratio/2;

    //Horizontal Resize        

    for (int x = 0; x < screenWidth; x++) {
        g.setClip(x, 0, 1, srcHeight);
        g.drawImage(src, x - (pos >> 16), 0, Graphics.LEFT | Graphics.TOP);
        pos += ratio;
    }

    Image resizedImage = Image.createImage(screenWidth, screenHeight);
    g = resizedImage.getGraphics();
    ratio = (srcHeight << 16) / screenHeight;
    pos = ratio/2;        

    //Vertical resize

    for (int y = 0; y < screenHeight; y++) {
        g.setClip(0, y, screenWidth, 1);
        g.drawImage(tmp, 0, y - (pos >> 16), Graphics.LEFT | Graphics.TOP);
        pos += ratio;
    }
    return resizedImage;

}
}