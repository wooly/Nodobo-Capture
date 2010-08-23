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

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Squares extends Activity
{
    public final String TAG = "Squares";
    
    public final int[] sizes = {20, 40, 66, 86};
    
    public final int screenWidth = 320;
    public final int screenHeight = 533;
    
    public final String captureDir = "/nodobo/capture/analysis";
    
    public int userPosition = 0;
    
    public Boolean started = false;
    public Button button = null;
    public File outFile = null;
    public FileOutputStream outStream = null;
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
        
        boolean success = (new File(captureDir)).mkdir();
        if (success) {
          Log.d(TAG, "Directory: " + captureDir + " created");
        }
        
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        
        try {
            outFile = new File(captureDir + "/" + dateFormat.format(new Date()) + ".csv");
    		outFile.createNewFile();
            outStream = new FileOutputStream(outFile, true);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
        
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
                    
                    try {
                        String data = "" + System.currentTimeMillis() + "," + nextSize + "," + (int)((nextParams.leftMargin + nextSize/2)*1.5) + "," + (int)((nextParams.topMargin + nextSize/2)*1.5) + "\n";
                        outStream.write(data.getBytes("UTF-8"));
                    } catch (Exception e) {
                        Log.e(TAG, "Exception: " + e);
                    }
                }
                else
                {
                    Log.d(TAG, "80 Boxes Completed");
                    try {
                        String finish = "Finished: " + System.currentTimeMillis();
                        outStream.write(finish.getBytes("UTF-8"));
                        outStream.close();
                    } catch (Exception e) {
                        Log.e(TAG, "Exception: e");
                    }
                    finish();
                }
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try {
            outStream.write(new String("interrupted").getBytes("UTF-8"));
            outStream.close();
        } catch (Exception e) {
            Log.d(TAG, "Exception: e");
        }
        finish();
    }
}
