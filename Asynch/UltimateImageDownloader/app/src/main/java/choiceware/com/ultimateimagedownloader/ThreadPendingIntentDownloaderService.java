package choiceware.com.ultimateimagedownloader;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CancellationSignal;
import android.os.IBinder;

/**
 * Created by andyc_000 on 7/15/2014.
 */
public class ThreadPendingIntentDownloaderService extends Service {

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

                    // Creates pending intent


                }
                catch (Exception ex) {

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
