package com.fer.ppij.what.database.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by antes on 6.5.2017..
 */
@IgnoreExtraProperties
public class FillInQuestion extends AbstractQuestion {

    public FillInQuestion(){}

    public FillInQuestion(String text, String correctAnswer){
        super(text, correctAnswer);
    }
}
