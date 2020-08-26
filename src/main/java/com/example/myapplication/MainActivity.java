package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText redValue;
    EditText blueValue;
    EditText greenValue;
    Button searchButton;
    TextView responseText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redValue = findViewById(R.id.RED);
        blueValue = findViewById(R.id.BLUE);
        greenValue = findViewById(R.id.GREEN);
        responseText = findViewById(R.id.responseText);
        searchButton = findViewById(R.id.SUBMIT);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String red = redValue.getText().toString();
                String green = greenValue.getText().toString();
                String blue = blueValue.getText().toString();

                int dRed = Integer.parseInt(red);
                int dGreen = Integer.parseInt(green);
                int dBlue = Integer.parseInt(blue);

                determineOutput(dRed, dGreen, dBlue);
            }
        });
    }



    void determineOutput(Integer red, Integer green, Integer blue){
        //This section of code is for File Reading
        Scanner trainingFile = null;
        try {
            trainingFile = new Scanner(new File("training.txt"));
        }
        catch (Exception e) {
            System.out.println("File did not open.");
            System.exit(-1);
        }

        //This section of code is to Train the Perceptron
        //Creates integer arrays to save the R, G, B, and Answer Values from Training File
        int[] rTraining = new int[1000];
        int[] gTraining = new int[1000];
        int[] bTraining = new int[1000];
        int[] trainingAnswer = new int[1000];

        //Creates random double values for the weights as well as the threshold
        double rWeight = Math.random() * 2;
        double gWeight = Math.random() * 2;
        double bWeight = Math.random() * 2;
        double threshold = Math.random() * 1000;

        //Creates variables that will be later used for Training and Testing purposes
        int predictedOutput;
        int error;
        double learningRate = 0.0001;
        int correct = 0;
        int incorrect = 0;

        //Obtains the R, G, B, and Answer values from the Training File and saves them in integer arrays
        for (int i = 0; i < 1000; i++) {
            rTraining[i] = trainingFile.nextInt();
            gTraining[i] = trainingFile.nextInt();
            bTraining[i] = trainingFile.nextInt();
            trainingAnswer[i] = trainingFile.nextInt();
        }

        //This section of code is to repeat until the Perceptron gets all of the predicted outputs correct
        do {
            correct = 0;
            incorrect = 0;
            for (int i = 0; i < 1000; i++) {

                //Implementing Feed Forward Algorithm
                double weightedSumTraining = (rWeight*rTraining[i]) + (gWeight*gTraining[i]) + (bWeight*bTraining[i]);

                //Determines the output by using the weightSum and the threshold
                if (weightedSumTraining < threshold) {
                    predictedOutput = -1;
                }
                else {
                    predictedOutput = 1;
                }

                //Calculates the error, and uses this value to determine if the weights and threshold should be changed or not
                error = trainingAnswer[i] - predictedOutput;

                //Implementing Training Algorithm
                if (error != 0 ) {
                    incorrect += 1;
                    rWeight = rWeight + (error*learningRate*rTraining[i]);
                    gWeight = gWeight + (error*learningRate*gTraining[i]);
                    bWeight = bWeight + (error*learningRate*bTraining[i]);
                    threshold = threshold + (error*learningRate);
                }
                else {
                    correct += 1;
                }
            }
        }
        while (correct < 1000);

        //This section of code tests the Perceptron

        String x = "black";
        String y = "white";

        //Implementing Feed Forward Algorithm
        double weightedSumTest = (rWeight*red) + (gWeight*green) + (bWeight*blue);

        //Determines the output by using the weightSum and the threshold
        if (weightedSumTest < threshold) {
            predictedOutput = -1;
            responseText.setText(x);
        }
        else {
            predictedOutput = 1;
            responseText.setText(y);
        }
    }

}