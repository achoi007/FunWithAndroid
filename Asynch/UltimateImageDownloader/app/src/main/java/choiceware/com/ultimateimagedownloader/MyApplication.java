package choiceware.com.ultimateimagedownloader;

import android.app.Application;
import android.content.Context;

/**
 * Created by andyc_000 on 7/2/2014.
 */
public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getStrFromContext(int resId) {
        return mContext.getString(resId);
    }

    public static String getStrFromContext(int resId, Object... formatArgs) {
        return mContext.getString(resId, formatArgs);
    }
}
