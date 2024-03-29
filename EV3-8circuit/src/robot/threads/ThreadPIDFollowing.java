package robot.threads;

import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import robot.RobotControl;
import robot.utils.Params;
import slave.Message;
import slave.ThreadSlave;

public class ThreadPIDFollowing implements Runnable {

	private RobotControl control;
	private ThreadSlave threadSlave;
	private ThreadUpdateColor threadColor;
	private ThreadDistance threadDistance;

	private float averageSpeed = Params.averageSpeed;

	/**
	 * Constructor for the thread used to follow the circuit
	 * @param control The robot
	 * @param threadSlave The thread used for communication and sequencing
	 * @param threadColor The thread used for color recognition
	 * @param threadDistance The thread used to get the current distance
	 */
	public ThreadPIDFollowing(RobotControl control, ThreadSlave threadSlave, ThreadUpdateColor threadColor, ThreadDistance threadDistance){
		this.control = control;
		this.threadSlave = threadSlave;
		this.threadColor = threadColor;
		this.threadDistance = threadDistance;
	}

	@Override
	public void run(){

		float blue = Params.blueColor;

		float currentColor = 0f;
		float currentDistance = 0f;

		float midpoint = blue;
		float kp = 0.9f, ki = 0.04f, kd = 0; // kp: harder, ki: accuracy, kd: smoother

		float lastError = 0;
		float error = 0, integral = 0, derivative = 0, correction = 0;

		while(true){

			synchronized (threadColor) {
				currentColor = threadColor.getColor();

				if( threadColor.getFlagOrange()) {
					//Button.LEDPattern(LedColor.F_BLINK_RED.toInt());

					threadSlave.sendQuery(Message.REQUEST_CROSS);
					control.resetTachoCount();
				}
			}

			LCD.drawString("Tacho : " +control.getTachoCount(), 0, 4);

			currentDistance = threadDistance.getDistance();

			LCD.drawString("Dist: "+currentDistance, 0, 3);


			if( control.getTachoCount() < Params.outTacho ){
				threadSlave.sendQuery(Message.REQUEST_UPDATE);

			} else if (control.getTachoCount() >= Params.outTacho && control.getTachoCount() < (Params.outTacho + Params.outSpamTacho) ){
				threadSlave.sendQuery(Message.REQUEST_OUT); 
				control.setAllowed(false);
			}

			error = midpoint - currentColor;

			if(error > 0){ // to be centered on the blue line
				error *= 1.2;
			}

			integral = Math.max(Math.min((error + integral), 2), -2);
			derivative = error - lastError;

			correction = kp * error + ki * integral + kd * derivative;
			
			if( currentDistance > Params.limitDistanceSensor ){ // nothing in front of this robot

				synchronized(threadSlave){

					if( (control.getTachoCount() >= 0 && control.getTachoCount() < Params.outTacho) ){ // in crossing area
						if( control.isAllowed() && threadSlave.isCrossing() ){ // the robot knows that the server knows that the robot is in the crossing area 
							smoothAccelerate(correction);
						} else {
							smoothStop(correction);
						}
					} else { // out of crossing area
						smoothAccelerate(correction);
					}
					
				}

			} else {
				smoothStop(correction);
			}

			lastError = error;
			Delay.msDelay(11);

		}		
	}

	private void smoothAccelerate(float correction){
		averageSpeed = (float) Math.min((averageSpeed + 0.01) * Params.fasterFactor, threadSlave.getAverageSpeed());

		control.setSpeed(Params.motorRight, averageSpeed+correction);
		control.setSpeed(Params.motorLeft, averageSpeed-correction);

		control.forward();
	}

	private void smoothStop(float correction){
		if( averageSpeed > Params.averageSpeed/10.0f ){
			averageSpeed *= Params.slowerFactor;

			control.setSpeed(Params.motorRight, averageSpeed+correction);
			control.setSpeed(Params.motorLeft, averageSpeed-correction);

			control.forward();
		} else {
			control.stop();
		}
	}

}

