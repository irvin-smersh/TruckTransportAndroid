package com.example.irvin.trucktransport.model;

/**
 * Created by IvanX on 28.6.2017..
 */

public class Poruka {

    private String poruka_id;
    private String vrijeme;
    private String text;
    private String odvozaca;
    private String vozac_id;

    public Poruka(Poruka poruka){
        this.poruka_id = poruka.poruka_id;
        this.vrijeme = poruka.vrijeme;
        this.text = poruka.text;
        this.odvozaca = poruka.odvozaca;
        this.vozac_id = poruka.vozac_id;
    }

    public Poruka(String poruka_id, String vrijeme, String text, String odvozaca, String vozac_id) {
        this.poruka_id = poruka_id;
        this.vrijeme = vrijeme;
        this.text = text;
        this.odvozaca = odvozaca;
        this.vozac_id = vozac_id;
    }

    public String getPoruka_id() {
        return poruka_id;
    }

    public void setPoruka_id(String poruka_id) {
        this.poruka_id = poruka_id;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getOdvozaca() {
        return odvozaca;
    }

    public void setOdvozaca(String odvozaca) {
        this.odvozaca = odvozaca;
    }

    public String getVozac_id() {
        return vozac_id;
    }

    public void setVozac_id(String vozac_id) {
        this.vozac_id = vozac_id;
    }

    public Poruka(){

    }
}
