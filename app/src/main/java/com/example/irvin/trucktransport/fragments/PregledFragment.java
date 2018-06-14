package com.example.irvin.trucktransport.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.controllers.GeoTackeController;
import com.example.irvin.trucktransport.controllers.NaloziController;
import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.listeners.DataDownloadedListener;
import com.example.irvin.trucktransport.model.GeoTacka;
import com.example.irvin.trucktransport.model.Nalog;
import com.example.irvin.trucktransport.model.QueryBundle;
import com.example.irvin.trucktransport.model.ResultBundle;
import com.example.irvin.trucktransport.model.Zadatak;
import com.example.irvin.trucktransport.networking.VolleyTask;
import com.example.irvin.trucktransport.utils.DirectionsJSONParser;
import com.example.irvin.trucktransport.utils.SessionManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class PregledFragment extends Fragment implements OnMapReadyCallback{
    private static final String TAG = PregledFragment.class.getSimpleName();
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private GeoTackeController geoTackeController;
    private List<GeoTacka> geoTacke;
    private List<LatLng> points;
    private SessionManager session;
    private Nalog nalog;
    private NaloziController naloziController;
    private List<Zadatak> zadaci;
    private Zadatak trenutniZadatak;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nalog = new Nalog();
        naloziController = new NaloziController(getContext());
        zadaci = new ArrayList<>();
        trenutniZadatak = new Zadatak();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pregled, container, false);
        session = new SessionManager(getContext());


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.map_pregled);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        points = new ArrayList<>();
        geoTackeController = new GeoTackeController(getContext());
        geoTacke = new ArrayList<>();
        geoTacke = geoTackeController.getAllGeoTacka();

        for(int i=0; i<geoTacke.size(); i++){
            double lat = Double.parseDouble(geoTacke.get(i).getSirina());
            double lng = Double.parseDouble(geoTacke.get(i).getDuzina());
            LatLng position = new LatLng(lat, lng);

            points.add(position);
        }
        //dobaviti sve tacke iz baze i staviti ih u listu points


        PolylineOptions lineOptions = null;
        lineOptions = new PolylineOptions();
        lineOptions.addAll(points);
        lineOptions.width(12);
        lineOptions.color(Color.BLUE);
        lineOptions.geodesic(true);
        mGoogleMap.addPolyline(lineOptions);

        nalog = naloziController.getNalogByID(session.getNalogAktivni());
        zadaci = nalog.getZadaci();

        for(int i=0; i<zadaci.size(); i++){
            if(zadaci.get(i).getZadatak_id().equals(session.getZadatakTrenutni())){
                trenutniZadatak = zadaci.get(i);
            }
        }

        double lat = Double.parseDouble(trenutniZadatak.getPoznataLokacija().getDuzina());
        double lng = Double.parseDouble(trenutniZadatak.getPoznataLokacija().getSirina());

        LatLng latLng = new LatLng(lat, lng);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        //mGoogleMap.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
    }

}
