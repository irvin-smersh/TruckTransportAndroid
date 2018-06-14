package com.example.irvin.trucktransport.model;

import java.io.Serializable;

/**
 * Created by IvanX on 28.6.2017..
 */

public class PoznataLokacija implements Serializable{

    private String poznatalokacija_id;
    private String duzina;
    private String sirina;
    private String naziv;
    private String opis;
    private String kategorija_id;

    public PoznataLokacija(){

    }

    public PoznataLokacija(PoznataLokacija poznataLokacija){
        this.poznatalokacija_id = poznataLokacija.poznatalokacija_id;
        this.duzina = poznataLokacija.duzina;
        this.sirina = poznataLokacija.sirina;
        this.naziv = poznataLokacija.naziv;
        this.opis = poznataLokacija.opis;
        this.kategorija_id = poznataLokacija.kategorija_id;
    }

    public PoznataLokacija(String poznatalokacija_id, String duzina, String sirina, String naziv, String opis, String kategorija_id) {
        this.poznatalokacija_id = poznatalokacija_id;
        this.duzina = duzina;
        this.sirina = sirina;
        this.naziv = naziv;
        this.opis = opis;
        this.kategorija_id = kategorija_id;
    }

    public String getPoznatalokacija_id() {
        return poznatalokacija_id;
    }

    public void setPoznatalokacija_id(String poznatalokacija_id) {
        this.poznatalokacija_id = poznatalokacija_id;
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

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getKategorija_id() {
        return kategorija_id;
    }

    public void setKategorija_id(String kategorija_id) {
        this.kategorija_id = kategorija_id;
    }
}
