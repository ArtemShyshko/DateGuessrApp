package com.example.dateguessr;

import android.app.Application;

import com.example.dateguessr.dependency_injection.CompositionRoot;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.disposables.Disposable;

public class DateGuessrApp extends Application {

    private CompositionRoot compositionRoot;

    @Override
    public void onCreate() {
        super.onCreate();
        compositionRoot = new CompositionRoot();
    }

    public CompositionRoot getCompositionRoot() {
        return compositionRoot;
    }
}
