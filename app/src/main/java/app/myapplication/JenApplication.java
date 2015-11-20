package app.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class JenApplication extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        setupApplicationList();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if( extras != null ){
            String company = extras.getString("company");
            int emp_profile_id = extras.getInt("emp_profile_id");
            int application_id = extras.getInt("application_id");
            int post_id = extras.getInt("post_id");


        }else{

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //JenService.NETWORK_STATUS = isNetworkAvailable();
        Log.e("test", "network status=" + JenService.NETWORK_STATUS);
    }

    private void setupApplicationList(){
        ListView applications = (ListView)findViewById(R.id.applicationList);
        JenApplicationAdapter jaa = new JenApplicationAdapter(getApplicationContext());
        applications.setAdapter(jaa);

        //Log.e("test", String.valueOf(jaa.getCount()));

        TextView noApplication = (TextView)findViewById(R.id.no_application);

        if(Integer.valueOf(jaa.getCount()) == 0){
            applications.setVisibility(View.GONE);
            noApplication.setVisibility(View.VISIBLE);
        }

        //if( Integer.valueOf(jaa.getCount()) == 0 ){
            //Log.e("test", "===0");
        //}else{
            //Log.e("test", "===!0");
        //}

        //Log.e("test", "visibility? "+String.valueOf(noApplication.getVisibility()));

    }


}
