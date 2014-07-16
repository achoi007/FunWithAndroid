package choiceware.com.ultimateimagedownloader;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.util.Log;

import java.io.File;

/**
 * Created by andyc_000 on 7/15/2014.
 */
public class ThreadPendingIntentDownloaderService extends Service {

    private static final String TAG = ThreadPendingIntentDownloaderService.class.getSimpleName();

    public ThreadPendingIntentDownloaderService() {
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    // Downloads bitmap
                    Bitmap image = DownloaderUtils.loadImageSync(intent, new CancellationSignal());

                    // Create result intent which contains pathname of image
                    Intent resultIntent = new Intent();
                    File imageFile = MessageUtils.saveBitmapAsFile(image);
                    resultIntent.putExtra(IntentExtraData.IMAGE, imageFile.getPath());

                    // Get pending intent from bundle extra
                    PendingIntent pi = intent.getParcelableExtra(IntentExtraData.PENDING_INTENT);
                    pi.send(MyApplication.getContext(), Activity.RESULT_OK, resultIntent);
                }
                catch (Exception ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                    PendingIntent pi = intent.getParcelableExtra(IntentExtraData.PENDING_INTENT);
                    if (pi != null) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(IntentExtraData.EXCEPTION, ex);
                        try {
                            pi.send(MyApplication.getContext(), Activity.RESULT_CANCELED,
                                    resultIntent);
                        }
                        catch (Exception ex2) {
                            Log.e(TAG, ex.getMessage(), ex2);
                        }
                    }
                }

                // Stops if no more requests.
                ThreadPendingIntentDownloaderService.this.stopSelf(startId);
            }
        });
        t.start();

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
