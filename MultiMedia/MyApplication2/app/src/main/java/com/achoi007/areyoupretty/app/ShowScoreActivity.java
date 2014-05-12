package com.achoi007.areyoupretty.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ShowScoreActivity extends Activity {

    public final static String SCORE = "score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_score);

        Intent intent = getIntent();
        PhotoAnalysisScore score = (PhotoAnalysisScore) intent.getSerializableExtra(SCORE);
        setField(R.id.textFriendliness, R.string.Friendliness, score.getFriendliness());
        setField(R.id.textHealthy, R.string.Healthy, score.getHealthy());
        setField(R.id.textLook, R.string.Look, score.getLook());
        setField(R.id.textMystique, R.string.Mystique, score.getMystique());
    }

    private void setField(int id, int attrName, int value) {
        TextView txt = (TextView) findViewById(id);
        txt.setText(getString(attrName) + ": " + value);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_score, menu);
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

}
