package com.example.irvin.trucktransport.controllers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.irvin.trucktransport.dataaccess.DataAccess;
import com.example.irvin.trucktransport.dataaccess.DatabaseConstants;
import com.example.irvin.trucktransport.model.Nalog;
import com.example.irvin.trucktransport.model.PoznataLokacija;
import com.example.irvin.trucktransport.model.Stajaliste;
import com.example.irvin.trucktransport.model.StajalisteNalog;
import com.example.irvin.trucktransport.model.Zadatak;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanX on 28.11.2017..
 */

public class NaloziController extends DataAccess {

    private static final String TAG = NaloziController.class.getSimpleName();

    public NaloziController(Context context){
        super(context);
    }

    public Nalog getNalogByID(String nalog_id){
        Nalog nalog = new Nalog();

        List<Nalog> nalozi = new ArrayList<>();
        nalozi = getNalozi();
        for(int i=0; i<nalozi.size(); i++){
            if(nalog_id.equals(nalozi.get(i).getNalog_id()))
                nalog = nalozi.get(i);
        }

        return nalog;
    }

    public List<Nalog> getNalozi(){

        List<Nalog> nalozi = new ArrayList<>();

        Cursor cursor_nalozi = getAllRows(DatabaseConstants.TABLE_NALOZI);

        if(cursor_nalozi.getCount()>0){
            for(int i=0; i<cursor_nalozi.getCount(); i++){
                Nalog nalog = new Nalog();
                nalog.setNalog_id(cursor_nalozi.getString(1));
                nalog.setVrijeme_kreiranja(cursor_nalozi.getString(2));
                nalog.setStanje_id(cursor_nalozi.getString(3));
                nalog.setVozilo_id(cursor_nalozi.getString(4));
                nalog.setVozac_id(cursor_nalozi.getString(5));

                String where_zadatak = "nalog_id = " + nalog.getNalog_id();
                Cursor cursor_zadaci = getAllRowsWhere(DatabaseConstants.TABLE_ZADACI, where_zadatak);

                List<Zadatak> zadaci = new ArrayList<>();

                if(cursor_zadaci.getCount()>0){
                    for(int y=0; y<cursor_zadaci.getCount(); y++){
                        Zadatak zadatak = new Zadatak();
                        zadatak.setZadatak_id(cursor_zadaci.getString(1));
                        zadatak.setNaziv(cursor_zadaci.getString(2));
                        zadatak.setOpis(cursor_zadaci.getString(3));
                        zadatak.setCheckin(cursor_zadaci.getString(4));
                        zadatak.setCheckout(cursor_zadaci.getString(5));
                        zadatak.setPoznatalokacija_id(cursor_zadaci.getString(7));
                        zadatak.setBroj_zadatka(cursor_zadaci.getString(8));

                        String where_poznatalokacija = "poznatalokacija_id = " + zadatak.getPoznatalokacija_id();
                        Cursor cursor_poznatalokacija = getAllRowsWhere(DatabaseConstants.TABLE_POZNATELOKACIJE, where_poznatalokacija);

                        if(cursor_poznatalokacija.getCount()>0){
                            PoznataLokacija poznataLokacija = new PoznataLokacija();
                            poznataLokacija.setPoznatalokacija_id(cursor_poznatalokacija.getString(1));
                            poznataLokacija.setNaziv(cursor_poznatalokacija.getString(2));
                            poznataLokacija.setOpis(cursor_poznatalokacija.getString(3));
                            poznataLokacija.setDuzina(cursor_poznatalokacija.getString(4));
                            poznataLokacija.setSirina(cursor_poznatalokacija.getString(5));
                            poznataLokacija.setKategorija_id(cursor_poznatalokacija.getString(6));

                            zadatak.setPoznataLokacija(poznataLokacija);
                        }

                        zadaci.add(zadatak);
                        cursor_zadaci.moveToNext();

                    }
                }

                nalog.setZadaci(zadaci);


                String where_stajalisteNalog = "nalog_id = " + nalog.getNalog_id();
                Cursor cursor_stajalista_nalozi = getAllRowsWhere(DatabaseConstants.TABLE_STAJALISTANALOZI, where_stajalisteNalog);

                List<StajalisteNalog> stajalistaNalozi = new ArrayList<>();
                if(cursor_stajalista_nalozi.getCount()>0){
                    for(int z=0; z<cursor_stajalista_nalozi.getCount(); z++){
                        StajalisteNalog stajalisteNalog = new StajalisteNalog();

                        stajalisteNalog.setStajaliste_nalog_id(cursor_stajalista_nalozi.getString(1));
                        stajalisteNalog.setStajaliste_id(cursor_stajalista_nalozi.getString(2));
                        stajalisteNalog.setNalog_id(cursor_stajalista_nalozi.getString(3));
                        stajalisteNalog.setCheckin(cursor_stajalista_nalozi.getString(4));
                        stajalisteNalog.setCheckout(cursor_stajalista_nalozi.getString(5));
                        stajalisteNalog.setBroj_stajalista(cursor_stajalista_nalozi.getString(6));

                        String where_stajaliste = "stajaliste_id = " + stajalisteNalog.getStajaliste_id();
                        Cursor cursor_stajaliste = getAllRowsWhere(DatabaseConstants.TABLE_STAJALISTA, where_stajaliste);

                        if(cursor_stajaliste.getCount()>0){
                            Stajaliste stajaliste = new Stajaliste();

                            stajaliste.setStajaliste_id(cursor_stajaliste.getString(1));
                            stajaliste.setNaziv(cursor_stajaliste.getString(2));
                            stajaliste.setOpis(cursor_stajaliste.getString(3));
                            stajaliste.setDuzina(cursor_stajaliste.getString(4));
                            stajaliste.setSirina(cursor_stajaliste.getString(5));

                            stajalisteNalog.setStajaliste(stajaliste);
                        }

                        stajalistaNalozi.add(stajalisteNalog);
                        cursor_stajalista_nalozi.moveToNext();

                    }
                }

                nalog.setStajalistaNalozi(stajalistaNalozi);

                nalozi.add(nalog);
                cursor_nalozi.moveToNext();
            }
        }else {
            Log.e(TAG, "gfdtghsdfhzju");
        }

        return nalozi;
    }

