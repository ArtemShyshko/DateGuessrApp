package com.example.dateguessr.view;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dateguessr.R;
import com.example.dateguessr.view.common.BaseObservableView;

import java.util.Map;

public class MainScreenViewImpl
        extends BaseObservableView<MainSceenView.Listener>
        implements MainSceenView {

    private final TextView levelIndicator;
    private final ImageView imageView;
    private final EditText yearField;
    private final TextView scoreText;
    private final Button submitButton;
    private final Button nextLevelButton;

    public MainScreenViewImpl(LayoutInflater inflater, ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.activity_main, parent, false));
        levelIndicator = (TextView) findViewById(R.id.levelIndicator);
        imageView = (ImageView) findViewById(R.id.imageView);
        yearField = (EditText) findViewById(R.id.yearField);
        scoreText = (TextView) findViewById(R.id.scoreText);
        submitButton = (Button) findViewById(R.id.buttonSubmit);
        nextLevelButton = (Button) findViewById(R.id.buttonNextLevel);
        scoreText.setMovementMethod(new ScrollingMovementMethod());

        submitButton.setOnClickListener(view -> {
            getListeners().forEach(listener -> {
                listener.onSubmitClicked(getYearFromField());
            });
        });

        nextLevelButton.setOnClickListener(view -> {
            getListeners().forEach(Listener::onNextLevelClicked);
        });
    }

    @Override
    public void setError(String errorMsg) {
        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setLevelStart(int level, String imageUrl, boolean isLastLvl) {
        // Place new image
        RequestOptions options = new RequestOptions()
                .placeholder(getProgressDrawable())
                .error(R.mipmap.ic_launcher_round);
        Glide.with(getRootView())
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .into(imageView);

        scoreText.setVisibility(View.INVISIBLE);
        nextLevelButton.setVisibility(View.INVISIBLE);

        submitButton.setVisibility(View.VISIBLE);
        String levelText = getResources().getText(R.string.level).toString() + " " + level;
        levelIndicator.setText(levelText);
        yearField.setText("");
        yearField.setEnabled(true);

        if (isLastLvl) {
            nextLevelButton.setText(R.string.show_results);
        } else {
            nextLevelButton.setText(R.string.next_level);
        }
    }

    @Override
    public void setLevelFinish(int score, int originalDate, String imageDescription) {
        String result = "Original date: " + originalDate
                + "\n" + imageDescription
                + "\nYou scored: " + score;

        submitButton.setVisibility(View.INVISIBLE);

        scoreText.setText(result);
        scoreText.setVisibility(View.VISIBLE);
        nextLevelButton.setVisibility(View.VISIBLE);
        yearField.setEnabled(false);
    }

    private Integer getYearFromField() {
        int year = 0;
        try {
            year = Integer.parseInt(yearField.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return year;
    }

    private CircularProgressDrawable getProgressDrawable() {
        CircularProgressDrawable progressDrawable =
                new CircularProgressDrawable(getContext());
        progressDrawable.setStrokeWidth(10f);
        progressDrawable.setCenterRadius(50f);
        progressDrawable.start();
        return progressDrawable;
    }
}
