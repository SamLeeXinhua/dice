package com.samlee.jason.dice;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class dicesActivity extends AppCompatActivity {



    Random randOne = new Random();
    Random randTwo = new Random();
    Random randThree = new Random();

    int currentOneImageNum;
    int currentTwoImageNum;
    int currentThreeImageNum;
    int diceTotalNum = 0 ;
    int diceRollTime = 1;

    // this onCreateOptionsMenu is  used to show the element on action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dices_activity_actions, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dices);

        final ImageView diceOneImage = (ImageView) findViewById(R.id.diceOne);
        final ImageView diceTwoImage = (ImageView) findViewById(R.id.diceTwo);
        final ImageView diceThreeImage = (ImageView) findViewById(R.id.diceThree);
        final MenuItem aboutMenu = (MenuItem) findViewById(R.id.action_about);

        final TextView point = (TextView) findViewById(R.id.totalPoint);

        final Button clearButton = (Button) findViewById(R.id.clearButton);

        // create a sound for dice roll
        final SoundPool diceSoundPool;
        final SoundPool bonusSoundPool;
        final SoundPool clearSoundPool;

        // Create a sound pool
        if (Build.VERSION.SDK_INT <= 15) {
            //API level 15
            diceSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
            bonusSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
            clearSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

        } else {
            //API level 21
            diceSoundPool = new SoundPool.Builder().setMaxStreams(10).build();
            bonusSoundPool = new SoundPool.Builder().setMaxStreams(10).build();
            clearSoundPool = new SoundPool.Builder().setMaxStreams(10).build();
        }

        final int diceSound;
        final int bonusSound;
        final int clearSound;

        diceSound = diceSoundPool.load(this , R.raw.coin,1);
        bonusSound = bonusSoundPool.load(this , R.raw.lync_videocall,1);
        clearSound = clearSoundPool.load(this , R.raw.windows_notify,1);

        //TODO start

        final VideoView firework = (VideoView) findViewById(R.id.firework);
        firework.setVisibility(View.INVISIBLE);

        diceOneImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            // catch the touch event here , only down event will be caught.
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        // dice one
                        //play the dice roll music
                        diceSoundPool.play(diceSound,1,1,0,0,1);
                        // this is used for frame action
                        AnimationDrawable animOne = new AnimationDrawable();
                        AnimationDrawable animTwo = new AnimationDrawable();
                        AnimationDrawable animThree = new AnimationDrawable();

                        for (int i = 1; i <= 3 ; i++) {
                            try {
                                // pick up 3 dices randomly for dice One
                                currentOneImageNum = randOne.nextInt(6) + 1;
                                InputStream streamOne = getResources().getAssets().open("dice" + currentOneImageNum + ".png");
                                Drawable dOne = Drawable.createFromStream(streamOne, null);
                                animOne.addFrame(dOne, 90);

                                // pick up 3 dices randomly for dice Two
                                currentTwoImageNum = randTwo.nextInt(6) + 1;
                                InputStream streamTwo = getResources().getAssets().open("dice" + currentTwoImageNum + ".png");
                                Drawable dTwo = Drawable.createFromStream(streamTwo, null);
                                animTwo.addFrame(dTwo, 90);

                                // pick up 3 dices randomly for dice Three
                                currentThreeImageNum = randThree.nextInt(6) + 1;
                                InputStream streamThree = getResources().getAssets().open("dice" + currentThreeImageNum + ".png");
                                Drawable dThree = Drawable.createFromStream(streamThree, null);
                                animThree.addFrame(dThree, 90);


                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }

                        animOne.setOneShot(true);
                        animTwo.setOneShot(true);
                        animThree.setOneShot(true);
                        diceOneImage.setImageDrawable(animOne);
                        diceTwoImage.setImageDrawable(animTwo);
                        diceThreeImage.setImageDrawable(animThree);
                        animOne.start();
                        animTwo.start();
                        animThree.start();

                        // this is for roate action
                        RotateAnimation rotate = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(280);
                        rotate.setFillAfter(false);
                        diceOneImage.startAnimation(rotate);
                        diceTwoImage.startAnimation(rotate);
                        diceThreeImage.startAnimation(rotate);

                        // catch the point and store into the array
                        diceTotalNum = diceTotalNum + currentOneImageNum + currentTwoImageNum + currentThreeImageNum;

                        // if 3 dices were rolled the same value, 10 additional bonus will be added
                        if(currentOneImageNum == currentTwoImageNum && currentOneImageNum == currentThreeImageNum ){
                            Toast showCheer = Toast.makeText(getApplicationContext(), "Cheer! , 10 bonus additional ", Toast.LENGTH_LONG);
                            showCheer.show();
                            diceTotalNum = diceTotalNum +10;
                            bonusSoundPool.play(diceSound,1,1,0,0,1);
//
                            firework.setVisibility(View.VISIBLE);

                            Uri rawUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fireworks1c);
                            firework.setVideoURI(rawUri);
                            firework.start();
                            firework.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    Toast Cheer = Toast.makeText(getApplicationContext(), "Cheer! , 10 bonus additional for same dice", Toast.LENGTH_LONG);
                                    Cheer.show();

                                    firework.setVisibility(View.GONE);
                                }
                            });
                        }

                        point.setText("Total bonus : " + diceTotalNum + "\n\nRolled times: " + diceRollTime++);
                        break;
                    // We can add more action event here
                    case MotionEvent.ACTION_UP:
