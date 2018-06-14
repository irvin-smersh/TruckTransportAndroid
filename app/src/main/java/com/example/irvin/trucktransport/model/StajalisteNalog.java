package com.example.irvin.trucktransport.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by IvanX on 24.4.2018..
 */

public class StajalisteNalog implements Serializable{
    private String stajaliste_nalog_id;
    private String stajaliste_id;
    private String nalog_id;
    private String checkin = "";
    private String checkout = "";
    @SerializedName("broj stajalista")
    private String broj_stajalista;
    private Stajaliste stajaliste;

    public StajalisteNalog(){

    }

    public StajalisteNalog(StajalisteNalog stajaliste_nalog) {
        this.stajaliste_nalog_id = stajaliste_nalog.stajaliste_nalog_id;
        this.stajaliste_id = stajaliste_nalog.stajaliste_id;
        this.nalog_id = stajaliste_nalog.nalog_id;
        this.checkin = stajaliste_nalog.checkin;
        this.checkout = stajaliste_nalog.checkout;
        this.broj_stajalista = stajaliste_nalog.broj_stajalista;
        this.stajaliste = stajaliste_nalog.stajaliste;
    }

    public StajalisteNalog(String stajaliste_nalog_id, String stajaliste_id, String nalog_id, String checkin, String checkout, String broj_stajalista, Stajaliste stajaliste) {
        this.stajaliste_nalog_id = stajaliste_nalog_id;
        this.stajaliste_id = stajaliste_id;
        this.nalog_id = nalog_id;
        this.checkin = checkin;
        this.checkout = checkout;
        this.broj_stajalista = broj_stajalista;
        this.stajaliste = stajaliste;
    }

    public String getStajaliste_nalog_id() {
        return stajaliste_nalog_id;
    }

    public void setStajaliste_nalog_id(String stajaliste_nalog_id) {
        this.stajaliste_nalog_id = stajaliste_nalog_id;
    }

    public String getStajaliste_id() {
        return stajaliste_id;
    }

    public void setStajaliste_id(String stajaliste_id) {
        this.stajaliste_id = stajaliste_id;
    }

    public String getNalog_id() {
        return nalog_id;
    }

    public void setNalog_id(String nalog_id) {
        this.nalog_id = nalog_id;
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

    public String getBroj_stajalista() {
        return broj_stajalista;
    }

    public void setBroj_stajalista(String broj_stajalista) {
        this.broj_stajalista = broj_stajalista;
    }

    public Stajaliste getStajaliste() {
        return stajaliste;
    }

    public void setStajaliste(Stajaliste stajaliste) {
        this.stajaliste = stajaliste;
    }
}

