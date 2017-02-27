package com.example.android.scarnedice;

import android.os.Handler;
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


    TextView timerTextView;
    long startTime = 0;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();

    /*
    Runnable computerRollRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            //timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            int diceRoll = 1 + random.nextInt(DICE_SIDES);
            timerTextView.setText("Dice rolled: " + diceRoll);
            Log.v(TAG, "simulating computer rolling dice!!!");
            if (diceRoll == 1) {
                Log.v(TAG, "dice = 1 ...stopping computer rolling dice!!!");
                timerHandler.removeCallbacks(computerRollRunnable);
            } else {
                Log.v(TAG, "dice = " + diceRoll);
                timerHandler.postDelayed(this, 1000);
            }
        }
    };
    */

    Runnable computerRollRunnable = new Runnable() {
        @Override
        public void run() {
            TextView scoreView = (TextView) findViewById(R.id.textScore);

            disableRollAndHold();

            int diceRoll = 1 + random.nextInt(DICE_SIDES);
            Log.v(TAG, "beginning computer total score: " + computerOverallScore);
            Log.v(TAG, "computer turn: rolled " + diceRoll + " computerTurnScore: " + computerTurnScore);
            setDiceDisplay(diceRoll);
            if (diceRoll == 1) {
                Log.v(TAG, "computer rolled 1 - give turn to user!");
                computerTurnScore = 0;
                timerHandler.removeCallbacks(computerRollRunnable);
                userTurn = true;
                enableRollAndHold();
            } else if (computerTurnScore + diceRoll > 20) {
                Log.v(TAG, "computer going over >= 20, do not add but wrap up then give it to user");
                //Toast.makeText(this, "computer going over >= 20", Toast.LENGTH_SHORT).show();
                computerOverallScore += computerTurnScore;
                timerHandler.removeCallbacks(computerRollRunnable);
                userTurn = true;
                enableRollAndHold();
            } else {
                computerTurnScore += diceRoll;
                Log.v(TAG, "computerTurnScore is now " + computerTurnScore + ", " +
                        "try another roll shortly...");
                timerHandler.postDelayed(this, 5000);
            }

            scoreView.setText("Your score: " + userOverallScore
                    + " computer score: " + computerOverallScore
                    + whoseTurn() + computerTurnScore);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = (TextView) findViewById(R.id.timerTextView);
        Button b = (Button) findViewById(R.id.button);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Button b = (Button) v;
                 if (b.getText().equals("stop")) {
                     timerHandler.removeCallbacks(computerRollRunnable);
                     b.setText("start");
                 } else {
                     startTime = System.currentTimeMillis();
                     timerHandler.postDelayed(computerRollRunnable, 0);
                     b.setText("stop");
                 }
             }
        });

        setInitialDisplay();
        setRollOnClickListener();
        setHoldOnClickListener();
        setResetOnClickListener();
    }


    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(computerRollRunnable);
        Button b = (Button)findViewById(R.id.button);
        b.setText("start");
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
        Log.v(TAG, "beginning computer total score: " + computerOverallScore);
        Log.v(TAG, "computer turn: 1st rolled " + diceRoll + " computerTurnScore: " + computerTurnScore);
        setDiceDisplay(diceRoll);
        if (diceRoll == 1) {
            Toast.makeText(this, "Computer rolled 1 - give turn to user!", Toast.LENGTH_SHORT).show();
            computerTurnScore = 0;
        }

        while ((computerTurnScore + diceRoll < 20) && (diceRoll != 1)) {
            //Toast.makeText(this, "Computer rolled: " + diceRoll, Toast.LENGTH_SHORT).show();
            processRoll(diceRoll);
            diceRoll = 1 + random.nextInt(DICE_SIDES);
            setDiceDisplay(diceRoll);
            if (diceRoll == 1) {
                Log.v(TAG, "computer rolled 1 - give turn to user!");
                Toast.makeText(this, "Computer rolled 1 - give turn to user!", Toast.LENGTH_SHORT).show();
                computerTurnScore = 0;

            } else if (computerTurnScore + diceRoll < 20) {
                Log.v(TAG, "computer rolled: " + diceRoll);
            } else {
                Log.v(TAG, "computer going over >= 20");
                Toast.makeText(this, "computer going over >= 20", Toast.LENGTH_SHORT).show();
            }
        }

        // add up the computer score before passing the control to the user
        computerOverallScore += computerTurnScore;

        scoreView.setText("Your score: " + userOverallScore
                + " computer score: " + computerOverallScore
                + whoseTurn() + computerTurnScore);

        userTurn = true;
        Log.v(TAG, "give the turn to user!");
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

        //Log.v(TAG, "rolled :" + rollValue);
        if (userTurn) {
            Log.v(TAG, "Update player's score - before: " + userTurnScore + " + " + rollValue);
            userTurnScore += rollValue;
            Log.v(TAG, "                      - after: " + userTurnScore);

            scoreView.setText("Your score: " + userOverallScore
                    + " computer score: " + computerOverallScore
                    + whoseTurn() + userTurnScore);
        } else {
            Log.v(TAG, "Update computer's score - before: " + computerTurnScore + " + " + rollValue);
            computerTurnScore += rollValue;
            Log.v(TAG, "                        - after: " + computerTurnScore);

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
                userTurn = true;
                Log.v(TAG, "Roll button clicked!");
                int diceRoll = 1 + random.nextInt(DICE_SIDES);
                // user rolled 1
                if (diceRoll == 1) {
                    Toast.makeText(getApplicationContext(), "User rolled 1!",
                            Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "User rolled 1! -> let computer roll now");
                    setDiceDisplay(diceRoll);
                    // user's turn score gets wiped out!
                    userTurnScore = 0;
                    userOverallScore += userTurnScore;
                    scoreView.setText("Your score: " + userOverallScore
                            + " computer score: " + computerOverallScore
                            + whoseTurn() + userTurnScore);

                    // call computerTurn for computer to play
                    /*
                    userTurn = false;
                    computerTurn();
                    */
                    computerTurnScore = 0;
                    timerHandler.postDelayed(computerRollRunnable, 0);


                } else {
                    Toast.makeText(getApplicationContext(), "" + "User rolled " + diceRoll,
                            Toast.LENGTH_SHORT).show();
                    Log.v(TAG, "user rolled " + diceRoll);

                    processRoll(diceRoll);
                    setDiceDisplay(diceRoll);
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
                userTurn = true;
                Log.v(TAG, "Hold button clicked");
                Toast.makeText(MainActivity.this, "Hold button clicked!", Toast.LENGTH_SHORT)
                        .show();
                userOverallScore += userTurnScore;
                userTurnScore = 0;

                scoreView.setText("Your score: " + userOverallScore
                        + " computer score: " + computerOverallScore
                        + whoseTurn() + userTurnScore);

                // call computerTurn for computer to play
                /*
                userTurn = false;
                computerTurn();
                */
                computerTurnScore = 0;
                timerHandler.postDelayed(computerRollRunnable, 0);


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
