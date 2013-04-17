package util;

import android.database.sqlite.SQLiteDatabase;

public class DBUtil {
    public static SQLiteDatabase getDBInstance(String dbPath){
        return SQLiteDatabase.openOrCreateDatabase(dbPath, null);
    }
    //public static void 

}
