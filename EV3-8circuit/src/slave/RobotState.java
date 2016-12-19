package slave;

/**
 * Class gathering the three informations we need for a robot : name, position and speed
 */
public class RobotState {

	private String name;
	private float position;
	private float speed;
	
	/**
	 * RobotState constructor
	 */
	public RobotState(String name, float position, float speed) {
		this.name = name;
		this.position = position;
		this.speed = speed;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getPosition() {
		return position;
	}
	public void setPosition(float position) {
		this.position = position;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	
}
