package slave;

/**
 * Enum containing the three types of messages the slave can send to the server
 */
public enum Message {
	//Request type
	REQUEST_CROSS(0),
	REQUEST_UPDATE(1),
	REQUEST_OUT(2);
	
	private final int messageId;
	
	/**
	 * Constructor used for the enum
	 */
	private Message(int messageId) {
        this.messageId = messageId;
    }
	
	/**
	 * Returns a type of message as an integer
	 * @return an int defining the type of request
	 */
	public int toInt(){
    	return this.messageId;
    }
	
	
}
