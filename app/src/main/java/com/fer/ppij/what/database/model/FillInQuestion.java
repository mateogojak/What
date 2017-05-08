package com.fer.ppij.what.database.model;

import com.fer.ppij.what.database.QuestionType;
import com.fer.ppij.what.database.QuestionVisitor;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by antes on 6.5.2017..
 */
@IgnoreExtraProperties
public class FillInQuestion extends AbstractQuestion {

    public FillInQuestion(){}

    public FillInQuestion(String text, String correctAnswer, String category){
        this(text, correctAnswer, category, false);
    }

    protected FillInQuestion(String text, String correctAnswer, String category, boolean hasImage) {
        super(text, correctAnswer, category, (!hasImage ? QuestionType.FILL_IN : QuestionType.IMAGE_FILL_IN));
    }

    @Override
    public void accept(QuestionVisitor questionVisitor) {
        questionVisitor.visit(this);
    }
}
