package org.bohverkill.pointsundercover.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bohverkill.pointsundercover.R;
import org.bohverkill.pointsundercover.adapter.PointsAdapter;
import org.bohverkill.pointsundercover.dialog.AgentPicker;
import org.bohverkill.pointsundercover.dialog.AgentStopPicker;
import org.bohverkill.pointsundercover.dialog.RightPickPicker;
import org.bohverkill.pointsundercover.dialog.StopPicker;
import org.bohverkill.pointsundercover.model.User;
import org.bohverkill.pointsundercover.util.Persistence;
import org.bohverkill.pointsundercover.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PointsActivity extends AppCompatActivity implements AgentPicker.AgentPickerListener, RightPickPicker.RightPickListener, StopPicker.StopPickListener, AgentStopPicker.AgentStopPickListener{

    @BindView(R.id.points_toolbar)
    Toolbar pointsToolbar;
    @BindView(R.id.agent_text)
    TextView agentText;
    @BindView(R.id.agent_row)
    LinearLayout agentRow;
    @BindView(R.id.right_pick_text)
    TextView rightPickText;
    @BindView(R.id.right_pick_row)
    LinearLayout rightPickRow;
    @BindView(R.id.stop_text)
    TextView stopText;
    @BindView(R.id.stop_row)
    LinearLayout stopRow;
    @BindView(R.id.agent_stop_text)
    TextView agentStopText;
    @BindView(R.id.agent_stop_row)
    LinearLayout agentStopRow;
    @BindView(R.id.points_recycler_view)
    RecyclerView pointsRecyclerView;

    private List<User> users;
    private Persistence persistence;
    private RecyclerView.Adapter pointsAdapter;
    private RecyclerView.LayoutManager pointsLayoutManager;

    private boolean stopped;
    private User agent;
    private boolean rightGroupPick;
    private User stop;
    private boolean rightAgnetPick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        // Setup butterknife
        ButterKnife.bind(this);

        setSupportActionBar(this.pointsToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.stopped = getIntent().getBooleanExtra(GameActivity.EXTRA_STOPPED, false);

        this.persistence = new Persistence(this);
        this.users = this.persistence.getUsers("users");
        this.setupTextViews();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        this.pointsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        this.pointsLayoutManager = new LinearLayoutManager(this);
        this.pointsRecyclerView.setLayoutManager(this.pointsLayoutManager);
        this.pointsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        this.pointsAdapter = new PointsAdapter(this.users, this);
        this.pointsRecyclerView.setAdapter(this.pointsAdapter);

        this.switchStopVisibility(this.stopped);
    }

    @OnClick(R.id.agent_row)
    public void agentPicker() {
        DialogFragment agentDialog = new AgentPicker();
        agentDialog.show(getSupportFragmentManager(), "AgentPicker");
    }

    @OnClick(R.id.right_pick_row)
    public void rightPickPicker() {
        DialogFragment rightPickDialog = new RightPickPicker();
        rightPickDialog.show(getSupportFragmentManager(), "RightPickPicker");
    }

    @OnClick(R.id.stop_row)
    public void stopPicker() {
        DialogFragment stopDialog = new StopPicker();
        stopDialog.show(getSupportFragmentManager(), "StopPicker");
    }

    @OnClick(R.id.agent_stop_row)
    public void agentStopPicker() {
        DialogFragment agentStopDialog = new AgentStopPicker();
        agentStopDialog.show(getSupportFragmentManager(), "AgentStopPicker");
    }

    @Override
    public void onAgentPicked(int index) {
        //TODO implement me
        this.agent = this.users.get(index);
        Util.setMultilineTextInTextView(this.agentText, getResources().getString(R.string.agent_header), this.agent.toString());
        this.calculatePoints();
    }

    @Override
    public void onRightPickPicked(boolean rightGroupPick, int text) {
        //TODO implement me as well
        this.rightGroupPick = rightGroupPick;
        Util.setMultilineTextInTextView(this.rightPickText, getResources().getString(R.string.right_pick_header), getResources().getString(text));
        this.calculatePoints();
    }


    @Override
    public void onStopPicked(int index) {
        //TODO implement me as well
        this.stop = this.users.get(index);
        Util.setMultilineTextInTextView(this.stopText, getResources().getString(R.string.stop_header), this.stop.toString());
        this.calculatePoints();
    }

    @Override
    public void onAgentStopPicked(boolean rightAgentPick, int text) {
        //TODO implement me as well
        this.rightAgnetPick = rightAgentPick;
        Util.setMultilineTextInTextView(this.agentStopText, getResources().getString(R.string.agent_stop_header), getResources().getString(text));
        this.calculatePoints();
    }

    public void calculatePoints() {
        int agentPlus = 0;
        int otherPlus = 0;
        int stopCallerPlus = 0;
        this.switchStopVisibility(this.stopped);
        if (this.agent == null) return;
        for (User u: this.users) {
            if (u.getPlusPoints() != 0) {
                u.setPoints(u.getPoints()-u.getPlusPoints());
                u.setPlusPoints(0);
            }
        }
        if (this.stopped) {
            //TODO implement me
            if (this.stop != null && this.stop.equals(this.agent)) {
                if (this.rightAgnetPick) {
                    agentPlus = 4;
                    otherPlus = 0;
                    stopCallerPlus = 0;
                } else {
                    agentPlus = 0;
                    otherPlus = 1;
                    stopCallerPlus = 0;
                }
            } else if (this.stop != null && !this.stop.equals(this.agent)) {
                if (this.rightGroupPick) {
                    agentPlus = 0;
                    otherPlus = 1;
                    stopCallerPlus = 1;
                } else {
                    agentPlus = 4;
                    otherPlus = 0;
                    stopCallerPlus = 0;
                }
            } else return;
        } else {
            //TODO implement me
            if (this.rightGroupPick) {
                agentPlus = 0;
                otherPlus = 1;
                stopCallerPlus = 0;
            } else {
                agentPlus = 2;
                otherPlus = 0;
                stopCallerPlus = 0;
            }
        }
        for(User u: this.users) {
            if (u.equals(this.agent)) {
                u.setPlusPoints(agentPlus);
                u.setPoints(u.getPoints()+agentPlus);
            } else {
                if (stopCallerPlus != 0 && u.equals(this.stop)) {
                    u.setPlusPoints(otherPlus+stopCallerPlus);
                    u.setPoints(u.getPoints()+otherPlus);
                } else {
                    u.setPlusPoints(otherPlus);
                    u.setPoints(u.getPoints() + otherPlus);
                }
            }
        }
        this.pointsAdapter = new PointsAdapter(this.users, this);
        this.pointsRecyclerView.setAdapter(this.pointsAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (User u: this.users) {
            u.setPlusPoints(0);
        }
        this.persistence.saveUsers("users", this.users);
    }

    private void setupTextViews() {
        Util.setMultilineTextInTextView(this.agentText, getResources().getString(R.string.agent_header), "");
        Util.setMultilineTextInTextView(this.rightPickText, getResources().getString(R.string.right_pick_header), "");
        Util.setMultilineTextInTextView(this.stopText, getResources().getString(R.string.stop_header), "");
        Util.setMultilineTextInTextView(this.agentStopText, getResources().getString(R.string.agent_stop_header), "");
    }

    private void switchStopVisibility(boolean stopped) {
        if (stopped) {
            this.agentRow.setVisibility(View.VISIBLE);
            this.stopRow.setVisibility(View.VISIBLE);
            if (this.stop != null && this.stop.equals(this.agent)) {
                this.agentStopRow.setVisibility(View.VISIBLE);
            } else {
                this.agentStopRow.setVisibility(View.GONE);
            }
            if (this.stop != null && !this.stop.equals(this.agent)) {
                this.rightPickRow.setVisibility(View.VISIBLE);
            } else {
                this.rightPickRow.setVisibility(View.GONE);
            }
        } else {
            this.stopRow.setVisibility(View.GONE);
            this.agentStopRow.setVisibility(View.GONE);

            this.agentRow.setVisibility(View.VISIBLE);
            this.rightPickRow.setVisibility(View.VISIBLE);
        }
    }
}
