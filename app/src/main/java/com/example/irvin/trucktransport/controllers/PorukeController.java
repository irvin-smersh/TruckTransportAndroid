package com.example.irvin.trucktransport.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.service.textservice.SpellCheckerService;
import android.util.Log;

import com.example.irvin.trucktransport.dataaccess.DataAccess;
import com.example.irvin.trucktransport.dataaccess.DatabaseConstants;
import com.example.irvin.trucktransport.model.Poruka;
import com.example.irvin.trucktransport.utils.SessionManager;

import java.util.List;

/**
 * Created by IvanX on 21.5.2018..
 */

public class PorukeController extends DataAccess {
    private static final String TAG = PorukeController.class.getSimpleName();
    SessionManager session;

    public PorukeController(Context context){
        super(context);
    }

    public void addPoruka(Poruka poruka){

        ContentValues porukaContent = new ContentValues();

        porukaContent.put(DatabaseConstants.PORUKA_ID, poruka.getPoruka_id());
        porukaContent.put(DatabaseConstants.PORUKA_VRIJEME, poruka.getVrijeme());
        porukaContent.put(DatabaseConstants.PORUKA_TEXT, poruka.getText());
        porukaContent.put(DatabaseConstants.PORUKA_ODVOZACA, poruka.getOdvozaca());
        porukaContent.put(DatabaseConstants.PORUKA_VOZAC_ID, poruka.getVozac_id());

        try {
            insertOrReplaceRow(DatabaseConstants.TABLE_PORUKE, porukaContent);
        } catch (SQLException e) {
            Log.e(TAG, "SQL error: " + e.getMessage());
        }
    }

    public void addPoruke(List<Poruka> poruke){
        ContentValues porukeContent = new ContentValues();
        for(int i=0; i<poruke.size(); i++){
            Poruka poruka = poruke.get(i);
            porukeContent.put(DatabaseConstants.PORUKA_ID, poruka.getPoruka_id());
            porukeContent.put(DatabaseConstants.PORUKA_VRIJEME, poruka.getVrijeme());
            porukeContent.put(DatabaseConstants.PORUKA_TEXT, poruka.getText());
            porukeContent.put(DatabaseConstants.PORUKA_ODVOZACA, poruka.getOdvozaca());
            porukeContent.put(DatabaseConstants.PORUKA_VOZAC_ID, poruka.getVozac_id());

            try {
                insertOrReplaceRow(DatabaseConstants.TABLE_PORUKE, porukeContent);
            } catch (SQLException e) {
                Log.e(TAG, "SQL error: " + e.getMessage());
            }
        }
    }
}
