package fr.utbm.tr54.server;

public class MessageBuilder {

	private String robotType;
	private String dest;
	private CrossOutList crossOutList;

	/**
	 * Builds a message
	 * @param robotType Robot type (SLAVE or SERVER)
	 * @param dest Destination (ANY)
	 * @param crossOutList List to include
     */
	public MessageBuilder(String robotType, String dest, CrossOutList crossOutList) {
		this.robotType = robotType;
		this.dest = dest;
		this.crossOutList = crossOutList;
	}

	/**
	 * Get the "serialized" message
	 * @return String message
     */
	@Override
	public String toString(){
		// Example: SERVER/ANY/{...}
		return robotType+"/"+dest+"/"+ crossOutList.toString();
	}

	/**
	 * Set the robot type
	 * @param robotType Robot type (SERVER/SLAVE)
     */
	public void setRobotType(String robotType) {
		this.robotType = robotType;
	}

	/**
	 * Set the destination
	 * @param dest Robot destination (ANY)
	 */
	public void setDest(String dest) {
		this.dest = dest;
	}

	/**
	 * Set the Cross or Out list
	 * @param crossOutList List
     */
	public void setCrossOutList(CrossOutList crossOutList) {
		this.crossOutList = crossOutList;
	}

}

