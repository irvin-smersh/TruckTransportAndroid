package com.example.irvin.trucktransport.broadcastReceivers;

import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.irvin.trucktransport.LoginActivity;
import com.example.irvin.trucktransport.NavigationActivity;
import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.app.AppConfig;
import com.example.irvin.trucktransport.controllers.CheckInOutController;
import com.example.irvin.trucktransport.controllers.NaloziController;
import com.example.irvin.trucktransport.controllers.PorukeController;
import com.example.irvin.trucktransport.controllers.VozacController;
import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.fragments.PorukeFragment;
import com.example.irvin.trucktransport.listeners.DataDownloadedListener;
import com.example.irvin.trucktransport.model.Nalog;
import com.example.irvin.trucktransport.model.Poruka;
import com.example.irvin.trucktransport.model.QueryBundle;
import com.example.irvin.trucktransport.model.ResultBundle;
import com.example.irvin.trucktransport.model.StajalisteNalog;
import com.example.irvin.trucktransport.model.Vozac;
import com.example.irvin.trucktransport.model.Zadatak;
import com.example.irvin.trucktransport.networking.VolleyTask;
import com.example.irvin.trucktransport.utils.SessionManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IvanX on 16.5.2018..
 */

public class NotificationReceiver extends BroadcastReceiver implements DataDownloadedListener{

    private static final String TAG = NotificationReceiver.class.getSimpleName();
    SessionManager sesion;
    CheckInOutController checkInOutController;
    NaloziController naloziController;
    String checkInZadatak;
    String checkOutZadatak;
    String checkInStajaliste;
    String checkOutStajaliste;
    Nalog nalog;
    AlertDialog novaPorukaDialog;
    private ProgressDialog pDialog;
    PorukeController porukeController;
    SessionManager session;
    private static final int TEXT_ID = 0;

