package com.fer.ppij.what.database.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;

/**
 * Created by antes on 6.5.2017..
 */
@IgnoreExtraProperties
public class ImageFillInQuestion extends FillInQuestion {

    private File image;

    public ImageFillInQuestion() {}

    public ImageFillInQuestion(String text, String correctAnswer, String category, File image) {
        super(text, correctAnswer, category);
        this.image = image;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
