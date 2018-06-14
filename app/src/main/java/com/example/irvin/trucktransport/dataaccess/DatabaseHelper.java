package com.example.irvin.trucktransport.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by IvanX on 28.6.2017..
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "truck_transport";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VOZILA_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_VOZILA + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.VOZILO_ID
                + " TEXT," + DatabaseConstants.VOZILO_MARKA + " TEXT," + DatabaseConstants.VOZILO_NAZIV
                + " TEXT," + DatabaseConstants.VOZILO_TIP + " TEXT," + DatabaseConstants.VOZILO_GODISTE
                + " TEXT," + DatabaseConstants.VOZILO_NOSIVOST + " TEXT" + ")";

        String CREATE_VOZACI_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_VOZACI + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.VOZAC_ID
                + " TEXT," + DatabaseConstants.VOZAC_IME + " TEXT," + DatabaseConstants.VOZAC_PREZIME
                + " TEXT," + DatabaseConstants.VOZAC_USER + " TEXT," + DatabaseConstants.VOZAC_PASS
                + " TEXT," + DatabaseConstants.VOZAC_JMBG + " TEXT" + ")";

        String CREATE_GEOTACKE_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_GEOTACKE + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.GEOTACKA_ID
                + " TEXT," + DatabaseConstants.GEOTACKA_DUZINA + " TEXT," + DatabaseConstants.GEOTACKA_SIRINA
                + " TEXT," + DatabaseConstants.GEOTACKA_VRIJEME + " TEXT," + DatabaseConstants.GEOTACKA_NALOG_ID
                + " TEXT" + ")";

        String CREATE_NALOZI_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_NALOZI + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.NALOG_ID
                + " TEXT," + DatabaseConstants.NALOG_VRIJEME_KREIRANJA + " TEXT," + DatabaseConstants.NALOG_STANJE_ID
                + " TEXT," + DatabaseConstants.NALOG_VOZILO_ID + " TEXT," + DatabaseConstants.NALOG_VOZAC_ID
                + " TEXT" + ")";

        String CREATE_STAJALISTA_NALOZI_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_STAJALISTANALOZI + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.STAJALISTE_NALOG_ID
                + " TEXT," + DatabaseConstants.SN_STAJALISTE_ID + " TEXT," + DatabaseConstants.SN_NALOG_ID
                + " TEXT," + DatabaseConstants.SN_CHECKIN + " TEXT," + DatabaseConstants.SN_CHECKOUT
                + " TEXT," + DatabaseConstants.SN_BROJ_STAJALISTA + " TEXT" + ")";

        String CREATE_STAJALISTA_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_STAJALISTA + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.STAJALISTE_ID
                + " TEXT," + DatabaseConstants.STAJALISTE_NAZIV + " TEXT," + DatabaseConstants.STAJALISTE_OPIS
                + " TEXT," + DatabaseConstants.STAJALISTE_DUZINA + " TEXT," + DatabaseConstants.STAJALISTE_SIRINA
                + " TEXT" + ")";

        String CREATE_ZADACI_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_ZADACI + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.ZADATAK_ID
                + " TEXT," + DatabaseConstants.ZADATAK_NAZIV + " TEXT," + DatabaseConstants.ZADATAK_OPIS
                + " TEXT," + DatabaseConstants.ZADATAK_CHECKIN + " TEXT," + DatabaseConstants.ZADATAK_CHECKOUT
                + " TEXT," + DatabaseConstants.ZADATAK_NALOG_ID + " TEXT," + DatabaseConstants.ZADATAK_POZNATALOKACIJA_ID
                + " TEXT," + DatabaseConstants.ZADATAK_BROJ_ZADATKA + " TEXT" + ")";

        String CREATE_POZNATELOKACIJE_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_POZNATELOKACIJE + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.POZNATALOKACIJA_ID
                + " TEXT," + DatabaseConstants.POZNATALOKACIJA_NAZIV + " TEXT," + DatabaseConstants.POZNATALOKACIJA_OPIS
                + " TEXT," + DatabaseConstants.POZNATALOKACIJA_DUZINA + " TEXT," + DatabaseConstants.POZNATALOKACIJA_SIRINA
                + " TEXT," + DatabaseConstants.POZNATALOKACIJA_KATEGORIJA_ID + " TEXT" + ")";

        String CREATE_PORUKE_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_PORUKE + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.PORUKA_ID
                + " TEXT," + DatabaseConstants.PORUKA_TEXT + " TEXT," + DatabaseConstants.PORUKA_VRIJEME
                + " TEXT," + DatabaseConstants.PORUKA_VOZAC_ID + " TEXT," + DatabaseConstants.PORUKA_ODVOZACA
                + " TEXT" + ")";

        String CREATE_STANJA_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_STANJA + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.STANJE_ID
                + " TEXT," + DatabaseConstants.STANJE_OPIS + " TEXT" + ")";

        String CREATE_KATEGORIJE_TABLE = "CREATE TABLE " + DatabaseConstants.TABLE_KATEGORIJE + "("
                + DatabaseConstants.KEY_ID + " INTEGER PRIMARY KEY," + DatabaseConstants.KATEGORIJA_ID
                + " TEXT," + DatabaseConstants.KATEGORIJA_NAZIV + " TEXT" + ")";


        db.execSQL(CREATE_VOZILA_TABLE);
        db.execSQL(CREATE_VOZACI_TABLE);
        db.execSQL(CREATE_GEOTACKE_TABLE);
        db.execSQL(CREATE_NALOZI_TABLE);
        db.execSQL(CREATE_ZADACI_TABLE);
        db.execSQL(CREATE_POZNATELOKACIJE_TABLE);
        db.execSQL(CREATE_PORUKE_TABLE);
        db.execSQL(CREATE_STANJA_TABLE);
        db.execSQL(CREATE_KATEGORIJE_TABLE);
        db.execSQL(CREATE_STAJALISTA_NALOZI_TABLE);
        db.execSQL(CREATE_STAJALISTA_TABLE);

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_VOZILA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_VOZACI);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_GEOTACKE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_NALOZI);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_ZADACI);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_POZNATELOKACIJE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_PORUKE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_STANJA);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_KATEGORIJE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_STAJALISTANALOZI);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_STAJALISTA);
        onCreate(db);
    }
}
