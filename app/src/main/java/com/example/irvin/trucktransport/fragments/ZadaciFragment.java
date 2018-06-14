package com.example.irvin.trucktransport.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.controllers.NaloziController;
import com.example.irvin.trucktransport.model.Nalog;
import com.example.irvin.trucktransport.utils.SessionManager;
import com.example.irvin.trucktransport.utils.ZadaciRecyclerAdapter;


public class ZadaciFragment extends Fragment {

    private static final String TAG = ZadaciFragment.class.getSimpleName();

    private Nalog nalog;
    private NaloziController naloziController;
    private SessionManager session;
    private RecyclerView mRecyclerView;
    private ZadaciRecyclerAdapter zadaciRecyclerAdapter;
    private FloatingActionButton fab;
    OnFloatingButtonClickedListener mCallback;

    public interface OnFloatingButtonClickedListener {
        void onFloatingButtonClicked();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zadaci, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        nalog = new Nalog();
        naloziController = new NaloziController(getContext());
        if(!session.getNalogAktivni().isEmpty()){
            nalog = naloziController.getNalogByID(session.getNalogAktivni());
            mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_zadaci);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            zadaciRecyclerAdapter = new ZadaciRecyclerAdapter(getContext(), nalog.getZadaci());
            mRecyclerView.setAdapter(zadaciRecyclerAdapter);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onFloatingButtonClicked();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;

            try {
                mCallback = (OnFloatingButtonClickedListener) a;
            }catch (ClassCastException e){
                throw new ClassCastException(a.toString() + " must implement OnFloatingButtonClicked");
            }
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }
}