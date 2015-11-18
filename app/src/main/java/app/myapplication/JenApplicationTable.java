package app.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JenApplicationTable extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "_pouch_application";
    public static final String TABLE_NAME = "[by-sequence]";

    private static String SQL_CREATE_ENTRIES = "CREATE TABLE '"+JenApplicationTable.TABLE_NAME
            +"' (seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, json, deleted TINYINT(1), doc_id, rev);";
    private static String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+JenApplicationTable.TABLE_NAME;

    public JenApplicationTable(Context context) {
        super(context, DATABASE_NAME , null, 1);
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
        SQLiteDatabase db = this.getReadableDatabase();
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
        }else{
            arr = new String[0];
        }
        return arr;
    }

    public boolean addApplication(){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("json", "\"{1:2}\"");
        Long newRow = db.insert(JenApplicationTable.TABLE_NAME, null, cv);
        Log.e("test", newRow.toString());

        JSONObject jo = new JSONObject();
        try {
            jo.put("test1", "test1");
            jo.put("test2", "test2");
            jo.put("test3", "test3");

            ContentValues cv2 = new ContentValues();
            cv.put("json", jo.toString());
            cv.put("deleted", "0");
            cv.put("doc_id", "application");

            Long rowNumber = db.insert(JenApplicationTable.TABLE_NAME, null, cv);
            Log.e("test", rowNumber.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
}
