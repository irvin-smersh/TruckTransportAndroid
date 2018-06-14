package com.example.irvin.trucktransport.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;


public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();

    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Login";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_VOZAC_ID = "vozacID";
    private static final String KEY_NALOG_ID = "nalogAktivni";
    private static final String KEY_ZADATAK_ID = "zadatakTrenutni";
    private static final String KEY_STAJALISTE_NALOG_ID = "stajalisteNalogTrenutni";
    private static final String KEY_VRIJEME = "vrijeme";
    private static final String KEY_UDALJENOSTI = "udaljenosti";
    private static final String KEY_VRIJEME_PAUZE = "vrijeme_pauze";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setVrijemePauze(String vrijemePauze){
        editor.putString(KEY_VRIJEME_PAUZE, vrijemePauze);
        editor.commit();
    }

    public void setUdaljenosti(String udaljenosti){
        editor.putString(KEY_UDALJENOSTI, udaljenosti);
        editor.commit();
    }

    public void setVrijeme(String vrijeme){
        editor.putString(KEY_VRIJEME, vrijeme);
        editor.commit();
    }

    public void setZadatakTrenutni(String zadatak_id){
        editor.putString(KEY_ZADATAK_ID, zadatak_id);
        editor.commit();
    }

    public void setStajalisteNAlogTrenutni(String stajalisteNalog_id){
        editor.putString(KEY_STAJALISTE_NALOG_ID, stajalisteNalog_id);
        editor.commit();
    }

    public void setLogin(boolean isLoggedIn, String vozac_id){

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.putString(KEY_VOZAC_ID, vozac_id);
        editor.commit();

        Log.d(TAG, "User login session midified!");
    }

    public void setNalogAktivni(String nalog_id){
        editor.putString(KEY_NALOG_ID, nalog_id);
        editor.commit();
    }

    public String getVrijeme(){
        return pref.getString(KEY_VRIJEME, "");
    }

    public String getUdaljenosti(){
        return pref.getString(KEY_UDALJENOSTI, "");
    }

    public String getNalogAktivni(){
        return pref.getString(KEY_NALOG_ID, "");
    }

    public String getZadatakTrenutni(){
        return pref.getString(KEY_ZADATAK_ID, "");
    }

    public String getStajalisteNalogTrenutni(){
        return pref.getString(KEY_STAJALISTE_NALOG_ID, "");
    }
    public String getVrijemePauze(){
        return pref.getString(KEY_VRIJEME_PAUZE, "0");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getVozacID(){
        return pref.getString(KEY_VOZAC_ID, "");
    }
}
