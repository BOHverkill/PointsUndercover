package org.bohverkill.pointsundercover.model;

public class User {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return getPoints() == user.getPoints() && getPlusPoints() == user.getPlusPoints() && getName().equals(user.getName());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getPoints();
        result = 31 * result + getPlusPoints();
        return result;
    }
}
