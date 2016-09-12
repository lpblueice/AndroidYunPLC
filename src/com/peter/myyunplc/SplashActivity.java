package com.peter.myyunplc;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class SplashActivity extends Activity
{
	private final int SPLASH_DISPLAY_LENGHT = 3000; //—”≥Ÿ»˝√Î 
	 
    @Override
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_splash); 
        new Handler().postDelayed(new Runnable(){  
         @Override
         public void run() { 
             Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class); 
             startActivity(mainIntent); 
             finish(); 
         } 
             
        }, SPLASH_DISPLAY_LENGHT); 
    } 

}
