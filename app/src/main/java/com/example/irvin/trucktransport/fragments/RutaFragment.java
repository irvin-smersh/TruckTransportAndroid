package com.example.irvin.trucktransport.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.irvin.trucktransport.Manifest;
import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.controllers.NaloziController;
import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.listeners.DataDownloadedListener;
import com.example.irvin.trucktransport.model.Nalog;
import com.example.irvin.trucktransport.model.QueryBundle;
import com.example.irvin.trucktransport.model.ResultBundle;
import com.example.irvin.trucktransport.model.StajalisteNalog;
import com.example.irvin.trucktransport.model.Zadatak;
import com.example.irvin.trucktransport.networking.VolleyTask;
import com.example.irvin.trucktransport.services.GpsService;
import com.example.irvin.trucktransport.utils.DirectionsJSONParser;
import com.example.irvin.trucktransport.utils.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class RutaFragment extends Fragment implements OnMapReadyCallback, DataDownloadedListener, com.google.android.gms.location.LocationListener {

    private View view;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Nalog nalog;
    private NaloziController naloziController;
    private SessionManager session;
    private List<Zadatak> zadaci;
    private List<StajalisteNalog> stajalista;
    private FloatingActionButton startNalog;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    List<List<HashMap<String, String>>> routes = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ruta, container, false);

        runtime_permissions();

        startNalog = (FloatingActionButton) view.findViewById(R.id.start_nalog);
        session = new SessionManager(getContext());
        nalog = new Nalog();
        naloziController = new NaloziController(getContext());
        zadaci = new ArrayList<>();
        stajalista = new ArrayList<>();
        if(!session.getNalogAktivni().isEmpty()) {
            nalog = naloziController.getNalogByID(session.getNalogAktivni());
        }
        zadaci = nalog.getZadaci();
        stajalista = nalog.getStajalistaNalozi();
        int v = zadaci.size();
        String origin = zadaci.get(0).getPoznataLokacija().getDuzina() + "," + zadaci.get(0).getPoznataLokacija().getSirina();
        String destination = zadaci.get(v-1).getPoznataLokacija().getDuzina() + "," + zadaci.get(v-1).getPoznataLokacija().getSirina();
        String waypoints = "";
        int zadatak_redni = 1;
        int stajaliste_redni = 0;
        for(int i=1; i<zadaci.size()+stajalista.size(); i++){
            if(i==1)
                waypoints = "&waypoints=";
            //waypoints += zadaci.get(i).getPoznataLokacija().getDuzina() + "," + zadaci.get(i).getPoznataLokacija().getSirina() + "|";
            if(zadatak_redni<zadaci.size()-1) {
                if (Integer.parseInt(zadaci.get(zadatak_redni).getBroj_zadatka()) == i) {
                    waypoints += zadaci.get(zadatak_redni).getPoznataLokacija().getDuzina() + "," + zadaci.get(zadatak_redni).getPoznataLokacija().getSirina() + "|";
                    zadatak_redni++;
                }
            }
            if(stajaliste_redni<stajalista.size()) {
                if (Integer.parseInt(stajalista.get(stajaliste_redni).getBroj_stajalista()) == i) {
                    waypoints += stajalista.get(stajaliste_redni).getStajaliste().getDuzina() + "," + stajalista.get(stajaliste_redni).getStajaliste().getSirina() + "|";
                    stajaliste_redni++;
                }
            }
        }

        String urlRute = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + waypoints + "&key=AIzaSyCih9W228VA-73BZRjfLtwsLLYeVddgSnQ";

        Map<String, String> params = new HashMap<>();
        params.put("trip", "trip");
        params.put("time", "time");

        QueryBundle queryBundle = new QueryBundle(urlRute, QueryType.ALERTS, params);
        VolleyTask.addTask(getContext(), RutaFragment.this, queryBundle);
        Log.e(TAG, "-----ON CREATE-----");

        startNalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nalogAktivniID = session.getNalogAktivni();
                nalog = naloziController.getNalogByID(nalogAktivniID);
                session.setZadatakTrenutni(nalog.getZadaci().get(0).getZadatak_id());
                session.setStajalisteNAlogTrenutni(nalog.getStajalistaNalozi().get(0).getStajaliste_nalog_id());
                String vrijeme = Long.toString(System.currentTimeMillis()/1000);
                session.setVrijeme(vrijeme);
                getActivity().startService(new Intent(getActivity(),GpsService.class));
                startNalog.hide();
            }
        });


        return view;
    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        }
        DrawPolyline(routes);
    }


    @Override
    public void dataDownloaded(QueryType queryType, ResultBundle object) {
        String userResponseString = object.getResult().toString();
        List<String> durations = new ArrayList<>();


        try {
            JSONObject jObj = new JSONObject(userResponseString);
            if(jObj.getString("status").equals("OK")){
                JSONArray jRoutes = jObj.getJSONArray("routes");
                JSONArray jLegs = null;
                for(int i=0;i<jRoutes.length();i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                }
                for(int j=0; j<jLegs.length(); j++){
                    durations.add(((JSONObject) jLegs.get(j)).getJSONObject("duration").get("value").toString());
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (String s : durations){
                    stringBuilder.append(s);
                    stringBuilder.append(",");
                }

                session.setUdaljenosti(stringBuilder.toString());

                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObj);
                Log.i(TAG, "Success " + object.getResult());
            }else{
                Toast.makeText(getContext(), "Location didn't added", Toast.LENGTH_LONG).show();
            }

        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Json error. " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorLoading(QueryType queryType, ResultBundle object) {
        Log.i(TAG, "Error " + object.getResult());
    }

    public void DrawPolyline (List<List<HashMap<String, String>>> result) {
        ArrayList points = null;
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();

        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = result.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(12);
            lineOptions.color(R.color.rute);
            lineOptions.geodesic(true);

        }

// Drawing polyline in the Google Map for the i-th route
        mGoogleMap.addPolyline(lineOptions);

        for(int i=0; i<zadaci.size(); i++){
            double duzina = Double.parseDouble(zadaci.get(i).getPoznataLokacija().getDuzina());
            double sirina = Double.parseDouble(zadaci.get(i).getPoznataLokacija().getSirina());
            String title = zadaci.get(i).getNaziv();
            LatLng latLng = new LatLng(duzina, sirina);
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(title));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }



    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

}