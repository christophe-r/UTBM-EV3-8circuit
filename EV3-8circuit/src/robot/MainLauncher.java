package robot;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import robot.threads.ThreadDistance;
import robot.threads.ThreadPIDFollowing;
import robot.threads.ThreadUpdateColor;
import robot.utils.LedColor;
import slave.ThreadSlave;

public class MainLauncher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Button.LEDPattern(LedColor.F_BLINK_RED.toInt());

		LCD.drawString("Initializing...", 0, 0);

		RobotControl control = new RobotControl();

		Button.LEDPattern(LedColor.NOTHING.toInt());

		LCD.clear(0);
		LCD.drawString("Initialized", 0, 0);


		LCD.drawString("Name: " + control.getName() , 0, 1);

		// Launch Slave
		ThreadSlave threadSlave = new ThreadSlave(control);
		new Thread(threadSlave).start();

		// PID Following
		ThreadUpdateColor threadColor = new ThreadUpdateColor(control);
		new Thread(threadColor).start();
		
		ThreadDistance threadDistance = new ThreadDistance(control);
		new Thread(threadDistance).start();
		
		ThreadPIDFollowing threadPID = new ThreadPIDFollowing(control, threadSlave, threadColor, threadDistance);
		new Thread(threadPID).start();




	}
}
