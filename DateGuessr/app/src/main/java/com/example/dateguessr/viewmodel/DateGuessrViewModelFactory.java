package com.example.dateguessr.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.dateguessr.model.images_repository.ImagesRepository;

public class DateGuessrViewModelFactory implements ViewModelProvider.Factory {

    private final ImagesRepository imgRepo;

    public DateGuessrViewModelFactory(ImagesRepository imgRepo) {
        this.imgRepo = imgRepo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DateGuessrViewModel.class)) {
            return (T) new DateGuessrViewModel(imgRepo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
