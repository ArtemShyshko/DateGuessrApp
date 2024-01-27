package com.example.dateguessr.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dateguessr.model.images_repository.ImageData;
import com.example.dateguessr.model.images_repository.ImagesRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class DateGuessrViewModel extends ViewModel {

    private static final int MAX_SCORE = 1000;
    private static final int MAX_DELTA = 40;
    private static final int LVLS_IN_ITERATION = 5;

    private final ImagesRepository imgRepo;
    private final MutableLiveData<UiState> uiState;
    private final Map<String, Integer> scoresOnStage = new LinkedHashMap<>();
    private List<ImageData> allImagesList;
    private Disposable initDisposable;
    private Disposable fetchDisposable;
    private int positionInList = 0;
    private int currentLevel = 1;

    public DateGuessrViewModel(ImagesRepository imgRepo) {
        this.imgRepo = imgRepo;
        this.uiState = new MutableLiveData<>(new UiState.Loading());
        // fetchNewData();
        refreshImagesList();
    }

    public LiveData<UiState> getUiState() {
        return uiState;
    }

    private void fetchNewData() {
        fetchDisposable = imgRepo.getImagesCount().subscribe(count -> {
            if (count < 25) {
                imgRepo.fetchDataAndSaveToDatabase();
            }
        });
//        int count = imgRepo.getImagesCount().blockingFirst();
//        if (count < 100) {
//            Thread t = new Thread(imgRepo::fetchDataAndSaveToDatabase);
//            t.start();
//            try { t.join(); } catch (InterruptedException e) { throw new RuntimeException(e); }
//            count = imgRepo.getImagesCount().blockingFirst();
//            System.out.println("VIEWMODEL: " + count);
//        }
    }

    public void refreshImagesList() {
        initDisposable = imgRepo.getAllImageData()
                .subscribe(list -> {
                    if (list != null) {
                        allImagesList = list;
                        uiState.setValue(new UiState.LevelStart(
                                currentLevel,
                                getImageDataOnCurrentLvl().getImageUrl(),
                                false
                        ));
                    }
                }, throwable -> uiState.setValue(new UiState.Error("Error while doing API request")));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        initDisposable.dispose();
    }

    public void updateScore(int answer) {
        int date = Integer.parseInt(getImageDataOnCurrentLvl().getDate());
        String desc = getImageDataOnCurrentLvl().getDescription();
        int score = MAX_SCORE - (Math.abs(date - answer) * MAX_SCORE / MAX_DELTA);
        if (score < 0) score = 0;

        scoresOnStage.put(getImageDataOnCurrentLvl().getPageTitle(), score);
        uiState.setValue(new UiState.LevelFinish(score, date, desc));
    }

    public void moveToNextLevel() {
        if (currentLevel == LVLS_IN_ITERATION) {
            currentLevel = 0;
            int totalScore = scoresOnStage.values().stream().mapToInt(Integer::intValue).sum();
            uiState.setValue(new UiState.FinalStage(totalScore, scoresOnStage));
        } else {
            positionInList++;
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
        moveToNextLevel();
    }

    private ImageData getImageDataOnCurrentLvl() {
        return allImagesList.get(positionInList);
    }
}
