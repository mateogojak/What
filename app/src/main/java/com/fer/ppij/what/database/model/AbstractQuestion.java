package com.fer.ppij.what.database.model;

import com.fer.ppij.what.database.IQuestion;
import com.fer.ppij.what.database.QuestionVisitor;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by antes on 6.5.2017..
 */
@IgnoreExtraProperties
public class AbstractQuestion implements IQuestion {

    public enum QuestionType {
        MULTIPLE_CHOICE("multiple_choice"),
        FILL_IN("fill_in"),
        IMAGE_MULTIPLE_CHOICE("image_multiple_choice"),
        IMAGE_FILL_IN("image_fill_in");

        private String name;

        QuestionType(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    protected String text;
    protected String correctAnswer;
    protected String category;
    protected String questionType;
    protected boolean hasImage;

    protected AbstractQuestion() {}

    protected AbstractQuestion(String text, String correctAnswer, String category, QuestionType questionType) {
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.category = category;
        this.questionType = questionType.getName();
        this.hasImage = this.questionType.contains("image");
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

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    @Override
    public void accept(QuestionVisitor questionVisitor) {
        questionVisitor.visit(this);
    }
}
