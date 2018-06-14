package com.example.irvin.trucktransport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.irvin.trucktransport.dataaccess.DataAccess;
import com.example.irvin.trucktransport.dataaccess.DatabaseConstants;

import java.sql.SQLException;

/**
 * Created by IvanX on 19.5.2018..
 */

public class CheckInOutController extends DataAccess {

    private static final String TAG = CheckInOutController.class.getSimpleName();

    public CheckInOutController(Context context){
        super(context);
    }

    public void updateCheckInZadatak(String checkInZadatak, String zadatak_id){
        ContentValues values = new ContentValues();

        values.put(DatabaseConstants.ZADATAK_CHECKIN, checkInZadatak);

        try {
            updateField(DatabaseConstants.TABLE_ZADACI, values, DatabaseConstants.ZADATAK_ID+"="+zadatak_id);
        }catch (android.database.SQLException e){
            Log.e(TAG, "SQL error: " + e.getMessage());
        }
    }
    public void updateCheckOutZadatak(String checkOutZadatak, String zadatak_id){
        ContentValues values = new ContentValues();

        values.put(DatabaseConstants.ZADATAK_CHECKOUT, checkOutZadatak);

        try {
            updateField(DatabaseConstants.TABLE_ZADACI, values, DatabaseConstants.ZADATAK_ID+"="+zadatak_id);
        }catch (android.database.SQLException e){
            Log.e(TAG, "SQL error: " + e.getMessage());
        }
    }
    public void updateCheckInStajaliste(String checkInStajaliste, String stajaliste_nalog_id){
        ContentValues values = new ContentValues();

        values.put(DatabaseConstants.SN_CHECKIN, checkInStajaliste);

        try {
            updateField(DatabaseConstants.TABLE_STAJALISTANALOZI, values, DatabaseConstants.STAJALISTE_NALOG_ID+"="+stajaliste_nalog_id);
        }catch (android.database.SQLException e){
            Log.e(TAG, "SQL error: " + e.getMessage());
        }
    }
    public void updateCheckOutStajaliste(String checkOutStajaliste, String stajaliste_nalog_id){
        ContentValues values = new ContentValues();

        values.put(DatabaseConstants.SN_CHECKOUT, checkOutStajaliste);

        try {
            updateField(DatabaseConstants.TABLE_STAJALISTANALOZI, values, DatabaseConstants.STAJALISTE_NALOG_ID+"="+stajaliste_nalog_id);
        }catch (android.database.SQLException e){
            Log.e(TAG, "SQL error: " + e.getMessage());
        }
    }
}
