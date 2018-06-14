package com.example.irvin.trucktransport.model;

/**
 * Created by IvanX on 13.9.2017..
 */

public class UserResponse {

    private boolean error;
    Vozac vozac;

    public UserResponse(boolean error, Vozac vozac) {
        this.error = error;
        this.vozac = vozac;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Vozac getUser() {
        return vozac;
    }

    public void setUser(Vozac user) {
        this.vozac = user;
    }
}
