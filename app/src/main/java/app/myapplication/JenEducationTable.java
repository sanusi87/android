package app.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JenEducationTable extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "_pouch_education";
    public static final String TABLE_NAME = "by-sequence";
    public static final String TABLE_NAME2 = "[by-sequence]";

    private static String SQL_CREATE_ENTRIES = "CREATE TABLE '"+JenEducationTable.TABLE_NAME
            +"' (seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, json, deleted TINYINT(1), doc_id, rev);";
    private static String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS '"+JenEducationTable.TABLE_NAME+"'";

    private static SQLiteDatabase db;
    private static Context context;

    public JenEducationTable(Context context) {
        super(context, DATABASE_NAME , null, 1);
        this.context = context;
        this.db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public static String[] getEducation(){
        //SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM [by-sequence] WHERE seq IN ("
                +"SELECT MAX(seq) FROM [by-sequence] WHERE deleted=0 AND doc_id NOT IN("
                +"SELECT doc_id FROM [by-sequence] WHERE deleted=1) GROUP BY doc_id)", null);

        String[] arr;
        if( c != null ){
            arr = new String[c.getCount()];
            int i=0;
            c.moveToFirst();
            while( !c.isAfterLast() ){
                Log.e("edu", c.getString(1));
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

    public boolean addEducation(){
        JSONObject jo = new JSONObject();
        ContentValues cv2 = new ContentValues();

        /*
		[
		    {
		        "id":792080,
		        "level":{
		            "id":1,
		            "name":"Primary School/UPSR"
		        },
		        "field":{
		            "id":46,
		            "name":"Others"
		        },
		        "country":null,
		        "school":"SKBN",
		        "major":null,
		        "grade":null,
		        "date_graduated":"1999-01-01",
		        "info":null,
		        "_id":"71F73105-FA3F-0EDD-870F-C2DA9EE2B5E0",
		        "_rev":"1-fc9cdf57251569d8f68656aa294ff0ea"
		    }
	    ]
        */

        try {
            jo.put("id", 792080);
            jo.put("level", "{\"id\":1,\"name\":\"Primary School/UPSR\"}");
            jo.put("field", "{\"id\":46,\"name\":\"Others\"}");
            jo.put("country", null);
            jo.put("school", "SKBN");
            jo.put("major", null);
            jo.put("grade", null);
            jo.put("date_graduated", "1999-01-01");
            jo.put("info", null);
            jo.put("_id", "71F73105-FA3F-0EDD-870F-C2DA9EE2B5E0");
            jo.put("_rev", "1-fc9cdf57251569d8f68656aa294ff0ea");

        } catch (JSONException e) {
            //e.printStackTrace();
            Log.e("jsonExp", e.getLocalizedMessage());
        }

        cv2.put("json", jo.toString());
        cv2.put("deleted", "0");
        cv2.put("doc_id", "application");

        Long rowNumber = db.insert(JenApplicationTable.TABLE_NAME2, null, cv2);
        Log.e("test", rowNumber.toString());

        return false;
    }

    public static void startSynchronisation(String accessToken) {
        // get saved education
        String[] edu = JenEducationTable.getEducation();

        // download education
        Log.e("accessToken", accessToken);
        JenHttpRequest req = new JenHttpRequest(JenHttpRequest.GET_REQUEST, "http://api.jenjobs.com/jobseeker/qualification?access-token="+accessToken, null);
        int i=0;
        while( req.response != null ){
            Log.e("response", ""+i);
            i++;
        }
        Log.e("endwhile", ""+i);
    }
}
