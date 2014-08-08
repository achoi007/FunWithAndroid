package choiceware.com.ultimateimagedownloader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.CancellationSignal;

/**
 * An IDownloader that uses AsyncTask
 * Created by achoi on 7/2/2014.
 */
public class AsyncTaskDownloader extends AbstractDownloader {

    private class MyAsyncTask extends AsyncTask<LoadOptions, Void, Object> {

        /**
         * Either return Bitmap if success, Exception if error, or null if cancelled.
         *
         * @param loadOpts
         * @return
         */
        @Override
        protected Object doInBackground(LoadOptions... loadOpts) {
            try {
                LoadOptions opt = loadOpts[0];
                Bitmap image = DownloaderUtils.loadImageSync(opt.getUri(), opt.getMaxWidth(),
                        opt.getMaxHeight(), opt.getCxlSig());
                return image;
            } catch (Exception ex) {
                return ex;
            }
        }

        /**
         * Call appropriate function of callback based on result.
         *
         * @param o
         */
        @Override
        protected void onPostExecute(Object o) {
            try {
                if (o == null) {
                    mCallback.onCancelled();
                } else if (o instanceof Bitmap) {
                    mCallback.onImage((Bitmap) o);
                } else {
                    mCallback.onError((Exception) o);
                }
            } finally {
                notifyIsDoneLoading();
            }
        }
    }

    public AsyncTaskDownloader() {
        super(MyApplication.getStrFromContext(R.string.AsyncTask));
    }

    @Override
    protected void onLoad(LoadOptions loadOpt) {
        MyAsyncTask task = new MyAsyncTask();
        task.execute(loadOpt);
    }
}
