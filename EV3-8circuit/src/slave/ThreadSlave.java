package slave;

import java.io.IOException;
import java.net.SocketException;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.network.BroadcastListener;
import lejos.network.BroadcastManager;
import lejos.network.BroadcastReceiver;
import robot.RobotControl;
import robot.utils.LedColor;
import robot.utils.Params;


// Message Analyzer

/**
 * Thread used to manage the messages and the behaviour of the robot
 */
public class ThreadSlave implements Runnable {

	private RobotControl control;

	private CrossList crossList = new CrossList();
	
	//private int pos = Params.initPosition;
	private float averageSpeed = Params.averageSpeed;
	private LedColor currentLed = LedColor.NOTHING;

	public ThreadSlave(RobotControl control){
		this.control = control;
	}

	@Override
	public void run(){
		try {
			BroadcastReceiver.getInstance().addListener(new BroadcastListener() {
				@Override
				public void onBroadcastReceived(byte[] message) {

					String str = new String(message);
					String[] messageElements = str.split("/");

					if( messageElements[0].equals("SERVER") ){
						onServerMessageReceived(messageElements);
					}	
				}

			});


		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Used when a message is received from the server
	 * @param messageElements message received
	 */
	public void onServerMessageReceived(String[] messageElements){

		String serverCrossList = messageElements[2];
		
		crossList.clear();
		crossList.importServerCrossList(serverCrossList);
		
		int pos = crossList.getRobotPosition(control.getName());

		LCD.clear(7);
		LCD.drawString("l: "+crossList.toString(), 0, 7);		
		
		LCD.clear(5);
		LCD.drawString("POS : " + pos, 0, 5);
		
		updateLedColor(pos);

		if(pos < 1 ){							
			control.setAllowed(true);
			setAverageSpeed(Params.averageSpeed);
			LCD.drawString("NO SYNC", 0, 6);
			
			//LCD.drawString("prev : noprev", 0, 7);
			
		} else {
			if(control.getTachoCount() < Params.crossTacho){
				control.setAllowed(true);
				
				float getNewSpeed = (crossList.get(pos-1).getPosition() / Params.outTacho) * crossList.get(pos-1).getSpeed();
				
				setAverageSpeed(getNewSpeed);
				LCD.drawString("SYNC : " + getNewSpeed, 0, 6);
	
			} else {
				control.setAllowed(false);
				LCD.drawString("STOP", 0, 6);
			}
			
			//LCD.drawString("prev : " + crossList.get(pos-1).getName(), 0, 7);
		}
		
	}
	

	public void setAverageSpeed(float speed){
		this.averageSpeed = speed;
	}	

	public float getAverageSpeed(){
		return this.averageSpeed;
	}

	/**
	 * Manages the color of the led depending of the position of the robot
	 * @param pos the position of the robot in the cross list
	 */
	private void updateLedColor(int pos){
		LedColor newLed;
		
		if( pos < 0 ){
			newLed = LedColor.NOTHING;
		} else if( pos == 0 ){
			newLed = LedColor.STATIC_GREEN;
		} else if (pos == 1) {
			newLed = LedColor.STATIC_ORANGE;
		} else {
			newLed = LedColor.STATIC_RED;
		}

		if( currentLed != newLed ){
			Button.LEDPattern(newLed.toInt());
			currentLed = newLed;
		}

	}
	
	/**
	 * Used to send a message
	 * @param request the type of the request
	 */
	public void sendQuery(Message request){
		sendMsg(request, control.getTachoCount(), control.getLinearSpeed());
	}
	
	/**
	 * Sends the message after building it
	 * @param request the type of message
	 * @param position the current position of the robot
	 * @param speed the current speed of the robot
	 */
	public void sendMsg(Message request, float position, float speed){

		MessageBuilder messageBuilder = new MessageBuilder("SLAVE", control.getName(), request, position, speed);
		
		try {
			BroadcastManager.getInstance().broadcast(messageBuilder.toString().getBytes());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
