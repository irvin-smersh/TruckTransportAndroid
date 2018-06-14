package com.example.irvin.trucktransport.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.irvin.trucktransport.NavigationActivity;
import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.app.AppConfig;
import com.example.irvin.trucktransport.broadcastReceivers.NotificationReceiver;
import com.example.irvin.trucktransport.controllers.PorukeController;
import com.example.irvin.trucktransport.controllers.VozacController;
import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.fragments.PorukeFragment;
import com.example.irvin.trucktransport.listeners.DataDownloadedListener;
import com.example.irvin.trucktransport.model.Poruka;
import com.example.irvin.trucktransport.model.QueryBundle;
import com.example.irvin.trucktransport.model.ResultBundle;
import com.example.irvin.trucktransport.model.Vozac;
import com.example.irvin.trucktransport.networking.VolleyTask;
import com.example.irvin.trucktransport.utils.SessionManager;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IvanX on 21.5.2018..
 */

public class PorukeService extends Service implements DataDownloadedListener{

    private Timer timer = new Timer();
    private Context ctx;
    private static final String TAG = PorukeService.class.getSimpleName();
    SessionManager session;
    VozacController vozacController;
    Vozac vozac;
    private long get_time;
    PorukeController porukeController;
    NotificationCompat.Builder mBuilder;
    int notificationID = 0;

    public PorukeService(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        startService();
        session = new SessionManager(ctx);
        get_time = System.currentTimeMillis()/1000;
        vozacController = new VozacController(getApplicationContext());
        vozac = new Vozac();
        vozac = vozacController.getVozac(session.getVozacID());
        porukeController = new PorukeController(this);
    }

    public void startService() {
        mBuilder = new NotificationCompat.Builder(this);
        timer.scheduleAtFixedRate(new mainTask(), 10 * 1000, 10 * 1000);
    }

    public class mainTask extends TimerTask {
        public void run(){
            String time = String.valueOf(get_time);
            Log.d(TAG, "-----GET MESSAGES-----" + time);
            Map<String, String> params = new HashMap<>();
            params.put("vozac_id", vozac.getVozac_id());
            params.put("vrijeme", time);
            get_time = System.currentTimeMillis()/1000;
            Log.i(TAG, "vrijeme: " + time);
            QueryBundle queryBundle = new QueryBundle(AppConfig.URL_NOVA_PORUKA, QueryType.NEW_MESSAGES, params);
            VolleyTask.addQuery(getApplicationContext(), PorukeService.this, queryBundle);
        }
    }


    @Override
    public void dataDownloaded(QueryType queryType, ResultBundle object) {
        String userNewMessages = object.getResult().toString();
        Log.d("fghdgj", userNewMessages);
        List<Poruka> poruke = Arrays.asList(new Gson().fromJson(userNewMessages, Poruka[].class));
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Intent openProfile = new Intent(this, NavigationActivity.class);
        openProfile.putExtra("nova_poruka", "nova_poruka");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NavigationActivity.class);
        stackBuilder.addNextIntent(openProfile);
        PendingIntent pi = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        for (Poruka poruka:poruke) {
            mBuilder.setSmallIcon(R.drawable.ic_stat_forum);
            mBuilder.setContentTitle("Nova poruka");
            mBuilder.setContentText(poruka.getText());
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(poruka.getText()));
            mBuilder.setContentIntent(pi);
            //mBuilder.addAction(R.drawable.notification_icon_message, "Odgovori", pendingIntentNovaPoruka);
            mNotificationManager.notify(notificationID, mBuilder.build());
            notificationID++;
            porukeController.addPoruka(poruka);
        }
    }

    @Override
    public void onErrorLoading(QueryType queryType, ResultBundle object) {
        Log.i(TAG, "Error " + object.getResult());
    }

    @Override
    public void onDestroy() {
        Log.d("Destroy", "ON DESTROY MESSAGE SERVICE");
        timer.cancel();
        super.onDestroy();
    }
}
