package com.okd.bsharp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.StringTokenizer;

public class TabActivity extends AppCompatActivity{

    public static final String TAG = "BSharp";
    public static final int UNSET = -1;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TabFragment fragment;
    private ImageButton recordButton;

    private SoundAnalyzer soundAnalyzer = null ;
//    private TextView mainMessage = null;
    private int previousNote = UNSET;
    private boolean recording = false;

    private Queue history;
    private double[] frequencies = {82.4, 87.3, 92.5, 98.0, 103.8,
                                    110.0, 116.5, 123.5, 130.8, 138.6,
                                    146.8, 155.6, 164.8, 174.6, 185.0,
                                    196.0, 207.6, 220.0, 233.1,
                                    246.9, 261.6, 277.2, 293.6, 311.1,
                                    329.6, 349.2, 370.0, 392.0, 415.3,
                                    440.0, 466.1, 493.8, 523.2, 554.3,
                                    587.3, 622.2, 659.2, 698.5, 740.0,
                                    784.0, 830.6, 880.0, 932.3, 987.8, 1047};

    private int[] stringOffsets = {-24, -19, -15, -10, -5, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open_desc,
                R.string.drawer_close_desc);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null){
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) return;
            fragment = new TabFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu navigationMenu = navigationView.getMenu();
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        final String song = sharedPreferences.getString("song","");
        if(!"".equals(song)){
            StringTokenizer stringTokenizer = new StringTokenizer(song);
            String songName = stringTokenizer.nextToken(":");
            navigationMenu.add(Menu.NONE,Menu.NONE,0,songName);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
//                StringTokenizer stringTokenizer = new StringTokenizer(song);
//                String songName = stringTokenizer.nextToken(":");
//                while(stringTokenizer.hasMoreTokens()){
//                    String column = stringTokenizer.nextToken(";");
//                    StringTokenizer tokenizer = new StringTokenizer(column,",");
//                    while (tokenizer.hasMoreTokens())){
//
//                    }
//                }

                return true;
            }
        });

        recordButton = (ImageButton) findViewById(R.id.record);

        history = new Queue(5);

        UiController uiController = new UiController(TabActivity.this);
        try {
            soundAnalyzer = new SoundAnalyzer();
        } catch (Exception e) {
            Toast.makeText(this, "There are problems with your microphone", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Exception when instantiating SoundAnalyzer: " + e.getMessage());
        }
        soundAnalyzer.addObserver(uiController);
//        mainMessage = (TextView) findViewById(R.id.mainMessage);
    }

    @Override
    /**
     * Sync the Drawer Toggle after onCreate
     */
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    public void displayMessage(double freq, boolean positiveFeedback) {
        int textColor = positiveFeedback ? Color.rgb(34,139,34) : Color.rgb(255,36,0);
        int note = findClosestNote(freq, frequencies);
//        mainMessage.setText(freq + "\n" + note);
        if(note != previousNote){
            if(previousNote == UNSET){
                int stringIndex = findString(note);
                int position = note + stringOffsets[stringIndex];
                fragment.addTabItem(stringIndex, position);
                history.addItem(position);
            }
            previousNote = note;
        }
//        mainMessage.setTextColor(textColor);
    }

//    public void displayMessage(String msg, boolean positiveFeedback) {
//        int textColor = positiveFeedback ? Color.rgb(34,139,34) : Color.rgb(255,36,0);
//        mainMessage.setText(msg);
//        mainMessage.setTextColor(textColor);
//    }

    public void record(View view){
        if(recording){
            if(soundAnalyzer!=null){
                recording = false;
                soundAnalyzer.stop();
                recordButton.setImageResource(R.drawable.rec);
            }
        }else{
            if(soundAnalyzer!=null){
                recording = true;
                soundAnalyzer.ensureStarted();
                recordButton.setImageResource(R.drawable.stop);
            }
        }
    }

    public void delete(View view){
        fragment.deleteTab();
    }

    public void save(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Enter the name of the song:");

        // Set up the input
        final EditText input = new EditText(view.getContext());
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputString = input.getText().toString();
                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
                editor.putString("song",inputString + ":" + fragment.getTabItems());
                editor.commit();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(soundAnalyzer!=null) soundAnalyzer.stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    private int findClosestNote(double freq, double[] frequencies){
        double min = 100;
        int index = UNSET;
        for(int i=0; i<frequencies.length; i++){
            double diff = Math.abs(freq - frequencies[i]);
            if(diff < min){
                min = diff;
                index = i;
            }
        }
        if(min < 10) return index;
        else return UNSET;
    }

    private double findPreviousArea(){
        int[] curHistory = history.getArray();
        double sum = 0;
        for(int i=0; i<curHistory.length; i++){
            sum += curHistory[i];
        }
        sum /= curHistory.length;
        return sum;
    }

    private int findString(int note){
        double previousArea = findPreviousArea();
        double min = 100;
        int index = UNSET;
        for(int i=0; i<stringOffsets.length; i++){
            int offNote = note + stringOffsets[i];
            if(offNote >= 0){
                double diff = Math.abs(previousArea - offNote);
                if(diff < min){
                    min = diff;
                    index = i;
                }
            }
        }
        return index;
    }

    public void createPdf(MenuItem item) {
        Tab tab = fragment.getTab();
        Document document = new Document();

        try{
            // saving pdf document to sdcard
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyhhmmss");
            String pdfName = TAG + sdf.format(Calendar.getInstance().getTime()) + ".pdf";
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), TAG);
            // Create the storage directory if it does not exist
            if (! dir.exists()){
                if (! dir.mkdirs()){
                    Log.d(TAG, "failed to create directory");
                }
            }
            PdfWriter.getInstance(document,
                    new FileOutputStream(dir.getPath() + File.separator + pdfName));
            document.open();

            document.add(new Paragraph(tab.toString()));
            document.close();
            Toast.makeText(this,"PDF file created at \"DOWNLOAD\".", Toast.LENGTH_SHORT).show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}