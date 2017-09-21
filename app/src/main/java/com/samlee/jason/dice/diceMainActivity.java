package com.samlee.jason.dice;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class diceMainActivity extends AppCompatActivity {

    // this random is used to pick up the related dice image
    Random rand = new Random();
    int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_main);

        //this is the initial dice image, show this first before starting
        final ImageView diceinitImage = (ImageView) findViewById(R.id.diceinit);

        //register the dice action
        diceinitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sleep 600 ms to make it smooth
                SystemClock.sleep(600);
                Toast toast = Toast.makeText(getApplicationContext(), "rolling.", Toast.LENGTH_SHORT);
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
                SystemClock.sleep(300);

                num = rand.nextInt(6) + 1;
                String imageName = "dice" + num + ".png";
                try {
                    //fetch the image and show it
                    InputStream stream = getAssets().open(imageName);
                    Drawable d = Drawable.createFromStream(stream, null);
                    diceinitImage.setImageDrawable(d);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
