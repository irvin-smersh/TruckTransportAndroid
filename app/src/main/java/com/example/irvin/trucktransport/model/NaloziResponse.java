package com.example.irvin.trucktransport.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanX on 16.11.2017..
 */

public class NaloziResponse {

    private boolean error;
    List<Nalog> nalozi;

    public NaloziResponse(boolean error, List<Nalog> nalozi) {
        this.error = error;
        this.nalozi = new ArrayList<>();
        this.nalozi = nalozi;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Nalog> getNalozi() {
        return nalozi;
    }

    public void setNalozi(List<Nalog> nalozi) {
        this.nalozi = nalozi;
    }
}
