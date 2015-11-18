package app.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class JenApplication extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        setupApplicationList();
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