    @Override
    public void onReceive(final Context context, Intent intent) {
        String message = intent.getStringExtra("toastMessage");
        sesion = new SessionManager(context);
        checkInOutController = new CheckInOutController(context);
        naloziController = new NaloziController(context);
        nalog = new Nalog();
        pDialog = new ProgressDialog(context);
        session = new SessionManager(context);
        porukeController = new PorukeController(context);
        novaPorukaDialog = new AlertDialog.Builder(context).create();

        if(message.equals("zadatak_check_in")){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //upisi check-in na server bazu i u lokalnu

            checkInZadatak = Long.toString(System.currentTimeMillis()/1000);
            Map<String, String> params = new HashMap<>();
            params.put("zadatak_id", sesion.getZadatakTrenutni());
            params.put("check_in", checkInZadatak);
            QueryBundle queryBundle = new QueryBundle(AppConfig.URL_ZADATAK_CHECK_IN, QueryType.ZADATAK_CHECK_IN, params);
            VolleyTask.addQuery(context, NotificationReceiver.this, queryBundle);
            NotificationManagerCompat.from(context).cancel(10);
        }
        if(message.equals("zadatak_check_out")){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            nalog = naloziController.getNalogByID(sesion.getNalogAktivni());
            String zadatak_trenutni_id = sesion.getZadatakTrenutni();
            List<Zadatak> zadaci = new ArrayList<>();
            zadaci = nalog.getZadaci();
            boolean zadnji = false;
            Zadatak zadatak = new Zadatak();
            Zadatak zadatak_sljedeci = new Zadatak();
            for (int i=0; i<zadaci.size(); i++){
                if(zadaci.get(i).getZadatak_id().equals(zadatak_trenutni_id)){
                    zadatak = zadaci.get(i);
                    if(i+1==zadaci.size()) {
                        zadnji = true;
                    }
                }
            }
            //upisi check-out na bazu u serveru i u lokalnu te promijeni zadatak trenutni u sesiji

            checkOutZadatak = Long.toString(System.currentTimeMillis()/1000);
            Map<String, String> params = new HashMap<>();
            params.put("zadatak_id", sesion.getZadatakTrenutni());
            params.put("check_out", checkOutZadatak);
            QueryBundle queryBundle = new QueryBundle(AppConfig.URL_ZADATAK_CHECK_OUT, QueryType.ZADATAK_CHECK_OUT, params);
            VolleyTask.addQuery(context, NotificationReceiver.this, queryBundle);
            if(zadnji){
                Intent intentKraj = new Intent(context, NavigationActivity.class);
                intentKraj.putExtra("kraj", "kraj");
                context.startActivity(intentKraj);
                NotificationManagerCompat.from(context).cancel(10);
            }
            NotificationManagerCompat.from(context).cancel(10);
        }
        if(message.equals("check_in_stajaliste_neobavezno")){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //upisi check_in u bazu na server i lokalnu za stajaliste i upisi vrijeme pauze

            checkInStajaliste = Long.toString(System.currentTimeMillis()/1000);
            Map<String, String> params = new HashMap<>();
            params.put("stajaliste_nalog_id", sesion.getStajalisteNalogTrenutni());
            params.put("check_in", checkInStajaliste);
            QueryBundle queryBundle = new QueryBundle(AppConfig.URL_STAJALISTE_CHECK_IN, QueryType.STAJALISTE_CHECK_IN, params);
            VolleyTask.addQuery(context, NotificationReceiver.this, queryBundle);
            NotificationManagerCompat.from(context).cancel(10);
        }
        if(message.equals("check_in_stajaliste")){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //upisi check_in u bazu na server i lokalnu za stajaliste i upisi vrijeme pauze

            checkInStajaliste = Long.toString(System.currentTimeMillis()/1000);
            Map<String, String> params = new HashMap<>();
            params.put("stajaliste_nalog_id", sesion.getStajalisteNalogTrenutni());
            params.put("check_in", checkInStajaliste);
            QueryBundle queryBundle = new QueryBundle(AppConfig.URL_STAJALISTE_CHECK_IN, QueryType.STAJALISTE_CHECK_IN, params);
            VolleyTask.addQuery(context, NotificationReceiver.this, queryBundle);
            NotificationManagerCompat.from(context).cancel(10);
        }
        if(message.equals("check_out_stajaliste")){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            //upisi check_out na bazu na serveru i lokalnu za stajaliste, ponisti vrijeme pauze (na 0), postavi vrijeme(pocetka) na trenutno i promijeni sljedece stajaliste

            checkOutStajaliste = Long.toString(System.currentTimeMillis()/1000);
            Map<String, String> params = new HashMap<>();
            params.put("stajaliste_nalog_id", sesion.getStajalisteNalogTrenutni());
            params.put("check_out", checkOutStajaliste);
            QueryBundle queryBundle = new QueryBundle(AppConfig.URL_STAJALISTE_CHECK_OUT, QueryType.STAJALISTE_CHECK_OUT, params);
            VolleyTask.addQuery(context, NotificationReceiver.this, queryBundle);
            NotificationManagerCompat.from(context).cancel(10);
        }

        if(message.equals("nova_poruka")){
            novaPorukaDialog = new AlertDialog.Builder(context).create();
            novaPorukaDialog.setMessage("Your Reply: ");

            final EditText input = new EditText(context);
            input.setId(TEXT_ID);
            novaPorukaDialog.setView(input);

            novaPorukaDialog.setButton(-1, "POŠALJI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String value = input.getText().toString();
                    long time = System.currentTimeMillis();

                    Map<String, String> params = new HashMap<>();
                    params.put("text_poruke", value);
                    params.put("vrijeme", String.valueOf(time / 1000));
                    params.put("vozac_id", session.getVozacID());
                    QueryBundle queryBundle = new QueryBundle(AppConfig.URL_SENDPORUKA, QueryType.REPLY, params);
                    VolleyTask.addQuery(context, NotificationReceiver.this, queryBundle);
                    pDialog.setMessage("Slanje poruke...");
                    showDialog();
                }
            });

