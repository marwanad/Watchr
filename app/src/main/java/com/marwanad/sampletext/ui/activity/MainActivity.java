package com.marwanad.sampletext.ui.activity;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import com.marwanad.sampletext.AudioInputListener;
import com.marwanad.sampletext.AudioInputRunnable;
import com.marwanad.sampletext.R;
import com.marwanad.sampletext.SampleApplication;
import com.marwanad.sampletext.widget.MeterDrawable;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements AudioInputListener {

    private AudioInputRunnable _audioInput;
    @Bind(R.id.meter_view)
    MeterDrawable _meterDrawable;
    @Bind(R.id.record_on_off_button)
    ToggleButton _inputToggleButton;
    @Bind(R.id.DB_tens_text_view)
    TextView _DBTextView;
    @Bind(R.id.DB_fraction_text_view)
    TextView _DBFractionTextView;
    @Inject
    Socket _socket;

    double mOffsetdB = 10;
    double mGain = 2500.0 / Math.pow(10.0, 90.0 / 20.0);
    double mRmsSmoothed;
    double mAlpha = 0.9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((SampleApplication) getApplication()).getSampleComponent().inject(this);
        _audioInput = new AudioInputRunnable(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.record_on_off_button)
    public void startRecording() {
        if (_inputToggleButton.isChecked()) {
            _audioInput.start();
            _socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    _socket.emit("msg", "hi, I'm connected, here's my mac address" + "ayy lmao");
                }

            });
            _socket.connect();
        } else {
            _audioInput.stop();
            _socket.disconnect();
        }
    }

    // Find RMS and process input
    @Override
    public void processSampleFrames(short[] sampleFrame) {
        // Find rms value
        double rms = 0;
        for (int i = 0; i < sampleFrame.length; i++) {
            rms += sampleFrame[i] * sampleFrame[i];
        }
        rms = Math.sqrt(rms / sampleFrame.length);

        mRmsSmoothed = mRmsSmoothed * mAlpha + (1 - mAlpha) * rms;
        final double rmsdB = 20.0 * Math.log10(mGain * mRmsSmoothed);

        final DecimalFormat df = new DecimalFormat("##");

        final String dbValue = df.format(20 + rmsdB) + "." + (int) (Math.round(Math.abs(rmsdB * 10))) % 10;
        if (Integer.valueOf(dbValue) > 50) {
            _socket.emit("msg", dbValue);
        }
        _meterDrawable.post(new Runnable() {
            @Override
            public void run() {
                _meterDrawable.setLevel((mOffsetdB + rmsdB) / 60);

                _DBTextView.setText(df.format(20 + rmsdB));

                int one_decimal = (int) (Math.round(Math.abs(rmsdB * 10))) % 10;
                _DBFractionTextView.setText(Integer.toString(one_decimal));
            }
        });
    }
}
