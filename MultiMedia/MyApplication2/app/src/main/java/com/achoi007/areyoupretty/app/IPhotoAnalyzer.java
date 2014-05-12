package com.achoi007.areyoupretty.app;

import android.graphics.Bitmap;
import android.os.Bundle;

/**
 * Created by alang_000 on 5/11/2014.
 */
public interface IPhotoAnalyzer {

    public interface IScoreListener {
        void onResult(PhotoAnalysisScore score);
    }

    void init(Bundle initParams);

    void destroy();

    void setScoreListener(IScoreListener listener);

    void analyze(Bitmap photo);
}
