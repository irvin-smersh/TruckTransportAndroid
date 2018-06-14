package com.example.irvin.trucktransport.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.irvin.trucktransport.R;
import com.example.irvin.trucktransport.controllers.NaloziController;
import com.example.irvin.trucktransport.controllers.VozacController;
import com.example.irvin.trucktransport.model.Nalog;
import com.example.irvin.trucktransport.model.Vozac;
import com.example.irvin.trucktransport.utils.ItemClickSupport;
import com.example.irvin.trucktransport.utils.MyRecyclerAdapter;
import com.example.irvin.trucktransport.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;


public class NaloziFragment extends Fragment {

    private static final String TAG = NaloziFragment.class.getSimpleName();

    private Vozac vozac;
    private VozacController vozacController;
    private List<Nalog> nalozi;
    private NaloziController naloziController;
    private SessionManager session;
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter myRecycleAdapter;
    private Nalog nalog;
    OnNalogClickedListener mCallback;
    AlertDialog alertDialog;

    public interface OnNalogClickedListener{
        void OnNalogClicked();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nalozi, container, false);

        nalozi = new ArrayList<>();
        nalog = new Nalog();
        naloziController = new NaloziController(getActivity());
        nalozi = naloziController.getNalozi();

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view_nalozi);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        myRecycleAdapter = new MyRecyclerAdapter(getContext(), nalozi);
        mRecyclerView.setAdapter(myRecycleAdapter);

        alertDialog = new AlertDialog.Builder(getContext()).create();

        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                nalog = nalozi.get(position);
                showDialog(nalog);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void showDialog(final Nalog nalog) {
        alertDialog.setTitle("Potvrda naloga");
        alertDialog.setMessage("Potvrdite da li želite ovaj nalog za izvršenje");

        alertDialog.setButton(-1, "Potvrdi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                session.setNalogAktivni(nalog.getNalog_id());
                mCallback.OnNalogClicked();
                dialog.cancel();

            }
        });

        alertDialog.setButton(-2, "Izađi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;

            try {
                mCallback = (OnNalogClickedListener) a;
            }catch (ClassCastException e){
                throw new ClassCastException(a.toString() + " must implement OnNalogClicked");
            }
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

}
