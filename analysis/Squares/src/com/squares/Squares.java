package com.squares;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Random;

public class Squares extends Activity
{
    public int x = 50;
    public int y = 60;
    
    public final int smallButtonSize = 20;
    public final int mediumButtonSize = 40;
    public final int largeButtonSize = 60;
    public final int hugeButtonSize = 80;
    
    public Boolean started = false;
    public Button button = null;
    public RelativeLayout rl = null;
    public RelativeLayout.LayoutParams startParams = new RelativeLayout.LayoutParams(120, 40);
    public RelativeLayout.LayoutParams smallParams = new RelativeLayout.LayoutParams(30, 30);
    public Random generator = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        setContentView(R.layout.main);
        
        rl = (RelativeLayout) findViewById(R.id.layout);
        
        Display display = getWindowManager().getDefaultDisplay(); 
        int width = display.getHeight();
        int height = display.getWidth();
        
        long seed = System.currentTimeMillis() % 1000;
        generator = new Random(seed);
        
        button = new Button(this);
        button.setText("Start!");
        
        startParams.leftMargin = 100;
        startParams.topMargin = 246;
        rl.addView(button, startParams);
        setContentView(rl);
        
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int nextX = generator.nextInt(290);
                int nextY = generator.nextInt(503);
                
                smallParams.leftMargin = nextX;
                smallParams.topMargin = nextY;
                
                rl.updateViewLayout(button, smallParams);
                // setContentView(rl);
            }
        });                
    }

    @Override
    public void onPause()
    {
        super.onPause();
        finish();
    }
}
