package com.marwanad.sampletext.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.paolorotolo.appintro.AppIntro2;
import com.marwanad.sampletext.R;
import com.marwanad.sampletext.SampleService;
import com.marwanad.sampletext.ui.intro.fragment.IntroFragment;

/**
 * Created by marwanad on 2016-04-02.
 * Stub Intro Activity for the app, starts the service if needed.
 */
public class IntroActivity extends AppIntro2
{
    @Override
    public void init(@Nullable Bundle savedInstanceState)
    {
        addSlide(IntroFragment.newInstance(R.layout.intro_frag_one));
        addSlide(IntroFragment.newInstance(R.layout.intro_frag_two));
        addSlide(IntroFragment.newInstance(R.layout.intro_frag_three));
    }

    @Override
    public void onDonePressed()
    {
        startService(new Intent(this, SampleService.class));
    }

    @Override
    public void onNextPressed()
    {

    }

    @Override
    public void onSlideChanged()
    {

    }
}
