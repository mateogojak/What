package com.fer.ppij.what.database.model;

import com.fer.ppij.what.database.QuestionType;
import com.fer.ppij.what.database.QuestionVisitor;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by antes on 6.5.2017..
 */
@IgnoreExtraProperties
public class MultipleChoiceQuestion extends AbstractQuestion {

    private List<String> answers = new ArrayList<>();

    public MultipleChoiceQuestion(){}

    public MultipleChoiceQuestion(String text, String correctAnswer, String category, String... answers){
        this(text,correctAnswer, category, false, answers);
    }

    protected MultipleChoiceQuestion(String text, String correctAnswer, String category, boolean hasImage,
                                     String... answers) {
        super(text, correctAnswer, category, (hasImage? QuestionType.IMAGE_MULTIPLE_CHOICE : QuestionType.MULTIPLE_CHOICE));

        for (String answer : answers) {
            this.answers.add(answer);
        }
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    @Override
    public void accept(QuestionVisitor questionVisitor) {
        questionVisitor.visit(this);
    }

}
