package robot;

import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;

public class Params {
	// Robot
	public static final String ROBOT_SERVER = "tt";
	
	public static final int distanceWheels = 108; // mm
	public static final int wheelDiameter = 56; // mm
	
	// Hardware Motors/Sensors
	public static final Port motorLeft = MotorPort.B;
	public static final Port motorRight = MotorPort.C;
	
	public static final Port sensorDistance = SensorPort.S2;
	public static final Port sensorColor = SensorPort.S3;
	
	
			
}
