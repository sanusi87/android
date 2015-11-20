package app.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    LocalService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start service to check new application
        /*
        Log.e("test", "starting service...");
        Intent intent = new Intent();
        intent.putExtra("test", "test2");
        intent.setClass(this.getApplicationContext(), JenService.class);
        startService(intent);
        */

        TextView tv = (TextView) findViewById(R.id.string_one);
        tv.setText("warld");

        // button to open application activity
        Button applicationButton = (Button) findViewById(R.id.getApplication);
        applicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), JenApplication.class);
                startActivity(intent);
            }
        });

        // button to test notification
        Button testNotificationButton = (Button) findViewById(R.id.testNotification);
        testNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applicationAlert();
            }
        });

        // button to test bound service
        Button testBoundService = (Button)findViewById(R.id.testBound);
        testBoundService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    // Call a method from the LocalService.
                    // However, if this call were something that might hang, then this request should
                    // occur in a separate thread to avoid slowing down the activity performance.
                    int num = mService.getRandomNumber();
                    Toast.makeText(getApplicationContext(), "number: " + num, Toast.LENGTH_SHORT).show();
                }

                // test db
                JenProfileTable jpt = new JenProfileTable(getApplicationContext());
                String[] profile = jpt.getProfile();

            }
        });

        setupTable();
    }

    // setup table for testing
    private void setupTable() {
        new JenApplicationTable(getApplicationContext()).addApplication();
        new JenSettingsTable(getApplicationContext()).addSettings();
        new JenTokenTable(getApplicationContext()).addNewToken();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.e("test", "app started...");

        // Bind to LocalService
        //Intent intent = new Intent(this, LocalService.class);

        // binding service
        //bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        Intent intent2 = new Intent();
        intent2.putExtra("test", "test2");
        intent2.setClass(this.getApplicationContext(), JenService.class);
        startService(intent2);

        JenService.NETWORK_STATUS = isNetworkAvailable();
        Log.e("test", "network status="+JenService.NETWORK_STATUS);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        Log.e("test", "app paused, begin synchronisation...");
        // start service to synchronize

    }

    @Override
    public void onStop(){
        super.onStop();
        Log.e("test", "app stopped...");
        // start service to synchronize

        if (mBound) {
            // unbinding service
            unbindService(mConnection);
            mBound = false;
            Log.e("test", "unbind service...");
        }
    }



    public void applicationAlert(){
        int status = 0;
        String s = null;
        if( status == 0 ){
            s = "COMPANY has reviewed your application.";
        }else{
            s = "COMPANY has rejected your application.";
        }

        Bundle b = new Bundle();
        b.putString("company", "COMPANY"); // company
        b.putInt("emp_profile_id", 0); // company id
        b.putInt("application_id", 0); // application id
        b.putInt("post_id", 0); // job post id

        Intent intent = new Intent(this, JenApplication.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification n = new NotificationCompat.Builder(this)
        .setContentTitle(getResources().getString(R.string.application_responded_title))
        .setContentText(s)
        .setSmallIcon(R.drawable.icon)
        .setExtras(b)
        .setContentIntent(pIntent)
        .setAutoCancel(true)
        .build();

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0,n);
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Log.e("test", "bound service connected...");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            Log.e("test", "bound service disconnected...");
        }
    };


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
