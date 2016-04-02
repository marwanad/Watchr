package com.marwanad.sampletext;

import com.marwanad.sampletext.ui.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by marwanad on 2016-04-02.
 */
@Component(modules = SampleModule.class)
@Singleton
public interface SampleComponent
{
    void inject(MainActivity activity);
}
