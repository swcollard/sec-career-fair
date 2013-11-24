/**
 * SECDataSource.java
 * @author Samuel Collard
 * 
 * Handles all interaction with the database
 */
package mobile.SEC;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SECDataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_DATETIME,
		  MySQLiteHelper.COLUMN_ABOUT, MySQLiteHelper.COLUMN_ITINERARY };

  public SECDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  /**
   * Clears DB and inserts new data into the table
   * @param dateTime
   * @param about
   * @param itinerary
   * @return -1 if there is an error
   */
  public long updateDB(long dateTime, String about, String itinerary) {
	  ContentValues values = new ContentValues();
	  values.put(MySQLiteHelper.COLUMN_DATETIME, dateTime);
	  values.put(MySQLiteHelper.COLUMN_ABOUT, about);
	  values.put(MySQLiteHelper.COLUMN_ITINERARY, itinerary);
	  database.delete(MySQLiteHelper.TABLE_SEC, MySQLiteHelper.COLUMN_DATETIME + " > 0 ", null);
	  long insertId = database.insert(MySQLiteHelper.TABLE_SEC, null, values);
	  return insertId;
  }
  
  public long getLastUpdated() {
	  Cursor cursor = database.query(MySQLiteHelper.TABLE_SEC, 
			  allColumns, null, null, null, null, null);
	  if (!cursor.moveToFirst()) {
		  return 0;
	  }
	  else {
		  long date = cursor.getLong(0);
		  cursor.close();
		  return date;
	  }
  }
  
  public String getAbout() {
	  Cursor cursor = database.query(MySQLiteHelper.TABLE_SEC, 
			  allColumns, null, null, null, null, null);
	  if (!cursor.moveToFirst()) {
		  return "";
	  }
	  else {
		  String about = cursor.getString(1);
		  cursor.close();
		  return about;
	  }
  }
  
  public String getItinerary() {
	  Cursor cursor = database.query(MySQLiteHelper.TABLE_SEC, 
			  allColumns, null, null, null, null, null);
	  if (!cursor.moveToFirst()) {
		  return "";
	  }
	  else {
		  String itinerary = cursor.getString(2);
		  cursor.close();
		  return itinerary;
	  }
  }
} 
