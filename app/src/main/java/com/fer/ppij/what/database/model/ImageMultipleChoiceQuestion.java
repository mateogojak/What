package com.fer.ppij.what.database.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.File;

/**
 * Created by antes on 6.5.2017..
 */
@IgnoreExtraProperties
public class ImageMultipleChoiceQuestion extends MultipleChoiceQuestion {

    private File image;

    public ImageMultipleChoiceQuestion() {}

    public ImageMultipleChoiceQuestion(String text, String correctAnswer, String category, File image, String... answers) {
        super(text, correctAnswer, category, answers);
        this.image = image;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
}
