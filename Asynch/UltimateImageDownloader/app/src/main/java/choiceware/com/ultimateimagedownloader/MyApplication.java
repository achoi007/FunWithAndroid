package choiceware.com.ultimateimagedownloader;

import android.app.Application;
import android.content.Context;

/**
 * Added function to support getting singleton context from anywhere.  getString can
 * now be called from anywhere instead of only code that has access to Context.
 * <p/>
 * Created by andyc on 7/2/2014.
 */
public class MyApplication extends Application {

    private static Context mContext;

    /**
     * Remember singleton context
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    /**
     * Get singleton context
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * Same as getContext().getString(resId)
     *
     * @param resId
     * @return
     */
    public static String getStrFromContext(int resId) {
        return mContext.getString(resId);
    }

    /**
     * Same as getContext().getString(resId, formatArgs)
     *
     * @param resId
     * @param formatArgs
     * @return
     */
    public static String getStrFromContext(int resId, Object... formatArgs) {
        return mContext.getString(resId, formatArgs);
    }
}
