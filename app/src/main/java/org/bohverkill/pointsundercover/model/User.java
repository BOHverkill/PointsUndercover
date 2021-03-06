package org.bohverkill.pointsundercover.model;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private int points;
    private int plusPoints;

    public User() {
        this("User", 0);
    }

    public User(String name) {
        this(name, 0);
    }

    public User(int points) {
        this("User", points);
    }

    public User(String name, int points) {
        this.name = name;
        this.points = points;
        this.plusPoints = 0;
    }

    public User(User user) {
        this(user.getName(), user.getPoints());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public int getPlusPoints() {
        return this.plusPoints;
    }

    public void setPlusPoints(int plusPoints) {
        this.plusPoints = plusPoints;
    }

    @Override
    public String toString() {
        return name;
    }
}
