package com.example.irvin.trucktransport.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by IvanX on 28.6.2017..
 */

public class DataAccess extends DatabaseHelper {

    static final String TAG = DataAccess.class.getSimpleName();

    private SQLiteDatabase mDatabase;

    public DataAccess(Context context){
        super(context);
    }

    public void open(){
        try{
            mDatabase = this.getWritableDatabase();
        }catch (Exception e){
            Log.e(TAG, "Database cannot be opened for writing. Error: " + e.getMessage());
        }
    }

    public void openForReading(){
        try{
            mDatabase = this.getReadableDatabase();
        }catch (SQLiteException e){
            Log.e(TAG, "Database cannot be opened for reading. Error: " + e.getMessage());
        }
    }

    public void close(){
        super.close();
    }

    /*
        All CRUD (Create, Update, Delete) Operations
    */

    protected void updateField(String tableName, ContentValues values, String where){
        open();
        mDatabase.update(tableName, values, where, null);
    }

    protected void insertOrReplaceRow(String tableName, ContentValues values) throws SQLiteException{
        open();
        //mDatabase.insertOrThrow(tableName, null, values);
        mDatabase.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    protected void deleteRow(String tableName, String columnName, String columnValue){
        open();
        mDatabase.delete(tableName, columnName + " = ?", new String[]{columnValue});
        close();
    }

    protected void deleteAll(String tableName) {
        open();
        try {
            mDatabase.execSQL("DELETE FROM " + tableName);
        } catch (android.database.SQLException e) {
            Log.e(DataAccess.class.getSimpleName(), "Invalid SQL String: " + e.getMessage());
        }

        close();
    }

    protected Cursor getRow(String selectQuery) {
        open();
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    protected Cursor getRowById(String selectQuery, int id) {
        open();
        Cursor cursor = mDatabase.rawQuery(selectQuery + " WHERE id=" + id, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    protected Cursor getAllRows(String tableName) {
        open();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + tableName, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    protected Cursor getAllRowsById(String tableName, String id) {
        open();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + tableName + " WHERE id=" + id, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    protected Cursor getAllRowsWhere(String tableName, String where) {
        open();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + tableName + " WHERE " + where, null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

    protected Cursor getLastRow(String tableName){
        open();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + tableName + " ORDER BY " + DatabaseConstants.KEY_ID + " DESC LIMIT 1", null);

        if (cursor != null)
            cursor.moveToFirst();

        return cursor;
    }

}
