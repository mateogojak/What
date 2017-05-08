package com.fer.ppij.what.database.model;

import android.graphics.Bitmap;

import com.fer.ppij.what.database.QuestionVisitor;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;

/**
 * Created by antes on 6.5.2017..
 */
@IgnoreExtraProperties
public class ImageMultipleChoiceQuestion extends MultipleChoiceQuestion {

    private Bitmap image;

    public ImageMultipleChoiceQuestion() {}

    public ImageMultipleChoiceQuestion(String text, String correctAnswer, String category, Bitmap image, String... answers) {
        super(text, correctAnswer, category, true,answers);
        this.image = image;
    }

    @Exclude
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public void accept(QuestionVisitor questionVisitor) {
        questionVisitor.visit(this);
    }
}
