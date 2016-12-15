package slave;

public enum Message {
	//Request type
	REQUEST_CROSS(0),
	REQUEST_UPDATE(1),
	REQUEST_OUT(2);
	
	private final int messageId;
	
	private Message(int messageId) {
        this.messageId = messageId;
    }
	
	public int toInt(){
    	return this.messageId;
    }
	
	
}
