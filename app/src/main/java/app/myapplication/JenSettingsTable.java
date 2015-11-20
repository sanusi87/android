package app.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class JenSettingsTable extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "_pouch_settings";
    public static final String TABLE_NAME = "by-sequence";
    public static final String TABLE_NAME2 = "[by-sequence]";

    // list of doc_id
    public static final String COMPLETENESS = "completeness";
    public static final String NOTIFICATION_ALERT = "notification_alert";
    public static final String NEWSLETTER_ALERT = "newsletter_alert";
    public static final String PROMOTION_ALERT = "promotion_alert";
    public static final String SMS_JOB_ALERT = "sms_job_alert";
    public static final String ATTACHED_RESUME = "attachedResume";
    public static final String INFO = "info";
    public static final String JOB_PREFERENCE = "jobPref";
    public static final String SYNC_REQUIRED = "sync_required";

    private static String SQL_CREATE_ENTRIES = "CREATE TABLE '"+JenSettingsTable.TABLE_NAME
            +"' (seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, json, deleted TINYINT(1), doc_id, rev);";
    private static String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS '"+JenSettingsTable.TABLE_NAME+"'";

    public static SQLiteDatabase db;
    private static Context context;

    public JenSettingsTable(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.e("create", SQL_CREATE_ENTRIES);
        db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db2) {
        Log.e("on_create", SQL_CREATE_ENTRIES);
        db2.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {
        db2.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db2);
    }

    public String[] getSettings(){
        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM [by-sequence] WHERE seq IN ("
                +"SELECT MAX(seq) FROM [by-sequence] WHERE deleted=0 AND doc_id NOT IN("
                +"SELECT doc_id FROM [by-sequence] WHERE deleted=1) GROUP BY doc_id)", null);

        String[] arr;
        if( c != null ){
            arr = new String[c.getCount()];
            int i=0;
            c.moveToFirst();
            while( !c.isAfterLast() ){
                arr[i] = c.getString(1);
                c.moveToNext();
                i++;
            }
            c.close();
        }else{
            arr = new String[0];
        }
        return arr;
    }

    public String[] getSettingsById( String doc_id ){
        // SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM [by-sequence] WHERE seq IN ("
                +"SELECT MAX(seq) FROM [by-sequence] WHERE deleted=0 AND doc_id='"+doc_id+"')", null);

        String[] arr;
        if( c != null ){
            arr = new String[c.getCount()];
            int i=0;
            c.moveToFirst();
            while( !c.isAfterLast() ){
                arr[i] = c.getString(1);
                c.moveToNext();
                i++;
            }
            c.close();
        }else{
            arr = new String[0];
        }
        return arr;
    }


    public static ArrayList<BasicNameValuePair> checkSyncRequired(){
        Cursor c = db.rawQuery("SELECT * FROM [by-sequence] WHERE seq IN (SELECT MAX(seq) FROM [by-sequence] WHERE deleted=0 AND doc_id='sync_required')", null);
        if( c != null ){
            c.moveToFirst();
            String str = "";
            while( !c.isAfterLast() ){
                str = c.getString(1);
                c.moveToNext();
            }
            c.close();

            Log.e("test", "str="+str);

            try {
                JSONObject jo = new JSONObject(str);

                ArrayList<BasicNameValuePair> arr = new ArrayList<BasicNameValuePair>();
                Iterator<String> it = jo.keys();
                while( it.hasNext() ){
                    String key = it.next();

                    String value = jo.getString(key);
                    Boolean _value = jo.getBoolean(key);
                    Log.e("test", "key="+key+",value="+value);
                    Log.e("test", "boolean="+jo.getBoolean(key) );

                    // if require sync is TRUE, then we add into the array
                    if( _value ){
                        arr.add(new BasicNameValuePair(key, value));
                        Log.e("test", "isBool=true");
                    }else{
                        Log.e("test", "isBool=false");
                    }
                }
                return arr;
            } catch (JSONException e) {
                Log.e("test", e.getMessage());
            }
        }else{
            Log.e("test", "c=null");
        }
        return null;
    }

    public static boolean startSynchronisation(){
        Log.e("test", "sync started");
        ArrayList<BasicNameValuePair> arr = JenSettingsTable.checkSyncRequired();
        Log.e("test", "arr="+arr);
        if( arr != null ){
            // get saved access token
            JenTokenTable jtt = new JenTokenTable(context);
            String[] token = jtt.getToken();

            if( token != null ){
                String accessToken = token[0];

                for( BasicNameValuePair value:arr ){
                    if( value.getName() == "workExp" ){
                        //JenWorkTable.startSynchronisation(accessToken);
                    }else if( value.getName() == "education" ){
                        //JenEducationTable.startSynchronisation(accessToken);
                    }else if( value.getName() == "profile" ){
                        //JenProfileTable.startSynchronisation(accessToken);
                    }else if( value.getName() == "jobseek" ){

                    }else if( value.getName() == "jobPref" ){

                    }else if( value.getName() == "attachment" ){

                    }else if( value.getName() == "language" ){
                        //JenLanguageTable.startSynchronisation(accessToken);
                    }else if( value.getName() == "skill" ){
                        //JenSkillTable.startSynchronisation(accessToken);
                    }else if( value.getName() == "additionalinfo" ){

                    }else if( value.getName() == "application" ){
                        JenApplicationTable.startSynchronisation(accessToken);
                    }else if( value.getName() == "bookmark" ){
                        //JenBookmarkTable.startSynchronisation(accessToken);
                    }
                }
            }else{
                Log.e("test", "no access token");
            }

            //String url = "";
            //Intent intent = new Intent();
            //JenHttpRequest jr = new JenHttpRequest(url, intent);

            return true;
        }else{
            Log.e("test", "null");
        }

        return false;
    }

    public void addSettings() {
        ContentValues cv = new ContentValues();
        JSONObject jo = new JSONObject();

        try {
            /*
                "workExp":false,
                "education":false,
                "profile":false,
                "jobseek":false,
                "jobPref":false,
                "attachment":false,
                "language":false,
            */

            jo.put("workExp", false);
            jo.put("education", true);
            jo.put("profile", false);
            jo.put("jobseek", true);
            jo.put("jobPref", false);
            jo.put("attachment", true);
            jo.put("language", false);

        } catch (JSONException e) {
            Log.e("test", e.getMessage());
        }

        cv.put("json", jo.toString());
        cv.put("deleted", "0");
        cv.put("doc_id", "sync_required");
        cv.put("rev", "1-6f741f3d7727c76bbd38cd453a9e6398");

        db.insert(JenSettingsTable.TABLE_NAME2, null, cv);
    }
}
