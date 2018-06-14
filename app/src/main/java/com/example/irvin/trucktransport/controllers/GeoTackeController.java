package com.example.irvin.trucktransport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.example.irvin.trucktransport.dataaccess.DataAccess;
import com.example.irvin.trucktransport.dataaccess.DatabaseConstants;
import com.example.irvin.trucktransport.model.GeoTacka;
import com.example.irvin.trucktransport.model.Poruka;
import com.example.irvin.trucktransport.model.Vozac;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanX on 13.2.2018..
 */

public class GeoTackeController extends DataAccess{

    private static final String TAG = GeoTackeController.class.getSimpleName();

    public GeoTackeController(Context context){
        super(context);
    }

    public void addGeoTacka(GeoTacka geoTacka){
        ContentValues values = new ContentValues();

        values.put(DatabaseConstants.GEOTACKA_DUZINA, geoTacka.getDuzina());
        values.put(DatabaseConstants.GEOTACKA_SIRINA, geoTacka.getSirina());
        values.put(DatabaseConstants.GEOTACKA_VRIJEME, geoTacka.getVrijeme());
        values.put(DatabaseConstants.GEOTACKA_NALOG_ID, geoTacka.getNalog_id());

        try {
            insertOrReplaceRow(DatabaseConstants.TABLE_GEOTACKE, values);
        } catch (SQLException e) {
            Log.e(GeoTackeController.class.getSimpleName(), "SQL error: " + e.getMessage());
        }

    }

    public GeoTacka getLastGeoTacka(){

        GeoTacka geoTacka = new GeoTacka();
        Cursor cursor_geotacka = getLastRow(DatabaseConstants.TABLE_GEOTACKE);

        if (cursor_geotacka.getCount() > 0){

            geoTacka.setLokalna_id(cursor_geotacka.getString(0));
            geoTacka.setGeotacka_id(cursor_geotacka.getString(1));
            geoTacka.setDuzina(cursor_geotacka.getString(2));
            geoTacka.setSirina(cursor_geotacka.getString(3));
            geoTacka.setVrijeme(cursor_geotacka.getString(4));
            geoTacka.setNalog_id(cursor_geotacka.getString(5));

        }

        return geoTacka;
    }

    public List<GeoTacka> getAllGeoTacka(){

        List<GeoTacka> geoTacke = new ArrayList<>();
        Cursor cursor_geotacka = getAllRows(DatabaseConstants.TABLE_GEOTACKE);

        if (cursor_geotacka.getCount() > 0){

            for(int i=0; i<cursor_geotacka.getCount(); i++){
                GeoTacka geoTacka = new GeoTacka();
                geoTacka.setLokalna_id(cursor_geotacka.getString(0));
                geoTacka.setGeotacka_id(cursor_geotacka.getString(1));
                geoTacka.setDuzina(cursor_geotacka.getString(2));
                geoTacka.setSirina(cursor_geotacka.getString(3));
                geoTacka.setVrijeme(cursor_geotacka.getString(4));
                geoTacka.setNalog_id(cursor_geotacka.getString(5));
                geoTacke.add(geoTacka);
                cursor_geotacka.moveToNext();
            }
        }
        return geoTacke;
    }
}
