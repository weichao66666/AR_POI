package io.weichao.ar_poi.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import java.text.DecimalFormat;

import io.weichao.ar_poi.domain.ARData;
import io.weichao.ar_poi.domain.Marker;
import io.weichao.ar_poi.view.AugmentedView;
import io.weichao.ar_poi.view.CameraSurface;

public class AugmentedActivity extends SensorsActivity implements OnTouchListener {
    private static final String TAG = "AugmentedActivity";
    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    protected static WakeLock wakeLock = null;
    protected static CameraSurface camScreen = null;
    protected static AugmentedView augmentedView = null;

    public static boolean useCollisionDetection = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        camScreen = new CameraSurface(this);
        setContentView(camScreen);

        augmentedView = new AugmentedView(this);
        augmentedView.setOnTouchListener(this);
        LayoutParams augLayout = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        addContentView(augmentedView, augLayout);

        updateDataOnZoom();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "DimScreen");
    }

    @Override
    public void onResume() {
        super.onResume();

        wakeLock.acquire();
    }

    @Override
    public void onPause() {
        super.onPause();

        wakeLock.release();
    }

    @Override
    public void onSensorChanged(SensorEvent evt) {
        super.onSensorChanged(evt);

        if (evt.sensor.getType() == Sensor.TYPE_ACCELEROMETER ||
                evt.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            augmentedView.postInvalidate();
        }
    }

    protected void updateDataOnZoom() {
        ARData.setRadius(11);
        ARData.setZoomLevel(FORMAT.format(11));
        ARData.setZoomProgress(50);
    }

    public boolean onTouch(View view, MotionEvent me) {
        for (Marker marker : ARData.getMarkers()) {
            if (marker.handleClick(me.getX(), me.getY())) {
                if (me.getAction() == MotionEvent.ACTION_UP) {
                    markerTouched(marker);
                }
                return true;
            }
        }
        return super.onTouchEvent(me);
    }

    protected void markerTouched(Marker marker) {
        Log.w(TAG, "markerTouched() not implemented.");
    }
}