package org.bohverkill.pointsundercover.activity;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.bohverkill.pointsundercover.R;
import org.bohverkill.pointsundercover.fragment.RetainedCountDownTimerFragment;
import org.bohverkill.pointsundercover.model.Countdown;
import org.bohverkill.pointsundercover.util.CountDownTimer;
import org.bohverkill.pointsundercover.util.Persistence;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity implements CountDownTimer.CountDownTimerListener {

    public final static String EXTRA_STOPPED = "org.bohverkill.pointsundercover.stopped";
    public final static int NOTIFICATION_ID = 0;

    @BindView(R.id.game_toolbar)
    Toolbar gameToolbar;
    @BindView(R.id.countdown_view)
    TextView countdownView;
    @BindView(R.id.pause_button)
    ToggleButton pauseButton;
    @BindView(R.id.stop_button)
    Button stopButton;

    private CountDownTimer countdownTimer;
    private RetainedCountDownTimerFragment timerFragment;
    private Countdown countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Setup butterknife
        ButterKnife.bind(this);

        setSupportActionBar(this.gameToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // clear the previous notifications
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(GameActivity.NOTIFICATION_ID);

        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        this.timerFragment = (RetainedCountDownTimerFragment) fm.findFragmentByTag("timer");
        Persistence persistence = new Persistence(this);
        this.countdown = persistence.getCountdown("countdown");
        if (this.timerFragment == null) {
            this.startCountdown();
            this.timerFragment = new RetainedCountDownTimerFragment();
            fm.beginTransaction().add(this.timerFragment, "timer").commit();
            this.timerFragment.setData(this.countdownTimer);
        } else {
            this.countdownTimer = this.timerFragment.getTimer();
            this.countdownTimer.setListener(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (this.countdownTimer.isCancelled()) {
            this.startCountdown();
        }
    }

    private void startCountdown() {
        this.countdownView.setText(this.countdown.toString());
        this.countdownTimer = new CountDownTimer(this, this.countdown.toMillis(), 1000).start();
    }

    @OnCheckedChanged(R.id.pause_button)
    public void pauseButtonChanged(boolean checked) {
        if (checked) {
            this.countdownTimer.pause();
        } else {
            this.countdownTimer.resume();
        }
    }

    @OnClick(R.id.stop_button)
    public void pauseStopPressed() {
        this.onStopOrFinish(true);
    }

    private void onStopOrFinish(boolean stopped) {
        this.countdownTimer.pause();
        this.countdownTimer.cancel();
        Intent intent = new Intent(this, PointsActivity.class);
        intent.putExtra(GameActivity.EXTRA_STOPPED, stopped);
        if (!stopped) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(getResources().getString(R.string.app_name));
            builder.setContentText(getResources().getString(R.string.notification_content));
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            builder.setAutoCancel(true);
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            builder.setLights(Color.GREEN, 3000, 3000);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                builder.setVibrate(new long[]{0, 1000, 1000, 1000, 1000, 1000, 1000});
            }
//            // notify the user that the time run out
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//            // Adds the back stack
//            stackBuilder.addParentStack(GameActivity.class);
//            // Adds the Intent to the top of the stack
//            stackBuilder.addNextIntent(intent);
//            // Gets a PendingIntent containing the entire back stack
//            PendingIntent resultPendingIntent =
//                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//            builder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(GameActivity.NOTIFICATION_ID, builder.build());
        }
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            this.countdownTimer.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isFinishing()) {
            this.countdownTimer.setListener(null);
            this.timerFragment.setData(this.countdownTimer);
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        double secondsUntilFinished = millisUntilFinished / 1000;
        this.countdownView.setText(String.format(Locale.getDefault(), "%02d:%02d", (int) (secondsUntilFinished / 60), (int) (secondsUntilFinished % 60)));
    }

    @Override
    public void onFinish() {
        this.onStopOrFinish(false);
    }
}
