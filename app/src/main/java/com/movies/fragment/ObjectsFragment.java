package com.movies.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.movies.adapter.ApObjects;
import com.movies.setting.item_mov;
import com.statailo.cinemaxhd.R;

import java.util.ArrayList;

public class ObjectsFragment extends Fragment {
    static ArrayList<item_mov> objects;
    public RecyclerView recyclerView;
    ApObjects adapter;


    public static ObjectsFragment newInstance(ArrayList<item_mov> categoryId) {
        ObjectsFragment f = new ObjectsFragment();
        objects=categoryId;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recyclerview, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new ApObjects(getActivity(), objects);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
