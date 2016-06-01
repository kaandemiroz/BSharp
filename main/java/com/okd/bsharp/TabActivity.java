package com.okd.bsharp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class TabActivity extends AppCompatActivity{//} implements RecognitionListener{

    TextView spokenWords;

    public static final String TAG = "BSharp";
    public static final int UNSET = -1;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TabFragment fragment;

    private SoundAnalyzer soundAnalyzer = null ;
    private TextView mainMessage = null;
    private TextView editText = null;
    private Tab tab = new Tab();
    private int previousNote = UNSET;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu navigationMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                FragmentManager fragmentManager = TabActivity.this.getSupportFragmentManager();
                TabFragment feedListFragment = (TabFragment) fragmentManager
                        .findFragmentById(R.id.fragment_container);
                String fragmentName = menuItem.getTitle().toString();
                mToolbar.setTitle(fragmentName);
//                feedListFragment.setContent(nameToIDMap.get(fragmentName), 0, true);

                return true;
            }
        });

        if (findViewById(R.id.fragment_container) != null) {
            // If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) return;
            fragment = new TabFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }

        history = new Queue(5);
//        spokenWords = (TextView) findViewById(R.id.speech);

        UiController uiController = new UiController(TabActivity.this);
        try {
            soundAnalyzer = new SoundAnalyzer();
        } catch (Exception e) {
            Toast.makeText(this, "There are problems with your microphone", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Exception when instantiating SoundAnalyzer: " + e.getMessage());
        }
        soundAnalyzer.addObserver(uiController);
        mainMessage = (TextView) findViewById(R.id.mainMessage);
        editText = (TextView) findViewById(R.id.editText);

//        Spinner tuningSelector = (Spinner) findViewById(R.id.tuningSelector);
//        Tuning.populateSpinner(this, tuningSelector);
//        tuningSelector.setOnItemSelectedListener(uiController);
    }

    @Override
    /**
     * Sync the Drawer Toggle after onCreate
     */
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
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

    public void displayMessage(double freq, boolean positiveFeedback) {
        int textColor = positiveFeedback ? Color.rgb(34,139,34) : Color.rgb(255,36,0);
        int note = findClosestNote(freq, frequencies);
        mainMessage.setText(freq + "\n" + note);
        if(note != previousNote){
            if(previousNote == UNSET){
                int stringIndex = findString(note);
                int position = note + stringOffsets[stringIndex];
                tab.addColumn(stringIndex, position);
                fragment.addTabItem(stringIndex, position);
                history.addItem(position);
//                editText.setText(tab.toString());
            }
            previousNote = note;
        }
        mainMessage.setTextColor(textColor);
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
        double totalWeight = 0;
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

}