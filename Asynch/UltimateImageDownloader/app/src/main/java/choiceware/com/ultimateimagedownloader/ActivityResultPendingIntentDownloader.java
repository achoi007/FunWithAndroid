package choiceware.com.ultimateimagedownloader;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;

/**
 * Created by alang_000 on 7/16/2014.
 */
public class ActivityResultPendingIntentDownloader extends AbstractDownloader {

    private Class mServiceClass;

    public ActivityResultPendingIntentDownloader(Class serviceClass) {
        super(MyApplication.getStrFromContext(R.string.ActivityResultPendingIntent));
        mServiceClass = serviceClass;
    }

    @Override
    protected void onLoad(LoadOptions loadOpt) {

    }
}