//                        diceinitImage.setBackgroundColor(0);
                        break;
                }

                return false;
            }
        });


        // dice two , the same as dice one
        diceTwoImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            // catch the touch event here , only down event will be caught.
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        // dice one
                        //play the dice roll music
                        diceSoundPool.play(diceSound,1,1,0,0,1);
                        // this is used for frame action
                        AnimationDrawable animOne = new AnimationDrawable();
                        AnimationDrawable animTwo = new AnimationDrawable();
                        AnimationDrawable animThree = new AnimationDrawable();

                        for (int i = 1; i <= 3 ; i++) {
                            try {
                                // pick up 3 dices randomly for dice One
                                currentOneImageNum = randOne.nextInt(6) + 1;
                                InputStream streamOne = getResources().getAssets().open("dice" + currentOneImageNum + ".png");
                                Drawable dOne = Drawable.createFromStream(streamOne, null);
                                animOne.addFrame(dOne, 90);

                                // pick up 3 dices randomly for dice Two
                                currentTwoImageNum = randTwo.nextInt(6) + 1;
                                InputStream streamTwo = getResources().getAssets().open("dice" + currentTwoImageNum + ".png");
                                Drawable dTwo = Drawable.createFromStream(streamTwo, null);
                                animTwo.addFrame(dTwo, 90);

                                // pick up 3 dices randomly for dice Three
                                currentThreeImageNum = randThree.nextInt(6) + 1;
                                InputStream streamThree = getResources().getAssets().open("dice" + currentThreeImageNum + ".png");
                                Drawable dThree = Drawable.createFromStream(streamThree, null);
                                animThree.addFrame(dThree, 90);


                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }

                        animOne.setOneShot(true);
                        animTwo.setOneShot(true);
                        animThree.setOneShot(true);
                        diceOneImage.setImageDrawable(animOne);
                        diceTwoImage.setImageDrawable(animTwo);
                        diceThreeImage.setImageDrawable(animThree);
                        animOne.start();
                        animTwo.start();
                        animThree.start();

                        // this is for roate action
                        RotateAnimation rotate = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(280);
                        rotate.setFillAfter(false);
                        diceOneImage.startAnimation(rotate);
                        diceTwoImage.startAnimation(rotate);
                        diceThreeImage.startAnimation(rotate);

                        // catch the point and store into the array
                        diceTotalNum = diceTotalNum + currentOneImageNum + currentTwoImageNum + currentThreeImageNum;

                        // if 3 dices were rolled the same value, 10 additional bonus will be added
                        if(currentOneImageNum == currentTwoImageNum && currentOneImageNum == currentThreeImageNum ){
                            Toast showCheer = Toast.makeText(getApplicationContext(), "Cheer! , 10 bonus additional ", Toast.LENGTH_LONG);
                            showCheer.show();
                            diceTotalNum = diceTotalNum +10;
                            bonusSoundPool.play(diceSound,1,1,0,0,1);
//
                            firework.setVisibility(View.VISIBLE);

                            Uri rawUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fireworks1c);
                            firework.setVideoURI(rawUri);
                            firework.start();
                            firework.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    Toast Cheer = Toast.makeText(getApplicationContext(), "Cheer! , 10 bonus additional for same dice", Toast.LENGTH_LONG);
                                    Cheer.show();

                                    firework.setVisibility(View.GONE);
                                }
                            });
                        }

                        point.setText("Total bonus : " + diceTotalNum + "\n\nRolled times: " + diceRollTime++);
                        break;
                    // We can add more action event here
                    case MotionEvent.ACTION_UP:
