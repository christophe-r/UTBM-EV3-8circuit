package robot.threads;

import lejos.utility.Delay;
import robot.RobotControl;

public class ThreadUpdateColor implements Runnable {

	private RobotControl control;
	private float color = 0;
	
	private boolean flagOrange = false;

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

	private void setFlagOrange(boolean flag){
		this.flagOrange = flag;
	}
	
	public boolean getFlagOrange(){
		if( flagOrange == true ){
			flagOrange = false;
			return true;
		} else {
			return false;
		}
	}

	public float getColor(){
		return color;
	}
}
