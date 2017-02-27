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
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.view.View.X;
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
    String whoseTurn = "";

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialDisplay();
        setRollOnClickListener();
        setHoldOnClickListener();
        setResetOnClickListener();
    }

    private void disableRollAndHold() {
        Button rollBtn = (Button) findViewById(R.id.roll);
        Button holdBtn = (Button) findViewById(R.id.hold);
        rollBtn.setEnabled(false);
        holdBtn.setEnabled(false);
    }

    private void enableRollAndHold() {
        Button rollBtn = (Button) findViewById(R.id.roll);
        Button holdBtn = (Button) findViewById(R.id.hold);
        rollBtn.setEnabled(true);
        holdBtn.setEnabled(true);
    }

    /**
     *
     *
     * 1. disable the roll and hold button
     * 2. Create a while loop that loops over each of the computer's turn. During each iteration of
     *    the loop:
     *    pick a random die value and display it (hopefully using the helper you created earlier)
     *    follow the game rules depending on the value of the roll.
     *
     */
    private void computerTurn() {
        TextView scoreView = (TextView) findViewById(R.id.textScore);

        computerTurnScore = 0;

        disableRollAndHold();
        int diceRoll = 1 + random.nextInt(DICE_SIDES);
        Log.v(TAG, "computer turn: 1st rolled " + diceRoll + " computerTurnScore: " + computerTurnScore);
        Log.v(TAG, "display dice with value: " + diceRoll);
        setDiceDisplay(diceRoll);

        while ((computerTurnScore < 20) && (diceRoll != 1)) {
            processRoll(diceRoll);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            diceRoll = 1 + random.nextInt(DICE_SIDES);
            setDiceDisplay(diceRoll);
            Log.v(TAG, "computer rolled: " + diceRoll);
        }

        // add up the computer score before passing the control to the user
        computerOverallScore += computerTurnScore;

        // computer rolled 1
        if (diceRoll == 1) {
            computerTurnScore = 0;
        }

        Log.v(TAG, "give the turn to user!");
        scoreView.setText("Your score: " + userOverallScore
                + " computer score: " + computerOverallScore
                + whoseTurn() + computerTurnScore);
        enableRollAndHold();
    }

    private void setInitialDisplay() {
        TextView scoreView = (TextView) findViewById(R.id.textScore);

        scoreView.setText("Your score: " + userOverallScore
                + " computer score: " + computerOverallScore
                + whoseTurn() + userTurnScore);
    }

    private String whoseTurn() {
        if (userTurn) {
            whoseTurn = " your turn score: ";
        } else {
            whoseTurn = " computer score: ";
        }
        return whoseTurn;
    }

    private void processRoll(int rollValue) {
        TextView scoreView = (TextView) findViewById(R.id.textScore);

        if (userTurn) {
            Log.v(TAG, "Update player's score - before: " + userTurnScore);
            userTurnScore += rollValue;
            Log.v(TAG, "Update player's score - after: " + userTurnScore);

            scoreView.setText("Your score: " + userOverallScore
                    + " computer score: " + computerOverallScore
                    + whoseTurn() + userTurnScore);
        } else {
            Log.v(TAG, "Update computer's score - before: " + computerTurnScore);
            computerTurnScore += rollValue;
            Log.v(TAG, "Update computer's score - after: " + computerTurnScore);

            scoreView.setText("Your score: " + userOverallScore
                    + " computer score: " + computerOverallScore
                    + whoseTurn() + computerTurnScore);
        }
    }

    private void setDiceDisplay(int diceRoll) {
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

    public void setRollOnClickListener() {
        Button rollBtn = (Button) findViewById(R.id.roll);
        final TextView scoreView = (TextView) findViewById(R.id.textScore);

        rollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int diceRoll = 1 + random.nextInt(DICE_SIDES);
                Log.v(TAG, "Roll button clicked: " + diceRoll);
                Toast.makeText(MainActivity.this, "Roll button clicked: " + diceRoll,
                        Toast.LENGTH_SHORT)
                        .show();
                if (diceRoll != 1) {
                    processRoll(diceRoll);
                    setDiceDisplay(diceRoll);
                } else {
                    Toast.makeText(getApplicationContext(), "" + whoseTurn() + " Rolled 1!", Toast.LENGTH_SHORT)
                            .show();
                    setDiceDisplay(1);

                    userOverallScore += userTurnScore;
                    scoreView.setText("Your score: " + userOverallScore
                            + " computer score: " + computerOverallScore
                            + whoseTurn() + userTurnScore);
                    userTurn = false;
                    computerTurn();
                }
            }
        });
    }

    public void setHoldOnClickListener() {
        Button holdBtn = (Button) findViewById(R.id.hold);
        final TextView scoreView = (TextView) findViewById(R.id.textScore);

        holdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Hold button clicked");
                Toast.makeText(MainActivity.this, "Hold button clicked!", Toast.LENGTH_SHORT)
                        .show();
                userOverallScore += userTurnScore;
                userTurnScore = 0;

                scoreView.setText("Your score: " + userOverallScore
                        + " computer score: " + computerOverallScore
                        + whoseTurn() + userTurnScore);

                userTurn = false;
                computerTurn();

            }
        });
    }

    public void setResetOnClickListener() {
        Button resetBtn = (Button) findViewById(R.id.reset);
        final TextView scoreView = (TextView) findViewById(R.id.textScore);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Reset button clicked");
                Toast.makeText(MainActivity.this, "Reset button clicked!", Toast.LENGTH_SHORT)
                        .show();
                userOverallScore = 0;
                userTurnScore = 0;
                computerOverallScore = 0;
                computerTurnScore = 0;
                userTurn = true;
                setInitialDisplay();
                enableRollAndHold();
                setDiceDisplay(1);  // start with dice face value of 1
            }
        });
    }
}
