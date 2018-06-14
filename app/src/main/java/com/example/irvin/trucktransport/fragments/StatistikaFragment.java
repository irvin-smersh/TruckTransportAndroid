package com.example.irvin.trucktransport.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.app.AppConfig;
import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.listeners.DataDownloadedListener;
import com.example.irvin.trucktransport.model.Poruka;
import com.example.irvin.trucktransport.model.QueryBundle;
import com.example.irvin.trucktransport.model.ResultBundle;
import com.example.irvin.trucktransport.model.StatistikaResponse;
import com.example.irvin.trucktransport.networking.VolleyTask;
import com.example.irvin.trucktransport.utils.MyRecyclerAdapter;
import com.example.irvin.trucktransport.utils.SessionManager;
import com.example.irvin.trucktransport.utils.StatistikaRecyclerAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.irvin.trucktransport.app.AppConfig.URL_GET_STATISTIKA;

public class StatistikaFragment extends Fragment{
    private static final String TAG = StatistikaFragment.class.getSimpleName();
    private SessionManager session;
    private RecyclerView mRecyclerView;
    private StatistikaRecyclerAdapter statistikaRecyclerAdapter;
    private ProgressDialog pDialog;
    private StatistikaResponse statistikaResponse;

    public StatistikaFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statistikaResponse = new StatistikaResponse();
        statistikaResponse = (StatistikaResponse) getArguments().getSerializable("statistika");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_statistika, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_statistika);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        statistikaRecyclerAdapter = new StatistikaRecyclerAdapter(getContext(), statistikaResponse);
        mRecyclerView.setAdapter(statistikaRecyclerAdapter);

        return view;
    }
}
