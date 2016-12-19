package robot.threads;

import lejos.utility.Delay;
import robot.RobotControl;

public class ThreadDistance implements Runnable {

	private RobotControl control;
	private float distance = 0;
	
	/**
	 * Constructor for the thread distance
	 * @param control The robot 
	 */
	public ThreadDistance(RobotControl control){
		this.control = control;
	}

	@Override
	public void run(){
		while(true){
			distance = control.distance();
			
			Delay.msDelay(10);
		}
	}  
	
	/**
	 * Gets the current distance
	 * @return the distance
	 */
	public float getDistance(){
		return this.distance;
	}
}
