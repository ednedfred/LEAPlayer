package main;



import java.util.Date;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.Listener;


public class PlayerFrameListener extends Listener
{
	static MusicPlayer thePlayer;

	//These variable names suck, this is just me being really lazy.
	Date initDate = new Date();
	Long lastCircleTime = initDate.getTime(); //The last time that a circle gesture was performed
	Long lastSwipeTime = initDate.getTime(); //The last time that a swipe gesture was performed
	Long lastKTapTime = initDate.getTime();//The last time that a key tap gesture was performed
	Long lastSTapTime = initDate.getTime();//The last time that a screen tap gesture was performed
	Long CdeltaTime; //time since last circle in milliseconds
	Long SdeltaTime; //time since last swipe in milliseconds
	Long KTapDeltaTime; //time since last key tap in milliseconds
	Long STapDeltaTime; //time since last swipe in milliseconds


	


	public PlayerFrameListener(MusicPlayer p)
	{
		super();
		thePlayer = p;
	}

    public void onConnect(Controller controller) {
	        System.out.println("Leap Connected");

	        //These are required to place in order for the leap motion to be able to detect any gestures of these types
	        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
//	        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
	        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
	    }	

	/* (non-Javadoc)
	 * @see com.leapmotion.leap.Listener#onFrame(com.leapmotion.leap.Controller)
	 * This is the main portion of the leap listener that pays attention when there is something on detected by the Leap.
	 */
	public void onFrame(Controller controller)
	{
		// TODO Auto-generated method stub

		super.onFrame(controller);
		Frame frame = controller.frame(); //This is like a snapshot of the current frame that the leap has registered
		processFrameGestures(frame); //Sends the frame for processing

	}


	/**This method takes a frame and analyzes it for gestures. If there are gestures, then it decides what to do. In this case it prints out what
	 * gesture was completed. I have put in a little extra to make sure that it doesn't pick up 10 gestures from the same gesture by making it so
	 * that it will only print out if the time since the last gesture of that type has been long enough to justify this being a "new" gesture.
	 * I made the gesture times individual for the sake of modularity but in practice it is best if we make sure that it only pays attention
	 * one gesture at a time by making a "time since last gesture" variable instead of individual delta times.
	 * 
	 * @param frame The particular frame that has gestures that want to be processed
	 */
	public void processFrameGestures(Frame frame)
	{
		if(frame.gestures().count() > 0)
		{
			for(int g = 0; g < frame.gestures().count() ; g++) //Iterate through the gestures in the frame   frame.gestures().count()
			{

				//frame.gestures().get(g).type() returns an Enum of one of four different types of gestures. This enum list is not changable, the source code 
				//is not public. I have set this up with a switch between the four different enums.
			    switch (frame.gestures().get(g).type()) {
				    case TYPE_CIRCLE:
						//Handle circle gestures
				    	CdeltaTime = new Date().getTime() - lastCircleTime; //Time since last gesture = Current Time - Time at which last gesture happened

				    	if(CdeltaTime > 1000) //This is the buffer time, to prevent duplicate gesture analysis
				    	{
							System.out.println("CIRCLE"); //woo
					    	lastCircleTime = new Date().getTime(); //set the time at which the last gesture was completed. (NOW)
					    
					    	CircleGesture circle = new CircleGesture(frame.gestures().get(g));
					    	Boolean cw;
					        if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
					                cw = true;
					                System.out.println("clockwise");
					        }
					        else
					        {
					            cw = false;
					            System.out.println("counterclockwise");
					        }
					    	
					        
					        if(cw == true)
					        	thePlayer.volume.setValue(thePlayer.volume.getValue()+1);
					        else
					        	thePlayer.volume.setValue(thePlayer.volume.getValue()-1);
					    	
					    	
					    	
				    	}
						break;

					case TYPE_KEY_TAP:
						//Handle key tap gestures
				    	KTapDeltaTime = new Date().getTime() - lastKTapTime;

				    	if(KTapDeltaTime > 400)
				    	{
							System.out.println("KEY TAP");
							thePlayer.pp.doClick(); //Click the button
							
							lastKTapTime = new Date().getTime();
				    	}
						break;

					case TYPE_SCREEN_TAP:
						//Handle screen tap gestures			    	
						STapDeltaTime = new Date().getTime() - lastSTapTime;

				    	if(STapDeltaTime > 400)
				    	{
							System.out.println("SCREEN TAP");
							lastSTapTime = new Date().getTime();
				    	}
						break;

					case TYPE_SWIPE:
						//Handle swipe gestures
				    	SdeltaTime = new Date().getTime() - lastSwipeTime;

				    	if(SdeltaTime > 700)
				    	{
				    		if(frame.hands().leftmost().palmVelocity().getX() > 400)
				    		{
				    			System.out.println("RIGHT SWIPE " + frame.hands().leftmost().palmVelocity().getX());
				    			thePlayer.next.doClick(); //click the button
				    			
				    		}

				    		if(frame.hands().leftmost().palmVelocity().getX() < -400)
				    		{
				    			System.out.println("LEFT SWIPE " + frame.hands().leftmost().palmVelocity().getX());
				    			thePlayer.prev.doClick();
				    		}
				    		
				    		
				    		
//							System.out.println("SWIPE");
							lastSwipeTime = new Date().getTime();
				    	}
						break;

					default:
						//Handle unrecognized gestures - I have not been able to trigger this.
						System.out.println("UNRECOGNIZED");
						break;
			    }
			}

		}

	}







}