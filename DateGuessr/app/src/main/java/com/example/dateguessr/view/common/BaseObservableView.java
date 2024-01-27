package com.example.dateguessr.view.common;

import java.util.ArrayList;

public abstract class BaseObservableView<ListenerType>
        extends BaseView
        implements CustomObservableView<ListenerType> {

    private final ArrayList<ListenerType> listeners = new ArrayList<>();

    @Override
    public void registerListener(ListenerType listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(ListenerType listener) {
        listeners.remove(listener);
    }

    protected ArrayList<ListenerType> getListeners() {
        return listeners;
    }
}
