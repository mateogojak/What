package com.fer.ppij.what.database;

import android.util.Log;

import com.fer.ppij.what.database.model.MultipleChoiceQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by antes on 5.5.2017..
 * https://firebase.google.com/docs/database/android/read-and-write
 * https://firebase.google.com/docs/database/android/structure-data
 */

public class FirebaseTest {

    private DatabaseReference mDatabase;

    public FirebaseTest() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        // or FireBaseDatabase.getInstance.getReference("questions");
    }

    public void writeTest() {
        MultipleChoiceQuestion mcq = new MultipleChoiceQuestion("Pitanje",
                "Točan odgovor", "Točan odgovor", "Odgovor1", "Odgovor2", "Odgovor3");
        String id = "PrvoPitanje";
        mDatabase.child("questions").child(id).setValue(mcq);
        // updating a specific field
        // mDatabase.child("questions").child(qId).child("questionName").setValue("NewName");
    }

    private void readTest() {
        ValueEventListener questionListener =
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //QuestionModel question = dataSnapshot.getValue(QuestionModel.class);
                        //...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };
        FirebaseDatabase.getInstance().getReference("questions")
                .addValueEventListener(questionListener);

        // or addListenerForSingleValueEvent
    }

}
