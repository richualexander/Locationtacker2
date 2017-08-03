package com.example.richu.locationtacker.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.richu.locationtacker.R;

/**
 * Created by richu on 20/07/17.
 */
public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empt
        // y public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment,container,false);
    }
}
