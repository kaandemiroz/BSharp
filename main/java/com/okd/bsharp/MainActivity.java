package com.okd.bsharp;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

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

}