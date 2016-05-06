package com.okd.bsharp;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public int sampleRate = 44100;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // construct AudioRecord to record audio from microphone with sample rate of 44100Hz
//        int minSize = AudioRecord.getMinBufferSize(sampleRate,AudioFormat.
//                                               CHANNEL_CONFIGURATION_MONO,
//                                               AudioFormat.ENCODING_PCM_16BIT);
//        AudioRecord audioInput = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
//                                                 AudioFormat.CHANNEL_CONFIGURATION_MONO,
//                                                 AudioFormat.ENCODING_PCM_16BIT,
//                                                 minSize);
//    }

    private MediaRecorder mediaRecorder = null;
    private MediaPlayer mediaPlayer = null;
    private boolean recording = false;
    private boolean playing = false;
    private boolean recorded = false;
    protected String outputFile = null;

    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    class RecordButton extends Button {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }

    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }

    public MainActivity() {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        LinearLayout ll = new LinearLayout(this);
        mRecordButton = new RecordButton(this);
        ll.addView(mRecordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        mPlayButton = new PlayButton(this);
        ll.addView(mPlayButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        0));
        setContentView(ll);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }



    public void record(View view){
        if(!recording){
            outputFile = getFilesDir().getAbsolutePath() + "/audio_message.3gpp";
            //outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio_message.3gpp";
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