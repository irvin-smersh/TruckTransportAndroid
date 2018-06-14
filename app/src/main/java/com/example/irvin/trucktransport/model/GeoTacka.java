package com.example.irvin.trucktransport.model;

/**
 * Created by IvanX on 28.6.2017..
 */

public class GeoTacka {

    private String geotacka_id;
    private String lokalna_id;
    private String duzina;
    private String sirina;
    private String vrijeme;
    private String nalog_id;

    public GeoTacka(){

    }

    public GeoTacka(GeoTacka geotacka){
        this.geotacka_id = geotacka.geotacka_id;
        this.lokalna_id = geotacka.lokalna_id;
        this.duzina = geotacka.duzina;
        this.sirina = geotacka.sirina;
        this.vrijeme = geotacka.vrijeme;
        this.nalog_id = geotacka.nalog_id;
    }

    public GeoTacka(String geotacka_id, String lokalna_id, String duzina, String sirina, String vrijeme, String nalog_id) {
        this.geotacka_id = geotacka_id;
        this.lokalna_id = lokalna_id;
        this.duzina = duzina;
        this.sirina = sirina;
        this.vrijeme = vrijeme;
        this.nalog_id = nalog_id;
    }

    public String getGeotacka_id() {
        return geotacka_id;
    }

    public String getLokalna_id() {
        return lokalna_id;
    }

    public void setLokalna_id(String lokalna_id) {
        this.lokalna_id = lokalna_id;
    }

    public void setGeotacka_id(String geotacka_id) {
        this.geotacka_id = geotacka_id;
    }

    public String getDuzina() {
        return duzina;
    }

    public void setDuzina(String duzina) {
        this.duzina = duzina;
    }

    public String getSirina() {
        return sirina;
    }

    public void setSirina(String sirina) {
        this.sirina = sirina;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getNalog_id() {
        return nalog_id;
    }

    public void setNalog_id(String nalog_id) {
        this.nalog_id = nalog_id;
    }
}
