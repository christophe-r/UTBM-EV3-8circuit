package robot.utils;

public enum LedColor {	

	/*
	0: turn off button lights
	1/2/3: static light green/red/yellow
	4/5/6: normal blinking light green/red/yellow
	7/8/9: fast blinking light green/red/yellow
	 */
	
	//LED Colors
	NOTHING(0),
	STATIC_GREEN(1), 
	STATIC_RED(2),
	STATIC_ORANGE(3),
	N_BLINK_GREEN(4),
	N_BLINK_RED(5),
	N_BLINK_ORANGE(6),
	F_BLINK_GREEN(7),
	F_BLINK_RED(8),
	F_BLINK_ORANGE(9);
	
    private final int color;
    
    private LedColor(int color) {
        this.color = color;
    }
    
    public int toInt(){
    	return this.color;
    }


}
