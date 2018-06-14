package com.example.irvin.trucktransport.model;

/**
 * Created by IvanX on 24.4.2018..
 */

public class Stajaliste {
    private String stajaliste_id;
    private String naziv;
    private String opis;
    private String duzina;
    private String sirina;

    public Stajaliste(){

    }

    public Stajaliste(Stajaliste stajaliste) {
        this.stajaliste_id = stajaliste.stajaliste_id;
        this.naziv = stajaliste.naziv;
        this.opis = stajaliste.opis;
        this.duzina = stajaliste.duzina;
        this.sirina = stajaliste.sirina;
    }

    public Stajaliste(String stajaliste_id, String naziv, String opis, String duzina, String sirina) {
        this.stajaliste_id = stajaliste_id;
        this.naziv = naziv;
        this.opis = opis;
        this.duzina = duzina;
        this.sirina = sirina;
    }

    public String getStajaliste_id() {
        return stajaliste_id;
    }

    public void setStajaliste_id(String stajaliste_id) {
        this.stajaliste_id = stajaliste_id;
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
}
