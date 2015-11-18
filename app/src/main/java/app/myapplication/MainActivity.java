package app.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("test", "starting service...");
        Intent intent = new Intent();
        intent.putExtra("test", "test2");
        intent.setClass(this.getApplicationContext(), MyService.class);
        startService(intent);

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

            }
        });
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
    public void onPause() {
        super.onPause();  // Always call the superclass method first

    }





    public void applicationAlert(){
        int status = 0;
        String s = null;
        if( status == 0 ){
            s = "COMPANY has reviewed your application.";
        }else{
            s = "COMPANY has rejected your application.";
        }

        Bundle b = null;
        b.putString("company", "COMPANY"); // company
        b.putInt("employer_id", 0); // company id
        b.putInt("application_id", 0); // application id
        b.putInt("post_id", 0); // job post id

        Intent intent = new Intent(this, JenApplication.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationCompat.Builder n = new NotificationCompat.Builder(getApplicationContext());
        //n.addAction();
        n.setContentTitle(getResources().getString(R.string.application_responded_title));
        n.setContentText(s);
        n.setSmallIcon(R.drawable.icon);
        n.setExtras(b);
        n.build();

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify();
    }
}
