package com.example.irvin.trucktransport.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanX on 11.6.2018..
 */

public class StatistikaResponse implements Serializable{
    private List<Nalog> nalozi = new ArrayList<>();
    private List<String> udaljenosti = new ArrayList<>();
    private boolean error;

    public StatistikaResponse(){

    }

    public StatistikaResponse(List<Nalog> nalozi, List<String> udaljenosti, boolean error) {
        this.nalozi = nalozi;
        this.udaljenosti = udaljenosti;
        this.error = error;
    }

    public StatistikaResponse(StatistikaResponse statistikaResponse) {
        this.nalozi = statistikaResponse.nalozi;
        this.udaljenosti = statistikaResponse.udaljenosti;
        this.error = statistikaResponse.error;
    }

    public List<Nalog> getNalozi() {
        return nalozi;
    }

    public void setNalozi(List<Nalog> nalozi) {
        this.nalozi = nalozi;
    }

    public List<String> getUdaljenosti() {
        return udaljenosti;
    }

    public void setUdaljenosti(List<String> udaljenosti) {
        this.udaljenosti = udaljenosti;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
