package fr.utbm.tr54.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedHashMap;
import java.util.Map;


import fr.utbm.tr54.app.MainActivity;
import fr.utbm.tr54.lejos.network.BroadcastListener;
import fr.utbm.tr54.lejos.network.BroadcastManager;
import fr.utbm.tr54.lejos.network.BroadcastReceiver;


public class ThreadServer implements Runnable {

    private MainActivity activity;
    private BroadcastListener broadcastListener = null;

    private CrossOutList crossList = new CrossOutList();
    private CrossOutList outList = new CrossOutList();

    /**
     * Stops the server
     */
    public void stop(){

        try {
            BroadcastReceiver.getInstance().removeListener(broadcastListener);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }


    private void uiWriteLog(String msg){
        final String str = msg;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.writeLog(str);
            }
        });
    }

    private void uiWriteCross(int nb, String list){
        final int nbCross = nb;
        final String listCross = list;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.writeCross(nbCross, listCross);
            }
        });
    }

    private void uiWriteOut(int nb, String list){
        final int nbOut = nb;
        final String listOut = list;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.writeOut(nbOut, listOut);
            }
        });
    }


    /**
     * Create the server by listening on input packets and sending output packets.
     * @param activity App activity
     */
	public ThreadServer(final MainActivity activity) {
		this.activity = activity;

		try {
			BroadcastReceiver.getInstance().addListener(broadcastListener = new BroadcastListener() {
				@Override
				public void onBroadcastReceived(byte[] message) {

					String str = new String(message);
					String[] messageElements = str.split("/");

                    String fromType = messageElements[0];

					if( fromType.equals("SLAVE") ){

                        String robotFrom = messageElements[1];
                        int query = Integer.parseInt(messageElements[2]); // 0=cross, 1=update, 2=out

                        RobotState robot = new RobotState(robotFrom,
                                                        Float.parseFloat(messageElements[3]), // position
                                                        Float.parseFloat(messageElements[4]) // speed
                                                );

                        onSlaveMessageReceived(query, robot);

                    }
				}
			});

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}


    private void onSlaveMessageReceived(int query, RobotState robot){
        switch(query){
            case 0: // Cross
                cross(robot);
                break;

            case 1: // Update
                update(robot);
                break;

            case 2: // Out
                out(robot);
                break;
        }

        sendListToSlaves(crossList);

        uiWriteCross(crossList.size(), crossList.robotsNameString());
        uiWriteOut(outList.size(), outList.robotsNameString());
    }



	private void cross(RobotState robot){
        crossList.add(robot);
        outList.removeByName(robot.getName());
        uiWriteLog("["+robot.getName()+"] Cross");
	}

	private void update(RobotState robot){
        crossList.add(robot);
        uiWriteLog("["+robot.getName()+"] Update");
	}


	private void out(RobotState robot){
        crossList.removeByName(robot.getName());
        outList.add(robot);
        uiWriteLog("["+robot.getName()+"] Out");
	}


    private void sendListToSlaves(CrossOutList list){

        MessageBuilder message = new MessageBuilder("SERVER", "ANY", list);

        try {
            uiWriteLog("[SERVER] " + message.toString());
            BroadcastManager.getInstance().broadcast(message.toString().getBytes());
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launches server
     */
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            /*
            // Development feature
            try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

            try {
                BroadcastManager.getInstance().broadcast(("SLAVE/rob1/0/0.0/0.4").getBytes());
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */

        }

	}

}
