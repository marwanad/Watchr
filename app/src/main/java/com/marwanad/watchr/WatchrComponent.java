package com.marwanad.watchr;

import com.marwanad.watchr.ui.activity.IntroActivity;
import com.marwanad.watchr.ui.activity.MeterActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by marwanad on 2016-04-02.
 */
@Component(modules = WatchrModule.class)
@Singleton
public interface WatchrComponent
{
    void inject(MeterActivity activity);

    void inject(WatchrService service);

    void inject(IntroActivity activity);
}
