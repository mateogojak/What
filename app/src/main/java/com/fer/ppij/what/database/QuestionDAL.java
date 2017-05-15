package com.fer.ppij.what.database;

import android.graphics.Bitmap;

import com.fer.ppij.what.database.model.AbstractQuestion;
import com.fer.ppij.what.database.model.ImageFillInQuestion;
import com.fer.ppij.what.database.model.ImageMultipleChoiceQuestion;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

/**
 * Created by antes on 6.5.2017..
 */

public class QuestionDAL {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("questions");
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    public QuestionDAL() {
    }

    public static void createQuestion(final String id, final AbstractQuestion question) {
        if (question instanceof ImageFillInQuestion || question instanceof ImageMultipleChoiceQuestion) {
            storeQuestionImage(id, question);
        }
        mDatabase.child(question.getCategory())
                .child(question.getQuestionType())
                .child(id)
                .setValue(question);

    }

    public static void storeQuestionImage(final String id, final AbstractQuestion question) {
        Bitmap bitmap = null;
        if (question instanceof ImageMultipleChoiceQuestion) {
            bitmap = ((ImageMultipleChoiceQuestion) question).getImage();
        } else if (question instanceof ImageFillInQuestion) {
            bitmap = ((ImageFillInQuestion) question).getImage();
        }
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] data = baos.toByteArray();
            mStorage.child(id + ".jpg").putBytes(data);
        }
    }

    public static void getQuestionImage(String id, final AbstractQuestion question, OnSuccessListener<byte[]> onSuccessListener) {
        boolean isMultiple = question.getQuestionType().contains("multiple");

        mStorage.child(id + (isMultiple ? "_multiple" : "fill_in") + ".jpg").getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(onSuccessListener);
    }

    public static void getQuestion(String category, String id, ValueEventListener valueEventListener) {
        mDatabase.child(category).child(id).addValueEventListener(valueEventListener);
//        mDatabase.child(category).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                dataSnapshot.getValue(AbstractQuestion.class);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "loadQuestion:onCancelled", databaseError.toException());
//            }
//        });
    }

    public static void getQuestions(String category, int numberOfQuestions, ValueEventListener valueEventListener) {
        //mDatabase.child(category).limitToFirst(numberOfQuestions).addChildEventListener(childEventListener);
        mDatabase.child(category).limitToFirst(numberOfQuestions).addValueEventListener(valueEventListener);
    }

}
