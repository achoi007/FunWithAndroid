package com.achoi007.areyoupretty.app;

import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.Date;
import java.util.Random;

/**
 * Created by alang_000 on 5/11/2014.
 */
public class RandomPhotoAnalyzer implements IPhotoAnalyzer {

    // Init params
    public static final String GOODNESS = "goodness";
    public static final String SEED = "seed";

    private int mGoodness;
    private Random mRnd = new Random();
    private IScoreListener mListener;

    private int getRandomScore() {
        return mGoodness + mRnd.nextInt(100 - mGoodness);
    }

    @Override
    public void init(Bundle initParams) {

        // Seed
        long seed = initParams.getLong(SEED, new Date().getTime());
        mRnd.setSeed(seed);

        // Goodness
        mGoodness = initParams.getInt(GOODNESS, 0);
    }

    @Override
    public void destroy() {

    }

    @Override
    public void setScoreListener(IScoreListener listener) {
        mListener = listener;
    }

    @Override
    public void analyze(Bitmap photo) {
        if (mListener != null) {
            PhotoAnalysisScore score = new PhotoAnalysisScore();
            score.setFriendliness(getRandomScore());
            score.setMystique(getRandomScore());
            score.setLook(getRandomScore());
            score.setHealthy(getRandomScore());
            mListener.onResult(score);
        }
    }
}
