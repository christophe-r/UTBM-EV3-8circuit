package slave;

public class MessageBuilder {

	
	//String msg = "SLAVE/"+control.getName()+"/"+request.toInt()+"/"+position+"/"+speed;
	
	String robotType;
	String name;
	Message message;
	float position;
	float speed;
	
	public MessageBuilder(String robotType, String name, Message message, float position, float speed) {
		this.robotType = robotType;
		this.name = name;
		this.message = message;
		this.position = position;
		this.speed = speed;
	}
	
	
	public String toString(){
		return robotType+"/"+name+"/"+message.toInt()+"/"+position+"/"+speed;
	}
	
	public void setRobotType(String robotType) {
		this.robotType = robotType;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public void setPosition(float position) {
		this.position = position;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	
	
	
	
	
	
	
}
