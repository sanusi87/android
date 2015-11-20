package app.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JenTokenTable extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "_pouch_token";
    public static final String TABLE_NAME = "by-sequence";
    public static final String TABLE_NAME2 = "[by-sequence]";

    private static String SQL_CREATE_ENTRIES = "CREATE TABLE '"+JenTokenTable.TABLE_NAME
            +"' (seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, json, deleted TINYINT(1), doc_id, rev);";
    private static String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS '"+JenTokenTable.TABLE_NAME+"'";

    private static SQLiteDatabase db;

    public JenTokenTable(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db2) {
        db2.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {
        db2.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db2);
    }

    public String[] getToken(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM [by-sequence] WHERE seq IN ("
                +"SELECT MAX(seq) FROM [by-sequence] WHERE deleted=0 AND doc_id NOT IN("
                +"SELECT doc_id FROM [by-sequence] WHERE deleted=1) GROUP BY doc_id)", null);

        String[] arr;
        if( c != null ){
            arr = new String[c.getCount()];
            int i=0;
            c.moveToFirst();
            while( !c.isAfterLast() ){
                Log.e("test", "profile["+i+"]=" + c.getString(1));
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


    public void addNewToken(){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        JSONObject jo = new JSONObject();
        /*
        {
            "access_token":"e266933a50a52949e1d263dbff8b3eb375dd9945",
            "expires_in":86400,
            "token_type":"Bearer",
            "scope":null,
            "refresh_token":"36ac42412d151c3cd738aa0ae8bff6d2697c5604"
        }
        */

        try {
            jo.put("access_token", "e266933a50a52949e1d263dbff8b3eb375dd9945");
            jo.put("expires_in", 86400);
            jo.put("token_type", "Bearer");
            jo.put("scope", null);
            jo.put("refresh_token", "36ac42412d151c3cd738aa0ae8bff6d2697c5604");
        } catch (JSONException e) {
            Log.e("test", e.getMessage());
        }

        cv.put("json", jo.toString());
        cv.put("deleted", "0");
        cv.put("doc_id", "8250A1E8-AB9A-56E1-A649-7030462F8127");
        cv.put("rev", "1-6f741f3d7727c76bbd38cd453a9e6398");

        db.insert(JenTokenTable.TABLE_NAME2, null, cv);
    }
}
