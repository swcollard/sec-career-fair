/**
 * MySQLiteHelper.java
 * @author Samuel Collard
 * 
 * Extends SQLiteOpenHelper
 * Based on code from http://www.vogella.com/articles/AndroidSQLite/article.html
 */
package mobile.SEC;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // General info table
    public static final String TABLE_SEC = "sec";
    public static final String COLUMN_ABOUT = "about";
    public static final String COLUMN_ITINERARY = "itinerary";
    public static final String COLUMN_DATETIME = "date";

    // Saved companies table
    public static final String TABLE_SAVED_COMPANIES = "savedcompanies";
    public static final String COLUMN_GUID = "guid";
    public static final String COLUMN_NAME = "name";

    private static final String DATABASE_NAME = "sec.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statements
    private static final String SEC_TABLE_CREATE = "create table "
        + TABLE_SEC + "(" + COLUMN_DATETIME
        + " integer primary key, " + COLUMN_ABOUT
        + " text not null, " + COLUMN_ITINERARY
        + " text not null);";

    private static final String SAVED_COMPANIES_TABLE_CREATE = "create table "
            + TABLE_SAVED_COMPANIES + "(" + COLUMN_GUID
            + " text primary key, " + COLUMN_NAME
            + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SEC_TABLE_CREATE);
        database.execSQL(SAVED_COMPANIES_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(MySQLiteHelper.class.getSimpleName(),
            "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_COMPANIES);
        onCreate(db);
    }
} 
