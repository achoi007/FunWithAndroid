package choiceware.com.ultimateimagedownloader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;


public class MainActivity extends Activity implements IDownloaderCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView mImgView;
    private List<IDownloader> mDownloaders;
    private Button mLoadBtn, mCxlBtn;
    private EditText mUri;
    private Spinner mLoaderSpinner;
    private IDownloader mCurrLoader;

    private List<IDownloader> createLoaders() {
        List<IDownloader> loaders = new LinkedList<IDownloader>();

        // Simple thread version
        loaders.add(new SimpleThreadDownloader());

        // Async Task
        loaders.add(new AsyncTaskDownloader());

        // Handler Thread with Messenger
        loaders.add(new MsgHdlrThrdDownloader());

        // Using ExecutorService
        loaders.add(new ExecutorServicesDownloader());

        return loaders;
    }

    private void populateLoaderGuiList(List<IDownloader> loaders) {
        ArrayAdapter<IDownloader> adapter = new ArrayAdapter<IDownloader>(this,
                android.R.layout.simple_list_item_1, loaders);
        mLoaderSpinner.setAdapter(adapter);
    }

    private void registerLoaderCallback(List<IDownloader> downloaders) {
        for (IDownloader downloader : downloaders) {
            downloader.setCallback(this);
        }
    }

    private void enableLoading(boolean v) {
        mLoadBtn.setEnabled(v);
        mCxlBtn.setEnabled(!v);
        if (v) {
            mCurrLoader = null;
        }
        // FINISH - dismiss progress dialog
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remember some controls
        mImgView = (ImageView) findViewById(R.id.imgView);
        mLoadBtn = (Button) findViewById(R.id.btnLoad);
        mCxlBtn = (Button) findViewById(R.id.btnCxl);
        mUri = (EditText) findViewById(R.id.txtUri);
        mLoaderSpinner = (Spinner) findViewById(R.id.loaders);

        // Set up downloaders
        mDownloaders = createLoaders();
        populateLoaderGuiList(mDownloaders);
        registerLoaderCallback(mDownloaders);
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

    public void onCancelLoad(View v) {
        if (mCurrLoader != null) {
            mCurrLoader.requestCancel();
        }
    }

    public void onLoadImage(View v) {

        // Disallow loading while current loading is active
        enableLoading(false);

        // Get loader to use
        IDownloader loader = (IDownloader) mLoaderSpinner.getSelectedItem();
        Log.d(TAG, "selected loader: " + loader);

        // Ignore if loader is currently busy
        if (loader.isLoading()) {
            Toast.makeText(this, getString(R.string.LoaderIsBusy), Toast.LENGTH_SHORT).show();
            return;
        }

        // Set current loader
        mCurrLoader = loader;

        // Bring up progress dialog
        // FINISH

        // Get Uri
        Uri uri = null;
        try {
            String urlStr = mUri.getText().toString();
            if (!urlStr.startsWith("http:")) {
                urlStr = "http://" + urlStr;
            }
            uri = Uri.parse(urlStr);
        } catch (Exception ex) {
            mUri.setError(ex.getMessage());
            return;
        }

        // Load image
        loader.load(uri, mImgView.getWidth(), mImgView.getHeight());
    }

    public void onResetImage(View v) {
        mImgView.setImageResource(android.R.color.transparent);
    }

    @Override
    public void onImage(final Bitmap image) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImgView.setImageBitmap(image);
                enableLoading(true);
            }
        });
    }

    @Override
    public void onError(final Exception ex) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
                enableLoading(true);
            }
        });
    }

    @Override
    public void onProgress(int percentComplete) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void onCancelled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, getString(R.string.Cancelled), Toast.LENGTH_LONG).show();
                enableLoading(true);
            }
        });
    }
}
