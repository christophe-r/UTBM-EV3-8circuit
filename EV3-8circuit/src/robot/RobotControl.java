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
		
	
	public void setAllowed(boolean allowed){		
		this.allowed = allowed;
		
	}
	
	public boolean isAllowed(){
		return this.allowed;
	}
	
	
	public float getTachoCount(){
		return (motorLeft.getTachoCount()+motorRight.getTachoCount())/2.0f;
	}
	
	public void resetTachoCount(){
		motorLeft.resetTachoCount();
		motorRight.resetTachoCount();
	}

	public String getName(){
		return name;
	}
	
	public float getLinearSpeed(){
		return (float) (((motorLeft.getSpeed()+motorRight.getSpeed())/2.0f)/maxSpeed);
	}

	public void setSpeed(float d){
		setSpeed(Params.motorLeft, d);
		setSpeed(Params.motorRight, d);
	}
	
	public void setSpeed(Port motor, float d){
			if( motor == Params.motorLeft ){
				motorLeft.setSpeed(d*maxSpeed);
			} else if( motor == Params.motorRight ) {
				motorRight.setSpeed(d*maxSpeed);
			}	
	}
	
	
	public void forward(){
		motorLeft.forward();
		motorRight.forward();
	}
	
	
	public void stop(){
		motorLeft.stop(true);
		motorRight.stop();
	}
	
	public void rotate(float rad){
		float angleEachWheelRad = (float) (rad / (2*Math.PI) * Params.distanceWheels/Params.wheelDiameter);
		
		motorLeft.rotate((int) (-2*Math.PI * angleEachWheelRad*(180/Math.PI)), true);
		motorRight.rotate((int) (2*Math.PI * angleEachWheelRad*(180/Math.PI)));
	}
	
	
	public float distance(){
		float[] sample = new float[distance.sampleSize()];
		distance.fetchSample(sample, 0);
		
		return sample[0]*1000;
	}
	
	
	public float distance(int n){
		SampleProvider average = new MeanFilter(distance, n);
		float[] sample = new float[average.sampleSize()];
		average.fetchSample(sample, 0);
		
		return sample[0]*1000;
	}

	public float[] color(){
		float[] colorSample = new float[color.sampleSize()];
		color.fetchSample(colorSample, 0);
		
		return colorSample;
	}
	
}

