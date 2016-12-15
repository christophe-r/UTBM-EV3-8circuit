package fr.utbm.tr54.lejos.network;

/**
 * Broadcast listener interface
 * @author Alexandre Lombard
 */
public interface BroadcastListener {
	/**
	 * Triggered on broadcast received
	 * @param message the raw message
	 */
	void onBroadcastReceived(byte[] message);
}
