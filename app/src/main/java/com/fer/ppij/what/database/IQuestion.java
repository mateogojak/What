package com.fer.ppij.what.database;

/**
 * Created by antes on 7.5.2017..
 */

public interface IQuestion {
    void accept(QuestionVisitor questionVisitor);
}
