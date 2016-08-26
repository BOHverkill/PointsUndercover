package org.bohverkill.pointsundercover.util;

import android.content.Context;

import org.bohverkill.pointsundercover.R;
import org.bohverkill.pointsundercover.model.Countdown;
import org.bohverkill.pointsundercover.model.User;

import java.util.ArrayList;
import java.util.List;

public class Persistence {

    private final Context context;
    private final TinyDB tinyDB;

    public Persistence(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void saveUsers(String key, List<User> users) {
        this.tinyDB.putListObject(key, users);
    }

    public List<User> getUsers(String key) {
        List<User> users = this.tinyDB.getListObject(key, User.class);
        if (users == null || users.isEmpty() || users.size() <= 0) {
            users = new ArrayList<>();
            for (int i = 1; i <= this.context.getResources().getInteger(R.integer.user_default_min_list_size); i++) {
                User u = new User("User" + i);
                users.add(u);
            }
        }
        return users;
    }

    public void removeKey(String key) {
        this.tinyDB.remove(key);
    }

    public void saveCountdown(String key, Countdown countdown) {
        this.tinyDB.putObject(key, countdown);
    }

    public Countdown getCountdown(String key) {
        Countdown countdown = this.tinyDB.getObject(key, Countdown.class);
        if (countdown == null)
            countdown = new Countdown(this.context.getResources().getInteger(R.integer.countdown_default_minutes), this.context.getResources().getInteger(R.integer.countdown_default_seconds));
        return countdown;
    }
}
