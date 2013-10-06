package com.arpan;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;
//import java.util.Hashtable;

public class GameStarts extends Canvas {

	private Sprite bg, gameO;// the background and the pause menu
	Random rand = new Random();

final int BOTTOM = 0, RIGHT = 1,TOP = 2, LEFT = 3;
	int count = 0, bubble_dir = 0, score = 3000, lives = 5 , position = BOTTOM ; 
	private Sprite stick1;
//	these arrays will contain all the images for stick and bubbles  
	private Sprite[] left = new Sprite[8] ;
	private Sprite[] right = new Sprite[8] ;
	private Sprite[] standing = new Sprite[2];  
//	private Sprite[] down = new Sprite[8];
	private int bubbles_dir[] = {1,2,3,4,3};
	private Sprite stains[] = new Sprite[9];
	boolean stains_displayed[] = new boolean[9];
	
	private Sprite[] life = new Sprite[3];
//	private boolean life_display[] = {true,true,true};
//	private Sprite[] dead = new Sprite[3];
	
	
//	private Sprite[] bubbles = new Sprite[3];
	
	boolean killed[] = {false,false,false,false,false};
	boolean resume = true; //move now
	
	private Sprite[] bubbles_alive = new Sprite[5]; // the bubbles to be displayed
	private Sprite stick;// the stick image that is to be displayed: before changing this variable, make sure to store its position somewhere and use that position to get the next position of the image 
	/* logic:
	 first look for the special condition like stick-bubble collision and stick-wall collision and the bubble-wall collision
	 then, depending on:
	1. Stick man is running right: use right array list(run_right  = true && run_left = false)
	2. Stick man is running left:  use left array list(run_right  = false && run_left = true)
	3. Stick man is standing : use stand array list(run_right  = false && run_left = false)
	then, use the position variable
	4. Decide who's next
	
	*/

	
	boolean run_right = false;
	boolean run_left =  false;
	boolean some_flag = false;
	Display disp;
	BubblesMidlet midlet;
	Timer timer;
	private static final int TIMER_DELAY =20;
	TimerTask engine;
	public GameStarts(BubblesMidlet midlet) {
	
		
		this.midlet = midlet;
		disp = midlet.getDisplay();
		setFullScreenMode(true);
		
		try {
			
			bg = new Sprite(Image.createImage("/bg.png"));
			bg.setPosition(0, 0);
			gameO = new Sprite(Image.createImage("/gameover.png"));
			gameO.setPosition(26,158);
		//	pause = new Sprite(Image.createImage("/pausemenu.png"));
		//	pause.setPosition(40,133);
			for (int i = 0; i<= 7; ++i)
			{
				left[i] = new Sprite(Image.createImage("/l"+(i+1)+".png"));	
				
			}
			for(int i=0; i<3;++i)
			{
				life[i] = new Sprite(Image.createImage("/s1.png"));
				
			}
			
			for(int i=0; i<3;++i)
			{
				if(i > 0)
				{
					life[i].setPosition(life[i-1].getX()+179 , 4);
				}
				else life[i].setPosition(174, 4);
				
			}
			
			for(int i=0; i<9;++i)
			{
				stains_displayed[i] = false;
				stains[i] = new Sprite(Image.createImage("/stain"+ (rand.nextInt(3)+1) + ".png"));
				
			}
			
			for (int i = 0; i<= 7; ++i)
			{
				right[i] = new Sprite(Image.createImage("/r"+(i+1)+".png"));	
				
			}
			for (int i = 0; i<= 1; ++i)
			{
				standing[i] = new Sprite(Image.createImage("/s"+(i+1)+".png"));	
				
			}
//			for (int i = 0; i<= 7; ++i)
//			{
//				down[i] = new Sprite(Image.createImage("/d"+(i+1)+".png"));	
//				
//			}
			for(int i=0; i<3; ++i)
			{
				stains[i] = new Sprite(Image.createImage("/stain"+(i+1)+".png"));
			}
			
			bubbles_alive[0] = new Sprite(Image.createImage("/b1.png"));
			bubbles_alive[1] = new Sprite(Image.createImage("/b1.png"));
			bubbles_alive[2] = new Sprite(Image.createImage("/b2.png")); 		
			bubbles_alive[3] = new Sprite(Image.createImage("/b2.png"));
			bubbles_alive[4] = new Sprite(Image.createImage("/b3.png"));
			
			for(int i =0; i<bubbles_alive.length; ++i)
			{
			int r =  rand.nextInt(bg.getHeight()) ;
			while (r  < 51 ||  r > 327- bubbles_alive[0].getHeight() )
				r =  rand.nextInt(bg.getHeight()) ;
			
			bubbles_alive[i].setPosition(rand.nextInt(bg.getWidth()),r );
			}
			
			/*
			
			for (int i = 0; i<= 2; ++i)
			{
				bubbles[i] = new Sprite(Image.createImage("/b"+(i+1)+".png"));	
				
			}
			*/
			stick = standing[0];
			stick.setPosition((bg.getWidth()/2) - 17, 327-stick.getHeight());
			
//			stick.collidesWith(bubbles[0], true);
	
			//stand.setPosition((bg.getWidth()/2) - 8, 327-r1.getHeight());
			
		} catch (IOException e) {
			
			
			e.printStackTrace();
		}
		
		timer = new Timer();
		engine = new TimerTask() {
		public synchronized void run() {
													
			
			if (resume && lives > 0 )
			{	
				score = score+1;
//			checkSpecial(); // check for the resume condition
			checkCollisionAndMove(); // to check for collision between any of the bubbles and the the stickman, between bubbles and the wall; between stickman and the wall; set killed for that bubble in the array(set a message OUCH! on the title bar), also account for lives and score 
			 //also set the next position of the bubbles in bubbles_alive array and of the stick man taking into account all the collision conditions 
			repaint();
			}
			else repaint();
			
			}
		private void checkCollisionAndMove() {
			
			int stickX = stick.getX(), 
			stickY = stick.getY();
			boolean[] flag = {false,false,false,false,false};
			// first look for a collision then generate the new postions
			for(int i=0; i<bubbles_alive.length; ++i ) // bubbles part
			{	/*if(  (bubbles_alive[i].getX() + bubbles_alive[i].getWidth() >= stickX ) && (bubbles_alive[i].getY() + bubbles_alive[i].getHeight() >= stickY ) 
					&&  (bubbles_alive[i].getX() + bubbles_alive[i].getWidth() <= stickX + stick.getWidth()) && (bubbles_alive[i].getY() + bubbles_alive[i].getHeight() <= stickY + stick.getHeight()) )
				{
					
					killed[i] = true;
					bubbles_alive[i].setPosition(bg.getWidth()/2 - 10, 179);
					--lives;
				}*/
				
			    if(bubbles_alive[i].collidesWith(stick, true))
			    {
			    	killed[i] = true;
			    	
			    		score-=50;
			    	int x_ = bubbles_alive[i].getX();
			    	int y_ = bubbles_alive[i].getY();
					bubbles_alive[i].setPosition(bg.getWidth()/2 - 10, 179);
					--lives;
//					int j;
					int j =0;
					while(stains_displayed[j] == true)
					{
						++j;
					}
				stains[j].setPosition(x_, y_);
				stains_displayed[j] = true;
//				if(lives ==0)
//				{
//					int x__ = stick.getX() ;
//					int y__ = stick.getY();
//					switch(position)
//					{
//					case BOTTOM:
//						stick1 = down[2*BOTTOM];
//						stick1.setPosition(x__, y__);
////						stick.paint(g);
//						break;
//					case TOP:
//						stick1 = down[2*TOP];
//						stick1.setPosition(x__, y__);
////						stick.paint(g);
//						break;
//					case LEFT:
//						stick1 = down[2*LEFT];
//						stick1.setPosition(x__, y__);
////						stick.paint(g);
//						break;
//					case RIGHT:
//						stick1 = down[2*RIGHT];
//						stick1.setPosition(x__, y__);
////						stick.paint(g);
//					
//					}
					
//				}
			    	
			    }
				int diff;
				int safe;
				if(( bubbles_alive[i].getX() >= ( safe = bg.getWidth() - bubbles_alive[i].getWidth() ))) // right wall
				{
					flag[i] = true;
					//diff = bubbles_alive[i].getX() - safe;
					//bubbles_alive[i].setPosition(bubbles_alive[i].getX() - diff -3, bubbles_alive[i].getY());
					switch(bubbles_dir[i])
					{
					case 1:
						bubbles_dir[i] = 2; break;
					case 4:
						bubbles_dir[i] = 3; break;
					}
					
				}
				
				if(( bubbles_alive[i].getX() <=  0))        // left wall
				{
					flag[i] = true;
					diff = bubbles_alive[i].getX()*-1;
					bubbles_alive[i].setPosition(bubbles_alive[i].getX() + diff +3, bubbles_alive[i].getY());
					
					switch(bubbles_dir[i])
					{
					case 2:
						bubbles_dir[i] = 1; break;
					case 3:
						bubbles_dir[i] = 4; break;
					}
					
				}
				
				if(( bubbles_alive[i].getY() <=  51 )) 		//top wall
				{
					flag[i] = true;
					bubbles_alive[i].setPosition(bubbles_alive[i].getX() , bubbles_alive[i].getY()+3);
					switch(bubbles_dir[i])
					{
					case 3:
						bubbles_dir[i] = 2; break;
					case 4:
						bubbles_dir[i] = 1; break;
					}
					
				}
				
				if(( bubbles_alive[i].getY() >=  (safe = 326 - bubbles_alive[i].getHeight()) ))      //floor
				{
					flag[i] = true;
					diff = bubbles_alive[i].getY() - safe;
					bubbles_alive[i].setPosition(bubbles_alive[i].getX() , bubbles_alive[i].getY()-diff -3);
					
					switch(bubbles_dir[i])
					{
					case 1:
						bubbles_dir[i] = 4; break;
					case 2:
						bubbles_dir[i] = 3; break;
					}
					
					
					
				}
				
				
			}
			
			int sx = stick.getX();
			int sy = stick.getY();
			// stick part
			if((++count)%2 == 0)
				some_flag = true;
			else some_flag = false; 
			switch(position)
			{
			case BOTTOM:
				if(run_right == true && run_left == false ) // running right and at the bottom
						if( sx > bg.getWidth() - stick.getWidth() )// collision case
						{
							
							position = RIGHT;
							stick = right[RIGHT*2];
							stick.setPosition( bg.getWidth() - stick.getWidth(), 327- stick.getHeight() );
						}
						else // no collision 
						{
							if(some_flag)
							{
								stick = right[2*BOTTOM];
								stick.setPosition(sx + 3 , sy );
							}
							else
							{
								stick = right[2*BOTTOM +1];
								stick.setPosition(sx + 3 , sy );
							}
						}
				else if(run_right == false && run_left == true)// running left and at the bottom
				{
						if( sx <= 0 ) //collision case
						{
							position = LEFT;
							stick = left[RIGHT*2];
							stick.setPosition( 0, 327- stick.getHeight() );
							
						}
						
						else
						{
							if(some_flag)
							{
								stick = left[2*BOTTOM];
								stick.setPosition(sx -3 , sy );
							}
							else
							{
								stick = left[2*BOTTOM +1];
								stick.setPosition(sx -3 , sy );
							}
							
						}
					
				}
				
				else if(run_right == false && run_left == false)// standing at the bottom
				{
					if(some_flag)
					{
						stick = standing[2*BOTTOM];
						stick.setPosition(sx  , sy );
					}
					else
					{
						stick = standing[2*BOTTOM +1];
						stick.setPosition(sx , sy );
					}
					
				}
					
				
				break;
				
			case RIGHT:
				if(run_right == true && run_left == false ) // running right and at the right
				{
					if( sy < 51  )
					{
						position = TOP;
						stick = right[TOP*2];
						stick.setPosition( bg.getWidth() - stick.getWidth(), 51);
					}
				else{
					if(some_flag)
					{
						stick = right[2*RIGHT];
						stick.setPosition(sx  , sy -3 );
					}
					else
					{
						stick = right[2*RIGHT +1];
						stick.setPosition(sx  , sy -3);
					}
					
				}
				}
			
			else if(run_right == false && run_left == true)// running left and at the right
			{
					if(sy > 327 - stick.getHeight() )
					{
						position = BOTTOM;
						stick = left[BOTTOM*2];
						stick.setPosition( bg.getWidth() - stick.getWidth() , 327- stick.getHeight() );
					}
				
					else	if(some_flag)
					{
						stick = left[2*LEFT];
						stick.setPosition(sx  , sy +3 );
					}
					else
					{
						stick = left[2*LEFT +1];
						stick.setPosition(sx  , sy +3);
					}
				
			}
				
			
				break;
			case LEFT:
				if(run_right == true && run_left == false ) // running right and on the left
						if( sy > 327 - stick.getHeight() )// collision case
						{
							
							position = BOTTOM;
							stick = right[BOTTOM*2];
							stick.setPosition( 0, 327- stick.getHeight() );
						}
						else // no collision 
						{
							if(some_flag)
							{
								stick = right[2*LEFT];
								stick.setPosition(sx , sy+3 );
							}
							else
							{
								stick = right[2*LEFT +1];
								stick.setPosition(sx , sy +3);
							}
						}
				else if(run_right == false && run_left == true)// running left and on the left
				{
						if( sy <= 51 ) //collision case
						{
							position = TOP;
							stick = left[TOP*2];
							stick.setPosition( 0, 51);
							
						}
						
						else
						{
							if(some_flag)
							{
								stick = left[2*RIGHT];
								stick.setPosition(sx , sy -3 );
							}
							else
							{
								stick = left[2*RIGHT +1];
								stick.setPosition(sx  , sy -3 );
							}
							
						}
					
				}
				break;
				
			case TOP:
				if(run_right == true && run_left == false ) // running right and at the top
						if( sx <= 0 )// collision case
						{
							
							position = LEFT;
							stick = right[LEFT*2];
							stick.setPosition( 0 , 51 );
						}
						else // no collision 
						{
							if(some_flag)
							{
								stick = right[2*TOP];
								stick.setPosition(sx -3 , sy );
							}
							else
							{
								stick = right[2*TOP +1];
								stick.setPosition(sx - 3 , sy );
							}
						}
				else if(run_right == false && run_left == true)// running left and at the TOP
				{
						if( sx >= bg.getWidth() - stick.getWidth() ) //collision case
						{
							position = RIGHT;
							stick = left[LEFT*2];
							stick.setPosition( bg.getWidth() - stick.getWidth(), 51 );
							
						}
						
						else
						{
							if(some_flag)
							{
								stick = left[2*TOP];
								stick.setPosition(sx +3 , sy );
							}
							else
							{
								stick = left[2*TOP +1];
								stick.setPosition(sx +3 , sy );
							}
							
						}
					
				}
				break;
				
			
			}
			
			for(int i=0; i<bubbles_alive.length; ++i )
			{
//					killed[i] = killed[i] ?false 
				
				if(killed[i] == false)
				{
					
					int x = bubbles_alive[i].getX();
					int y = bubbles_alive[i].getY();
					int x_ = rand.nextInt(5);
					int y_ = rand.nextInt(5);
					
					flag[i] = flag[i]? !flag[i] : flag[i];
				
					
					
					 switch(bubbles_dir[i])
					 {
					 
					 
					 case 1:
						 bubbles_alive[i].setPosition(x+ x_, y + y_);
						break;
					 case 2:
						 bubbles_alive[i].setPosition(x- x_, y + y_);
						break;
					 case 3:
						 bubbles_alive[i].setPosition(x - x_ ,y - y_);
						 break;
					 case 4:
						 bubbles_alive[i].setPosition(x+ x_ , y - y_  );
						break;
						
					 }
				
						
				}
				
				else{ killed[i] = false; }
			}
			
			
			
			
			
		}
		

		

		private void move_bubbles() {
		/*	int x = b1.getX();
			int y = b1.getY();
			Random rand = new Random();
			int r = rand.nextInt(4);
			 switch(r)
			 {
			 case 0:
				b1.setPosition(x+2,y );
				break;
			 case 1:
				b1.setPosition(x+2, y+2);
				break;
			 case 2:
				b1.setPosition(x-2,y );
				break;
			 case 3:
				b1.setPosition(x-2,y-2);

				
			 }*/
			
//			for(int i = 0;)
			
			
		}
/*
		private void move_stick() {
		
			if((++count)%2 == 0)
				some_flag = true;
			else some_flag = false; 
			
			if (run_right)
			{
				if(some_flag)
				{
				int x_ = stick_right.getX();
				int y_ = stick_right.getY();
				stick_right = r1;
				stick_right.setPosition(x_+3, y_);
				}
				else
				{
					int x_ = stick_right.getX();
					int y_ = stick_right.getY();
					
					

				stick_right = r2;
				stick_right.setPosition(x_+3, y_);
				}
			}
			else
			{
				int x_ = stand.getX();
				int y_ = stand.getY();
			
				if(some_flag)
				{
				
				stand = s1;
				stand.setPosition(x_, y_);
				}
				else
				{
					
					

				stand = s2;
				stand.setPosition(x_, y_);
				}
				
				
			}
			
			
		}*/

//		private void checkSpecial() {
//			// TODO Auto-generated method stub
//			
//		}

	
		
				};
				timer.schedule(engine, 20,TIMER_DELAY);
	}
	protected void pointerPressed(int x, int y){
		
		if(x >= 176 && x <=240 && y >= 324 && y<=400 )
		{
			run_right = true;
			run_left = false;
			repaint();
		}
		if(x >= 0 && x <=70 && y >= 325 && y<=400 )
		{
			run_right = false;
			run_left = true;
			repaint();
		}
		if(x >= 93 && x <=93+48 && y >= 333 && y<=(333+48) )
		{
			resume = !resume;
			repaint();
		}
		
		if( x >0 && y> 0 &&  lives ==0 )
		{
			disp.setCurrent(new HelloScreen(midlet));
			repaint();
		}
//		
//		if(x >= 40 && x <= 40+160 && y>= 133 && y>= 133+96 && !resume)
//		{
//			
//				resume = true;
//		}		
//		if( x >= 28 && x<= 28+181 && y>=181 && y>= 181+51 && !resume)
//		{
//			 midlet.notifyDestroyed(); 
//		}
//			
		
	
	}
	protected void paint( Graphics g) {
	
		if (resume && lives >0)
		{
		bg.paint(g);
		for(int i=0; i<stains.length;++i)
		{
			if(stains_displayed[i] == true)
				stains[i].paint(g);
		}
		stick.paint(g);
				
		for(int i = 0 ; i< bubbles_alive.length ; ++i)
		bubbles_alive[i].paint(g);
		g.drawString(""+(score), 0, 5,Graphics.LEFT| Graphics.TOP);
		
		g.drawString("LIVES: "+(lives), 174, 5,Graphics.LEFT| Graphics.TOP);
//		for(int i = 0 ; i<3 ; ++i)
//			life[i].paint(g);
//			g.drawString(""+(score), 0, 5,Graphics.LEFT| Graphics.TOP);
			
			
		
		}
		
		
		
		if(lives <=0 )
		{
			bg.paint(g);
			gameO.paint(g);
			for(int i=0; i<stains.length;++i)
			{
				if(stains_displayed[i] == true)
					stains[i].paint(g);
			}
//			if((++count)%2 ==0)
//			{	
//			stick1.paint(g);
//			}
//			else
//			{
//				int x__ = stick.getX() ;
//			int y__ = stick.getY();
//			switch(position)
//			{
//			case BOTTOM:
//				stick1 = down[2*BOTTOM+1];
//				stick1.setPosition(x__, y__+10);
////				stick.paint(g);
//				break;
//			case TOP:
//				stick1 = down[2*TOP+1];
//				stick1.setPosition(x__, y__-5);
////				stick.paint(g);
//				break;
//			case LEFT:
//				stick1 = down[2*LEFT+1];
//				stick1.setPosition(x__, y__);
////				stick.paint(g);
//				break;
//			case RIGHT:
//				stick1 = down[2*RIGHT+1];
//				stick1.setPosition(x__, y__);
////				stick.paint(g);
//			
//			}
//				stick1.paint(g);
//			}
			g.drawString(""+(score), 0, 5,Graphics.LEFT| Graphics.TOP);
			g.drawString("LIVES: "+(lives), 174, 5,Graphics.LEFT| Graphics.TOP);
//			stick.paint(g);
			
		
		
	
	}

}}
