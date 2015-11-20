package app.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

public class NetworkBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkStateReceiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e(TAG, "Network connectivity change");

        Bundle extras = intent.getExtras();

        if (extras != null) {

            /*
            for(String key: extras.keySet()){
                Object value = extras.get(key);
                Log.e("test", "key="+key+",value="+value.toString());
            }
            */

            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnected()) {
                Log.e(TAG, "Network " + ni.getTypeName() + " connected");


                /*
                * start sync
                * 1 - update profile, resume etc
                * {
                    _id: 'sync_required',
                    workExp:false,
                    education:false,
                    profile:false,
                    jobseek:false,
                    jobPref:false,
                    attachment:false,
                    language: false,
                    skill: false,
                    additionalinfo: false,
                    application: false,
                    bookmark: false
                }

                * 2 - check applications status
                * 3 - check employers invitation
                *
                * */

                 // get token first
                //JenTokenTable token = new JenTokenTable(context);
                //String[] tokenList = token.getToken();

                // update network status status
                JenService.NETWORK_STATUS = true;

                JenSettingsTable.startSynchronisation();

            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.e(TAG, "There's no network connectivity");
                JenService.NETWORK_STATUS = false;
            }
        }else{
            //Log.e("test", "no intent");
        }

    }
}