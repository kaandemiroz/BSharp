package com.okd.bsharp;

//import edu.cmu.pocketsphinx.Decoder;
//import edu.cmu.pocketsphinx.SpeechRecognizer;

/**
 * Created by OKD on 20.5.2016.
 */
public class OldCode {

    public int AUDIO_SAMPLING_RATE = 44100;
//    private SpeechRecognizer recognizer;
//    private Decoder decoder;
    private static final String CHORD_KEY = "chord";

//    private IspikitWrapper ispikitWrapper;


//        runRecognizerSetup();


//        ispikitWrapper = new IspikitWrapper(this);
//        final InitHandler m_HandlerInit = new InitHandler(this);
//        ispikitWrapper.setInitHandler(m_HandlerInit);
//        final WordsHandler m_HandlerWords = new WordsHandler(this);
//        ispikitWrapper.setWordsHandler(m_HandlerWords);
//        final AudioHandler m_HandlerAudio = new AudioHandler(this);
//        ispikitWrapper.setAudioHandler(m_HandlerAudio);
//
//        if (!ispikitWrapper.Init()) Toast.makeText(this, "Error during initialization", Toast.LENGTH_SHORT).show();




    // You must create new classes deriving from Handler, one for each callback.
    // In the handle message, method, you can update the activity's UI
//    static class InitHandler extends Handler {
//        WeakReference<TabActivity> m_Activity;
//
//        InitHandler(TabActivity a) {
//            m_Activity = new WeakReference<>(a);
//        }
//
//        @Override
//        public void handleMessage(Message message) {
//            // After you get this, the library is now usable, you can update the app's
//            // UI to reflect it and allow users to start recording.
//            TabActivity a = m_Activity.get();
//            Toast.makeText(a, "Initialization Done", Toast.LENGTH_LONG).show();
//            a.ispikitWrapper.SetSentence("Dummy " + CHORD_KEY + " single");
//            a.ispikitWrapper.Start();
//        }
//    }
//
//    static class WordsHandler extends Handler {
//        WeakReference<TabActivity> m_Activity;
//
//        WordsHandler(TabActivity a) {
//            m_Activity = new WeakReference<>(a);
//        }
//
//        @Override
//        public void handleMessage(Message message) {
//            // You can parse the string to detect if the last word is recognized.
//            // If the last word is recognized, you can stop recognition calling
//            // m_simple_ispikit_wrapper.StopRecording(false);
//            TabActivity a = m_Activity.get();
//            TextView words = (TextView) a.findViewById(R.id.speech);
////            words.setText((String) message.obj);
//            if("0-1-0-0".equals( message.obj))
//            Toast.makeText(a, CHORD_KEY, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    static class AudioHandler extends Handler {
//        WeakReference<TabActivity> m_Activity;
//
//        AudioHandler(TabActivity a) {
//            m_Activity = new WeakReference<>(a);
//        }
//
//        @Override
//        public void handleMessage(Message message) {
//            // You can wire this to a ProgressBar to show completion to
//            // the user.
//            TabActivity a = m_Activity.get();
//            TextView pitch = (TextView) a.findViewById(R.id.speech);
//            pitch.setText(TextUtils.join(",", (Object[]) message.obj));
////            Toast.makeText(a, TextUtils.join(",", (Object[]) message.obj), Toast.LENGTH_SHORT).show();
//        }
//    }


//      onDestroy
//        if (recognizer != null) {
//            recognizer.cancel();
//            recognizer.shutdown();
//        }
//        ispikitWrapper.Shutdown();

//    private void runRecognizerSetup() {
//        // Recognizer initialization is a time-consuming and it involves IO,
//        // so we execute it in async task
//        new AsyncTask<Void, Void, Exception>() {
//            @Override
//            protected Exception doInBackground(Void... params) {
//                try {
//                    Assets assets = new Assets(TabActivity.this);
//                    File assetDir = assets.syncAssets();
//                    setupRecognizer(assetDir);
//                } catch (IOException e) {
//                    return e;
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Exception result) {
//                if (result != null) {
//                    spokenWords.setText("Failed to init recognizer " + result);
//                } else {
//                    recognizer.startListening(CHORD_KEY);
//                }
//            }
//        }.execute();
//    }
//
//
//    private void setupRecognizer(File assetsDir) throws IOException {
//        // The recognizer can be configured to perform multiple searches
//        // of different kind and switch between them
//
//        recognizer = SpeechRecognizerSetup.defaultSetup()
//                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
//                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
//                .setSampleRate(AUDIO_SAMPLING_RATE)
////                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
//                .setKeywordThreshold(1) // Threshold to tune for keyphrase to balance between false alarms and misses
//                .setBoolean("-allphone_ci", true)  // Use context-independent phonetic search, context-dependent is too slow for mobile
//
//
//                .getRecognizer();
//        recognizer.addListener(this);
//
//        /** In your application you might not need to add all those searches.
//         * They are added here for demonstration. You can leave just one.
//         */
//
//        // Create keyword-activation search.
//        recognizer.addKeyphraseSearch(CHORD_KEY, CHORD_KEY);
//
//    }
//
//
//    @Override
//    public void onBeginningOfSpeech() {    }
//
//    @Override
//    public void onEndOfSpeech() {
//
//    }
//
//    @Override
//    public void onPartialResult(Hypothesis hypothesis) {
//        if (hypothesis == null)
//            return;
////        decoder = recognizer.getDecoder();
//        String text = hypothesis.getHypstr();
//        spokenWords.setText(text);
//    }
//
//    @Override
//    public void onResult(Hypothesis hypothesis) {
//    }
//
//    @Override
//    public void onError(Exception e) {
//        spokenWords.setText(e.getMessage());
//    }
//
//    @Override
//    public void onTimeout() {
//
//    }
//
//    public static class AnalyzedSound {
//        public enum ReadingType  {
//            NO_PROBLEMS,
//            TOO_QUIET,
//            ZERO_SAMPLES,
//            BIG_VARIANCE,
//            BIG_FREQUENCY
//        }
//        public double loudness;
//        public boolean frequencyAvailable;
//        public double frequency;
//        public ReadingType error;
//        public AnalyzedSound(double l,ReadingType e) {
//            loudness = l;
//            frequencyAvailable = false;
//            error = e;
//        }
//        public AnalyzedSound(double l, double f) {
//            loudness = l;
//            frequencyAvailable = true;
//            frequency = f;
//            error = ReadingType.NO_PROBLEMS;
//        }
//
//    }
//
//
//    // square.
//    private double sq(double a) { return a*a; }
//
//    // Enough to pick up most of the frequencies.
//    private static final double MPM = 0.7;
//    private static final double maxStDevOfMeanFrequency = 2.0; // if stdev bigger than that
//    private static final double MaxPossibleFrequency = 2700.0;     // result considered rubbish
//    private static final double loudnessThreshold = 30.0; // below is too quiet
//    private static final double PercentOfWavelengthSamplesToBeIgnored = 0.2;
//
//    private int wavelengths;
//    private double [] wavelength;
//    private int currentFftMethodSize = -1;
//    DoubleFFT_1D fft_method;
//
//    private double hanning(int n, int N) {
//        return 0.5*(1.0 -Math.cos(2*Math.PI*(double)n/(double)(N-1)));
//    }
//
//    private void computeAutocorrelation(int elementsRead, double[] audioDataAnalysis) {
//        // Fourier Transform to calculate autocorrelation.
//        // Below i use circular convolution theorem.
//
//        // Should save some memory.
//        if(2*elementsRead != currentFftMethodSize) {
//            fft_method = new DoubleFFT_1D(2*elementsRead);
//            currentFftMethodSize = 2*elementsRead;
//        }
//
//        for(int i=elementsRead-1; i>=0; i--) {
//            audioDataAnalysis[2*i]=audioDataAnalysis[i] * hanning(i,elementsRead);
//            audioDataAnalysis[2*i+1] = 0;
//        }
//        for(int i=2*elementsRead; i<audioDataAnalysis.length; ++i)
//            audioDataAnalysis[i]=0;
//
//        // Compute FORWARD fft transform.
//        fft_method.complexInverse(audioDataAnalysis, false);
//
//        // Replace every frequency with it's magnitude.
//        for(int i=0; i<elementsRead; ++i) {
//            audioDataAnalysis[2*i] = sq(audioDataAnalysis[2*i]) + sq(audioDataAnalysis[2*i+1]);
//            audioDataAnalysis[2*i+1] = 0;
//        }
//        for(int i=2*elementsRead; i<audioDataAnalysis.length; ++i)
//            audioDataAnalysis[i]=0;
//
//        // Set first one on to 0.
//        audioDataAnalysis[0] = 0;
//
//        // Compute INVERSE fft.
//        fft_method.complexForward(audioDataAnalysis);
//
//        // Take real part of the result.
//        for(int i=0; i<elementsRead; ++i)
//            audioDataAnalysis[i] = audioDataAnalysis[2*i];
//        for(int i=elementsRead; i<audioDataAnalysis.length; ++i)
//            audioDataAnalysis[i]=0;
//    }
//
//    double getMeanWavelength() {
//        double mean = 0;
//        for(int i=0; i < wavelengths; ++i)
//            mean += wavelength[i];
//        mean/=(double)(wavelengths);
//        return mean;
//    }
//
//    double getStDevOnWavelength() {
//        double variance = 0; double mean = getMeanWavelength();
//        for(int i=1; i < wavelengths; ++i)
//            variance+= Math.pow(wavelength[i]-mean,2);
//        variance/=(double)(wavelengths-1);
//        return Math.sqrt(variance);
//    }
//
//    void removeFalseSamples() {
//        int samplesToBeIgnored =
//                (int)(PercentOfWavelengthSamplesToBeIgnored*wavelengths);
//        if(wavelengths <=2) return;
//        do {
//            double mean = getMeanWavelength();
//            // Looking for sample furthest away from mean.
//            int best = -1;
//            for(int i=0; i<wavelengths; ++i)
//                if(best == -1 || Math.abs(wavelength[i] -mean) >
//                        Math.abs(wavelength[best] -mean)) best = i;
//            // Removing it.
//            wavelength[best]=wavelength[wavelengths-1];
//            --wavelengths;
//        } while(getStDevOnWavelength() > maxStDevOfMeanFrequency &&
//                samplesToBeIgnored-- > 0 && wavelengths > 2);
//    }
//
//    private AnalyzedSound getFrequency(short[] data) {
//        int elementsRead = data.length;
//        double[] audioDataAnalysis = new double[elementsRead];
//        for(int i = 0; i< elementsRead; i++){
//            audioDataAnalysis[i] = data[i];
//        }
//
//        double loudness = 0.0;
//        for(int i=0; i<elementsRead; ++i)
//            loudness+=Math.abs(audioDataAnalysis[i]);
//        loudness/=elementsRead;
//        // Check loudness first - it's root of all evil.
//        if(loudness<loudnessThreshold)
//            return new AnalyzedSound(loudness, AnalyzedSound.ReadingType.TOO_QUIET);
//
//        computeAutocorrelation(elementsRead,audioDataAnalysis);
//
//        //chopOffEdges(0.2);
//
//        double maximum=0;
//        for(int i=1; i<elementsRead; ++i)
//            maximum = Math.max(audioDataAnalysis[i], maximum);
//
//        int lastStart = -1;
//        wavelengths = 0;
//        boolean passedZero = true;
//        for(int i=0; i<elementsRead; ++i) {
//            if(audioDataAnalysis[i]*audioDataAnalysis[i+1] <=0) passedZero = true;
//            if(passedZero && audioDataAnalysis[i] > MPM*maximum &&
//                    audioDataAnalysis[i] > audioDataAnalysis[i+1]) {
//                if(lastStart != -1)
//                    wavelength[wavelengths++]=i-lastStart;
//                lastStart=i; passedZero = false;
//                maximum = audioDataAnalysis[i];
//            }
//        }
//        if(wavelengths <2)
//            return new AnalyzedSound(loudness, AnalyzedSound.ReadingType.ZERO_SAMPLES);
//
//        removeFalseSamples();
//
//        double mean = getMeanWavelength(), stdv=getStDevOnWavelength();
//
//        double calculatedFrequency = (double)AUDIO_SAMPLING_RATE/mean;
//
//        // Log.d(TAG, "MEAN: " + mean + " STDV: " + stdv);
//        // Log.d(TAG, "Frequency:" + calculatedFrequency);
//
//        if(stdv >= maxStDevOfMeanFrequency)
//            return new AnalyzedSound(loudness, AnalyzedSound.ReadingType.BIG_VARIANCE);
//        else if(calculatedFrequency>MaxPossibleFrequency)
//            return new AnalyzedSound(loudness, AnalyzedSound.ReadingType.BIG_FREQUENCY);
//        else
//            return new AnalyzedSound(loudness, calculatedFrequency);
//
//    }

}
