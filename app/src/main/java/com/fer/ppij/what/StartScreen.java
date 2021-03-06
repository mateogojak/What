package com.fer.ppij.what;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Mateo on 5/2/2017.
 */

public class StartScreen extends AppCompatActivity {
    private EditText nicknameInputField;
    private FloatingActionButton goToGameSelect;
    private String nickname, nickSaved;
    SharedPreferences myPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Boolean changeNick = getIntent().getBooleanExtra("changeNick",false);

        myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        nickSaved = myPrefs.getString("nickname", "");

        if (!nickSaved.equals("")&& !changeNick) {
            Intent intent = new Intent(StartScreen.this, SelectGameScreen.class);
            intent.putExtra("nickname", nickSaved);
            startActivity(intent);
            finish();
        }

        nicknameInputField = (EditText) findViewById(R.id.nicknameInput);
        goToGameSelect = (FloatingActionButton) findViewById(R.id.buttonProceed);
    }

    @Override
    protected void onStart() {
        super.onStart();
        goToGameSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = nicknameInputField.getText().toString();
                if(nickname.equals("")){
                    Toast.makeText(StartScreen.this,"Unesi nadimak",Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor e = myPrefs.edit();
                    e.putString("nickname", nickname); // add or overwrite someValue
                    e.commit(); // this saves to disk and notifies observers

                    Intent intent = new Intent(StartScreen.this, SelectGameScreen.class);
                    intent.putExtra("nickname", nickname);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }
}
