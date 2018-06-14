package com.example.irvin.trucktransport.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.app.AppConfig;
import com.example.irvin.trucktransport.controllers.PorukeController;
import com.example.irvin.trucktransport.controllers.VozacController;
import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.listeners.DataDownloadedListener;
import com.example.irvin.trucktransport.model.Poruka;
import com.example.irvin.trucktransport.model.QueryBundle;
import com.example.irvin.trucktransport.model.ResultBundle;
import com.example.irvin.trucktransport.model.Vozac;
import com.example.irvin.trucktransport.networking.VolleyTask;
import com.example.irvin.trucktransport.utils.PorukeRecyclerAdapter;
import com.example.irvin.trucktransport.utils.SessionManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PorukeFragment extends Fragment implements DataDownloadedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = PorukeFragment.class.getSimpleName();
    private static final int TEXT_ID = 0;
    private List<Poruka> poruke;
    private RecyclerView mRecycleView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PorukeRecyclerAdapter adapter;
    private ProgressDialog pDialog;
    private FloatingActionButton novaPoruka;
    private LinearLayoutManager mLayoutManager;
    SessionManager session;
    VozacController vozacController;
    PorukeController porukeController;
    Vozac vozac;
    Poruka poruka;
    AlertDialog novaPorukaDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poruke, container, false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecycleView = (RecyclerView)view.findViewById(R.id.recycler_view_poruke);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        novaPoruka = (FloatingActionButton) view.findViewById(R.id.nova_poruka);
        pDialog = new ProgressDialog(getContext());
        session = new SessionManager(getContext());
        vozac = new Vozac();
        vozacController = new VozacController(getContext());
        porukeController = new PorukeController(getContext());
        vozac = vozacController.getVozac(session.getVozacID());
        poruke = new ArrayList<>();
        poruke = vozac.getPoruke();
        poruka = new Poruka();

        swipeRefreshLayout.setOnRefreshListener(this);

        adapter = new PorukeRecyclerAdapter(getContext(), poruke);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setAdapter(adapter);
        novaPorukaDialog = new AlertDialog.Builder(getContext()).create();

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        fetchPoruke();
                                    }
                                }
        );

        novaPoruka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaPorukaDialog();
            }
        });

        return view;
    }

    public void novaPorukaDialog(){

        novaPorukaDialog = new AlertDialog.Builder(getContext()).create();
        novaPorukaDialog.setMessage("Odgovor: ");

        final EditText input = new EditText(getContext());
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
                VolleyTask.addQuery(getContext(), PorukeFragment.this, queryBundle);
                pDialog.setMessage("Slanje poruke...");
                showDialog();
            }
        });

        novaPorukaDialog.setButton(-2, "IZAĐI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getContext(), "Your clicked cancel!", Toast.LENGTH_SHORT).show();
            }
        });

        novaPorukaDialog.show();
    }

    @Override
    public void dataDownloaded(QueryType queryType, ResultBundle object) {
        hideDialog();
        String userResponseString = object.getResult().toString();
        Log.d("fghdgj", userResponseString);
        try {
            JSONObject jObj = new JSONObject(userResponseString);
            if(jObj.getString("error").equals("false")) {
                String response = jObj.getString("poruka");
                Poruka poruka = new Gson().fromJson(response, Poruka.class);
                porukeController.addPoruka(poruka);
                Toast.makeText(getContext(), "Poruka stigla", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void fetchPoruke() {
        swipeRefreshLayout.setRefreshing(true);
        vozac = new Vozac();
        vozacController = new VozacController(getContext());
        vozac = vozacController.getVozac(session.getVozacID());
        poruke = new ArrayList<>();
        poruke = vozac.getPoruke();
        poruke = vozac.getPoruke();
        adapter = new PorukeRecyclerAdapter(getContext(), poruke);
        mRecycleView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        fetchPoruke();
    }


}
