package robot.utils;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

/**
 * Params used by the robot
 */
public class Params {
	// Robot	
	public static final int distanceWheels = 108; // mm
	public static final int wheelDiameter = 56; // mm
	
	// Hardware Motors/Sensors
	public static final Port motorLeft = MotorPort.B;
	public static final Port motorRight = MotorPort.C;
	
	public static final Port sensorDistance = SensorPort.S2;
	public static final Port sensorColor = SensorPort.S3;
	
	//Control parameters
	public static final float averageSpeed = 0.5f;
	public static final float crossTacho = 850; // 600
	public static final float outTacho = 2100; // 2200
	public static final float outSpamTacho = 300;
	public static final float limitDistanceSensor = 250;
	
	public static final int initPosition = -1;
	
	public static final float slowerFactor = 0.8f;
	public static final float fasterFactor = 1.2f;
	
	public static final float synchFactor = 0.7f;
	
	public static final float blueColor = 0.10f;


			
}
