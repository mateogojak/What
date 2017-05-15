package com.fer.ppij.what;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fer.ppij.what.database.QuestionDAL;
import com.fer.ppij.what.database.model.FillInQuestion;
import com.fer.ppij.what.database.model.ImageFillInQuestion;
import com.fer.ppij.what.database.model.ImageMultipleChoiceQuestion;
import com.fer.ppij.what.database.model.MultipleChoiceQuestion;

/**
 * Created by Mateo on 5/2/2017.
 */

public class SelectGameScreen extends AppCompatActivity {

    private String nickname;
    private TextView nicknameDisplayTextView;
    private Button goToGame1, goToGame2, goToGame3, goToGame4, goToRoom, createRoom;
    private EditText roomNameEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game);

        nickname = getIntent().getStringExtra("nickname");
        nicknameDisplayTextView = (TextView) findViewById(R.id.nicknameDisplayTextView);
        goToGame1 = (Button) findViewById(R.id.selectGame1);
        goToGame2 = (Button) findViewById(R.id.selectGame2);
        goToGame3 = (Button) findViewById(R.id.selectGame3);
        goToGame4 = (Button) findViewById(R.id.selectGame4);

        goToRoom = (Button) findViewById(R.id.goToRoom);
        createRoom = (Button) findViewById(R.id.createNewRoom);
        roomNameEditText = (EditText) findViewById(R.id.roomNameEditText);

    }

    @Override
    protected void onStart() {
        super.onStart();

        nicknameDisplayTextView.setText(nickname);

        //TODO: Questions for testing, delete after
        QuestionDAL.createQuestion("prvo", new FillInQuestion("Filin1", "Lijep", "povijest"));
        QuestionDAL.createQuestion("drugo", new FillInQuestion("Filin2", "Ružan", "povijest"));
        QuestionDAL.createQuestion("prvo", new MultipleChoiceQuestion("Multiple1", "Lijep", "povijest", "Lijep", "Odgovor1", "Odgovor2", "Odgovor3"));
        QuestionDAL.createQuestion("drugo", new MultipleChoiceQuestion("Multiple2", "Ružan", "povijest", "Ružan", "Odgovor1", "Odgovor2", "Odgovor3"));
        QuestionDAL.createQuestion("drugo", new MultipleChoiceQuestion("Pritisni a", "a", "geografija", "a", "Odgovor1", "Odgovor2", "Odgovor3"));

        //TODO: Stvaranje pitanja sa slikom
        // ovdje možemo dohvatiti resurs ili sliku iz uređaja
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.europe);

        // ovdje se stvara pitanje sa slikom u obliku (String id_pitanja, i pitanje)
        QuestionDAL.createQuestion("prvo_fill_in", new ImageFillInQuestion("ImageFilin1", "Lijep", "povijest", bm));
        QuestionDAL.createQuestion("prvo_multiple", new ImageMultipleChoiceQuestion("ImageMultiple2", "Ružan", "povijest",
                bm, "Ružan", "Odgovor1", "Odgovor2", "Odgovor3"));

        goToGame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectGameScreen.this, GameScreen.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("gameName", goToGame1.getText());
                startActivity(intent);
                finish();
            }
        });

        goToGame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectGameScreen.this, GameScreen.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("gameName", goToGame2.getText());
                startActivity(intent);
                finish();
            }
        });

        goToGame3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectGameScreen.this, GameScreen.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("gameName", goToGame3.getText());
                startActivity(intent);
                finish();
            }
        });

        goToGame4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectGameScreen.this, GameScreen.class);
                intent.putExtra("nickname", nickname);
                intent.putExtra("gameName", goToGame4.getText());
                startActivity(intent);
                finish();
            }
        });


        goToRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (roomNameEditText.getText().length() != 0) {

                    if (roomNameEditText.getText().toString().equals(goToGame1.getText()) || roomNameEditText.getText().toString().equals(goToGame2.getText()) || roomNameEditText.getText().toString().equals(goToGame3.getText()) || roomNameEditText.getText().toString().equals(goToGame4.getText())) {

                        //ispisuje se nekakva poruka da se ne moze pristupiti sobi koja ima ime kao neko od zadanih podrucja.
                        Toast.makeText(getApplicationContext(), "Ime sobe ne smije odgovarati zadanim područjima", Toast.LENGTH_SHORT).show();

                    } else {

                        Intent intent = new Intent(SelectGameScreen.this, GameScreen.class);
                        intent.putExtra("nickname", nickname);
                        intent.putExtra("gameName", roomNameEditText.getText().toString());
                        startActivity(intent);
                        finish();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Upiši ime sobe", Toast.LENGTH_SHORT).show();
                }

            }

        });

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (roomNameEditText.getText().length() != 0) {

                    if (roomNameEditText.getText().toString().equals(goToGame1.getText()) || roomNameEditText.getText().toString().equals(goToGame2.getText()) || roomNameEditText.getText().toString().equals(goToGame3.getText()) || roomNameEditText.getText().toString().equals(goToGame4.getText())) {

                        //ispisuje se nekakva poruka da se ne moze stvoriti soba koja ima ime kao neko od zadanih podrucja.
                        Toast.makeText(getApplicationContext(), "Ime sobe ne smije odgovarati zadanim područjima", Toast.LENGTH_SHORT).show();

                    } else {
                        Intent intent = new Intent(SelectGameScreen.this, CreateRoomScreen.class);
                        intent.putExtra("nickname", nickname);
                        intent.putExtra("roomName", roomNameEditText.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                }
                Toast.makeText(getApplicationContext(), "Upiši ime sobe", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
