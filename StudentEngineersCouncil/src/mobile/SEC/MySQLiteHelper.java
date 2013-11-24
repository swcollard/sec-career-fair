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

  public static final String TABLE_SEC = "sec";
  public static final String COLUMN_ABOUT = "about";
  public static final String COLUMN_ITINERARY = "itinerary";
  public static final String COLUMN_DATETIME = "date";

  private static final String DATABASE_NAME = "sec.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_SEC + "(" + COLUMN_DATETIME
      + " integer primary key, " + COLUMN_ABOUT
      + " text not null, " + COLUMN_ITINERARY
      + " text not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEC);
    onCreate(db);
  }
} 