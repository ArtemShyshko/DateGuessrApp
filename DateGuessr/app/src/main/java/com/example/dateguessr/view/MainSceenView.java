package com.example.dateguessr.view;

import com.example.dateguessr.view.common.CustomObservableView;

import java.util.Map;

public interface MainSceenView extends CustomObservableView<MainSceenView.Listener> {

    interface Listener {
        void onSubmitClicked(int answer);
        void onNextLevelClicked();
    }

    void setError(String errorMsg);
    void setLevelStart(int level, String imageUrl, boolean isLastLvl);
    void setLevelFinish(int score, int originalDate, String imageDescription);
}