            novaPorukaDialog.setButton(-2, "IZAĐI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(context, "Your clicked cancel!", Toast.LENGTH_SHORT).show();
                }
            });

            novaPorukaDialog.show();
        }


    }

    @Override
    public void dataDownloaded(QueryType queryType, ResultBundle object) {

        if(queryType == QueryType.ZADATAK_CHECK_IN){
            //ako je success upisi u lokalnu
            String result = object.getResult().toString();
            if(result.equals("success")){
                checkInOutController.updateCheckInZadatak(checkInZadatak, sesion.getZadatakTrenutni());
            }
        }
        if(queryType == QueryType.ZADATAK_CHECK_OUT){
            //ako je success upisi u lokalnu i promijeni zadatak trenutni u sesiji
            String result = object.getResult().toString();
            if(result.equals("success")){
                checkInOutController.updateCheckOutZadatak(checkOutZadatak, sesion.getZadatakTrenutni());
                //prmojeni zadatak trenutni u sesiji
                nalog = naloziController.getNalogByID(sesion.getNalogAktivni());
                String zadatak_trenutni_id = sesion.getZadatakTrenutni();
                List<Zadatak> zadaci = new ArrayList<>();
                zadaci = nalog.getZadaci();
                Zadatak zadatak = new Zadatak();
                Zadatak zadatak_sljedeci = new Zadatak();
                for (int i=0; i<zadaci.size(); i++){
                    if(zadaci.get(i).getZadatak_id().equals(zadatak_trenutni_id)){
                        zadatak = zadaci.get(i);
                        if(i+1<zadaci.size()){
                            zadatak_sljedeci = zadaci.get(i+1);
                            sesion.setZadatakTrenutni(zadatak_sljedeci.getZadatak_id());
                        }

                    }
                }
            }
        }
        if(queryType == QueryType.STAJALISTE_CHECK_IN){
            //ako je success upisi u lokalnu i upisi vrijeme pauze u sesiji
            String result = object.getResult().toString();
            if(result.equals("success")){
                checkInOutController.updateCheckInStajaliste(checkInStajaliste, sesion.getStajalisteNalogTrenutni());
                sesion.setVrijemePauze(Long.toString(System.currentTimeMillis()/1000));
            }
        }
        if(queryType == QueryType.STAJALISTE_CHECK_OUT){
            //ako je success upisi u lokalnu i ponisti vrijeme pauze u sesiji na 0 i postavi novo vrijeme pocetka na trenutno
            String result = object.getResult().toString();
            if(result.equals("success")){
                checkInOutController.updateCheckOutStajaliste(checkOutStajaliste, sesion.getStajalisteNalogTrenutni());
                sesion.setVrijemePauze("0");
                sesion.setVrijeme(Long.toString(System.currentTimeMillis()/1000));
                //promijenio sljedece stajaliste podigni za jedan
                nalog = naloziController.getNalogByID(sesion.getNalogAktivni());
                String stajalisteNalog_trenutni_id = sesion.getStajalisteNalogTrenutni();
                List<StajalisteNalog> stajalistaNalozi = new ArrayList<>();
                stajalistaNalozi = nalog.getStajalistaNalozi();
                StajalisteNalog stajalisteNalog = new StajalisteNalog();
                StajalisteNalog stajalisteNalog_sljedeci = new StajalisteNalog();
                for (int i=0; i<stajalistaNalozi.size(); i++){
                    if(stajalistaNalozi.get(i).getStajaliste_nalog_id().equals(stajalisteNalog_trenutni_id)){
                        if(i+1<stajalistaNalozi.size()) {
                            stajalisteNalog_sljedeci = stajalistaNalozi.get(i + 1);
                            sesion.setStajalisteNAlogTrenutni(stajalisteNalog_sljedeci.getStajaliste_nalog_id());
                        }
                    }
                }

            }
        }

        if(queryType == QueryType.REPLY){
            hideDialog();
            String userResponseString = object.getResult().toString();
            Log.d("fghdgj", userResponseString);
            try {
                JSONObject jObj = new JSONObject(userResponseString);
                if(jObj.getString("error").equals("false")) {
                    String response = jObj.getString("poruka");
                    Poruka poruka = new Gson().fromJson(response, Poruka.class);
                    porukeController.addPoruka(poruka);
                    //Toast.makeText(, "Poruka stigla", Toast.LENGTH_LONG).show();
                }else{
                    //Toast.makeText(context, "Error", Toast.LENGTH_LONG).show();
                }

            }catch (JSONException e){
                e.printStackTrace();
                //Toast.makeText(, "Json error. " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onErrorLoading(QueryType queryType, ResultBundle object) {
        Log.i(TAG, "Error " + object.getResult());
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
