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
		float kp = 0.9f, ki = 0.04f, kd = 0; // kp: + violent, ki: precision, kd: -violent

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
			
			//float currentTachoCount = control.getTachoCount();

			if( control.getTachoCount() < Params.outTacho ){
				threadSlave.sendQuery(Message.REQUEST_UPDATE);
				
			} else if (control.getTachoCount() >= Params.outTacho && control.getTachoCount() < (Params.outTacho + Params.outSpamTacho) ){
				threadSlave.sendQuery(Message.REQUEST_OUT); 
				control.setAllowed(false);
			}

			error = midpoint - currentColor;

			if(error > 0){ // symétrie pour le passage du bleu au noir et bleu au blanc
				error *= 1.2;
			}

			integral = Math.max(Math.min((error + integral), 2), -2);
			derivative = error - lastError;

			correction = kp * error + ki * integral + kd * derivative;

			if( currentDistance > Params.limitDistanceSensor 
					&& (control.isAllowed()
							|| (!control.isAllowed() 
									&& (control.getTachoCount() > Params.outTacho || control.getTachoCount() < Params.crossTacho)) ) ){

				synchronized(threadSlave){
					averageSpeed = (float) Math.min((averageSpeed + 0.01) * Params.fasterFactor, threadSlave.getAverageSpeed());

					control.setSpeed(Params.motorRight, averageSpeed+correction);
					control.setSpeed(Params.motorLeft, averageSpeed-correction);
					
					control.forward();
				}

			} else {

				if( averageSpeed > Params.averageSpeed/10.0f ){
					averageSpeed *= Params.slowerFactor;

					control.setSpeed(Params.motorRight, averageSpeed+correction);
					control.setSpeed(Params.motorLeft, averageSpeed-correction);

					control.forward();
				} else {
					control.stop();
				}

			}
			lastError = error;

			Delay.msDelay(11);
		}
	}   
}
