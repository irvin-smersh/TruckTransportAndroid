package com.example.irvin.trucktransport.dataaccess;

/**
 * Created by IvanX on 28.6.2017..
 */

public class DatabaseConstants {

    DatabaseConstants(){

    }

    //TABLES DATA
    public static final String TABLE_VOZILA = "vozila";
    public static final String TABLE_VOZACI = "vozaci";
    public static final String TABLE_GEOTACKE = "geotacke";
    public static final String TABLE_NALOZI = "nalozi";
    public static final String TABLE_ZADACI = "zadaci";
    public static final String TABLE_POZNATELOKACIJE = "poznatelokacije";
    public static final String TABLE_PORUKE = "poruke";
    public static final String TABLE_STANJA = "stanja";
    public static final String TABLE_KATEGORIJE = "kategorije";
    public static final String TABLE_STAJALISTANALOZI = "stajalista_nalozi";
    public static final String TABLE_STAJALISTA = "stajalista";

        /*  Columns data for all tables   */

    public static final String KEY_ID = "id";
    //Vozila
    public static final String VOZILO_ID = "vozilo_id";
    public static final String VOZILO_NAZIV = "naziv";
    public static final String VOZILO_MARKA = "marka";
    public static final String VOZILO_TIP = "tip";
    public static final String VOZILO_GODISTE = "godiste";
    public static final String VOZILO_NOSIVOST = "nosivost";

    //Vozaci
    public static final String VOZAC_ID = "vozac_id";
    public static final String VOZAC_USER = "user";
    public static final String VOZAC_PASS = "pass";
    public static final String VOZAC_IME = "ime";
    public static final String VOZAC_PREZIME = "prezime";
    public static final String VOZAC_JMBG = "jmbg";

    //GeoTacke
    public static final String GEOTACKA_ID = "geotacka_id";
    public static final String GEOTACKA_DUZINA = "duzina";
    public static final String GEOTACKA_SIRINA = "sirina";
    public static final String GEOTACKA_VRIJEME = "vrijeme";
    public static final String GEOTACKA_NALOG_ID = "nalog_id";

    //Nalozi
    public static final String NALOG_ID = "nalog_id";
    public static final String NALOG_VRIJEME_KREIRANJA = "vrijeme_kreiranja";
    public static final String NALOG_STANJE_ID = "stanje_id";
    public static final String NALOG_VOZILO_ID = "vozilo_id";
    public static final String NALOG_VOZAC_ID = "vozac_id";

    //Zadaci
    public static final String ZADATAK_ID = "zadatak_id";
    public static final String ZADATAK_NAZIV = "naziv";
    public static final String ZADATAK_OPIS = "opis";
    public static final String ZADATAK_CHECKIN = "checkin";
    public static final String ZADATAK_CHECKOUT = "checkout";
    public static final String ZADATAK_POZNATALOKACIJA_ID = "poznatalokacija_id";
    public static final String ZADATAK_NALOG_ID = "nalog_id";
    public static final String ZADATAK_BROJ_ZADATKA = "broj_zadatka";

    //PoznateLokacije
    public static final String POZNATALOKACIJA_ID = "poznatalokacija_id";
    public static final String POZNATALOKACIJA_DUZINA = "duzina";
    public static final String POZNATALOKACIJA_SIRINA = "sirina";
    public static final String POZNATALOKACIJA_NAZIV = "naziv";
    public static final String POZNATALOKACIJA_OPIS = "opis";
    public static final String POZNATALOKACIJA_KATEGORIJA_ID = "kategorija_id";

    //Poruke
    public static final String PORUKA_ID = "poruka_id";
    public static final String PORUKA_VRIJEME = "vrijeme";
    public static final String PORUKA_TEXT = "text";
    public static final String PORUKA_ODVOZACA = "odvozaca";
    public static final String PORUKA_VOZAC_ID = "vozac_id";

    //STANJA
    public static final String STANJE_ID = "stanje_id";
    public static final String STANJE_OPIS = "opis";

    //KATEGORIJE
    public static final String KATEGORIJA_ID = "kategorija_id";
    public static final String KATEGORIJA_NAZIV = "naziv";

    //StajalistaNalozi
    public static final String STAJALISTE_NALOG_ID = "stajaliste_nalog_id";
    public static final String SN_STAJALISTE_ID = "stajaliste_id";
    public static final String SN_NALOG_ID = "nalog_id";
    public static final String SN_CHECKIN = "checkin";
    public static final String SN_CHECKOUT = "checkout";
    public static final String SN_BROJ_STAJALISTA = "broj_stajalista";

    //Stajalista
    public static final String STAJALISTE_ID = "stajaliste_id";
    public static final String STAJALISTE_NAZIV = "naziv";
    public static final String STAJALISTE_OPIS = "opis";
    public static final String STAJALISTE_DUZINA = "duzina";
    public static final String STAJALISTE_SIRINA = "sirina";

}
