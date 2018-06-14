package com.example.irvin.trucktransport;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.irvin.trucktransport.app.AppConfig;
import com.example.irvin.trucktransport.controllers.GeoTackeController;
import com.example.irvin.trucktransport.controllers.NaloziController;
import com.example.irvin.trucktransport.controllers.PorukeController;
import com.example.irvin.trucktransport.controllers.VozacController;
import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.fragments.PorukeFragment;
import com.example.irvin.trucktransport.fragments.PregledFragment;
import com.example.irvin.trucktransport.fragments.RutaFragment;
import com.example.irvin.trucktransport.fragments.StatistikaFragment;
import com.example.irvin.trucktransport.fragments.NaloziFragment;
import com.example.irvin.trucktransport.fragments.ZadaciFragment;
import com.example.irvin.trucktransport.listeners.DataDownloadedListener;
import com.example.irvin.trucktransport.model.GeoTacka;
import com.example.irvin.trucktransport.model.Nalog;
import com.example.irvin.trucktransport.model.NaloziResponse;
import com.example.irvin.trucktransport.model.Poruka;
import com.example.irvin.trucktransport.model.QueryBundle;
import com.example.irvin.trucktransport.model.ResultBundle;
import com.example.irvin.trucktransport.model.StatistikaResponse;
import com.example.irvin.trucktransport.networking.VolleyTask;
import com.example.irvin.trucktransport.services.GpsService;
import com.example.irvin.trucktransport.services.PorukeService;
import com.example.irvin.trucktransport.utils.SessionManager;
import com.example.irvin.trucktransport.utils.StatistikaRecyclerAdapter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DataDownloadedListener, ZadaciFragment.OnFloatingButtonClickedListener, NaloziFragment.OnNalogClickedListener {

    private static final String TAG = NavigationActivity.class.getSimpleName();
    private CharSequence mTitle;
    private SessionManager session;
    private VozacController vozacController;
    private NaloziController naloziAccess;
    private ProgressDialog pDialog;
    private StatistikaResponse statistikaResponse;
    Fragment fragment;
    GpsService mService;
    AlertDialog sosPorukaDialog;
    private static final int TEXT_ID = 1;
    PorukeController porukeController;
    GeoTackeController geoTackeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManager(this);
        vozacController = new VozacController(this);
        statistikaResponse = new StatistikaResponse();
        naloziAccess = new NaloziController(this);
        porukeController = new PorukeController(this);
        geoTackeController = new GeoTackeController(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        String menuFragment = getIntent().getStringExtra("nova_poruka");
        String kraj = getIntent().getStringExtra("kraj");
        sosPorukaDialog = new AlertDialog.Builder(this).create();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //ovdje dobaviti naloge



        if (menuFragment != null) {

            // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
            if (menuFragment.equals("nova_poruka")) {
                fragment = new PorukeFragment();
                //fragmentTransaction.replace(android.R.id.content, favoritesFragment);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_navigation, fragment)
                        .addToBackStack(null)
                        .commit();
            }

        }

        if (kraj != null) {
            if (kraj.equals("kraj")) {
                //brisanja idu ovde
                //dosli smo do kraja obavili zadnji zadatak treba da ispise poruku
                //oznaci nalog kao obavljen u bazi lokalnoj i serveru,
                // izbrise sve iz sesije osim logina i prebaci se na naloge fragment
                //ugasi gps servis
                Map<String, String> nalog_id_za_kraj = new HashMap<>();
                nalog_id_za_kraj.put("nalog_id", session.getNalogAktivni());
                QueryBundle queryBundleKraj = new QueryBundle(AppConfig.URL_UPDATE_NALOG_KRAJ, QueryType.NALOG_KRAJ, nalog_id_za_kraj);
                VolleyTask.addQuery(getApplicationContext(), NavigationActivity.this, queryBundleKraj);


            }
        }

        Map<String, String> paramsStatistika = new HashMap<>();
        paramsStatistika.put("vozac_id", session.getVozacID());
        QueryBundle queryBundleStatistika = new QueryBundle(AppConfig.URL_GET_STATISTIKA, QueryType.GET_STATISTIKA, paramsStatistika);
        VolleyTask.addQuery(getApplicationContext(), NavigationActivity.this, queryBundleStatistika);


        if(naloziAccess.getNalozi().size()==0){
            Map<String, String> params = new HashMap<>();
            params.put("vozac_id", session.getVozacID());
            QueryBundle queryBundle = new QueryBundle(AppConfig.URL_GETNALOZI, QueryType.NALOG, params);
            VolleyTask.addQuery(getApplicationContext(), NavigationActivity.this, queryBundle);
            pDialog.setMessage("Logging in ...");
            showDialog();
        }
    }

    @Override
    protected void onPostResume() {
        if(session.isLoggedIn()){
            startService(new Intent(getBaseContext(), PorukeService.class));
        }
        super.onPostResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sos) {
            if(!session.getZadatakTrenutni().equals("")){
                novaPorukaDialog();
            }else{
                return true;
            }
        }

        if (id == R.id.action_logout) {
            session.setLogin(false, "");
            session.setZadatakTrenutni("");
            session.setNalogAktivni("");
            session.setStajalisteNAlogTrenutni("");
            session.setVrijeme("");
            session.setVrijemePauze("");
            session.setUdaljenosti("");
            vozacController.deleteVozac();
            naloziAccess.deleteNalozi();
            stopService(new Intent(this,GpsService.class));
            stopService(new Intent(this,PorukeService.class));
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragment = null;

        if (id == R.id.nav_nalozi) {
            if(session.getNalogAktivni().equals("")){
                fragment = new NaloziFragment();
                mTitle = "Nalozi";
            }
        }else if(id == R.id.nav_ruta){
            if(!session.getNalogAktivni().equals("")){
                fragment = new RutaFragment();
                mTitle = "Ruta";
            }
        } else if (id == R.id.nav_zadaci) {
            if(!session.getNalogAktivni().equals("")){
                fragment = new ZadaciFragment();
                mTitle = "Zadaci";
            }
        } else if (id == R.id.nav_poruke) {
            fragment = new PorukeFragment();
            mTitle = "Poruke";
        } else if (id == R.id.nav_pregled) {
            if(!session.getNalogAktivni().equals("")){
                fragment = new PregledFragment();
                mTitle = "Pregled";
            }
        } else if (id == R.id.nav_statistika) {
            fragment = new StatistikaFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("statistika", statistikaResponse);
            fragment.setArguments(bundle);
            mTitle = "Statistika";
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_navigation, fragment)
                    .addToBackStack(null)
                    .commit();

            getSupportActionBar().setTitle(mTitle);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else {
            // error in creating fragment
            Log.e("SplashActivity", "Error in creating fragment");
            return false;
        }

    }

    @Override
    public void dataDownloaded(QueryType queryType, ResultBundle object) {
        hideDialog();
        String naloziResponseString = object.getResult().toString();
        if(queryType == QueryType.NALOG){
            try {
                JSONObject jObj = new JSONObject(naloziResponseString);
                String response = jObj.toString();
                NaloziResponse naloziResponse = new Gson().fromJson(response, NaloziResponse.class);

                naloziResponse.isError();
                if (!naloziResponse.isError()){
                    naloziAccess.addNalozi(naloziResponse.getNalozi());
                }else{
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }

            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Json error. " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        if(queryType == QueryType.NALOG_KRAJ){
            naloziAccess.updateNalogStanje(session.getNalogAktivni(), "3");
            session.setUdaljenosti("");
            session.setVrijemePauze("");
            session.setVrijeme("");
            session.setStajalisteNAlogTrenutni("");
            session.setNalogAktivni("");
            session.setZadatakTrenutni("");
            naloziAccess.deleteNalozi();
            stopService(new Intent(this,GpsService.class));

            Toast.makeText(getApplicationContext(), "Ospješno ste obavili nalog. Čestitamo!!!", Toast.LENGTH_LONG).show();
            fragment = new NaloziFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_navigation, fragment)
                    .addToBackStack(null)
                    .commit();
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
                    Toast.makeText(this, "SOS poruka stigla", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                }

            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(this, "Json error. " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if(queryType == QueryType.GET_STATISTIKA){
            hideDialog();
            String userResponseString = object.getResult().toString();
            try {
                JSONObject jObj = new JSONObject(userResponseString);
                if(jObj.getString("error").equals("false")) {
                    String response = jObj.toString();
                    statistikaResponse = new Gson().fromJson(response, StatistikaResponse.class);
                    hideDialog();
                }else{
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                }

            }catch (JSONException e){
                e.printStackTrace();
                Toast.makeText(this, "Json error. " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void OnNalogClicked() {
        fragment = new ZadaciFragment();
        mTitle = "Zadaci";

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_navigation, fragment)
                .addToBackStack(null)
                .commit();

        getSupportActionBar().setTitle(mTitle);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onFloatingButtonClicked() {

        fragment = new RutaFragment();
        mTitle = "Ruta";

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_navigation, fragment)
                .addToBackStack(null)
                .commit();

        getSupportActionBar().setTitle(mTitle);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


    }

    public void novaPorukaDialog(){

        sosPorukaDialog = new AlertDialog.Builder(this).create();
        sosPorukaDialog.setMessage("SOS Poruka: ");

        final EditText input = new EditText(this);
        input.setId(TEXT_ID);
        sosPorukaDialog.setView(input);

        sosPorukaDialog.setButton(-1, "POŠALJI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = input.getText().toString();
                long time = System.currentTimeMillis();
                //ubaciti SOS i lat i long
                GeoTacka geoTacka = new GeoTacka();
                geoTacka = geoTackeController.getLastGeoTacka();
                Map<String, String> params = new HashMap<>();
                params.put("text_poruke", "SOS, " + geoTacka.getSirina() + ";" + geoTacka.getDuzina() + ", " + value);
                params.put("vrijeme", String.valueOf(time / 1000));
                params.put("vozac_id", session.getVozacID());
                QueryBundle queryBundle = new QueryBundle(AppConfig.URL_SENDPORUKA, QueryType.REPLY, params);
                VolleyTask.addQuery(getApplicationContext(), NavigationActivity.this, queryBundle);
                pDialog.setMessage("Slanje SOS poruke...");
                showDialog();
            }
        });

        sosPorukaDialog.setButton(-2, "IZAĐI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        sosPorukaDialog.show();
    }
}
