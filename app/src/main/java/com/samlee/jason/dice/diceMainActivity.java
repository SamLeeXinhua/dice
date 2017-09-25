package com.samlee.jason.dice;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class diceMainActivity extends AppCompatActivity {

    Random rand = new Random();
    int currentImageNum;
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

        // this is the bar for point and roll times shown
        final TextView point = (TextView) findViewById(R.id.totalPoint);

        // button to clear the point and roll times
        final Button clearButton = (Button) findViewById(R.id.clearButton);

        //cleant the arraylist
        recentNum.clear();
        //register the dice action

        diceinitImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            // catch the touch event here , only down event will be caught.
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        // this is used for frame action
                        AnimationDrawable anim = new AnimationDrawable();
                        for (int i = 1; i <= 6 ; i++) {
                            try {

                                // pick up 6 dices randomly
                                currentImageNum = rand.nextInt(6) + 1;
                                InputStream stream = getResources().getAssets().open("dice" + currentImageNum + ".png");
                                Drawable d = Drawable.createFromStream(stream, null);
                                anim.addFrame(d, 100);

                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }

                        anim.setOneShot(true);
                        diceinitImage.setImageDrawable(anim);
                        anim.start();

                        // this is for roate action
                        RotateAnimation rotate = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(600);
                        rotate.setFillAfter(false);
                        diceinitImage.startAnimation(rotate);

                        // catch the point and store into the array
                        totalNum = totalNum + currentImageNum;
                        recentNum.add(currentImageNum);

                        // if 3 dices were rolled at continue 3 times, 10 additional bonus will be added
                        if(locator >= 3 && (recentNum.get(locator - 2).equals(recentNum.get(locator - 1)) && recentNum.get(locator - 2).equals(recentNum.get(locator)) )){
                            Toast showCheer = Toast.makeText(getApplicationContext(), "Cheer! , 10 bonus additional ", Toast.LENGTH_LONG);
                            showCheer.show();
                            totalNum = totalNum +10;
                        }

                        locator++;
                        point.setText("Total bonus : " + totalNum + "\n\nRolled times: " + rollTime++);
                        break;
                    // We can add more action event here
                }
                return false;
            }
        });

        // clear button
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear the bonus and roll times
                totalNum = 0;
                rollTime = 1;
                locator = 0;
                recentNum.clear();
                try {
                    // show the initial image
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