    public void addNalozi(List<Nalog> nalozi){

        ContentValues valuesNalog = new ContentValues();
        for(int i=0; i<nalozi.size(); i++){
            valuesNalog.put(DatabaseConstants.NALOG_ID, nalozi.get(i).getNalog_id());
            valuesNalog.put(DatabaseConstants.NALOG_VRIJEME_KREIRANJA, nalozi.get(i).getVrijeme_kreiranja());
            valuesNalog.put(DatabaseConstants.NALOG_STANJE_ID, nalozi.get(i).getStanje_id());
            valuesNalog.put(DatabaseConstants.NALOG_VOZILO_ID, nalozi.get(i).getVozilo_id());
            valuesNalog.put(DatabaseConstants.NALOG_VOZAC_ID, nalozi.get(i).getVozac_id());

            ContentValues valuesZadatak = new ContentValues();
            for(int y=0; y<nalozi.get(i).getZadaci().size(); y++){
                List<Zadatak> zadaci = new ArrayList<>();
                zadaci = nalozi.get(i).getZadaci();
                valuesZadatak.put(DatabaseConstants.ZADATAK_ID, zadaci.get(y).getZadatak_id());
                valuesZadatak.put(DatabaseConstants.ZADATAK_NAZIV, zadaci.get(y).getNaziv());
                valuesZadatak.put(DatabaseConstants.ZADATAK_OPIS, zadaci.get(y).getOpis());
                valuesZadatak.put(DatabaseConstants.ZADATAK_CHECKIN, zadaci.get(y).getCheckin());
                valuesZadatak.put(DatabaseConstants.ZADATAK_CHECKOUT, zadaci.get(y).getCheckout());
                valuesZadatak.put(DatabaseConstants.ZADATAK_POZNATALOKACIJA_ID, zadaci.get(y).getPoznatalokacija_id());
                valuesZadatak.put(DatabaseConstants.ZADATAK_NALOG_ID, nalozi.get(i).getNalog_id());
                valuesZadatak.put(DatabaseConstants.ZADATAK_BROJ_ZADATKA, zadaci.get(y).getBroj_zadatka());

                try {
                    insertOrReplaceRow(DatabaseConstants.TABLE_ZADACI, valuesZadatak);
                }catch (SQLException e){
                    Log.e(TAG, "SQL error: " + e.getMessage());
                }

                PoznataLokacija poznataLokacija = zadaci.get(y).getPoznataLokacija();
                ContentValues valuesPoznataLokacija = new ContentValues();

                valuesPoznataLokacija.put(DatabaseConstants.POZNATALOKACIJA_ID, poznataLokacija.getPoznatalokacija_id());
                valuesPoznataLokacija.put(DatabaseConstants.POZNATALOKACIJA_DUZINA, poznataLokacija.getDuzina());
                valuesPoznataLokacija.put(DatabaseConstants.POZNATALOKACIJA_SIRINA, poznataLokacija.getSirina());
                valuesPoznataLokacija.put(DatabaseConstants.POZNATALOKACIJA_NAZIV, poznataLokacija.getNaziv());
                valuesPoznataLokacija.put(DatabaseConstants.POZNATALOKACIJA_OPIS, poznataLokacija.getOpis());
                valuesPoznataLokacija.put(DatabaseConstants.POZNATALOKACIJA_KATEGORIJA_ID, poznataLokacija.getKategorija_id());

                try {
                    insertOrReplaceRow(DatabaseConstants.TABLE_POZNATELOKACIJE, valuesPoznataLokacija);
                }catch (SQLException e){
                    Log.e(TAG, "SQL error: " + e.getMessage());
                }
            }

            ContentValues valuesStajalistaNalozi = new ContentValues();
            for(int z=0; z<nalozi.get(i).getStajalistaNalozi().size(); z++){
                List<StajalisteNalog> stajalistaNalozi = new ArrayList<>();
                stajalistaNalozi = nalozi.get(i).getStajalistaNalozi();
                valuesStajalistaNalozi.put(DatabaseConstants.STAJALISTE_NALOG_ID, stajalistaNalozi.get(z).getStajaliste_nalog_id());
                valuesStajalistaNalozi.put(DatabaseConstants.SN_STAJALISTE_ID, stajalistaNalozi.get(z).getStajaliste_id());
                valuesStajalistaNalozi.put(DatabaseConstants.SN_NALOG_ID, stajalistaNalozi.get(z).getNalog_id());
                valuesStajalistaNalozi.put(DatabaseConstants.SN_CHECKIN, stajalistaNalozi.get(z).getCheckin());
                valuesStajalistaNalozi.put(DatabaseConstants.SN_CHECKOUT, stajalistaNalozi.get(z).getCheckout());
                valuesStajalistaNalozi.put(DatabaseConstants.SN_BROJ_STAJALISTA, stajalistaNalozi.get(z).getBroj_stajalista());

                try {
                    insertOrReplaceRow(DatabaseConstants.TABLE_STAJALISTANALOZI, valuesStajalistaNalozi);
                }catch (SQLException e){
                    Log.e(TAG, "SQL error: " + e.getMessage());
                }

                Stajaliste stajaliste = stajalistaNalozi.get(z).getStajaliste();
                ContentValues valuesStajaliste = new ContentValues();

                valuesStajaliste.put(DatabaseConstants.STAJALISTE_ID, stajaliste.getStajaliste_id());
                valuesStajaliste.put(DatabaseConstants.STAJALISTE_NAZIV, stajaliste.getNaziv());
                valuesStajaliste.put(DatabaseConstants.STAJALISTE_OPIS, stajaliste.getOpis());
                valuesStajaliste.put(DatabaseConstants.STAJALISTE_DUZINA, stajaliste.getDuzina());
                valuesStajaliste.put(DatabaseConstants.STAJALISTE_SIRINA, stajaliste.getSirina());

                try {
                    insertOrReplaceRow(DatabaseConstants.TABLE_STAJALISTA, valuesStajaliste);
                }catch (SQLException e){
                    Log.e(TAG, "SQL error: " + e.getMessage());
                }
            }

            try {
                insertOrReplaceRow(DatabaseConstants.TABLE_NALOZI, valuesNalog);
            }catch (SQLException e){
                Log.e(TAG, "SQL error: " + e.getMessage());
            }
        }

    }

    public void updateNalogStanje(String nalog_id, String stanje){
        ContentValues values = new ContentValues();

        values.put(DatabaseConstants.NALOG_STANJE_ID, stanje);

        try {
            updateField(DatabaseConstants.TABLE_NALOZI, values, DatabaseConstants.NALOG_ID+"="+nalog_id);
        }catch (android.database.SQLException e){
            Log.e(TAG, "SQL error: " + e.getMessage());
        }
    }

    public void deleteNalozi(){
        deleteAll(DatabaseConstants.TABLE_POZNATELOKACIJE);
        deleteAll(DatabaseConstants.TABLE_ZADACI);
        deleteAll(DatabaseConstants.TABLE_NALOZI);
        deleteAll(DatabaseConstants.TABLE_STAJALISTANALOZI);
        deleteAll(DatabaseConstants.TABLE_STAJALISTA);
        deleteAll(DatabaseConstants.TABLE_GEOTACKE);
    }
}
