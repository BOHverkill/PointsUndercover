package org.bohverkill.pointsundercover.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.bohverkill.pointsundercover.util.CountDownTimer;

public class RetainedCountDownTimerFragment extends Fragment {

    // data object we want to retain
    private CountDownTimer timer;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(CountDownTimer timer) {
        this.timer = timer;
    }

    public CountDownTimer getTimer() {
        return this.timer;
    }
}