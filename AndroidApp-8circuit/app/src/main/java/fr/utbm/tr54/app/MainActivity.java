package fr.utbm.tr54.app;

import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Method;

import fr.utbm.tr54.server.ThreadServer;

public class MainActivity extends AppCompatActivity {

    private PowerManager.WakeLock wl;
    private boolean serverStarted = false;

    private Thread server;
    private ThreadServer threadServer;

    private TextView serverLog;
    private Button buttonStartStop;
    private ScrollView serverLogScrollView;
    private TextView labelCross;
    private TextView labelOut;
    private TextView labelCrossList;
    private TextView labelOutList;


    private boolean isSharingWiFi(){
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        try
        {
            final Method method = wifi.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifi);
        } catch (final Throwable ignored){}

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if( !isSharingWiFi() ){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.dialog_title_disabled_ap)
                    .setMessage(R.string.dialog_message_disabled_ap)
                    .setCancelable(false)
                    .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MainActivity.this.finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }


        // Wake lock
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Tag");
        wl.acquire();


        serverLog = (TextView) findViewById(R.id.server_logs);
        buttonStartStop = (Button) findViewById(R.id.button_start_stop);
        serverLogScrollView = (ScrollView) findViewById(R.id.server_log_scrollview);

        labelCross = (TextView) findViewById(R.id.label_nb_crossing);
        labelOut = (TextView) findViewById(R.id.label_nb_out);

        labelCrossList = (TextView) findViewById(R.id.label_list_cross);
        labelOutList = (TextView) findViewById(R.id.label_list_out);


        buttonStartStop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverStartStop();
            }
        });

    }


    private void serverStartStop(){
        if( !serverStarted ){
            writeLog(getString(R.string.starting_server));
            threadServer = new ThreadServer(MainActivity.this);
            server = new Thread(threadServer);
            server.start();
            writeLog(getString(R.string.server_started));
            buttonStartStop.setText(R.string.stop_server);
            buttonStartStop.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_stop_black, 0, 0, 0);

            serverStarted = true;
        } else {
            writeLog(getString(R.string.stopping_server));
            threadServer.stop();
            server.interrupt();

            writeLog(getString(R.string.server_stopped)+"\r\n");
            buttonStartStop.setText(R.string.start_server);
            buttonStartStop.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_play_arrow_black, 0, 0, 0);

            serverStarted = false;
        }

    }


    /**
     * Writes a log line in text view of the main activity
     * @param msg Line to add in the log
     */
    public void writeLog(String msg){
        serverLog.append("\n"+msg);

        // Avoids flooding the log.
        String serverLogText = serverLog.getText().toString();
        int count = serverLogText.length() - serverLogText.replace("\n", "").length();
        if( count > 300 ){
            serverLog.setText(serverLogText.substring(serverLogText.length()/2));
        }

        serverLogScrollView.post(new Runnable() {
            @Override
            public void run() {
                serverLogScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

    }

    /**
     * Updates the "Out" card of the main activity (number and list)
     * @param nb Number to set
     * @param list List to set
     */
    public void writeOut(int nb, String list){
        labelOut.setText(new StringBuilder().append(nb).toString());
        labelOutList.setText(list);
    }

    /**
     * Updates the "Cross" card of the main activity (number and list)
     * @param nb Number to set
     * @param list List to set
     */
    public void writeCross(int nb, String list){
        labelCross.setText(new StringBuilder().append(nb).toString());
        labelCrossList.setText(list);
    }


    protected void onDestroy() {
        super.onDestroy();

        if(serverStarted){
            serverStartStop();
        }

        wl.release();
    }

}
