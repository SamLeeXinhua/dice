package com.samlee.jason.dice;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class diceMainActivity extends AppCompatActivity {

    // this random is used to pick up the related dice image
    Random rand = new Random();
    int num;
    static int totalNum = 0 ;
    static int rollTime = 1;
    int locator = 0;
    ArrayList recentNum = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_main);

        //this is the initial dice image, show this first before starting
        final ImageView diceinitImage = (ImageView) findViewById(R.id.diceinit);
        final TextView point = (TextView) findViewById(R.id.totalPoint);
        final Button clearButton = (Button) findViewById(R.id.clearButton);
        recentNum.clear();
        //register the dice action
        diceinitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // sleep 600 ms to make it smooth
                SystemClock.sleep(300);
                Toast toast = Toast.makeText(getApplicationContext(), "rolled", Toast.LENGTH_SHORT);
                toast.show();

                // pick up the image for empty dice.
                try {
                    InputStream zeroStream = getAssets().open("dice0.png");
                    Drawable dTemp = Drawable.createFromStream(zeroStream,null);
                    diceinitImage.setImageDrawable(dTemp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //sleep 300 ms again
                SystemClock.sleep(200);

                num = rand.nextInt(6) + 1;
                totalNum = totalNum + num;
                recentNum.add(num);
                Log.i("locator: " + locator, "\nrecentNum: " + recentNum.get(locator));
                // try to see if we rolled the same number
                if(locator >= 3 && (recentNum.get(locator - 2).equals(recentNum.get(locator - 1)) && recentNum.get(locator - 2).equals(recentNum.get(locator)) )){
                    Toast showGree = Toast.makeText(getApplicationContext(), "Congrantulation! , same dice in 3 times, 10 points added", Toast.LENGTH_LONG);
                    showGree.show();
                    totalNum = totalNum +10;
                }
                //test code
//                if(locator >= 3 && (recentNum.get(locator - 2).equals(recentNum.get(locator - 1)) )){
//                    Log.i("trible same dice", ""+recentNum.get(locator));
//                }
                locator++;

                String imageName = "dice" + num + ".png";
                try {
                    //fetch the image and show it
                    InputStream stream = getAssets().open(imageName);
                    Drawable d = Drawable.createFromStream(stream, null);
                    diceinitImage.setImageDrawable(d);
                    point.setText("Total points : " + totalNum + "\n\nRolled times: " + rollTime++);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear the point and roll times
                totalNum = 0;
                rollTime = 1;
                locator = 0;
                recentNum.clear();
                try {
                    InputStream initStream = getAssets().open("diceinit.png");
                    Drawable dInit = Drawable.createFromStream(initStream,null);
                    diceinitImage.setImageDrawable(dInit);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                point.setText("Total points : 0" + "\n\nRolled times: 0" );
            }
        });

    }
}
