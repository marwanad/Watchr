package com.marwanad.sampletext.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro2;
import com.marwanad.sampletext.R;
import com.marwanad.sampletext.WatchrApplication;
import com.marwanad.sampletext.WatchrService;
import com.marwanad.sampletext.ui.intro.fragment.IntroFragment;

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
            finish();
        }
        addSlide(IntroFragment.newInstance(R.layout.intro_frag_one));
        addSlide(IntroFragment.newInstance(R.layout.intro_frag_two));
        addSlide(IntroFragment.newInstance(R.layout.intro_frag_three));
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
                "the data to an encrypted centralized database. We will not collect any personal information" +
                "for User privacy concerns.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startService();
                _sharedPrefs.edit().putBoolean(HAS_ACCEPTED, true).commit();
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
}
