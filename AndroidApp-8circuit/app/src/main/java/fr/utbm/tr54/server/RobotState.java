package fr.utbm.tr54.server;

public class RobotState {

	private String name;
	private float position;
	private float speed;

	/**
	 * Creates a new robot with its properties
	 * @param name Robot name
	 * @param position Robot position
	 * @param speed Robot speed
     */
	public RobotState(String name, float position, float speed) {
		this.name = name;
		this.position = position;
		this.speed = speed;
	}

	/**
	 * Gets the robot name
	 * @return Name
     */
	public String getName() {
		return name;
	}

	/**
	 * Gets the robot position
	 * @return Position
	 */
	public float getPosition() {
		return position;
	}

	/**
	 * Gets the robot speed
	 * @return Speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Get the "serialization" of the robot: robot=position,speed
	 * @return
     */
	@Override
	public String toString(){
		return name+"="+position+","+speed;
	}
	
	
}
