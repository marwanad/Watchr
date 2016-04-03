package com.marwanad.watchr.ui.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.marwanad.watchr.R;
import com.marwanad.watchr.WatchrService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by marwanad on 2016-04-03.
 */
public class MainActivity extends AppCompatActivity
{
    @Bind(R.id.main_activity_container) View _container;
    @Bind(R.id.start_stop_toggle) ImageButton _startStopToggle;
    @Bind(R.id.main_title) TextView _titleTextView;
    @Bind(R.id.description_textview) TextView _descriptionTextView;

    @OnClick(R.id.start_stop_toggle)
    public void onButtonClick()
    {
        Log.d("MARWAN", "is watcher service running : " + isWatchrServiceRunning());
        if (isWatchrServiceRunning()) {
            _container.setBackgroundColor(getResources().getColor(R.color.running_green));
            _startStopToggle.setImageDrawable(getDrawable(R.drawable.ic_action_start_96dp));
            _titleTextView.setText("Safety Mode is Currently Disabled");
            _descriptionTextView.setText("Tap To Enable");
            stopService(new Intent(MainActivity.this, WatchrService.class));
        }
        else {
            _container.setBackgroundColor(getResources().getColor(R.color.stop_red));
            _startStopToggle.setImageDrawable(getDrawable(R.drawable.ic_action_stop_96dp));
            _titleTextView.setText("Safety Mode is Currently Enabled");
            _descriptionTextView.setText("Tap To Dismiss");
            startService(new Intent(this, WatchrService.class));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initLayout();
    }

    private void initLayout()
    {
        if (isWatchrServiceRunning()) {
            _container.setBackgroundColor(getResources().getColor(R.color.stop_red));
            _startStopToggle.setImageDrawable(getDrawable(R.drawable.ic_action_stop_96dp));
        }
        else {
            _container.setBackgroundColor(getResources().getColor(R.color.running_green));
            _startStopToggle.setImageDrawable(getDrawable(R.drawable.ic_action_start_96dp));
        }
    }

    private boolean isWatchrServiceRunning()
    {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.marwanad.watchr.WatchrService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
