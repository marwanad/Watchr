package com.marwanad.watchr.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro2;
import com.marwanad.watchr.R;
import com.marwanad.watchr.WatchrApplication;
import com.marwanad.watchr.WatchrService;
import com.marwanad.watchr.ui.intro.fragment.IntroFragment;

import javax.inject.Inject;

/**
 * Created by marwanad on 2016-04-02.
 * Stub Intro Activity for the app, starts the service if needed.
 */
public class IntroActivity extends AppIntro2
{
    @Inject SharedPreferences _sharedPrefs;
    private static final String HAS_ACCEPTED = "intro.activity.accepted";

    @Override
    public void init(@Nullable Bundle savedInstanceState)
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ((WatchrApplication) getApplication()).getSampleComponent().inject(this);
        if (_sharedPrefs.getBoolean(HAS_ACCEPTED, false)) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else {
            addSlide(IntroFragment.newInstance(R.layout.intro_frag_one));
            addSlide(IntroFragment.newInstance(R.layout.intro_frag_two));
            addSlide(IntroFragment.newInstance(R.layout.intro_frag_three));
        }
    }

    @Override
    public void onDonePressed()
    {
        showConfirmDialog();
    }

    @Override
    public void onNextPressed()
    {
    }

    private void showConfirmDialog()
    {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(IntroActivity.this);
        builder.setTitle("Would you like to start using Watchr?");
        builder.setMessage("You agree to let Watchr collect your location and the sound level metadata and send " +
                "the data to an encrypted centralized database. We will not collect any personal information " +
                "for user privacy concerns.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                _sharedPrefs.edit().putBoolean(HAS_ACCEPTED, true).commit();
                startService();
                launchMainActivity();
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void startService()
    {
        startService(new Intent(this, WatchrService.class));
    }

    @Override
    public void onSlideChanged()
    {

    }

    private void launchMainActivity()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