//                        diceinitImage.setBackgroundColor(0);
                        break;
                }

                return false;
            }
        });

        // dice three , the same as dice one
        diceThreeImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            // catch the touch event here , only down event will be caught.
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        // dice one
                        //play the dice roll music
                        diceSoundPool.play(diceSound,1,1,0,0,1);
                        // this is used for frame action
                        AnimationDrawable animOne = new AnimationDrawable();
                        AnimationDrawable animTwo = new AnimationDrawable();
                        AnimationDrawable animThree = new AnimationDrawable();

                        for (int i = 1; i <= 3 ; i++) {
                            try {
                                // pick up 3 dices randomly for dice One
                                currentOneImageNum = randOne.nextInt(6) + 1;
                                InputStream streamOne = getResources().getAssets().open("dice" + currentOneImageNum + ".png");
                                Drawable dOne = Drawable.createFromStream(streamOne, null);
                                animOne.addFrame(dOne, 90);

                                // pick up 3 dices randomly for dice Two
                                currentTwoImageNum = randTwo.nextInt(6) + 1;
                                InputStream streamTwo = getResources().getAssets().open("dice" + currentTwoImageNum + ".png");
                                Drawable dTwo = Drawable.createFromStream(streamTwo, null);
                                animTwo.addFrame(dTwo, 90);

                                // pick up 3 dices randomly for dice Three
                                currentThreeImageNum = randThree.nextInt(6) + 1;
                                InputStream streamThree = getResources().getAssets().open("dice" + currentThreeImageNum + ".png");
                                Drawable dThree = Drawable.createFromStream(streamThree, null);
                                animThree.addFrame(dThree, 90);


                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }

                        animOne.setOneShot(true);
                        animTwo.setOneShot(true);
                        animThree.setOneShot(true);
                        diceOneImage.setImageDrawable(animOne);
                        diceTwoImage.setImageDrawable(animTwo);
                        diceThreeImage.setImageDrawable(animThree);
                        animOne.start();
                        animTwo.start();
                        animThree.start();

                        // this is for roate action
                        RotateAnimation rotate = new RotateAnimation(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(280);
                        rotate.setFillAfter(false);
                        diceOneImage.startAnimation(rotate);
                        diceTwoImage.startAnimation(rotate);
                        diceThreeImage.startAnimation(rotate);

                        // catch the point and store into the array
                        diceTotalNum = diceTotalNum + currentOneImageNum + currentTwoImageNum + currentThreeImageNum;

                        // if 3 dices were rolled the same value, 10 additional bonus will be added
                        if(currentOneImageNum == currentTwoImageNum && currentOneImageNum == currentThreeImageNum ){
                            Toast showCheer = Toast.makeText(getApplicationContext(), "Cheer! , 10 bonus additional ", Toast.LENGTH_LONG);
                            showCheer.show();
                            diceTotalNum = diceTotalNum +10;
                            bonusSoundPool.play(diceSound,1,1,0,0,1);
//
                            firework.setVisibility(View.VISIBLE);

                            Uri rawUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fireworks1c);
                            firework.setVideoURI(rawUri);
                            firework.start();
                            firework.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    Toast Cheer = Toast.makeText(getApplicationContext(), "Cheer! , 10 bonus additional for same dice", Toast.LENGTH_LONG);
                                    Cheer.show();

                                    firework.setVisibility(View.GONE);
                                }
                            });
                        }

                        point.setText("Total bonus : " + diceTotalNum + "\n\nRolled times: " + diceRollTime++);
                        break;
                    // We can add more action event here
                    case MotionEvent.ACTION_UP:
//                        diceinitImage.setBackgroundColor(0);
                        break;
                }

                return false;
            }
        });
        //TODO end


        // clear button; clear the bonus and initial dice start screen
        clearButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        diceTotalNum = 0;
                        diceRollTime = 1;
                        try {
                            // show the initial image
                            InputStream initStream = getAssets().open("diceinit.png");
                            Drawable dInit = Drawable.createFromStream(initStream,null);
                            diceOneImage.setImageDrawable(dInit);
                            diceTwoImage.setImageDrawable(dInit);
                            diceThreeImage.setImageDrawable(dInit);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // play the clear button sound
                        clearSoundPool.play(clearSound,1,1,0,0,1);
                        point.setText("Total bonus : 0" + "\n\nRolled times: 0" );

                        // add the motion for clear button
                        clearButton.setBackgroundColor(0x99000);
                        break;

                    // add the motion for clear button with above codes
                    case MotionEvent.ACTION_UP:
                        clearButton.setBackgroundResource(R.drawable.dice0);
                        break;
                }

                return false;
            }
        });

    }

    public void aboutDiceClickHandler(MenuItem item) {

        // open about activity
        Intent aboutIntent = new Intent(dicesActivity.this, aboutActivity.class);
        startActivity(aboutIntent);

    }
}
