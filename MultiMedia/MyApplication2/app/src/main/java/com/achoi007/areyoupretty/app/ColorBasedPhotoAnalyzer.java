package com.achoi007.areyoupretty.app;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

/**
 *
 * For each of the attribute color (look, healthy, mystique, friendly), and for each pixel, calculates the
 * Manhattan distance between pixel and attribute color.  If Manhattan distance <= closeness,
 * score for that attribute is increased by 1.
 *
 * Then, multiplies each attribute score by FILL_FACTOR and divides that by total number of
 * meaningful pixels (non black).
 *
 * Created by alang_000 on 5/11/2014.
 */
public class ColorBasedPhotoAnalyzer
        extends AsyncTask<Bitmap, Integer, PhotoAnalysisScore>
        implements IPhotoAnalyzer {

    private static final String TAG = ColorBasedPhotoAnalyzer.class.getSimpleName();
    public static final String LOOK_COLOR = "LookColor";
    public static final String HEALTHY_COLOR = "HealthyColor";
    public static final String MYSTIQUE_COLOR = "MystiqueColor";
    public static final String FRIENDLY_COLOR = "FriendlyColor";
    public static final String CLOSENESS = "Closeness";
    public static final String FILL_FACTOR = "FillFactor";

    private static final int IDX_LOOK = 0;
    private static final int IDX_HEALTHY = 1;
    private static final int IDX_MYSTIQUE = 2;
    private static final int IDX_FRIENDLY = 3;
    private static final int IDX_MAX = 4;

    private IScoreListener mListener;
    private int[][] mRGB;
    private int mCloseness;
    private int mFillFactor;

    private void pixelToRgb(int pixel, int[] rgb) {
        rgb[0] = Color.red(pixel);
        rgb[1] = Color.green(pixel);
        rgb[2] = Color.blue(pixel);
    }

    public ColorBasedPhotoAnalyzer() {
        mRGB = new int[IDX_MAX][3];
        for (int i = 0; i < IDX_MAX; i++) {
            mRGB[i] = new int[3];
        }
    }

    @Override
    public void init(Bundle initParams) {

        // The colors from initParams to process
        String[] colors = {
          LOOK_COLOR, HEALTHY_COLOR, MYSTIQUE_COLOR, FRIENDLY_COLOR
        };

        // Default values for each color
        int[] initColors = { Color.RED, Color.WHITE, Color.BLUE, Color.GREEN };

        // Sets up mRGB
        for (int i = 0; i < IDX_MAX; i++) {
            int pixel = initParams.getInt(colors[i], initColors[i]);
            Log.d(TAG, "Setting " + colors[i] + " to " + pixel);
            pixelToRgb(pixel, mRGB[i]);
        }

        // Other params
        mCloseness = initParams.getInt(CLOSENESS, 128);
        mFillFactor = initParams.getInt(FILL_FACTOR, 3);
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
            execute(photo);
        }
    }

    @Override
    protected PhotoAnalysisScore doInBackground(Bitmap... bitmaps) {

        // Get pixels from bitmap
        Bitmap bitmap = bitmaps[0];
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixels[] = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        Log.d(TAG, "size: " + width + "x" + height + "=" + pixels.length);

        // Calculates how often to report
        int reportCnt = pixels.length / 100;
        Log.d(TAG, "report cnt @" + reportCnt);

        // Calculates various scores based on pixels
        int[] scores = new int[IDX_MAX];
        int[] rgb = new int[3];
        int numMeaningPixels = 0;

        for (int np = 0; np < pixels.length; ++np) {

            int pixel = pixels[np];

            for (int i = 0; i < IDX_MAX; i++) {
                pixelToRgb(pixel, rgb);
                // Calculates Manhattan distance between 2 rgb values
                int dist = 0;
                for (int j = 0; j < 3; ++j) {
                    dist += Math.abs(rgb[j] - mRGB[i][j]);
                }

                // Adds to score if <= closeness
                if (dist <= mCloseness) {
                    scores[i]++;
                }
            }

            if (pixel != 0) {
                ++numMeaningPixels;
            }

            // Checks if report
            if (np % reportCnt == 0) {
                publishProgress(np / reportCnt);
            }
        }

        Log.d(TAG, "meaningful: " + numMeaningPixels);
        // Normalizes scores
        for (int i = 0; i < IDX_MAX; i++) {
            scores[i] = scores[i] * mFillFactor * 100 / numMeaningPixels;
            if (scores[i] > 100) {
                scores[i] = 100;
            }
        }

        // Creates score object
        PhotoAnalysisScore score = new PhotoAnalysisScore();
        score.setFriendliness(scores[IDX_FRIENDLY]);
        score.setHealthy(scores[IDX_HEALTHY]);
        score.setLook(scores[IDX_LOOK]);
        score.setMystique(scores[IDX_MYSTIQUE]);
        return score;
    }

    @Override
    protected void onPostExecute(PhotoAnalysisScore photoAnalysisScore) {
        mListener.onResult(photoAnalysisScore);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(TAG, "Processed " + values[0] + "%");
    }
}
