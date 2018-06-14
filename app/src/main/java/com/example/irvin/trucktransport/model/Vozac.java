package com.example.irvin.trucktransport.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanX on 28.6.2017..
 */

public class Vozac {

    private String vozac_id;
    private String user;
    private String pass;
    private String ime;
    private String prezime;
    private String jmbg;
    private List<Poruka> poruke = new ArrayList<>();


    public Vozac(){

    }

    public Vozac(Vozac vozac){
        this.vozac_id = vozac.vozac_id;
        this.user = vozac.user;
        this.pass = vozac.pass;
        this.ime = vozac.ime;
        this.prezime = vozac.prezime;
        this.jmbg = vozac.jmbg;
        this.poruke = vozac.poruke;
    }

    public Vozac(String vozac_id, String user, String pass, String ime, String prezime, String jmbg, List<Poruka> poruke) {
        this.vozac_id = vozac_id;
        this.user = user;
        this.pass = pass;
        this.ime = ime;
        this.prezime = prezime;
        this.jmbg = jmbg;
        this.poruke = poruke;
    }

    public String getVozac_id() {
        return vozac_id;
    }

    public void setVozac_id(String vozac_id) {
        this.vozac_id = vozac_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public List<Poruka> getPoruke() {
        return poruke;
    }

    public void setPoruke(List<Poruka> poruke) {
        this.poruke = poruke;
    }
}
