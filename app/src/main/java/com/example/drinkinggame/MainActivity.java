package com.example.drinkinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import java.util.Arrays;
import java.util.Random;
import android.os.AsyncTask;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView mNumberField;
    TextView mResultField;
    Button mButton;

    private class PlayGamesTask extends AsyncTask<Integer, Float, Float> {

        protected Float doInBackground(Integer... runs) {
            long total = 0;
            float average = 0.0f;

            for (int i = 1; i <= runs[0]; i++) {
                total += play_game();
                average = (float)total / (float)i;

                if ((i % 1000) == 0) {
                    publishProgress(average);
                }
            }
            return average;
        }

        protected void onProgressUpdate(Float... progress) {
            Log.d("DrinkingGame", String.format("%.2f...", progress[0].floatValue()));
            mResultField.setText(String.format("%.2f...", progress[0].floatValue()));
        }

        protected void onPostExecute(Float result) {
            mResultField.setText(String.format("%.2f.", result.floatValue()));
            mButton.setEnabled(true);
        }

        protected float play_game () {
            int[] glass = {1, 1, 1, 1, 1, 1};
            Random r = new Random();

            int drinks = 0;
            while (Arrays.stream(glass).sum() > 0) {
                int dice = r.nextInt(6);

                if (glass[dice] == 1) drinks++;
                glass[dice] = 1 - glass[dice];
            }
            //Log.d("Info: ", Integer.toString(drinks));
            return drinks;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNumberField = (TextView) findViewById(R.id.numberField);
        mResultField = (TextView) findViewById(R.id.resultField);
        mButton = (Button) findViewById(R.id.calcButton);
        mButton.setOnClickListener(this);
    }

    public void onClick(View v) {

        try {
            int runs = Integer.parseInt(mNumberField.getText().toString());
            mButton.setEnabled(false);

            Log.d("DrinkingGame", String.format("Running %d simulations...", runs));

            new PlayGamesTask().execute(runs);
        }
        catch(java.lang.NumberFormatException e) {
            mResultField.setText("Not a number");
        }
    }
}
