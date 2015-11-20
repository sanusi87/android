package app.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;

public class JenService extends Service {

    public static boolean NETWORK_STATUS = false;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        handleCommand(intent);
        return START_STICKY;
    }

    private void handleCommand(final Intent intent) {
        Log.e("test", "service started...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("test", "on different thread...");
                // sending request using intent as data
                // new JenHttpRequest("http://api.jenjobs.com/jobs/android", intent);

                JenApplicationTable jadb = new JenApplicationTable(getApplicationContext());
                //jadb.addApplication();

                String[] application = jadb.getApplication();
                for(String app: application){
                    Log.e("test", app);

                    try {
                        JSONObject jo = new JSONObject(app);
                        Iterator<String> it = jo.keys();

                        while( it.hasNext() ){
                            String itt = it.next();
                            Log.e("test", "itt="+itt);
                            Log.e("test", "value="+jo.getString(itt));
                        }
                    } catch (JSONException e) {
                        Log.e("test", e.getMessage());
                    }
                }


                // ------
                /*
                SQLiteConnection db = new SQLiteConnection(new File("_pouch_application"));
                try {
                    db.open(true);

                    SQLiteStatement st = db.prepare("SELECT seq FROM [by-sequence] WHERE deleted=0");
                    try {
                        //st.bind(1, minimumQuantity);
                        while (st.step()) {
                            //orders.add(st.columnLong(0));
                            Log.e("test", String.valueOf(st.columnLong(0)));
                        }
                    } finally {
                        st.dispose();
                    }

                    db.dispose();
                } catch (SQLiteException e) {
                    e.printStackTrace();
                }
                */
                // ------

            };
        }).start();
    }


}
