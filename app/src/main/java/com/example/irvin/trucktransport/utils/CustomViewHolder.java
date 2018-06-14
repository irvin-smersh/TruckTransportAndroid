package com.example.irvin.trucktransport.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.irvin.trucktransport.R;

/**
 * Created by IvanX on 2.12.2017..
 */

public class CustomViewHolder extends RecyclerView.ViewHolder {

    protected TextView brojNaloga;
    protected TextView vrijemeKreiranja;
    protected TextView pocetnaPozicija;
    protected TextView zavrsnaPozicija;

    public CustomViewHolder(View view) {
        super(view);
        this.brojNaloga = (TextView)view.findViewById(R.id.broj_naloga);
        this.vrijemeKreiranja = (TextView)view.findViewById(R.id.vrijeme_kreiranja);
        this.pocetnaPozicija = (TextView)view.findViewById(R.id.pocetna_pozicija);
        this.zavrsnaPozicija = (TextView)view.findViewById(R.id.zavrsna_pozicija);
    }
}
