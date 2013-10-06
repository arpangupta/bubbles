package com.arpan;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;



public class BubblesMidlet extends MIDlet {
	
	Display disp = Display.getDisplay(this);

	public BubblesMidlet() {
		
		
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		
		
	}

	protected void pauseApp() {
		
		
	}

	protected void startApp() throws MIDletStateChangeException {
		
		 Displayable current = Display.getDisplay(this).getCurrent();
	        if (current == null) {
	            HelloScreen helloScreen = new HelloScreen(this);
	            Display.getDisplay(this).setCurrent(helloScreen);
	        }
		
	}
	public Display getDisplay()
	{
		return disp;
	}


}
