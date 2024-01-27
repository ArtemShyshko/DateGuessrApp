package com.example.dateguessr.view;

import com.example.dateguessr.view.common.CustomObservableView;

import java.util.Map;

public interface ResultsView extends CustomObservableView<ResultsView.Listener> {
    public interface Listener {
        void onNewIterationClicked();
    }

    void setFinalStage(int totalScore, Map<String, Integer> scores);
}
