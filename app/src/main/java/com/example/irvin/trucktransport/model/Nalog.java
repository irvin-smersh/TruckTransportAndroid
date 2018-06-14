package com.example.irvin.trucktransport.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanX on 28.6.2017..
 */

public class Nalog implements Serializable{

    private String nalog_id;
    private String vrijeme_kreiranja;
    private String stanje_id;
    private String vozilo_id;
    private String vozac_id;
    private List<Zadatak> zadaci = new ArrayList<>();
    private List<StajalisteNalog> stajalistaNalozi = new ArrayList<>();

    public Nalog(){

    }

    public Nalog(Nalog nalog){
        this.nalog_id = nalog.nalog_id;
        this.vrijeme_kreiranja = nalog.vrijeme_kreiranja;
        this.stanje_id = nalog.stanje_id;
        this.vozilo_id = nalog.vozilo_id;
        this.vozac_id = nalog.vozac_id;
        this.zadaci = nalog.zadaci;
        this.stajalistaNalozi = nalog.stajalistaNalozi;
    }

    public Nalog(String nalog_id, String vrijeme_kreiranja, String stanje_id, String vozilo_id, String vozac_id, List<Zadatak> zadaci, List<StajalisteNalog> stajalistaNalozi) {
        this.nalog_id = nalog_id;
        this.vrijeme_kreiranja = vrijeme_kreiranja;
        this.stanje_id = stanje_id;
        this.vozilo_id = vozilo_id;
        this.vozac_id = vozac_id;
        this.zadaci = zadaci;
        this.stajalistaNalozi = stajalistaNalozi;
    }

    public String getNalog_id() {
        return nalog_id;
    }

    public void setNalog_id(String nalog_id) {
        this.nalog_id = nalog_id;
    }

    public String getVrijeme_kreiranja() {
        return vrijeme_kreiranja;
    }

    public void setVrijeme_kreiranja(String vrijeme_kreiranja) {
        this.vrijeme_kreiranja = vrijeme_kreiranja;
    }

    public String getStanje_id() {
        return stanje_id;
    }

    public void setStanje_id(String stanje_id) {
        this.stanje_id = stanje_id;
    }

    public String getVozilo_id() {
        return vozilo_id;
    }

    public void setVozilo_id(String vozilo_id) {
        this.vozilo_id = vozilo_id;
    }

    public String getVozac_id() {
        return vozac_id;
    }

    public void setVozac_id(String vozac_id) {
        this.vozac_id = vozac_id;
    }

    public List<Zadatak> getZadaci() {
        return zadaci;
    }

    public void setZadaci(List<Zadatak> zadaci) {
        this.zadaci = zadaci;
    }

    public List<StajalisteNalog> getStajalistaNalozi() {
        return stajalistaNalozi;
    }

    public void setStajalistaNalozi(List<StajalisteNalog> stajalistaNalozi) {
        this.stajalistaNalozi = stajalistaNalozi;
    }
}
