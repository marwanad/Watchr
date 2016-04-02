package com.marwanad.sampletext;

import javax.inject.Singleton;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by marwanad on 2016-04-02.
 */
@Module
public class SampleModule
{
    private final Context _app;

    public SampleModule(Context _app)
    {
        this._app = _app;
    }

    @Provides
    @Singleton
    Context provideContext()
    {
        return _app;
    }

    // Provide socket connection
}
