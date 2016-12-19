package robot;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.filter.MeanFilter;
import robot.utils.Params;

public class RobotControl {

	private EV3LargeRegulatedMotor motorLeft;
	private EV3LargeRegulatedMotor motorRight;
	
	private EV3UltrasonicSensor sensorDistance;
	private SampleProvider distance;
	
	private EV3ColorSensor sensorColor;
	private SampleProvider color;
	
	private float maxSpeed;
	private String name;
	
	boolean allowed = true;
	
	/**
	 * RobotControl constructor
	 * Initializes all the componants of the robot
	 */
	public RobotControl(){
		
		Brick ev3 = BrickFinder.getLocal();
		name = ev3.getName();
		
		motorLeft = new EV3LargeRegulatedMotor(Params.motorLeft);
		motorRight = new EV3LargeRegulatedMotor(Params.motorRight);	
		maxSpeed = Math.min(motorLeft.getMaxSpeed(), motorRight.getMaxSpeed());
		
		sensorDistance = new EV3UltrasonicSensor(Params.sensorDistance);
		distance = sensorDistance.getDistanceMode();
		
		sensorColor = new EV3ColorSensor(Params.sensorColor);
		color = sensorColor.getRGBMode();
		
	}
		
	/**
	 * Setter to allow the robot to go forward
	 * @param allowed a boolean defining if the robot is allowed to move
	 */
	public void setAllowed(boolean allowed){		
		this.allowed = allowed;
		
	}
	
	public boolean isAllowed(){
		return this.allowed;
	}
	
	/**
	 * Gets the current tacho count of the robot
	 * @return the position of the robot on the circuit
	 */
	public float getTachoCount(){
		return (motorLeft.getTachoCount()+motorRight.getTachoCount())/2.0f;
	}
	
	/**
	 * Resets the tacho count
	 */
	public void resetTachoCount(){
		motorLeft.resetTachoCount();
		motorRight.resetTachoCount();
	}
	
	/**
	 * Gets the name of the robot
	 * @return the name of the robot
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets the mean of the speed of the two motors
	 * @return the linear speed
	 */
	public float getLinearSpeed(){
		return (float) (((motorLeft.getSpeed()+motorRight.getSpeed())/2.0f)/maxSpeed);
	}
	
	/**
	 * Sets the speed of the robot
	 * @param speed the speed we want (from 0 for stop, to 100 for max speed)
	 */
	public void setSpeed(float d){
		setSpeed(Params.motorLeft, d);
		setSpeed(Params.motorRight, d);
	}
	
	/**
	 * Sets the speed for a given motor for the robot
	 * @param motor the left or right motor of the robot
	 * @param speed the speed we want (from 0 for stop, to 100 for max speed)
	 */
	public void setSpeed(Port motor, float d){
			if( motor == Params.motorLeft ){
				motorLeft.setSpeed(d*maxSpeed);
			} else if( motor == Params.motorRight ) {
				motorRight.setSpeed(d*maxSpeed);
			}	
	}
	
	/**
	 * Makes the robot goes forward in straight line
	 */
	public void forward(){
		motorLeft.forward();
		motorRight.forward();
	}
	
	/**
	 * Makes the robot stop
	 */
	public void stop(){
		motorLeft.stop(true);
		motorRight.stop();
	}
	
	/**
	 * Rotates the robot (the pivot of the rotation is the middle of the wheels)
	 * @param angle the angle of rotation in rad (trigonometric orientation)
	 */
	public void rotate(float rad){
		float angleEachWheelRad = (float) (rad / (2*Math.PI) * Params.distanceWheels/Params.wheelDiameter);
		
		motorLeft.rotate((int) (-2*Math.PI * angleEachWheelRad*(180/Math.PI)), true);
		motorRight.rotate((int) (2*Math.PI * angleEachWheelRad*(180/Math.PI)));
	}
	
	/**
	 * Gets the distance of the ultrasonic sensor
	 * @return distance the distance in meters
	 */
	public float distance(){
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		
		return sample[0]*1000;
	}
	
	/**
	 * Gets the mean of a given number of distances that the ultrasonic sensor detected
	 * @param n the number of values to compute the mean
	 * @return the mean of the computed distances
	 */
	public float distance(int n){
		SampleProvider average = new MeanFilter(distance, n);
		float[] sample = new float[average.sampleSize()];
		average.fetchSample(sample, 0);
		
		return sample[0]*1000;
	}
	
	/**
	 * Gets three values corresponding to the rgb colors detected by the color sensor
	 * @return the array with the three values of red, green and blue
	 */
	public float[] color(){
		float[] colorSample = new float[color.sampleSize()];
		color.fetchSample(colorSample, 0);
		
		return colorSample;
	}
	
}

