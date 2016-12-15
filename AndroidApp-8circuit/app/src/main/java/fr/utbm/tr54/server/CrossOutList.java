package fr.utbm.tr54.server;

import java.util.LinkedHashMap;
import java.util.Map;

public class CrossOutList {

	private LinkedHashMap<String, RobotState> list;

	/**
	 * Creates a Cross or Out list
	 */
	public CrossOutList() {
		 list =  new LinkedHashMap<>();
	}

	/**
	 * Adds a new robot in the list
	 * @param robot Robot to add
     */
	public void add(RobotState robot){
		list.put(robot.getName(), robot);
	}

	/**
	 * Removes a robot from the list by the robot name
	 * @param robotName Robot name
     */
	public void removeByName(String robotName){
		list.remove(robotName);
	}

	/**
	 * Gets the size of the list
	 * @return List size
     */
	public int size(){
		return list.size();
	}

	/**
	 * Gets the list of robots formatted as string. One line for one robot.
	 * @return String list of robots
     */
	public String robotsNameString(){
		if( list.size() > 0 ){
			String str = "";
			for(Map.Entry<String, RobotState> robot : list.entrySet()){
				str += robot.getKey()+"\n";
			}

			return str.substring(0, str.length()-1);
		}

		return "-";
	}

	/**
	 * Gets the "serialized" list: {rob1=0.0,0.0, rob2=0.0,0.0}
	 * @return Serialized list
     */
	@Override
	public String toString(){
		if( list.size() > 0 ){
			String str = "";
			for(Map.Entry<String, RobotState> robot : list.entrySet()){
				str += robot.getValue().getName()+"="+robot.getValue().getPosition()+","+robot.getValue().getSpeed()+", ";
			}

			return "{"+str.substring(0, str.length()-2)+"}";
		}

		return "{}";
	}
	
}
