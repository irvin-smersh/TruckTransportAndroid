package com.example.irvin.trucktransport.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.model.Nalog;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by IvanX on 2.12.2017..
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private List<Nalog> feedItemList;
    private Context mContext;

    public MyRecyclerAdapter(Context context, List<Nalog> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Nalog feedItem = feedItemList.get(position);

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.setTimeInMillis(Long.parseLong(feedItem.getVrijeme_kreiranja())*1000);
        String datum = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(calendar.get(Calendar.MINUTE)) + " "
                + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "."
                + String.valueOf(calendar.get(Calendar.MONTH)+1) + "."
                + String.valueOf(calendar.get(Calendar.YEAR));

        holder.brojNaloga.setText("Nalog br: " + feedItem.getNalog_id());
        holder.vrijemeKreiranja.setText("Kreirano: " + datum);
        holder.pocetnaPozicija.setText("Start: " + feedItem.getZadaci().get(0).getPoznataLokacija().getNaziv());
        int i = feedItem.getZadaci().size();
        holder.zavrsnaPozicija.setText("Kraj: " + feedItem.getZadaci().get(i-1).getPoznataLokacija().getNaziv());

    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
}
