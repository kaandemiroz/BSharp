package com.okd.bsharp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity{//} implements RecognitionListener{

    TextView spokenWords;

    public static final String TAG = "BSharp";

    private SoundAnalyzer soundAnalyzer = null ;
    private TextView mainMessage = null;

    private double[] frequencies = {82.4, 87.3, 92.5, 98.0, 103.8,
                                    110.0, 116.5, 123.5, 130.8, 138.6,
                                    146.8, 155.6, 164.8, 174.6, 185.0,
                                    196.0, 207.6, 220.0, 233.1,
                                    246.9, 261.6, 277.2, 293.6, 311.1,
                                    329.6, 349.2, 370.0, 392.0, 415.3,
                                    440.0, 466.1, 493.8, 523.2, 554.3,
                                    587.3, 622.2, 659.2, 698.5, 740.0,
                                    784.0, 830.6, 880.0, 932.3, 987.8, 1047};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spokenWords = (TextView) findViewById(R.id.speech);

        UiController uiController = new UiController(MainActivity.this);
        try {
            soundAnalyzer = new SoundAnalyzer();
        } catch (Exception e) {
            Toast.makeText(this, "There are problems with your microphone", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Exception when instantiating SoundAnalyzer: " + e.getMessage());
        }
        soundAnalyzer.addObserver(uiController);
        mainMessage = (TextView) findViewById(R.id.mainMessage);

        Spinner tuningSelector = (Spinner) findViewById(R.id.tuningSelector);
        Tuning.populateSpinner(this, tuningSelector);
        tuningSelector.setOnItemSelectedListener(uiController);
    }

    public void dumpArray(final double [] inputArray, final int elements) {
        Log.d(TAG, "Starting File writer thread...");
        final double [] array = new double[elements];
        System.arraycopy(inputArray, 0, array, 0, elements);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try { // catches IOException below
                    // Location: /data/data/your_project_package_structure/files/samplefile.txt
                    String name = "Chart_" + (int)(Math.random()*1000) + ".data";
                    FileOutputStream fOut = openFileOutput(name,
                            Context.MODE_WORLD_READABLE);
                    OutputStreamWriter osw = new OutputStreamWriter(fOut);

                    // Write the string to the file
                    for(int i=0; i<elements; ++i)
                        osw.write("" + array[i] + "\n");
					/* ensure that everything is
					 * really written out and close */
                    osw.flush();
                    osw.close();
                    Log.d(TAG, "Successfully dumped array in file " + name);
                } catch(Exception e) {
                    Log.e(TAG,e.getMessage());
                }
            }
        }).start();
    }

    int oldString = 0;
    // 1-6 strings (ascending frequency), 0 - no string.
    public void changeString(int stringId) {
        if(oldString!=stringId) {
            oldString=stringId;
        }
    }

    public void displayMessage(String msg, boolean positiveFeedback) {
        int textColor = positiveFeedback ? Color.rgb(34,139,34) : Color.rgb(255,36,0);
        mainMessage.setText(msg);
        mainMessage.setTextColor(textColor);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        ispikitWrapper.Stop(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(soundAnalyzer!=null) soundAnalyzer.ensureStarted();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(soundAnalyzer!=null) soundAnalyzer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(soundAnalyzer!=null) soundAnalyzer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}