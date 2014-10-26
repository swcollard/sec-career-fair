/**
 * SECDataSource.java
 * @author Samuel Collard
 * 
 * Handles all interaction with the database
 */
package mobile.SEC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SECDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private static final String[] SEC_COLUMNS = { MySQLiteHelper.COLUMN_DATETIME,
            MySQLiteHelper.COLUMN_ABOUT, MySQLiteHelper.COLUMN_ITINERARY };
    private static final String[] SAVED_COMPANIES_COLUMNS = 
            { MySQLiteHelper.COLUMN_GUID, MySQLiteHelper.COLUMN_NAME };

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
    public long updateSecDB(long dateTime, String about, String itinerary) {
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
                SEC_COLUMNS, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return 0;
        } else {
            long date = cursor.getLong(
                    cursor.getColumnIndex(MySQLiteHelper.COLUMN_DATETIME));
            cursor.close();
            return date;
        }
    }

    public String getAbout() {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SEC,
                SEC_COLUMNS, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return "";
        } else {
            String about = cursor.getString(
                    cursor.getColumnIndex(MySQLiteHelper.COLUMN_ABOUT));
            cursor.close();
            return about;
        }
    }

    public String getItinerary() {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SEC, 
                SEC_COLUMNS, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return "";
        } else {
            String itinerary = cursor.getString(
                    cursor.getColumnIndex(MySQLiteHelper.COLUMN_ITINERARY));
            cursor.close();
            return itinerary;
        }
    }

    public long saveCompany(Company company) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_GUID, company.getGuid());
        values.put(MySQLiteHelper.COLUMN_NAME, company.getName());
        long insertId = database.insert(MySQLiteHelper.TABLE_SAVED_COMPANIES, null, values);
        return insertId;
    }

    public int removeCompany(Company company) {
        return database.delete(MySQLiteHelper.TABLE_SAVED_COMPANIES,
                MySQLiteHelper.COLUMN_GUID + "=?", new String[] { company.getGuid() });
    }
    public List<Company> getSavedCompanies() {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SAVED_COMPANIES,
                SAVED_COMPANIES_COLUMNS, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return Collections.emptyList();
        }
        ArrayList<Company> companies = new ArrayList<Company>(cursor.getCount());
        while (!cursor.isAfterLast()) {
            companies.add(new Company(
                    cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_GUID)),
                    cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_NAME))));
            cursor.moveToNext();
        }
        cursor.close();
        return companies;
    }

    public boolean isSaved(String[] guid) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SAVED_COMPANIES,
                SAVED_COMPANIES_COLUMNS, MySQLiteHelper.COLUMN_GUID + "=?", guid,
                null, null, null);
        boolean retVal = cursor.moveToFirst();
        cursor.close();
        return retVal;
    }
}
