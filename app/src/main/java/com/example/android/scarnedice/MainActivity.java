package com.example.android.scarnedice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.R.attr.process;
import static com.example.android.scarnedice.R.drawable.dice1;
import static com.example.android.scarnedice.R.id.diceImage;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DiceApp";
    private static final int DICE_SIDES = 6;

    boolean userTurn = true;
    int userOverallScore = 0;
    int userTurnScore = 0;
    int computerOverallScore = 0;
    int computerTurnScore = 0;


    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setRollOnClickListener();
        setHoldOnClickListener();
        setResetOnClickListener();

    }

    private void processRoll(int rollValue) {
        TextView scoreView = (TextView) findViewById(R.id.textScore);

        if (rollValue == 1) {
            log.v(TAG, "Reset the score and give the control to the other");

        }
    }
    

    public void setRollOnClickListener() {
        Button rollBtn = (Button) findViewById(R.id.roll);
        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int diceRoll = 1 + random.nextInt(DICE_SIDES);
                Log.v(TAG, "Roll button clicked: " + diceRoll);
                Toast.makeText(MainActivity.this, "Roll button clicked: " + diceRoll,
                        Toast.LENGTH_SHORT)
                        .show();

                processRoll(diceRoll);

                ImageView diceImage = (ImageView) findViewById(R.id.diceImage);
                switch (diceRoll) {
                    case 1:
                        diceImage.setImageResource(R.drawable.dice1);
                        break;
                    case 2:
                        diceImage.setImageResource(R.drawable.dice2);
                        break;
                    case 3:
                        diceImage.setImageResource(R.drawable.dice3);
                        break;
                    case 4:
                        diceImage.setImageResource(R.drawable.dice4);
                        break;
                    case 5:
                        diceImage.setImageResource(R.drawable.dice5);
                        break;
                    case 6:
                        diceImage.setImageResource(R.drawable.dice6);
                        break;
                }
            }
        });
    }

    public void setHoldOnClickListener() {
        Button holdBtn = (Button) findViewById(R.id.hold);
        holdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Hold button clicked");
                Toast.makeText(MainActivity.this, "Hold button clicked!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    public void setResetOnClickListener() {
        Button resetBtn = (Button) findViewById(R.id.reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Reset button clicked");
                Toast.makeText(MainActivity.this, "Reset button clicked!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
