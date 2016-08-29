package org.bohverkill.pointsundercover.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.bohverkill.pointsundercover.model.User;

import java.util.HashMap;
import java.util.Map;

public class RetainedPointsFragment extends Fragment {

    // data objects we want to retain
    private User agent;
    private boolean rightGroupPick;
    private User stop;
    private boolean rightAgentPick;
    Map<String, CharSequence> textViewValues;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.textViewValues = new HashMap<>();
        // retain this fragment
        setRetainInstance(true);
    }

    public User getAgent() {
        return this.agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public boolean getRightGroupPick() {
        return this.rightGroupPick;
    }

    public void setRightGroupPick(boolean rightGroupPick) {
        this.rightGroupPick = rightGroupPick;
    }

    public User getStop() {
        return this.stop;
    }

    public void setStop(User stop) {
        this.stop = stop;
    }

    public boolean getRightAgentPick() {
        return this.rightAgentPick;
    }

    public void setRightAgentPick(boolean rightAgentPick) {
        this.rightAgentPick = rightAgentPick;
    }

    public CharSequence getTextViewValue(String key) {
        return this.textViewValues.get(key);
    }

    public CharSequence setTextViewValue(String key, CharSequence value) {
        return this.textViewValues.put(key, value);
    }
}
