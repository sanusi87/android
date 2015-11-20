package app.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class JenApplicationTable extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "_pouch_application";
    public static final String TABLE_NAME = "by-sequence";
    public static final String TABLE_NAME2 = "[by-sequence]";

    private static String SQL_CREATE_ENTRIES = "CREATE TABLE '"+JenApplicationTable.TABLE_NAME
            +"' (seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, json, deleted TINYINT(1), doc_id, rev);";
    private static String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS '"+JenApplicationTable.TABLE_NAME+"'";

    public static SQLiteDatabase db;

    public JenApplicationTable(Context context) {
        super(context, DATABASE_NAME , null, 1);
        db = this.getReadableDatabase();
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

    public String[] getApplication(){
        //SQLiteDatabase db = this.getReadableDatabase();
        //db.execSQL("SELECT * FROM "+SEQUENCE+" WHERE deleted=0 AND doc_id=?", new String[]{APPLICATION});
        Cursor c = db.rawQuery("SELECT * FROM [by-sequence] WHERE seq IN ("
                +"SELECT MAX(seq) FROM [by-sequence] WHERE deleted=0 AND doc_id NOT IN("
                +"SELECT doc_id FROM [by-sequence] WHERE deleted=1) GROUP BY doc_id)", null);

        String[] arr;
        if( c != null ){
            arr = new String[c.getCount()];
            int i=0;
            c.moveToFirst();
            while( !c.isAfterLast() ){
                //Log.e("test", "c="+c.getString(1));
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

    // check MainActivity.setupTable()
    public boolean addApplication(){
        //SQLiteDatabase db = this.getReadableDatabase();
        //ContentValues cv = new ContentValues();
        //cv.put("json", "\"{1:2}\"");
        //Long newRow = db.insert(JenApplicationTable.TABLE_NAME, null, cv);
        //Log.e("test", newRow.toString());

        JSONObject jo = new JSONObject();
        try {
            jo.put("test1", "test1");
            jo.put("test2", "test2");
            jo.put("test3", "test3");

            ContentValues cv2 = new ContentValues();
            cv2.put("json", jo.toString());
            cv2.put("deleted", "0");
            cv2.put("doc_id", "application");

            Long rowNumber = db.insert(JenApplicationTable.TABLE_NAME2, null, cv2);
            Log.e("test", rowNumber.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * GET
     {
         "id": 5389402,
         "post_id": 320159,
         "_id": 320159,
         "status": 0,
         "date_created": "2015-03-16 13:25:54",
         "date_updated": "2015-03-16 13:25:54",
         "title": "PHP Guru",
         "closed": true
     }
     *
     * POST
     {

     }
     * */

    public static void startSynchronisation(String token) {
        // download applications
        JenHttpRequest jenReq = new JenHttpRequest(JenHttpRequest.GET_REQUEST, "http://api.jenjobs.com/jobseeker/application?access-token="+token, null);
        Log.e("response", jenReq.response.toString());

        if( jenReq.response != null ){

        }

    }
}
