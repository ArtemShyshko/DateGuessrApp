package com.example.dateguessr.view.common;

public interface CustomObservableView<ListenerType> extends CustomView {

    void registerListener(ListenerType listener);
    void unregisterListener(ListenerType listener);
}
