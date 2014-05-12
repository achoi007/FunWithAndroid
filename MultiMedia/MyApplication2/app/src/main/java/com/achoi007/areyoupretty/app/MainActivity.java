package com.achoi007.areyoupretty.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private IPhotoAnalyzer mPicAnalyzer;
    private ImageView mPicImg;
    private boolean mUseThumbnail;
    private String mPhotoFile;

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

    private File createUniqueImageFile() throws IOException {

        // Creates unique timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());

        // Creates prefix of image file name
        String imageFilePrefix = "JPEG_" + timeStamp + "_";

        // Gets public pictures directory
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // Creates temporary image file
        File imageFile = File.createTempFile(imageFilePrefix, ".jpg", storageDir);
        return imageFile;
    }

    public void onAnalyze(View btn) {
        BitmapDrawable drawable = (BitmapDrawable) mPicImg.getDrawable();
        if (drawable != null) {
            Bitmap pic = drawable.getBitmap();
            mPicAnalyzer.analyze(pic);
        }
    }

    public void onChoosePic(View btn) {

        // Creates image capture intent and makes sure it can be resolved.
        Intent takePicInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicInt.resolveActivity(getPackageManager()) == null) {
            Log.d(TAG, "no activity can capture image");
            return;
        }

        // If not using thumb nail, creates output image file
        if (!mUseThumbnail) {
            try {
                File photoFile = createUniqueImageFile();
                mPhotoFile = photoFile.getAbsolutePath();
                Log.d(TAG, "photo file: " + photoFile.getAbsolutePath());
                takePicInt.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } catch (IOException ex) {
                Log.e(TAG, "Unable to create image file", ex);
                Toast.makeText(this, "Unable to create photo file", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Starts capture activity
        startActivityForResult(takePicInt, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Log.d(TAG, "Got an image");
            Bitmap pic;
            if (mUseThumbnail) {
                pic = (Bitmap) data.getExtras().get("data");
            } else {
                pic = getScaledImage(mPhotoFile, mPicImg.getWidth(), mPicImg.getHeight());
            }
            mPicImg.setImageBitmap(pic);
        }
    }

    private Bitmap getScaledImage(String imagePath, int width, int height) {

        // Gets dimension of image
        BitmapFactory.Options bmOpts = new BitmapFactory.Options();
        bmOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOpts);
        int imgWidth = bmOpts.outWidth;
        int imgHeight = bmOpts.outHeight;
        Log.d(TAG, "Scaling image from " + imgWidth + "x" + imgHeight + " to " + width + "x" +
                height);

        // Computes scaling factor
        int scaleFactor = Math.min(imgWidth/width, imgHeight/height);

        // Sets scaling options
        bmOpts.inJustDecodeBounds = false;  // Undo previous
        bmOpts.inSampleSize = scaleFactor;
        bmOpts.inPurgeable = true;

        // Generates scale image
        return BitmapFactory.decodeFile(imagePath, bmOpts);
    }
}
