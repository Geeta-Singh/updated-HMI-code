package com.bydesign.hmicontroller.activity;

/**
 * Created by PARIKSHIT on 1/21/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import info.bydesign.hmicontroller.R;

public class SplashscreenActivity extends Activity {

    // Set Duration of the Splash Screen
    int progress=0;
    int progressStatus=0;
    ProgressBar pb;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        pb=(ProgressBar)findViewById(R.id.progressbar);


        new Thread(new Runnable(){

            @Override
            public void run() {
                while(progress<10){
                    progressStatus=doSomeWork();
                    handler.post(new Runnable(){

                        @Override
                        public void run() {
                            pb.setProgress(progressStatus);
                        }

                    });

                }
                Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }

            private int doSomeWork() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return ++progress;
            }

        }).start();
    }
}