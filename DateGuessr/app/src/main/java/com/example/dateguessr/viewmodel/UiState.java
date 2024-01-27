package com.example.dateguessr.viewmodel;

import java.util.Map;

public abstract class UiState {

    public static class Loading extends UiState {}

    public static class Error extends UiState {
        private final String errorMsg;

        public String getErrorMsg() {
            return errorMsg;
        }

        public Error(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }

    public static class LevelStart extends UiState {
        private final int level;
        private final String imageUrl;
        private final boolean isLastLvl;

        public int getLevel() {
            return level;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public boolean getIsLastLvl() {
            return isLastLvl;
        }

        public LevelStart(int level, String imageUrl, boolean isLastLvl) {
            this.level = level;
            this.imageUrl = imageUrl;
            this.isLastLvl = isLastLvl;
        }
    }

    public static class LevelFinish extends UiState {

        private final int score;
        private final int date;
        private final String description;

        public int getScore() {
            return score;
        }

        public int getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public LevelFinish(int score, int date, String description) {
            this.score = score;
            this.date = date;
            this.description = description;
        }
    }

    public static class FinalStage extends UiState {

        private final int totalScore;
        private final Map<String, Integer> scores;

        public int getTotalScore() {
            return totalScore;
        }

        public Map<String, Integer> getScores() {
            return scores;
        }

        public FinalStage(int totalScore, Map<String, Integer> scores) {
            this.totalScore = totalScore;
            this.scores = scores;
        }
    }
}
