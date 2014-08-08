package choiceware.com.ultimateimagedownloader;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.util.Log;

import java.util.BitSet;

/**
 * Created by achoi on 7/19/2014.
 */
public class AsyncTaskBroadcastService extends Service {

    private static final String TAG = AsyncTaskBroadcastService.class.getSimpleName();
    private static final String START_ID = "START_ID";

    private class MyAsyncTask extends AsyncTask<Intent, Void, Object> {

        private int mStartId;

        @Override
        protected Object doInBackground(Intent... intents) {
            Intent intent = intents[0];
            mStartId = intent.getIntExtra(START_ID, -1);
            try {
                return DownloaderUtils.loadImageSync(intent, new CancellationSignal());
            }
            catch (Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
                return ex;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            try {

                // Process object and fill in result intent
                Intent resIntent = new Intent();
                if (o instanceof Bitmap) {
                    Bitmap image = (Bitmap) o;
                }
                else if (o instanceof Exception) {
                    Exception ex = (Exception) o;

                }
                else {
                    return;
                }

                // Broadcast intent


            }
            finally {
                AsyncTaskBroadcastService.this.stopSelf(mStartId);
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyAsyncTask task = new MyAsyncTask();
        intent.putExtra(START_ID, startId);
        task.execute(intent);
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
