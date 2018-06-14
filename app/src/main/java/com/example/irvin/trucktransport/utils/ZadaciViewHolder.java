package com.example.irvin.trucktransport.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.irvin.trucktransport.R;

/**
 * Created by IvanX on 5.12.2017..
 */

public class ZadaciViewHolder extends RecyclerView.ViewHolder {

    protected TextView naziv;
    protected TextView opis;
    protected TextView poznataLokacija;

    public ZadaciViewHolder(View view) {
        super(view);
        this.naziv = (TextView)view.findViewById(R.id.naziv);
        this.opis = (TextView)view.findViewById(R.id.opis);
        this.poznataLokacija = (TextView)view.findViewById(R.id.poznata_lokacija);
    }
}
