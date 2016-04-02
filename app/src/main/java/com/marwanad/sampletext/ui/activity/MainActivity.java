package com.marwanad.sampletext.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.marwanad.sampletext.SampleService;

/**
 * Created by marwanad on 2016-04-02.
 * Stub Intro Activity for the app, starts the service if needed.
 */
public class MainActivity extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, SampleService.class));
    }
}
