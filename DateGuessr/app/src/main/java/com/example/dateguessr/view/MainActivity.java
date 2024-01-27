package com.example.dateguessr.view;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.dateguessr.view.common.BaseActivity;
import com.example.dateguessr.viewmodel.DateGuessrViewModel;
import com.example.dateguessr.viewmodel.DateGuessrViewModelFactory;
import com.example.dateguessr.viewmodel.UiState;

public class MainActivity
        extends BaseActivity
        implements MainSceenView.Listener, ResultsView.Listener {

    private LoadingView loadingView;
    private MainScreenViewImpl mainScreenView;
    private ResultsViewImpl resultsScreenView;
    private DateGuessrViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadingView = new LoadingView(LayoutInflater.from(this), null);

        mainScreenView = new MainScreenViewImpl(LayoutInflater.from(this), null);
        mainScreenView.registerListener(this);

        resultsScreenView = new ResultsViewImpl(LayoutInflater.from(this), null);
        resultsScreenView.registerListener(this);

        DateGuessrViewModelFactory factory =
                new DateGuessrViewModelFactory(getCompositionRoot().getImagesRepository());
        viewModel =
                new ViewModelProvider(this, factory).get(DateGuessrViewModel.class);

        viewModel.getUiState().observe(this, uiState -> {
            if (uiState != null) {
                render(uiState);
            }
        });
    }

    private void render(UiState uiState) {
        if (uiState instanceof UiState.Loading) {
            setContentView(loadingView.getRootView());
        } else if (uiState instanceof UiState.Error) {
            setContentView(mainScreenView.getRootView());
            mainScreenView.setError(((UiState.Error) uiState).getErrorMsg());
        } else if (uiState instanceof UiState.LevelStart) {
            setContentView(mainScreenView.getRootView());
            mainScreenView.setLevelStart(
                    ((UiState.LevelStart) uiState).getLevel(),
                    ((UiState.LevelStart) uiState).getImageUrl(),
                    ((UiState.LevelStart) uiState).getIsLastLvl());
        } else if (uiState instanceof UiState.LevelFinish) {
            mainScreenView.setLevelFinish(
                    ((UiState.LevelFinish) uiState).getScore(),
                    ((UiState.LevelFinish) uiState).getDate(),
                    ((UiState.LevelFinish) uiState).getDescription());
        } else if (uiState instanceof UiState.FinalStage) {
            setContentView(resultsScreenView.getRootView());
            resultsScreenView.setFinalStage(
                    ((UiState.FinalStage) uiState).getTotalScore(),
                    ((UiState.FinalStage) uiState).getScores());
        }
    }

    @Override
    public void onSubmitClicked(int answer) {
        viewModel.updateScore(answer);
    }

    @Override
    public void onNextLevelClicked() {
        viewModel.moveToNextLevel();
    }

    @Override
    public void onNewIterationClicked() {
        viewModel.startNewIteration();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainScreenView.unregisterListener(this);
        resultsScreenView.unregisterListener(this);
    }
}