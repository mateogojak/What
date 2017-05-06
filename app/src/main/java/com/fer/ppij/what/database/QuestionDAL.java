package com.fer.ppij.what.database;

import android.util.Log;

import com.fer.ppij.what.database.model.AbstractQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by antes on 6.5.2017..
 */

public class QuestionDAL {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("questions");

    public QuestionDAL() {}

    public static void createQuestion(String id, AbstractQuestion question){
        mDatabase.child(id).setValue(question);
    }

    public static AbstractQuestion getQuestion(String id) {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AbstractQuestion aq = dataSnapshot.getValue(AbstractQuestion.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadQuestion:onCancelled", databaseError.toException());
            }
        });

        return null;
    }

    public static List<AbstractQuestion> getQuestions(String category) {
        return Collections.emptyList();
    }

}
