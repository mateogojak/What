package com.fer.ppij.what.database;

import com.fer.ppij.what.database.model.AbstractQuestion;
import com.fer.ppij.what.database.model.FillInQuestion;
import com.fer.ppij.what.database.model.ImageFillInQuestion;
import com.fer.ppij.what.database.model.ImageMultipleChoiceQuestion;
import com.fer.ppij.what.database.model.MultipleChoiceQuestion;

/**
 * Created by antes on 7.5.2017..
 */

public interface QuestionVisitor {
    void visit(MultipleChoiceQuestion multipleChoiceQuestion);
    void visit(ImageMultipleChoiceQuestion imageMultipleChoiceQuestion);
    void visit(FillInQuestion fillInQuestion);
    void visit(ImageFillInQuestion imageFillInQuestion);
    void visit(AbstractQuestion abstractQuestion);
}
