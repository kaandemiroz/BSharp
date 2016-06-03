package com.okd.bsharp;

import android.util.Log;

import com.okd.bsharp.SoundAnalyzer.AnalyzedSound;
import com.okd.bsharp.SoundAnalyzer.ArrayToDump;

import java.util.Observable;
import java.util.Observer;


public class UiController implements Observer {
	public static final String TAG = "BSharp";

	private TabActivity ui;
	private double frequency;
	
	private enum MessageClass {
		TUNING_IN_PROGRESS,
		TOO_QUIET,
		TOO_NOISY,
	}
	
	MessageClass message;
	MessageClass previouslyProposedMessage;
	MessageClass proposedMessage; // needs to get X consecutive votes.
	private int numberOfVotes;
	private final int minNumberOfVotes = 3; // X.
	
	public UiController(TabActivity u) {
		ui = u;
	}
	
	@Override
	public void update(Observable who, Object obj) {
		if(who instanceof SoundAnalyzer) {
			if(obj instanceof AnalyzedSound) {
				AnalyzedSound result = (AnalyzedSound)obj;
				// result.getDebug();
				frequency = FrequencySmoother.getSmoothFrequency(result);
				if(result.error==AnalyzedSound.ReadingType.BIG_FREQUENCY ||
						result.error==AnalyzedSound.ReadingType.BIG_VARIANCE ||
						result.error==AnalyzedSound.ReadingType.ZERO_SAMPLES)
					proposedMessage = MessageClass.TOO_NOISY;
				else if(result.error==AnalyzedSound.ReadingType.TOO_QUIET)
					proposedMessage = MessageClass.TOO_QUIET;
				else if(result.error==AnalyzedSound.ReadingType.NO_PROBLEMS)
					proposedMessage = MessageClass.TUNING_IN_PROGRESS;
				else {
					Log.e(TAG, "UiController: Unknown class of message.");
					proposedMessage=null;
				}
				//Log.e(TAG,"Frequency: " + frequency);
				updateUi();
			} else if(obj instanceof ArrayToDump) {
				ArrayToDump a = (ArrayToDump)obj;
//				ui.dumpArray(a.arr, a.elements);
			}
		}
	}
	
	private void updateUi() {
		if(message == null) {
			message = previouslyProposedMessage = proposedMessage;
		} if(message == proposedMessage) {
			// do nothing.
		} else {
			if(previouslyProposedMessage != proposedMessage) {
				previouslyProposedMessage = proposedMessage;
				numberOfVotes = 1;
			} else if(previouslyProposedMessage == proposedMessage) {
				numberOfVotes++;
			}
			if(numberOfVotes >= minNumberOfVotes) {
				message = proposedMessage;
			}
		}
		switch(message) {
////			case TUNING_IN_PROGRESS:
////				ui.displayMessage("Currently tuning string " + current.name +
////						" from " + tuning.getName() + " tuning, matched in " +
////						Math.round(100.0*match) + "%.", true);
////				break;
//			case TOO_NOISY:
////				ui.displayMessage("Please reduce background noise (or play louder).", false);
//				frequency = Double.NaN;
//                break;
			case TOO_QUIET:
//				ui.displayMessage("Please play louder!", false);
                frequency = Double.NaN;
				break;
			default:
				Log.d(TAG, "No message");
		}
		ui.displayMessage(frequency,false);
	}
	


}
