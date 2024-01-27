package com.example.dateguessr.view.common;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

public abstract class BaseView implements CustomView {

    private View rootView;

    protected void setRootView(View rootView) {
        this.rootView = rootView;
    }

    protected View findViewById(int id) {
        return getRootView().findViewById(id);
    }

    protected Context getContext() {
        return getRootView().getContext();
    }

    protected Resources getResources() {
        return getContext().getResources();
    }

    @Override
    public View getRootView() {
        return rootView;
    }
}
