package com.example.irvin.trucktransport.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.model.Poruka;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by IvanX on 20.5.2018..
 */

public class PorukeRecyclerAdapter extends RecyclerView.Adapter<PorukeViewHolder> {
    private List<Poruka> feedItemList;
    private Context mContext;

    @Override
    public void onBindViewHolder(PorukeViewHolder holder, int position) {
        Poruka feedItem = feedItemList.get(position);

        GregorianCalendar calendar1 = new GregorianCalendar(TimeZone.getDefault());
        calendar1.setTimeInMillis(Long.parseLong(feedItem.getVrijeme())*1000);
        String datum1 = String.valueOf(calendar1.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(calendar1.get(Calendar.MINUTE)) + " "
                + String.valueOf(calendar1.get(Calendar.DAY_OF_MONTH)) + "."
                + String.valueOf(calendar1.get(Calendar.MONTH)+1) + "."
                + String.valueOf(calendar1.get(Calendar.YEAR));

        holder.vrijeme.setText(datum1);
        holder.text_poruke.setText(feedItem.getText());
    }

    public PorukeRecyclerAdapter(Context context, List<Poruka> feedItemList){
        this.feedItemList = new ArrayList<>();
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public PorukeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_poruke, null);
        PorukeViewHolder viewHolder = new PorukeViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

}
