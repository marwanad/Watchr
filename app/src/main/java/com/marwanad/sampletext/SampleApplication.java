package com.marwanad.sampletext;

import android.app.Application;

/**
 * Created by marwanad on 2016-04-02.
 */
public class SampleApplication extends Application
{
    private SampleComponent _component;

    @Override
    public void onCreate()
    {
        super.onCreate();
        _component = DaggerSampleComponent.builder().sampleModule(new SampleModule(this)).build();
    }

    public SampleComponent getSampleComponent()
    {
        return _component;
    }
}
