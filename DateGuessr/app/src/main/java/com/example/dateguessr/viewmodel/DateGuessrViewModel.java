package com.example.dateguessr.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dateguessr.model.images_repository.ImageData;
import com.example.dateguessr.model.images_repository.ImagesRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DateGuessrViewModel extends ViewModel {

    private static final int MAX_SCORE = 1000;
    private static final int MAX_DELTA = 40;
    private static final int LVLS_IN_ITERATION = 5;
    private static final int ITERATIONS_LIMIT = 3;
    private static final int NUMBER_OF_LEVELS = LVLS_IN_ITERATION * ITERATIONS_LIMIT;

    private final ImagesRepository imgRepo;
    private final Map<String, Integer> scoresOnStage = new LinkedHashMap<>();
    private final CompositeDisposable disposable = new CompositeDisposable();
    private final MutableLiveData<UiState> uiState = new MutableLiveData<>();
    private List<ImageData> allImagesList;
    private int positionInList = 0;
    private int currentLevel = 0;

    public DateGuessrViewModel(ImagesRepository imgRepo) {
        this.imgRepo = imgRepo;
        loadNewImages();
    }

    private void loadNewImages() {
        uiState.setValue(new UiState.Loading());
        Disposable d = imgRepo.cleanUpData()
                .concatWith(imgRepo.fetchNewData(NUMBER_OF_LEVELS))
                .subscribe(this::refreshImagesList, throwable -> {
                    uiState.setValue(new UiState.Error("Error while doing API call: "
                            + throwable.getLocalizedMessage()));
                    System.out.println("Error while doing API call: "
                            + throwable.getLocalizedMessage());
                });
        disposable.add(d);
    }

    public void refreshImagesList() {
        Disposable d = imgRepo.getAllImageData().subscribe(list -> {
            if (list != null) {
                allImagesList = list;
                moveToNextLevel();
            }
        }, throwable -> uiState.setValue(new UiState.Error("Error while refreshing list: "
                + throwable.getLocalizedMessage())));
        disposable.add(d);
    }

    public LiveData<UiState> getUiState() {
        return uiState;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }

    public void updateScore(int answer) {
        ImageData img = getImageDataOnCurrentLvl();
        int date = Integer.parseInt(img.getDate());
        String desc = img.getDescription();
        int score = MAX_SCORE - (Math.abs(date - answer) * MAX_SCORE / MAX_DELTA);
        if (score < 0) score = 0;

        scoresOnStage.put(img.getPageTitle(), score);
        uiState.setValue(new UiState.LevelFinish(score, date, desc));
        positionInList++;
    }

    public void moveToNextLevel() {
        if (currentLevel == LVLS_IN_ITERATION) {
            currentLevel = 0;
            int totalScore = scoresOnStage.values().stream().mapToInt(Integer::intValue).sum();
            uiState.setValue(new UiState.FinalStage(totalScore, scoresOnStage));
        } else {
            currentLevel++;
            uiState.setValue(new UiState.LevelStart(
                    currentLevel,
                    getImageDataOnCurrentLvl().getImageUrl(),
                    currentLevel == LVLS_IN_ITERATION
            ));
        }
    }

    public void startNewIteration() {
        scoresOnStage.clear();
        if (positionInList == NUMBER_OF_LEVELS) {
            loadNewImages();
            positionInList = 0;
        } else {
            moveToNextLevel();
        }
    }

    private ImageData getImageDataOnCurrentLvl() {
        return allImagesList.get(positionInList);
    }
}
