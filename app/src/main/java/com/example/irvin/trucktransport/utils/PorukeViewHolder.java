package com.example.irvin.trucktransport.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.irvin.trucktransport.R;

/**
 * Created by IvanX on 20.5.2018..
 */

public class PorukeViewHolder extends RecyclerView.ViewHolder {

    protected TextView vrijeme;
    protected TextView text_poruke;

    public PorukeViewHolder(View view){
        super(view);
        this.vrijeme = (TextView)view.findViewById(R.id.vrijeme);
        this.text_poruke = (TextView)view.findViewById(R.id.text_poruke);
    }
}
