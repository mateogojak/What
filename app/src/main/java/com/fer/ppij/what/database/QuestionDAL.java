package com.fer.ppij.what.database;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.fer.ppij.what.database.model.AbstractQuestion;
import com.fer.ppij.what.database.model.ImageFillInQuestion;
import com.fer.ppij.what.database.model.ImageMultipleChoiceQuestion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

/**
 * Created by antes on 6.5.2017..
 */

public class QuestionDAL {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("questions");
    private static StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    public QuestionDAL() {}

    public static void createQuestion(String id, AbstractQuestion question){
        mDatabase.child(question.getCategory())
                .child(question.getQuestionType())
                .child(id)
                .setValue(question);
    }

    private static void storeQuestionImage(String id, AbstractQuestion question) {
        File f = null;
        if(question instanceof ImageMultipleChoiceQuestion) {
            f = ((ImageMultipleChoiceQuestion)question).getImage();
        } else if (question instanceof ImageFillInQuestion) {
            f = ((ImageFillInQuestion)question).getImage();
        }
        mStorage.child(id + ".jpg").putFile(Uri.fromFile(f));
    }

    private static void getQuestionImage(String id) {
        try {
            File localFile = File.createTempFile("images", "jpg");

            mStorage.child(id).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        } catch (IOException e) {
            //LOG.d(e, "Puce exception prilikom dohvatanja");
        }

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
