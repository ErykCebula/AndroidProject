package com.example.hawkish.arschedule;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class FragmentUz extends Fragment {
    RecyclerView recyclerView;
    AdapterUz adapter;
    ProgressDialog progress;
    private View v;
    private List<ModelUz> uzList = new ArrayList<>();
    private List<String> mDataKey = new ArrayList<>();
    SearchView sv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_uz, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        init();
        dismissKeyboardRV();
        progresLoad();
        loadData();
        searchViewLoad();
        listenForSearchTab();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.onDestroy();
        adapter = null;
        uzList.clear();
        mDataKey.clear();

    }

    private void init() {
        sv = Objects.requireNonNull(getActivity()).findViewById(R.id.mSearch);
        recyclerView = v.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterUz(getContext(), uzList);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void dismissKeyboardRV() {
        recyclerView.setOnTouchListener((v, event) -> {

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            return false;
        });
    }

    private void progresLoad() {
        progress = new ProgressDialog(getContext());
        progress.setTitle("Loading");
        progress.setMessage("Loading Lecturers ");
        progress.setCancelable(false);
        progress.show();
    }

    private void loadData() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Lecturers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uzList.clear();
                mDataKey.clear();
                for (DataSnapshot single : dataSnapshot.getChildren()) {
                    uzList.add(single.getValue(ModelUz.class));
                    mDataKey.add(single.getKey());
                }
                listSort();
                progress.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listSort() {
        Collections.sort(uzList, (w1, w2) -> w1.getName().compareToIgnoreCase(w2.getName()));
    }

    private void searchViewLoad() {
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sv.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                adapter.getFilter().filter(query);
                return false;
            }
        });
    }

    private void listenForSearchTab() {
        sv.setOnClickListener(v -> sv.setIconified(false));

    }



}
