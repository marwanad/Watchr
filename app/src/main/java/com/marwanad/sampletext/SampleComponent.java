package com.marwanad.sampletext;

import com.marwanad.sampletext.ui.activity.IntroActivity;
import com.marwanad.sampletext.ui.activity.MeterActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by marwanad on 2016-04-02.
 */
@Component(modules = SampleModule.class)
@Singleton
public interface SampleComponent
{
    void inject(MeterActivity activity);

    void inject(SampleService service);

    void inject(IntroActivity activity);
}
