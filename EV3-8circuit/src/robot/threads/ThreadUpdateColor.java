package robot.threads;

import lejos.utility.Delay;
import robot.RobotControl;

public class ThreadUpdateColor implements Runnable {

	private RobotControl control;
	private float color = 0;
	
	private boolean flagOrange = false;
	
	/**
	 * Constructor for the thread used to recognize the colors
	 * @param control The robot
	 */
	public ThreadUpdateColor(RobotControl control){
		this.control = control;
	}

	@Override
	public void run(){
		while(true){
			synchronized (this) {
				float[] fetchColor = control.color();
				color = ((fetchColor[0]+fetchColor[1]+fetchColor[2])/3);
				
				if( fetchColor[0] > 0.19 && fetchColor[1] < 0.15 && fetchColor[2] < 0.3 ){
					setFlagOrange(true);
				}
			}
			Delay.msDelay(9);
		}
	}       
	
	/**
	 * Sets the boolean flagOrange when orange is detected
	 * @param flag A boolean 
	 */
	private void setFlagOrange(boolean flag){
		this.flagOrange = flag;
	}
	
	/**
	 * Gets the current flag and changes it to false if it is true 
	 * @return the current flag
	 */
	public boolean getFlagOrange(){
		if( flagOrange == true ){
			flagOrange = false;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Gets the current color
	 * @return The current color
	 */
	public float getColor(){
		return color;
	}
}
