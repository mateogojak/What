package com.fer.ppij.what.database.model;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by antes on 15.5.2017..
 */
@IgnoreExtraProperties
public class ScoreModel implements Comparable<ScoreModel>{

    private String nickname;
    private Integer score;
    private String category;

    public ScoreModel() {
    }

    public ScoreModel(String nickname, Integer score, String category) {
        this.nickname = nickname;
        this.score = score;
        this.category = category;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int compareTo(@NonNull ScoreModel o) {
        return score.compareTo(o.getScore());
    }
}
