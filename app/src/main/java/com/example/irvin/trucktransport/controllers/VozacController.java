package com.example.irvin.trucktransport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.example.irvin.trucktransport.dataaccess.DataAccess;
import com.example.irvin.trucktransport.dataaccess.DatabaseConstants;
import com.example.irvin.trucktransport.model.Poruka;
import com.example.irvin.trucktransport.model.Vozac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IvanX on 4.10.2017..
 */

public class VozacController extends DataAccess{

    private static final String TAG = VozacController.class.getSimpleName();

    public VozacController(Context context){
        super(context);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();

        Cursor cursor = getAllRows(DatabaseConstants.TABLE_VOZACI);

        //cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("vozac_id", cursor.getString(1));
            user.put("user", cursor.getString(2));
            user.put("pass", cursor.getString(3));
            user.put("ime", cursor.getString(4));
            user.put("prezime", cursor.getString(5));
            user.put("jmbg", cursor.getString(6));
        }
        cursor.close();
        close();

        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public Vozac getVozac(String vozac_id){

        List<Poruka> poruke = new ArrayList<>();
        String where = "vozac_id = " + vozac_id;
        Cursor cursor_poruke = getAllRowsWhere(DatabaseConstants.TABLE_PORUKE, where);

        if (cursor_poruke.getCount() > 0){
            for(int i=0; i<cursor_poruke.getCount(); i++){
                Poruka poruka = new Poruka();
                poruka.setPoruka_id(cursor_poruke.getString(1));
                poruka.setText(cursor_poruke.getString(2));
                poruka.setVrijeme(cursor_poruke.getString(3));
                poruka.setOdvozaca(cursor_poruke.getString(5));

                poruke.add(poruka);
                cursor_poruke.moveToNext();
            }
        }

        Vozac vozac = new Vozac();

        Cursor cursor_vozac = getAllRowsById(DatabaseConstants.TABLE_VOZACI, vozac_id);

        if (cursor_poruke.getCount() > 0){
            vozac.setVozac_id(cursor_vozac.getString(1));
            vozac.setIme(cursor_vozac.getString(2));
            vozac.setPrezime(cursor_vozac.getString(3));
            vozac.setUser(cursor_vozac.getString(4));
            vozac.setPass(cursor_vozac.getString(5));
            vozac.setJmbg(cursor_vozac.getString(6));
            vozac.setPoruke(poruke);
        }
        return vozac;
    }

    public void addVozac(Vozac vozac){
        ContentValues values = new ContentValues();

        values.put(DatabaseConstants.VOZAC_ID, vozac.getVozac_id());
        values.put(DatabaseConstants.VOZAC_USER, vozac.getUser());
        values.put(DatabaseConstants.VOZAC_PASS, vozac.getPass());
        values.put(DatabaseConstants.VOZAC_IME, vozac.getIme());
        values.put(DatabaseConstants.VOZAC_PREZIME, vozac.getPrezime());
        values.put(DatabaseConstants.VOZAC_JMBG, vozac.getJmbg());

        try {
            insertOrReplaceRow(DatabaseConstants.TABLE_VOZACI, values);
        } catch (SQLException e) {
            Log.e(VozacController.class.getSimpleName(), "SQL error: " + e.getMessage());
        }

        ContentValues poruke = new ContentValues();
        for(Poruka poruka: vozac.getPoruke()){
            poruke.put(DatabaseConstants.PORUKA_ID, poruka.getPoruka_id());
            poruke.put(DatabaseConstants.PORUKA_VRIJEME, poruka.getVrijeme());
            poruke.put(DatabaseConstants.PORUKA_TEXT, poruka.getText());
            poruke.put(DatabaseConstants.PORUKA_ODVOZACA, poruka.getOdvozaca());
            poruke.put(DatabaseConstants.PORUKA_VOZAC_ID, vozac.getVozac_id());

            try {
                insertOrReplaceRow(DatabaseConstants.TABLE_PORUKE, poruke);
            } catch (SQLException e) {
                Log.e(VozacController.class.getSimpleName(), "SQL error: " + e.getMessage());
            }
        }
    }

    public void deleteVozac(){
        deleteAll(DatabaseConstants.TABLE_VOZACI);
        deleteAll(DatabaseConstants.TABLE_PORUKE);
    }
}
