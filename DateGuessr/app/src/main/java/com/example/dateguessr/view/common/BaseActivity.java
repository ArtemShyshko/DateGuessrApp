package com.example.dateguessr.view.common;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dateguessr.DateGuessrApp;
import com.example.dateguessr.dependency_injection.ActivityCompositionRoot;
import com.example.dateguessr.dependency_injection.CompositionRoot;

public abstract class BaseActivity extends AppCompatActivity {

    protected ActivityCompositionRoot getCompositionRoot() {
        CompositionRoot compositionRoot = ((DateGuessrApp)getApplication()).getCompositionRoot();
        return new ActivityCompositionRoot(this, compositionRoot);
    }
}
