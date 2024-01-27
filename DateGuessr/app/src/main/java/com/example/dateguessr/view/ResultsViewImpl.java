package com.example.dateguessr.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dateguessr.R;
import com.example.dateguessr.view.common.BaseObservableView;

import java.util.Map;

public class ResultsViewImpl
        extends BaseObservableView<ResultsView.Listener>
        implements ResultsView {

    private final TextView totalScoreView;
    private final ListView scoresList;
    private final Button newIterationButton;

    public ResultsViewImpl(LayoutInflater inflater, ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.result_screen, parent, false));
        this.totalScoreView = (TextView) findViewById(R.id.totalScore);
        this.scoresList = (ListView) findViewById(R.id.scoresList);
        this.newIterationButton = (Button) findViewById(R.id.newIterationButton);

        newIterationButton.setOnClickListener(view -> {
            getListeners().forEach(Listener::onNewIterationClicked);
        });
    }

    @Override
    public void setFinalStage(int totalScore, Map<String, Integer> scores) {
        String scoreText = getResources().getText(R.string.total_score).toString() + " " + totalScore;
        totalScoreView.setText(scoreText);

        ScoresListAdapter adapter = new ScoresListAdapter(getContext(), scores);
        scoresList.setAdapter(adapter);
    }
}
