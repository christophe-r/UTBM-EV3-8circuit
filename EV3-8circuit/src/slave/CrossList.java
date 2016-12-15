package slave;

import java.util.ArrayList;
import java.util.List;

import robot.utils.Params;

public class CrossList {
	private List<RobotState> crossList;

	
	public CrossList() {
		 crossList = new ArrayList<RobotState>();
	}
	
	
	public void add(RobotState robot){
		crossList.add(robot);
	}
	
	public void clear(){
		crossList.clear();
	}
	
	public RobotState get(int index){
		return crossList.get(index);
	}
	
	
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
	
	@Override
	public String toString(){
		String str = "";
		for(RobotState robot : crossList){
			str += robot.getName()+", ";
		}
		return str;
	}
	
	
}
