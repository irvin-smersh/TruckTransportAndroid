package com.example.irvin.trucktransport.model;

/**
 * Created by IvanX on 28.6.2017..
 */

public class Vozilo {

    private String vozilo_id;
    private String naziv;
    private String marka;
    private String tip;
    private String godiste;
    private String nosivost;

    public Vozilo(){

    }

    public Vozilo(Vozilo vozilo){
        this.vozilo_id = vozilo.vozilo_id;
        this.naziv = vozilo.naziv;
        this.marka = vozilo.marka;
        this.tip = vozilo.tip;
        this.godiste = vozilo.godiste;
        this.nosivost = vozilo.nosivost;
    }

    public Vozilo(String vozilo_id, String naziv, String marka, String tip, String godiste, String nosivost) {
        this.vozilo_id = vozilo_id;
        this.naziv = naziv;
        this.marka = marka;
        this.tip = tip;
        this.godiste = godiste;
        this.nosivost = nosivost;
    }

    public String getVozilo_id() {
        return vozilo_id;
    }

    public void setVozilo_id(String vozilo_id) {
        this.vozilo_id = vozilo_id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getGodiste() {
        return godiste;
    }

    public void setGodiste(String godiste) {
        this.godiste = godiste;
    }

    public String getNosivost() {
        return nosivost;
    }

    public void setNosivost(String nosivost) {
        this.nosivost = nosivost;
    }
}
