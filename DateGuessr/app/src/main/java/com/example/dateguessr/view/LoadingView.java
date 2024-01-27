package com.example.dateguessr.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.dateguessr.R;
import com.example.dateguessr.view.common.BaseView;

public class LoadingView extends BaseView {
    public LoadingView(LayoutInflater inflater, ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.loading_screen, parent, false));
    }
}
