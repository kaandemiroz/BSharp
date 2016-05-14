package com.okd.bsharp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int VOICE_RECOGNITION = 1;
    Button speakButton ;
    TextView spokenWords;
    public int sampleRate = 44100;

    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;
    private boolean recording = false;
    private boolean playing = false;
    private boolean recorded = false;
    protected String outputFile = null;

    public static final String TAG = "BSharp";

    private ImageView guitar = null;
    private ImageView tune = null;
    private SoundAnalyzer soundAnalyzer = null ;
    private TextView mainMessage = null;

    private Map<Integer, Bitmap> preloadedImages;
    private BitmapFactory.Options bitmapOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // construct AudioRecord to record audio from microphone with sample rate of 44100Hz
        int minSize = AudioRecord.getMinBufferSize(sampleRate,AudioFormat.
                                               CHANNEL_CONFIGURATION_MONO,
                                               AudioFormat.ENCODING_PCM_16BIT);
        AudioRecord audioInput = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                                                 AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                                 AudioFormat.ENCODING_PCM_16BIT,
                                                 minSize);

        speakButton = (Button) findViewById(R.id.start_reg);
        spokenWords = (TextView)findViewById(R.id.speech);


        UiController uiController = new UiController(MainActivity.this);
        try {
            soundAnalyzer = new SoundAnalyzer();
        } catch(Exception e) {
            Toast.makeText(this, "There are problems with your microphone", Toast.LENGTH_LONG ).show();
            Log.e(TAG, "Exception when instantiating SoundAnalyzer: " + e.getMessage());
        }
        soundAnalyzer.addObserver(uiController);
        guitar = (ImageView)findViewById(R.id.guitar);
        tune = (ImageView)findViewById(R.id.tune);
        mainMessage = (TextView)findViewById(R.id.mainMessage);

        Spinner tuningSelector = (Spinner) findViewById(R.id.tuningSelector);
        Tuning.populateSpinner(this, tuningSelector);
        tuningSelector.setOnItemSelectedListener(uiController);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (requestCode == VOICE_RECOGNITION && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String result = "";
            for(String string : matches){
                if("chord".equals(string)) result = string;
            }
            spokenWords.setText(result);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void btnSpeak(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // Specify free form input
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Please start speaking");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intent, VOICE_RECOGNITION);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void record(View view){
        if(!recording){
//            outputFile = getFilesDir().getAbsolutePath() + "/audio_message.3gpp";
            outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio_message.3gpp";
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(outputFile);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                recording = true;
                ((ImageButton)view).setImageResource(R.drawable.stop);
                view.setContentDescription(getString(R.string.stopRecStr));
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"record prepare() failed",Toast.LENGTH_LONG).show();
            }
        }else{
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            recording = false;
            recorded = true;
            ((ImageButton)view).setImageResource(R.drawable.rec);
            view.setContentDescription(getString(R.string.recordStr));
        }
    }

    public void play(View view){
        if(!recorded)return;
        if(!playing){
            mediaPlayer = new MediaPlayer();
            try{
                mediaPlayer.setDataSource(outputFile);
                mediaPlayer.prepare();
                mediaPlayer.start();
                playing = true;
                ((ImageButton)view).setImageResource(R.drawable.stop);
                view.setContentDescription(getString(R.string.stopRecStr));
            }catch(IOException e){
                Toast.makeText(getApplicationContext(),"play prepare() failed",Toast.LENGTH_LONG).show();
            }
        }else{
            mediaPlayer.release();
            mediaPlayer = null;
            playing = false;
            ((ImageButton)view).setImageResource(R.drawable.play);
            view.setContentDescription(getString(R.string.playRecStr));
        }
    }

    public void deleteAudio(View view){
        if(!recorded)return;
        if(new File(outputFile).delete()){
            recorded = false;
            outputFile = null;
            Toast.makeText(getApplicationContext(),getString(R.string.audio_del),Toast.LENGTH_SHORT).show();
        }else Toast.makeText(getApplicationContext(),getString(R.string.err), Toast.LENGTH_SHORT).show();
    }

    private Bitmap getAndCacheBitmap(int id) {
        if(preloadedImages == null)
            preloadedImages = new HashMap<>();
        Bitmap ret = preloadedImages.get(id);
        if(ret == null) {
            if(bitmapOptions == null) {
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 4; // The higher it goes, the smaller the image.
            }
            ret = BitmapFactory.decodeResource(getResources(), id, bitmapOptions);
            preloadedImages.put(id, ret);
        }
        return ret;
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

    private int [] stringNumberToImageId = new int[]{
            R.drawable.strings0,
            R.drawable.strings1,
            R.drawable.strings2,
            R.drawable.strings3,
            R.drawable.strings4,
            R.drawable.strings5,
            R.drawable.strings6
    };

    int oldString = 0;
    // 1-6 strings (ascending frequency), 0 - no string.
    public void changeString(int stringId) {
        if(oldString!=stringId) {
            guitar.setImageBitmap(getAndCacheBitmap(stringNumberToImageId[stringId]));
            oldString=stringId;
        }
    }

    int [] targetColor =         new int[]{160,80,40};
    int [] awayFromTargetColor = new int[]{160,160,160};


    public void coloredGuitarMatch(double match) {
        tune.setBackgroundColor(
                Color.rgb((int)(match*targetColor[0]+ (1-match)*awayFromTargetColor[0]),
                        (int)(match*targetColor[1]+ (1-match)*awayFromTargetColor[1]),
                        (int)(match*targetColor[2]+ (1-match)*awayFromTargetColor[2])));

    }

    public void displayMessage(String msg, boolean positiveFeedback) {
        int textColor = positiveFeedback ? Color.rgb(34,139,34) : Color.rgb(255,36,0);
        mainMessage.setText(msg);
        mainMessage.setTextColor(textColor);
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

}