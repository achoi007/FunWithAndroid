package com.achoi007.areyoupretty.app;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by alang_000 on 5/11/2014.
 */
public class PhotoAnalysisScore implements Serializable {

    private int look;
    private int friendliness;
    private int mystique;
    private int healthy;

    public int getLook() {
        return look;
    }

    public void setLook(int look) {
        this.look = look;
    }

    public int getFriendliness() {
        return friendliness;
    }

    public void setFriendliness(int friendliness) {
        this.friendliness = friendliness;
    }

    public int getMystique() {
        return mystique;
    }

    public void setMystique(int mystique) {
        this.mystique = mystique;
    }

    public int getHealthy() {
        return healthy;
    }

    public void setHealthy(int healthy) {
        this.healthy = healthy;
    }

    @Override
    public String toString() {
        return "PhotoAnalysisScore{" +
                "look=" + look +
                ", friendliness=" + friendliness +
                ", mystique=" + mystique +
                ", healthy=" + healthy +
                '}';
    }
}
