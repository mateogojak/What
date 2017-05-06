package com.fer.ppij.what.database.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by antes on 5.5.2017..
 */
@IgnoreExtraProperties
public class Question {

    private String questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3;

    public Question() {}

    public Question(String questionText, String correctAnswer, String incorrectAnswer1, String incorrectAnswer2, String incorrectAnswer3){

        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswer1 = incorrectAnswer1;
        this.incorrectAnswer2 = incorrectAnswer2;
        this.incorrectAnswer3  = incorrectAnswer3;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getIncorrectAnswer1() {
        return incorrectAnswer1;
    }

    public String getIncorrectAnswer2() {
        return incorrectAnswer2;
    }

    public String getIncorrectAnswer3() {
        return incorrectAnswer3;
    }
}
