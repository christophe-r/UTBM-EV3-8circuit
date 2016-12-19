package slave;

import java.util.ArrayList;
import java.util.List;

import robot.utils.Params;


/**
 * A class used to manage a list of RobotState defining who is allow to cross the intersection
 */
public class CrossList {
	private List<RobotState> crossList;

	/**
	 * CrossList contructor
	 */
	public CrossList() {
		 crossList = new ArrayList<RobotState>();
	}
	
	/**
	 * Adds a RobotState into the cross list
	 * @param robot a RobotState
	 */
	public void add(RobotState robot){
		crossList.add(robot);
	}
	
	/**
	 * Clears the cross list
	 */
	public void clear(){
		crossList.clear();
	}
	
	/**
	 * Returns a RobotState at a given position in the list
	 * @param index the index of the list where we want to get the RobotState
	 * @return the robot state at the index
	 */
	public RobotState get(int index){
		return crossList.get(index);
	}
	
	/**
	 * Cuts a message received from the server into three parts to build a RobotState
	 * The message is in the form {NAME_OF_THE_ROBOT1=POSITION1,SPEED1, NAME_OF_THE_ROBOT2=POSITION2,SPEED2, ...}
	 * @param serverCrossList the message from the server
	 */
	public void importServerCrossList(String serverCrossList){
		
		// Example of message : {rob1=200.55,0.5, rob2=800.78,0.45}
		
		serverCrossList = serverCrossList.substring(1, serverCrossList.length()-1); // Removes "{" and "}"
		String[] crossListElements = serverCrossList.split(", ");
		
		
		if(crossListElements.length > 0 ){
			for(String s : crossListElements){
				if( s.contains("=") ){
					String [] msgElement = s.split("=");
					String name = msgElement[0];
					
					String [] msgElementPositionSpeed = msgElement[1].split(",");
					float position = Float.parseFloat(msgElementPositionSpeed[0]);
					float speed = Float.parseFloat(msgElementPositionSpeed[1]);

					crossList.add(new RobotState(name, position, speed));
				}
			}
		}

	}
	
	/**
	 * Returns the position of a robot in the cross list
	 * @param the name of the robot
	 * @return the index of the given robot in the cross list if it is in it, else -1
	 */
	public int getRobotPosition(String name){
		
		int i = 0;
		for(RobotState robot : crossList){
			if( robot.getName().equals(name) ){
				return i;
			}
			i++;
		}
		
		return Params.initPosition;
	}
	
	/**
	 * Returns the names of the robots in the cross list
	 * @return the name of all the robots in the cross list in the form "name1, name2, ..."
	 */
	@Override
	public String toString(){
		String str = "";
		for(RobotState robot : crossList){
			str += robot.getName()+", ";
		}
		return str;
	}
	
	
}
