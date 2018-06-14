package com.example.irvin.trucktransport.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.irvin.trucktransport.R;

/**
 * Created by IvanX on 11.6.2018..
 */

public class StatistikaViewHolder extends RecyclerView.ViewHolder {

    protected TextView brojNaloga;
    protected TextView vrijemeKreiranja;
    protected TextView brojZadataka;
    protected TextView predjenaUdaljenost;

    public StatistikaViewHolder(View view){
        super(view);
        this.brojNaloga = (TextView)view.findViewById(R.id.brojNaloga);
        this.vrijemeKreiranja = (TextView)view.findViewById(R.id.vrijemeKreiranja);
        this.brojZadataka = (TextView)view.findViewById(R.id.brojZadataka);
        this.predjenaUdaljenost = (TextView)view.findViewById(R.id.predjenaUdaljenost);
    }
}
