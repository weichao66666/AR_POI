package io.weichao.ar_poi.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import io.weichao.ar_poi.datasource.LocalDataSource;
import io.weichao.ar_poi.domain.ARData;
import io.weichao.ar_poi.domain.Marker;

public class MainActivity extends AugmentedActivity  {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalDataSource localData = new LocalDataSource(this.getResources(), getAssets());
        ARData.addMarkers(localData.getMarkers());
    }

    @Override
    protected void markerTouched(Marker marker) {
        Toast t = Toast.makeText(getApplicationContext(), marker.getName(), Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }
}