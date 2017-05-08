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
public class ImageFillInQuestion extends FillInQuestion {

    private Bitmap image;

    public ImageFillInQuestion() {}

    public ImageFillInQuestion(String text, String correctAnswer, String category, Bitmap image) {
        super(text, correctAnswer, category, true);
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
