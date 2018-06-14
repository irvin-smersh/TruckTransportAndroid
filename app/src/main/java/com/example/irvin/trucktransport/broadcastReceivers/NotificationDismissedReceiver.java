package com.example.irvin.trucktransport.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.irvin.trucktransport.controllers.NaloziController;
import com.example.irvin.trucktransport.model.Nalog;
import com.example.irvin.trucktransport.model.StajalisteNalog;
import com.example.irvin.trucktransport.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IvanX on 10.6.2018..
 */

public class NotificationDismissedReceiver extends BroadcastReceiver {
    SessionManager session;
    NaloziController naloziController;
    Nalog nalog;
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        session = new SessionManager(context);
        naloziController = new NaloziController(context);
        nalog = new Nalog();


        if(message.equals("stajalisteSwipe")){
            nalog = naloziController.getNalogByID(session.getNalogAktivni());
            String stajalisteNalog_trenutni_id = session.getStajalisteNalogTrenutni();
            List<StajalisteNalog> stajalistaNalozi = new ArrayList<>();
            stajalistaNalozi = nalog.getStajalistaNalozi();
            StajalisteNalog stajalisteNalog = new StajalisteNalog();
            StajalisteNalog stajalisteNalog_sljedeci = new StajalisteNalog();
            for (int i=0; i<stajalistaNalozi.size(); i++){
                if(stajalistaNalozi.get(i).getStajaliste_nalog_id().equals(stajalisteNalog_trenutni_id)){
                    if(i+1<stajalistaNalozi.size()) {
                        stajalisteNalog_sljedeci = stajalistaNalozi.get(i + 1);
                        session.setStajalisteNAlogTrenutni(stajalisteNalog_sljedeci.getStajaliste_nalog_id());
                    }
                }
            }
        }
    }
}
