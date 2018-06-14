package com.example.irvin.trucktransport.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.model.Zadatak;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanX on 5.12.2017..
 */

public class ZadaciRecyclerAdapter extends RecyclerView.Adapter<ZadaciViewHolder> {

    private List<Zadatak> feedItemList;
    private Context mContext;

    public ZadaciRecyclerAdapter(Context context, List<Zadatak> feedItemList){
        this.feedItemList = new ArrayList<>();
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public ZadaciViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_zadaci, null);
        ZadaciViewHolder viewHolder = new ZadaciViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    @Override
    public void onBindViewHolder(ZadaciViewHolder holder, int position) {
        Zadatak feedItem = feedItemList.get(position);

        holder.naziv.setText("Naziv: " + feedItem.getNaziv());
        holder.opis.setText("Opis: " + feedItem.getOpis());
        holder.poznataLokacija.setText("Lokacija: " + feedItem.getPoznataLokacija().getNaziv());
    }

}
