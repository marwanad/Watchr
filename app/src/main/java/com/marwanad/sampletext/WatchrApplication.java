package com.marwanad.sampletext;

import android.app.Application;

/**
 * Created by marwanad on 2016-04-02.
 */
public class WatchrApplication extends Application
{
    private WatchrComponent _component;

    @Override
    public void onCreate()
    {
        super.onCreate();
        _component = DaggerWatchrComponent.builder().watchrModule(new WatchrModule(this)).build();
    }

    public WatchrComponent getSampleComponent()
    {
        return _component;
    }
}
