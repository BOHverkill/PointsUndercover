package org.bohverkill.pointsundercover.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.bohverkill.pointsundercover.R;
import org.bohverkill.pointsundercover.adapter.UserAdapter;
import org.bohverkill.pointsundercover.dialog.CountdownPicker;
import org.bohverkill.pointsundercover.dialog.UserEdit;
import org.bohverkill.pointsundercover.listener.RecyclerItemClickListener;
import org.bohverkill.pointsundercover.model.Countdown;
import org.bohverkill.pointsundercover.model.User;
import org.bohverkill.pointsundercover.util.Persistence;
import org.bohverkill.pointsundercover.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements CountdownPicker.CountdownPickerListener, UserEdit.UserEditListener {

    @BindView(R.id.countdown_text)
    TextView countdownText;
    @BindView(R.id.user_recycler_view)
    RecyclerView userRecyclerView;
    @BindView(R.id.add_fab)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;

    private Countdown countdown;
    private List<User> users;
    private RecyclerView.Adapter userAdapter;
    private RecyclerView.LayoutManager userLayoutManager;
    private int userCounter;
    private Persistence persistence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Android stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup butterknife
        ButterKnife.bind(this);

        setSupportActionBar(this.mainToolbar);

        this.persistence = new Persistence(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        this.userRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        this.userLayoutManager = new LinearLayoutManager(this);
        this.userRecyclerView.setLayoutManager(this.userLayoutManager);
        this.userRecyclerView.setItemAnimator(new DefaultItemAnimator());

        this.userRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), this.userRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        DialogFragment userEditDialog = new UserEdit();
                        Bundle args = new Bundle();
                        args.putInt("position", position);
                        userEditDialog.setArguments(args);
                        userEditDialog.show(getSupportFragmentManager(), "UserEdit");
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        onItemClick(view, position);
                    }
                })
        );

        // specify an adapter (see also next example)
        this.restoreSettings();
        this.userAdapter = new UserAdapter(this.users);
        this.userRecyclerView.setAdapter(this.userAdapter);
    }


    @OnClick(R.id.countdown_row)
    public void countdownPicker() {
        DialogFragment countdownDialog = new CountdownPicker();
        countdownDialog.show(getSupportFragmentManager(), "CountdownPicker");
    }

    @OnClick(R.id.add_fab)
    public void addRow() {
        this.userCounter++;
        User u = new User("User" + this.userCounter);
        this.users.add(u);
        this.userAdapter.notifyItemInserted(this.users.indexOf(u));
        this.enableFAB(!(this.users.size() >= getResources().getInteger(R.integer.user_max_list_size)));
    }

    @Override
    public void onCountdownPicked(Countdown countdown) {
        this.countdown = countdown;
        Util.setMultilineTextInTextView(this.countdownText, getResources().getString(R.string.countdown_header), this.countdown.toString());
    }

    @Override
    public Countdown getPreviouslyPicked() {
        return this.countdown;
    }

    @Override
    public void onUserChanged(User user, int position) {
        this.users.set(position, user);
        this.userAdapter.notifyItemChanged(position);
    }

    @Override
    public void onDeleteUser(int position) {
        this.users.remove(position);
        this.userAdapter.notifyItemRemoved(position);
        this.enableFAB(this.users.size() < getResources().getInteger(R.integer.user_max_list_size));
    }

    @Override
    public User getSelectedUser(int position) {
        return this.users.get(position);
    }

    @Override
    public int getUserCount() {
        return this.users.size();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start:
                //TODO implement start
                // User chose the "Start" item, show the app start UI...
                this.saveSettings();
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_clear_session:
                //TODO implement clear action
                this.clearSettings();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.saveSettings();
    }

    public void saveSettings() {
        this.persistence.saveUsers("users", this.users);
        this.persistence.saveCountdown("countdown", this.countdown);
    }

    public void restoreSettings() {
        this.onCountdownPicked(this.persistence.getCountdown("countdown"));
        Util.setMultilineTextInTextView(this.countdownText, getResources().getString(R.string.countdown_header), this.countdown.toString());
        this.users = this.persistence.getUsers("users");
        this.userCounter = this.users.size();
        //if (this.userAdapter != null) this.userAdapter.notifyDataSetChanged();
        this.userAdapter = new UserAdapter(this.users);
        this.userRecyclerView.setAdapter(this.userAdapter);
        this.enableFAB(this.users.size() < getResources().getInteger(R.integer.user_max_list_size));
    }

    public void clearSettings() {
        this.persistence.removeKey("countdown");
        this.persistence.removeKey("users");
        this.restoreSettings();
        this.saveSettings();
    }

    public void enableFAB(boolean enabled) {
        this.floatingActionButton.setEnabled(enabled);
        if (enabled) {
            this.floatingActionButton.show();
        } else {
            this.floatingActionButton.hide();
        }
    }
}
