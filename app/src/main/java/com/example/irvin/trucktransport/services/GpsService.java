package com.example.irvin.trucktransport.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.app.AppConfig;
import com.example.irvin.trucktransport.broadcastReceivers.NotificationDismissedReceiver;
import com.example.irvin.trucktransport.broadcastReceivers.NotificationReceiver;
import com.example.irvin.trucktransport.controllers.GeoTackeController;
import com.example.irvin.trucktransport.controllers.NaloziController;
import com.example.irvin.trucktransport.enums.AppConstant;
import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.listeners.DataDownloadedListener;
import com.example.irvin.trucktransport.model.GeoTacka;
import com.example.irvin.trucktransport.model.Nalog;
import com.example.irvin.trucktransport.model.QueryBundle;
import com.example.irvin.trucktransport.model.ResultBundle;
import com.example.irvin.trucktransport.model.StajalisteNalog;
import com.example.irvin.trucktransport.model.Zadatak;
import com.example.irvin.trucktransport.networking.VolleyTask;
import com.example.irvin.trucktransport.utils.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class GpsService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, DataDownloadedListener {

    private static final String TAG = GpsService.class.getSimpleName();
    //private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    double currentLatitude;
    double currentLongitude;
    private final IBinder mBinder = new LocalBinder();
    SessionManager sesion;
    GeoTackeController geoTackeController;
    private NaloziController naloziController;
    private Nalog nalog;
    private List<Zadatak> zadaci = new ArrayList<>();
    private Zadatak zadatak;
    private Zadatak zadatak_sljedeci;
    private List<StajalisteNalog> stajalistaNalozi = new ArrayList<>();
    private StajalisteNalog stajalisteNalog;
    private StajalisteNalog stajalisteNalog_sljedeci;
    private boolean krajnjeStajaliste;

    public class LocalBinder extends Binder {
        public GpsService getService(){
            return GpsService.this;
        }
    }

    @Override
    public void onCreate() {

        Log.d(TAG, "-----ON CREATE-----");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(10 * 1000); // 1 second, in milliseconds

        sesion = new SessionManager(this);
        geoTackeController = new GeoTackeController(this);
        nalog = new Nalog();
        zadatak = new Zadatak();
        stajalisteNalog = new StajalisteNalog();
        zadatak_sljedeci = new Zadatak();
        stajalisteNalog_sljedeci = new StajalisteNalog();
        naloziController = new NaloziController(this);
        krajnjeStajaliste = false;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "-----ON START COMMAND-----");
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    public LatLng getLatLng(){
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        return latLng;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "-----ON DESTOROY-----");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    public GpsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /*
    * LocationListener Callbacks
    */
    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    /*
    * GooglePlayServicesClient Callbacks
    */
    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(Bundle bundle) {
        Log.e(TAG, "-----USAO U onConnected-----");

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "CONNECTION FAILED.");
    }

    private void handleNewLocation(Location location){
        Log.d(TAG, location.toString());
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        long time = System.currentTimeMillis();
        if(sesion.isLoggedIn()){
            String nalogID = sesion.getNalogAktivni();
            GeoTacka geoTacka = new GeoTacka(null, null,
                    String.valueOf(currentLongitude), String.valueOf(currentLatitude), String.valueOf(time), nalogID);
            geoTackeController.addGeoTacka(geoTacka);

        }

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getDefault());
        calendar.setTimeInMillis(time);
        String datum = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(calendar.get(Calendar.MINUTE)) + ":"
                + String.valueOf(calendar.get(Calendar.SECOND)) + " "
                + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)) + "."
                + String.valueOf(calendar.get(Calendar.MONTH)+1) + "."
                + String.valueOf(calendar.get(Calendar.YEAR));
        Log.d(TAG, "DUZINA: " + currentLongitude + " - SIRINA: " + currentLatitude + " - Vrijeme: " + datum + " u milisekundama: " + time);
        latLng = new LatLng(currentLatitude, currentLongitude);

        float distance_poznata = 0;
        float distance_stajaliste = 0;
        Location currentLocation = new Location("currentlocation");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);

        //Trenutno aktivni nalog
        String nalogAktivniID = sesion.getNalogAktivni();
        nalog = naloziController.getNalogByID(nalogAktivniID);

        //Udaljenosti
        String udaljenostisesija = sesion.getUdaljenosti();
        String[] itemUdaljenosti = udaljenostisesija.split(",");
        List<String> udaljenosti = new ArrayList<>();
        for(int i=0; i<itemUdaljenosti.length; i++){
            udaljenosti.add(itemUdaljenosti[i]);
        }

        /*------------ZADACI---------------*/

        //trazenje trenutnog zadatka
        String zadatak_trenutni_id = sesion.getZadatakTrenutni();
        zadaci = nalog.getZadaci();
        for (int i=0; i<zadaci.size(); i++){
            if(zadaci.get(i).getZadatak_id().equals(zadatak_trenutni_id)){
                zadatak = zadaci.get(i);
                //zadatak_sljedeci = zadaci.get(i+1);
            }
        }
        //trazenje trenutnog zadatka .... mora se u pokretanju naloga definisati i prvi zadatak

        //udaljenost do poznate lokacije trenutnog zadatka
        Location poznataLokacija = new Location("poznataLokacija");
        poznataLokacija.setLatitude(Double.parseDouble(zadatak.getPoznataLokacija().getDuzina()));
        poznataLokacija.setLongitude(Double.parseDouble(zadatak.getPoznataLokacija().getSirina()));
        distance_poznata = currentLocation.distanceTo(poznataLokacija);
        //udaljenost do poznate lokacije trenutnog zadatka




        if(distance_poznata<50){
            if(zadatak.getCheckin().equals("0")){ //nije odradjen checkIN
                Log.i(TAG, "nije odradjen checkin");
                //pokreni notifikaciju za checkIN, ako vec nije pokrenuta

                Intent check_in = new Intent(this, NotificationReceiver.class);
                check_in.putExtra("toastMessage", "zadatak_check_in");
                PendingIntent pendingIntentCheckIN = PendingIntent.getBroadcast(this, 1234, check_in, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder notification = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Zadatak")
                        .setContentText("Stigli ste na lokaciju zadatka, uradite Check-in")
                        .setSmallIcon(R.drawable.ic_stat_announcement)
                        .setAutoCancel(true)
                        .addAction(R.drawable.ic_stat_announcement, "Check-in", pendingIntentCheckIN);

                notification.build().flags |= Notification.FLAG_AUTO_CANCEL;
                //notification.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(10,notification.getNotification());

            }else if(!zadatak.getCheckin().equals("0")){
                Intent check_out = new Intent(this, NotificationReceiver.class);
                check_out.putExtra("toastMessage", "zadatak_check_out");
                PendingIntent pendingIntentCheckOUT = PendingIntent.getBroadcast(this, 1234, check_out, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder notificationCheckOUT = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Zadatak")
                        .setContentText("Ako ste obavili zadatak, uradite Check-out i krenite")
                        .setSmallIcon(R.drawable.ic_stat_announcement)
                        .setAutoCancel(true)
                        .addAction(R.drawable.ic_stat_announcement, "Check-out", pendingIntentCheckOUT);

                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(10,notificationCheckOUT.getNotification());
                //ako je odradjen checkIN onda pokreni notifikaciju za checkOUT ako nije pokrenuta
            }
        }else{ //ako je udaljenost veca od 10m
            if(!zadatak.getCheckin().equals("0") && zadatak.getCheckout().equals("0")){
                Log.i(TAG, "odmakao se od zadatka");
                //izbaci notifikaciju da treba da odradi checkOUT.... odnosno da se vrati u krug
                //od 10m i sama ce mu se notifikacija pokrenuti za checkOut
                //ova notifikacija ne treba da ima button samo da se vrati
                Intent check_out_vrati_se = new Intent(this, NotificationReceiver.class);
                check_out_vrati_se.putExtra("toastMessage", "check_out_vrati_se");
                PendingIntent pendingIntentCheckOUTvratiSe = PendingIntent.getBroadcast(this, 1234, check_out_vrati_se, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder notificationCheckOUT = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Zadatak")
                        .setContentText("Udaljili ste se od lokacije, vratite se i uradite Check-out")
                        .setSmallIcon(R.drawable.ic_stat_announcement)
                        .setAutoCancel(true);
                        //.addAction(R.drawable.cast_ic_notification_small_icon, "Check-out", pendingIntentCheckOUTvratiSe);

                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(10,notificationCheckOUT.getNotification());
            }

            //ostala logika ide u notifikacije
        }
        //ako je udaljenost manja od 10 metara i nije odradjen checkIN pokrenuti notifikaciju ako vec nije pokrenuta
        //a ako je odradjen checkIN onda pokreni notifikaciju za checkOUT ako nije pokrenuta
        //a ako je udaljenost veca od 10 metara a odradjen checkIN a nije CheckOUT onda izbaci jos jednu notifikaciju da treba checkOUZ da odradi

        //nakon CheckOut-a zabiljezi da je zadatak odradjen, predji na sljedeci i sljedecu lokaciju (promijeni trenutni zadatak u sesiji)
        // i pokreni metodu za odredjivanje stajalista


        /*------------ZADACI---------------*/



        /*----------STAJALISTA-------------*/

        String stajalisteNalog_trenutni_id = sesion.getStajalisteNalogTrenutni();
        stajalistaNalozi = nalog.getStajalistaNalozi();
        for (int i=0; i<stajalistaNalozi.size(); i++){
            if(stajalistaNalozi.get(i).getStajaliste_nalog_id().equals(stajalisteNalog_trenutni_id)){
                stajalisteNalog = stajalistaNalozi.get(i);
                if(i+1<stajalistaNalozi.size()){
                    stajalisteNalog_sljedeci = stajalistaNalozi.get(i+1);
                }else{
                    krajnjeStajaliste = true;
                }
            }
        }

        Location narednoStajaliste = new Location("narednoStajaliste");
        narednoStajaliste.setLongitude(Double.parseDouble(stajalisteNalog.getStajaliste().getSirina()));
        narednoStajaliste.setLatitude(Double.parseDouble(stajalisteNalog.getStajaliste().getDuzina()));
        distance_stajaliste = currentLocation.distanceTo(narednoStajaliste);



        if(distance_stajaliste<10){
            if(stajalisteNalog.getCheckin()==null){ //nije odradjen checkIN
                if(!krajnjeStajaliste){
                    int udaljenostDvaStajalista = 0;
                    int udaljenostPocetna = Integer.parseInt(stajalisteNalog.getBroj_stajalista())-1;
                    int udaljenostKrajnja = Integer.parseInt(stajalisteNalog_sljedeci.getBroj_stajalista())-2;

                    for (int i=udaljenostPocetna; i<=udaljenostKrajnja; i++){
                        udaljenostDvaStajalista += Integer.parseInt(udaljenosti.get(i));
                    }
                    long vrijemePocetno = Long.parseLong(sesion.getVrijeme());
                    long vrijemeTrenutno = System.currentTimeMillis()/1000;
                    //zbir vremenske udaljenosti izmedju dva stajalista... ako je veca od 5 min??? onda stani na prvo
                    long vrijemeDoSljedecegStajalista = vrijemePocetno-vrijemeTrenutno+udaljenostDvaStajalista;

                    if(vrijemeDoSljedecegStajalista<300){  // 5 min
                        Intent check_in_stajaliste_opcionalno = new Intent(this, NotificationReceiver.class);
                        check_in_stajaliste_opcionalno.putExtra("toastMessage", "check_in_stajaliste_neobavezno");
                        PendingIntent pendingIntentCheckOUT = PendingIntent.getBroadcast(this, 4321, check_in_stajaliste_opcionalno, PendingIntent.FLAG_UPDATE_CURRENT);

                        /*---------------Dio za swipe out nottification------------------*/
                        Intent stajalisteSwipe = new Intent(this, NotificationDismissedReceiver.class);
                        stajalisteSwipe.putExtra("message", "stajalisteSwipe");
                        PendingIntent createOnDismissedIntent = PendingIntent.getBroadcast(this, 4824, stajalisteSwipe, PendingIntent.FLAG_UPDATE_CURRENT);
                        /*---------------Dio za swipe out nottification------------------*/

                        Notification.Builder notificationCheckINstajalisteOpcionalno = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Stajaliste")
                                .setContentText("Možeš stići na sljedeće stajalište! Ukoliko želiš stati na ovom uradi Check-in, ako ne iznorisi notifikaciju")
                                .setSmallIcon(R.drawable.ic_stat_announcement)
                                .setDeleteIntent(createOnDismissedIntent)
                                .setAutoCancel(true)
                                .addAction(R.drawable.ic_stat_announcement, "Check-in", pendingIntentCheckOUT);

                        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        nm.notify(10,notificationCheckINstajalisteOpcionalno.getNotification());

                    }else{
                        //stani na trenutnom
                        Intent check_in_stajaliste = new Intent(this, NotificationReceiver.class);
                        check_in_stajaliste.putExtra("toastMessage", "check_in_stajaliste");
                        PendingIntent pendingIntentCheckINstajaliste = PendingIntent.getBroadcast(this, 4321, check_in_stajaliste, PendingIntent.FLAG_UPDATE_CURRENT);

                        Notification.Builder notificationCheckINstajaliste = new Notification.Builder(getApplicationContext())
                                .setContentTitle("Stajaliste")
                                .setContentText("Stani na ovom stajalistu, uradi Check-in i napravi pauzu 6 sati")
                                .setSmallIcon(R.drawable.ic_stat_announcement)
                                .setAutoCancel(true)
                                .addAction(R.drawable.ic_stat_announcement, "Check-in", pendingIntentCheckINstajaliste);

                        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                        nm.notify(10,notificationCheckINstajaliste.getNotification());
                    }
                }


            }else if(stajalisteNalog.getCheckout()==null && System.currentTimeMillis()/1000-Long.parseLong(sesion.getVrijemePauze())>300){
                //obavio je pauzu i pokreni notifikaciju za check out
                Intent check_out_stajaliste = new Intent(this, NotificationReceiver.class);
                check_out_stajaliste.putExtra("toastMessage", "check_out_stajaliste");
                PendingIntent pendingIntentCheckOUTstajaliste = PendingIntent.getBroadcast(this, 4321, check_out_stajaliste, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification.Builder notificationCheckOUTstajaliste = new Notification.Builder(getApplicationContext())
                        .setContentTitle("Stajaliste")
                        .setContentText("Pauza je zavrsena, uradi Check-out i kreni")
                        .setSmallIcon(R.drawable.ic_stat_announcement)
                        .setAutoCancel(true)
                        .addAction(R.drawable.ic_stat_announcement, "Check-out", pendingIntentCheckOUTstajaliste);

                NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(10,notificationCheckOUTstajaliste.getNotification());
                //ako je odradjen checkIN onda je vec na stajalistu i samo provjeri vrijeme dali je odmorio i ponudi da krene i odradi check out ako jeste i time postavi novo vrijeme
            }
        }else{ //ako je udaljenost veca od 10m
            if(stajalisteNalog.getCheckin()!=null && stajalisteNalog.getCheckout()==null){
                //notifikacije bez klikova
                if(Long.parseLong(sesion.getVrijemePauze())!=0 && System.currentTimeMillis()/1000-Long.parseLong(sesion.getVrijemePauze())>300){
                    //vrati se i odradi check out
                    Intent check_out_stajaliste_vrati_se = new Intent(this, NotificationReceiver.class);
                    check_out_stajaliste_vrati_se.putExtra("toastMessage", "check_out_stajaliste_vrati_se");
                    PendingIntent pendingIntentCheckOUTstajalisteVratiSe = PendingIntent.getBroadcast(this, 4321, check_out_stajaliste_vrati_se, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification.Builder notificationCheckOUTstajaliste = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Stajaliste")
                            .setContentText("Vrati se na odmaralište i odradi Check-out")
                            .setSmallIcon(R.drawable.ic_stat_announcement)
                            .setAutoCancel(true);
                            //.addAction(R.drawable.cast_ic_notification_small_icon, "Check-out", pendingIntentCheckOUTstajalisteVratiSe);

                    NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(10,notificationCheckOUTstajaliste.getNotification());

                }else if(Long.parseLong(sesion.getVrijemePauze())!=0 && System.currentTimeMillis()/1000-Long.parseLong(sesion.getVrijemePauze())<300){
                    //vrati se i odmaraj do isteka odmora
                    Intent stajaliste_vrati_se_odmaraj = new Intent(this, NotificationReceiver.class);
                    stajaliste_vrati_se_odmaraj.putExtra("toastMessage", "stajaliste_vrati_se_odmaraj");
                    PendingIntent pendingIntentStajalisteVratiSeOdmaraj = PendingIntent.getBroadcast(this, 4321, stajaliste_vrati_se_odmaraj, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification.Builder notificationCheckOUTstajaliste = new Notification.Builder(getApplicationContext())
                            .setContentTitle("Stajaliste")
                            .setContentText("Vrati se na odmaralište i odradi Check-out")
                            .setSmallIcon(R.drawable.ic_stat_announcement)
                            .setAutoCancel(true);
                    //.addAction(R.drawable.cast_ic_notification_small_icon, "Check-out", pendingIntentStajalisteVratiSeOdmaraj);

                    NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(10,notificationCheckOUTstajaliste.getNotification());
                }
            }
        }


        /*----------STAJALISTA-------------*/



        GeoTackeController geoTackeController1 = new GeoTackeController(this);
        GeoTacka geoTackaZadnja = geoTackeController1.getLastGeoTacka();

        /* Slanje lokacije na server*/
        Map<String, String> params = new HashMap<>();

        params.put("duzina", geoTackaZadnja.getDuzina());
        params.put("sirina", geoTackaZadnja.getSirina());
        params.put("vrijeme", geoTackaZadnja.getVrijeme());
        params.put("nalog_id", geoTackaZadnja.getNalog_id());
        params.put("lokalna_id", geoTackaZadnja.getLokalna_id());
        QueryBundle queryBundle = new QueryBundle(AppConfig.URL_SENDGEOTACKA, QueryType.SEND_LOCATIONS, params);
        VolleyTask.addQuery(getApplicationContext(), GpsService.this, queryBundle);


        GregorianCalendar calendar1 = new GregorianCalendar(TimeZone.getDefault());
        calendar1.setTimeInMillis(Long.parseLong(geoTackaZadnja.getVrijeme()));
        String datum1 = String.valueOf(calendar1.get(Calendar.HOUR_OF_DAY)) + ":"
                + String.valueOf(calendar1.get(Calendar.MINUTE)) + ":"
                + String.valueOf(calendar1.get(Calendar.SECOND)) + " "
                + String.valueOf(calendar1.get(Calendar.DAY_OF_MONTH)) + "."
                + String.valueOf(calendar1.get(Calendar.MONTH)+1) + "."
                + String.valueOf(calendar1.get(Calendar.YEAR));


        Log.d(TAG, "DUZINA: " + geoTackaZadnja.getDuzina() + " - SIRINA: " + geoTackaZadnja.getSirina() + " - Vrijeme: " + datum1);
        latLng = new LatLng(currentLatitude, currentLongitude);


    }



    @Override
    public void dataDownloaded(QueryType queryType, ResultBundle object) {
        Log.i(TAG, "SUCCESS " + object.getResult());
    }

    @Override
    public void onErrorLoading(QueryType queryType, ResultBundle object) {
        Log.i(TAG, "Error " + object.getResult());
    }



}