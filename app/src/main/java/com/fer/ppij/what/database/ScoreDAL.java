package com.fer.ppij.what.database;

import com.fer.ppij.what.database.model.ScoreModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by antes on 15.5.2017..
 */

public class ScoreDAL {

    private static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("scores");

    private ScoreDAL(){}

    public static void createNewScore(ScoreModel score) {
        mDatabase.push().setValue(score);
    }

    /**
     * Zaraži prvih n igrača po skoru sa firebasea a do podataka se dolazi pomoću
     * Listenera koji će nam pružiti podatke iz firebasea,
     * koristi se da se u metodi OnChildAdded DataSnapshot.getValue nam dohvati sve podatke kao npr.
     * ovaj kod će nam dohvatiti sve rezultate
     * <code>
     *     <br>
     *     for(DataSnapshot snap : Datasnapshot.getChildren) { <br>
     *         &nbsp; ScoreModel score = snap.getValue(ScoreModel.class); <br>
     *     }
     *     <br>
     * </code>
     *
     * @param n
     * @param childEventListener
     */
    public static void getScores(int n, ChildEventListener childEventListener) {
        if (n <= 0) {
            throw new IllegalArgumentException("Broj pitanja mora biti veci od 0");
        }
        mDatabase.limitToFirst(n).orderByValue().addChildEventListener(childEventListener);
    }

}
