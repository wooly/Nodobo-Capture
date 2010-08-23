package com.squares;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.*;

public class Squares extends Activity
{
    public final String TAG = "Squares";
    
    public final int[] sizes = {20, 40, 66, 86};
    public final int smallButtonSize = 20;
    public final int mediumButtonSize = 40;
    public final int largeButtonSize = 66;
    public final int hugeButtonSize = 86;
    
    public final int screenWidth = 320;
    public final int screenHeight = 533;
    
    public int userPosition = 0;
    
    public Boolean started = false;
    public Button button = null;
    public List boxes = new ArrayList();
    public RelativeLayout rl = null;
    public RelativeLayout.LayoutParams startParams = new RelativeLayout.LayoutParams(120, 40);
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
                
        long seed = System.currentTimeMillis() % 1000;
        generator = new Random(seed);
        
        for (int i = 0; i < 20; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                boxes.add(sizes[j]);
            }
        }
        Log.d(TAG, "Boxes: " + boxes.size());
        
        Collections.shuffle(boxes);
        Log.d(TAG, "Shuffled boxes: " + boxes.size());
        
        button = new Button(this);
        button.setText("Start!");
        
        startParams.leftMargin = 100;
        startParams.topMargin = 246;
        rl.addView(button, startParams);
        setContentView(rl);
                
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (userPosition < 80)
                {
                    Integer nextSize = (Integer) boxes.get(userPosition);
                    Log.d(TAG, "Size of next box: " + nextSize);

                    RelativeLayout.LayoutParams nextParams = new RelativeLayout.LayoutParams(nextSize, nextSize);
                    nextParams.leftMargin = generator.nextInt(screenWidth - nextSize);
                    nextParams.topMargin = generator.nextInt(screenHeight - nextSize);
                    Log.d(TAG, "Next box position: (" + nextParams.leftMargin + "," + nextParams.topMargin + ")");
                
                    rl.updateViewLayout(button, nextParams);
                    userPosition++;
                }
                else
                {
                    Log.d(TAG, "80 Boxes Completed");
                    finish();
                }
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
