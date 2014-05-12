package com.achoi007.areyoupretty.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private IPhotoAnalyzer mPicAnalyzer;
    private ImageView mPicImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sets up views
        mPicImg = (ImageView) findViewById(R.id.imgPic);

        // Sets up picture analyzer
        // mPicAnalyzer = new RandomPhotoAnalyzer();
        mPicAnalyzer = new ColorBasedPhotoAnalyzer();
        mPicAnalyzer.init(new Bundle());
        mPicAnalyzer.setScoreListener(new IPhotoAnalyzer.IScoreListener() {
            @Override
            public void onResult(PhotoAnalysisScore score) {
                showScore(score);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showScore(PhotoAnalysisScore score) {
        Intent showScoreInt = new Intent(this, ShowScoreActivity.class);
        showScoreInt.putExtra(ShowScoreActivity.SCORE, score);
        startActivity(showScoreInt);
    }

    public void onAnalyze(View btn) {
        BitmapDrawable drawable = (BitmapDrawable) mPicImg.getDrawable();
        if (drawable != null) {
            Bitmap pic = drawable.getBitmap();
            mPicAnalyzer.analyze(pic);
        }
    }

    public void onChoosePic(View btn) {
        Intent takePicInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicInt.resolveActivity(getPackageManager()) != null) {
            Log.d(TAG, "starting take pic activity");
            startActivityForResult(takePicInt, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Log.d(TAG, "Got an image");
            Bitmap pic = (Bitmap) data.getExtras().get("data");
            mPicImg.setImageBitmap(pic);
        }
    }
}
