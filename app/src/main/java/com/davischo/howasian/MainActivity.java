package com.davischo.howasian;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button startButton, stopButton;
    TextView timer, score, result, expression;
    Switch difficulty;
    GridLayout grid;
    Random rand;
    RelativeLayout rel;
    int correctAnswer;
    int correct, tries;
    boolean done;
    CountDownTimer cd;

    public void start(View view){
        startButton.setVisibility(View.INVISIBLE);
        stopButton.setVisibility(View.VISIBLE);
        difficulty.setVisibility(View.INVISIBLE);
        rel.setBackgroundColor(0x00000000);
        timer.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        expression.setVisibility(View.VISIBLE);
        grid.setVisibility(View.VISIBLE);
        result.setVisibility(View.INVISIBLE);
        score.setText("0/0");
        for(int i = 0; i < grid.getChildCount(); i++){
            grid.getChildAt(i).setEnabled(true);
        }
        correct = tries = 0;
        generate();
        cd = new CountDownTimer(30100, 1000) {
            @Override
            public void onTick(long l) {
                String timeLeft = String.format("%02d", l/1000);
                timer.setText(timeLeft+"s");
            }

            @Override
            public void onFinish() {
                done = true;
                for(int i = 0; i < grid.getChildCount(); i++){
                    grid.getChildAt(i).setEnabled(false);
                }
                startButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                difficulty.setVisibility(View.VISIBLE);
                result.setText("Again?");
            }
        }.start();
    }

    public void stop(View view){
        cd.cancel();
        cd.onFinish();
    }

    public void answerClicked(View view){
        result.setVisibility(View.VISIBLE);
        if (Integer.valueOf( view.getTag().toString() ) == correctAnswer) {
            //Add score
            correct++;
            tries++;
            result.setText("Correct!");
        }
        else {
            tries++;
            result.setText("Incorrect.");
        }
        score.setText(correct + "/" + tries);
        generate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        score = (TextView) findViewById(R.id.scoreView);
        result = (TextView) findViewById(R.id.resultView);
        expression = (TextView) findViewById(R.id.expressionView);
        timer = (TextView) findViewById(R.id.timerView);
        difficulty = (Switch) findViewById(R.id.difficulty);
        grid = (GridLayout)findViewById(R.id.grid);
        rel = (RelativeLayout) findViewById(R.id.activity_main);
        rand = new Random();
    }

    public void generate(){
        int operation = rand.nextInt(4);
        String operationChar;
        int range = difficulty.isChecked() ? 1001:101;
        double a = 0;
        double b = 0;
        double answer = -1;
        if(operation==0) {
            //add
            a = rand.nextInt(range);
            b = rand.nextInt(range);
            answer = a + b;
            operationChar = "+";
        }
        else if(operation==1) {
            //subtract
            while(answer < 0){
                a = rand.nextInt(range);
                b = rand.nextInt(range);
                answer = a - b;
            }
            operationChar = "-";
        }
        else if(operation==2) {
            //multiply
            a = rand.nextInt(range/10);
            b = rand.nextInt(range/10);
            answer = a * b;
            operationChar = "X";
        }
        else{
            //divide
            while(b==0) {
                answer = rand.nextInt(range / 10);
                b = rand.nextInt(range / 10);
                a = answer * b;
            }
            operationChar = "/";
        }

        expression.setText((int)a + operationChar + (int)b);

        //Populate random answers
        for(int i = 0; i < 4; i++){
            Button curr = grid.findViewWithTag(String.valueOf(i));
            double randAnswer;
            if(operation==0) {
                //add
                randAnswer = rand.nextInt(2*range);
                while(randAnswer==answer){
                    randAnswer = rand.nextInt(2*range);
                }
            }
            else if(operation==1) {
                //subtract
                randAnswer = rand.nextInt(range);
                while(randAnswer==answer){
                    randAnswer = rand.nextInt(range);
                }
            }
            else if(operation==2) {
                //multiply
                randAnswer = rand.nextInt((range/5+1)) * rand.nextInt((range/5+1));
                while(randAnswer==answer){
                    randAnswer = rand.nextInt((range/5+1)) * rand.nextInt((range/5+1));
                }
            }
            else{
                randAnswer = rand.nextInt((range/5+1));
                while(randAnswer==answer){
                    randAnswer = rand.nextInt((range/5+1));
                }
            }
            curr.setText(String.valueOf((int)randAnswer));
        }

        //Add correct answer
        correctAnswer = rand.nextInt(4);
        Button setAnswer = grid.findViewWithTag(String.valueOf(correctAnswer));
        setAnswer.setText(String.valueOf((int)answer));
    }
}
