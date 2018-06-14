package com.example.irvin.trucktransport.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.model.Nalog;
import com.example.irvin.trucktransport.model.StatistikaResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by IvanX on 11.6.2018..
 */

public class StatistikaRecyclerAdapter extends RecyclerView.Adapter<StatistikaViewHolder> {

    List<Nalog> feedItemListNalozi;
    List<String> feedItemListUdaljenosti;
    private Context mContext;

    public StatistikaRecyclerAdapter(Context context, StatistikaResponse statistikaResponse){
        this.feedItemListNalozi = new ArrayList<>();
        this.feedItemListUdaljenosti = new ArrayList<>();
        this.feedItemListNalozi = statistikaResponse.getNalozi();
        this.feedItemListUdaljenosti =  statistikaResponse.getUdaljenosti();
        this.mContext = context;
    }

    @Override
    public StatistikaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_statistika, parent, false);
        return new StatistikaViewHolder(view);

    }

    @Override
    public void onBindViewHolder(StatistikaViewHolder holder, int position) {

        Nalog nalog = feedItemListNalozi.get(position);
        String udaljenost = feedItemListUdaljenosti.get(position);

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.setTimeInMillis(Long.parseLong(nalog.getVrijeme_kreiranja())*1000);
        String datum = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(calendar.get(Calendar.MINUTE)) + " "
                + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "."
                + String.valueOf(calendar.get(Calendar.MONTH)+1) + "."
                + String.valueOf(calendar.get(Calendar.YEAR));

        double d = Double.parseDouble(udaljenost);
        DecimalFormat df = new DecimalFormat("#.##");
        double km = Double.parseDouble(df.format(d));

        holder.brojNaloga.setText("Nalog br: " + nalog.getNalog_id());
        holder.brojZadataka.setText("Broj zadataka: " + nalog.getZadaci().size());
        holder.vrijemeKreiranja.setText("Kreirano: " + datum);
        holder.predjenaUdaljenost.setText("Pređeno :" + km + " km");
        //notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (null != feedItemListNalozi ? feedItemListNalozi.size() : 0);
    }
}
