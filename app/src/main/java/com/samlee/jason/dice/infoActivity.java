package com.samlee.jason.dice;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class infoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView infoText = (TextView) findViewById(R.id.description);


        PackageInfo pkinfo = null;
        try {
            pkinfo = getPackageManager().getPackageInfo("com.samlee.jason.dice", PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        int pkVersionCode = pkinfo.versionCode ;
        String pkVersionName = pkinfo.versionName;

        infoText.setText("Current Version:" + pkVersionCode);
//
//        Toast diceToast = Toast.makeText(getApplicationContext(),"versionCode :" + pkVersionCode +"\n" + "versionName:" + pkVersionName, Toast.LENGTH_LONG);
//        diceToast.show();


    }
}