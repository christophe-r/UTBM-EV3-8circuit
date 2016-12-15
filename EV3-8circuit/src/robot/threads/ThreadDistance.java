package robot.threads;

import lejos.utility.Delay;
import robot.RobotControl;

public class ThreadDistance implements Runnable {

	private RobotControl control;
	private float distance = 0;
	

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
	
	public float getDistance(){
		return this.distance;
	}
}
