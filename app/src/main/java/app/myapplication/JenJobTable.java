package app.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JenJobTable extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "_pouch_job";
    public static final String TABLE_NAME = "by-sequence";
    public static final String TABLE_NAME2 = "[by-sequence]";

    private static String SQL_CREATE_ENTRIES = "CREATE TABLE '"+JenJobTable.TABLE_NAME
            +"' (seq INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, json, deleted TINYINT(1), doc_id, rev);";
    private static String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS '"+JenJobTable.TABLE_NAME+"'";

    public JenJobTable(Context context) {
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

    public String[] getJob(){
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

}
