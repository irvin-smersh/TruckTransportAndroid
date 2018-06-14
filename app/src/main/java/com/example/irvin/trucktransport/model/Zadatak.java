package com.example.irvin.trucktransport.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanX on 28.6.2017..
 */

public class Zadatak implements Serializable{

    private String zadatak_id;
    private String naziv;
    private String opis;
    private String checkin = "";
    private String checkout = "";
    private String poznatalokacija_id;
    private PoznataLokacija poznatalokacija;
    @SerializedName("broj zadatka")
    private String broj_zadatka;

    public Zadatak(){

    }

    public Zadatak(Zadatak zadatak){
        this.zadatak_id = zadatak.zadatak_id;
        this.naziv = zadatak.naziv;
        this.opis = zadatak.opis;
        this.checkin = zadatak.checkin;
        this.checkout = zadatak.checkout;
        this.poznatalokacija_id = zadatak.poznatalokacija_id;
        this.poznatalokacija = zadatak.poznatalokacija;
        this.broj_zadatka = zadatak.broj_zadatka;
    }

    public Zadatak(String zadatak_id, String naziv, String opis, String checkin, String checkout, String poznatalokacija_id, PoznataLokacija poznataLokacija, String broj_zadatka) {
        this.zadatak_id = zadatak_id;
        this.naziv = naziv;
        this.opis = opis;
        this.checkin = checkin;
        this.checkout = checkout;
        this.poznatalokacija_id = poznatalokacija_id;
        this.poznatalokacija = poznataLokacija;
        this.broj_zadatka = broj_zadatka;
    }

    public String getZadatak_id() {
        return zadatak_id;
    }

    public void setZadatak_id(String zadatak_id) {
        this.zadatak_id = zadatak_id;
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

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getPoznatalokacija_id() {
        return poznatalokacija_id;
    }

    public void setPoznatalokacija_id(String poznatalokacija_id) {
        this.poznatalokacija_id = poznatalokacija_id;
    }

    public PoznataLokacija getPoznataLokacija() {
        return poznatalokacija;
    }

    public void setPoznataLokacija(PoznataLokacija poznataLokacija) {
        this.poznatalokacija = poznataLokacija;
    }

    public String getBroj_zadatka() {
        return broj_zadatka;
    }

    public void setBroj_zadatka(String broj_zadatka) {
        this.broj_zadatka = broj_zadatka;
    }
}
