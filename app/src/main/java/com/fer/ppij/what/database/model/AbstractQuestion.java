package com.fer.ppij.what.database.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by antes on 6.5.2017..
 */
@IgnoreExtraProperties
public class AbstractQuestion {

    protected String text;
    protected String correctAnswer;
    protected String category;

    protected AbstractQuestion() {}

    protected AbstractQuestion(String text, String correctAnswer, String category) {
        this.text = text;
        this.correctAnswer = correctAnswer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrectAnswer(String answer) {
        return this.correctAnswer.equals(answer);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
